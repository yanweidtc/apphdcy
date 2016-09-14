package com.hdcy.app.vedio.upload;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hdcy.app.R;
import com.hdcy.app.vedio.permission.PermissionsActivity;
import com.hdcy.app.vedio.permission.PermissionsChecker;
import com.hdcy.app.vedio.preference.Log2FileUtil;
import com.hdcy.app.vedio.preference.Settings;
import com.ucloud.common.logger.L;
import com.ucloud.common.util.DeviceUtils;
import com.ucloud.common.util.StringUtil;
import com.ucloud.live.UEasyStreaming;
import com.ucloud.live.UStreamingProfile;
import com.ucloud.live.widget.UAspectFrameLayout;


public class PublishDemo extends AppCompatActivity implements UEasyStreaming.UStreamingStateListener, View.OnClickListener {

    private static final String TAG = "PublishDemo";

    public static final int MSG_UPDATE_COUNTDOWN = 1;

    public static final int DEFAULT_BEAUTIFY_FILTER_LEVEL = 3;

    public static final int COUNTDOWN_DELAY = 1000;

    public static final  int COUNTDOWN_START_INDEX = 3;

    public static final  int COUNTDOWN_END_INDEX = 1;

    private static final int REQUEST_CODE = 0;

    protected Settings mSettings;

    protected String rtmpPushStreamDomain = "publish3.usmtd.ucloud.com.cn";

    //Views
    protected ImageView mCameraToggleIv;
    protected ImageView mLampToggleIv;
    protected ImageButton mCloseRecorderImgBtn;
    protected Button mBackImgBtn;
    protected View mFocusIndex;
    protected TextView mBitrateTxtv;
    protected TextView mCountDownTxtv;
    protected TextView mRecordedTimeTxtv;
    protected TextView mOutputStreamInfoTxtv;
    protected TextView mBufferOverfloInfoTxtv;
    protected TextView mStatusInfoTxtv;
    protected ViewGroup mContainer;

    protected UAspectFrameLayout mPreviewContainer;


    //for filter
    private ImageButton mToggleFilterImgBtn;

    protected boolean isShutDownCountdown = false;

    protected UEasyStreaming mEasyStreaming;

    protected UStreamingProfile mStreamingProfile;

    protected UiHandler uiHandler;

    protected  Button mCurrentBeautifyLevelBtn;

    protected static int[][] BEAUTIFY_LEVEL = {
            {100,100, 15, 15},
            {80, 90,  20, 20},
            {60, 80,  25, 25},
            {40, 70,  39, 30},
            {32, 62,  40, 35},
    };

    protected int DEFAULT_CAMERA_ID = UStreamingProfile.CAMERA_FACING_BACK; //UStreamingProfile.CAMERA_FACING_BACK

    protected int whichCamera = DEFAULT_CAMERA_ID;

    private PermissionsChecker mPermissionsChecker; // 权限检测器

    private ScrollView mScrollView;

    private boolean isMute = false;

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    public void onStateChanged(int type, Object event) {
       switch (type) {
            case UEasyStreaming.State.MSG_START_PREVIEW:
                //预览成功，startRecording方法需要在预览成功之后调用，若不想立即推流，可以给一个标志位，在合适的时候如点击按钮的时候，再调用startRecording激活。
                Log.i(TAG, "lifecycle->demo->event->MSG_START_PREVIEW");
                if (mEasyStreaming != null) {
                    mEasyStreaming.applyFilter(UEasyStreaming.FILTER_BEAUTIFY);
                    if (mCurrentBeautifyLevelBtn == null) {
                        mEasyStreaming.applyFilterLevel(
                                BEAUTIFY_LEVEL[DEFAULT_BEAUTIFY_FILTER_LEVEL - 1][0],
                                BEAUTIFY_LEVEL[DEFAULT_BEAUTIFY_FILTER_LEVEL - 1][1],
                                BEAUTIFY_LEVEL[DEFAULT_BEAUTIFY_FILTER_LEVEL - 1][2],
                                BEAUTIFY_LEVEL[DEFAULT_BEAUTIFY_FILTER_LEVEL - 1][3]);
                    } else {
                        mEasyStreaming.applyFilterLevel(
                                BEAUTIFY_LEVEL[mCurrentBeautifyLevelBtn.getId() - 1][0],
                                BEAUTIFY_LEVEL[mCurrentBeautifyLevelBtn.getId() - 1][1],
                                BEAUTIFY_LEVEL[mCurrentBeautifyLevelBtn.getId() - 1][2],
                                BEAUTIFY_LEVEL[mCurrentBeautifyLevelBtn.getId() - 1][3]);
                    }
                }
                break;
            case UEasyStreaming.State.MSG_START_RECORDING:
                //收到了startRecording的消息
                updateStatusInfo("streaming start.", 0);
                Log.e(TAG, "lifecycle->demo->event->start");
                break;
            case UEasyStreaming.State.MSG_NETOWRK_BLOCKS:
                //推流网络状况不好 event int类型 值为内部统计blocks次数
                mBufferOverfloInfoTxtv.setText("network block stats:" + Integer.valueOf(event.toString()));
                Log.e(TAG, "lifecycle->demo->event->network blocks");
                break;
            case UEasyStreaming.State.MSG_PREPARED_SUCCESSED:
                //推流prepare成功
                Log.e(TAG, "lifecycle->demo->event->" + event.toString());
                break;
            case UEasyStreaming.State.MSG_SIGNATRUE_FAILED:
                //推流key校验失败
                updateStatusInfo("streaming signature failed.", 0);
                L.e(TAG, "lifecycle->demo->event->MSG_SIGNATRUE_FAILED:" + event.toString());
                break;
            case UEasyStreaming.State.MSG_NETWORK_SPPED:
                //当前手机全局网络速度
                if (mBitrateTxtv != null) {
                    mBitrateTxtv.setVisibility(View.VISIBLE);
                    long speed = Long.valueOf(event.toString());
                    if (speed > 1024) {
                        mBitrateTxtv.setText(speed / 1024 + "K/s");
                    }
                    else {
                        mBitrateTxtv.setText(speed + "B/s");
                    }
                }
                break;
            case UEasyStreaming.State.MSG_PUBLISH_STREAMING_TIME:
                //sdk内部记录的推流时间,若推流被中断stop之后，该值会重新从0开始计数
                if (mRecordedTimeTxtv != null) {
                    mRecordedTimeTxtv.setVisibility(View.VISIBLE);
                    long time = Long.valueOf(event.toString());
                    String retVal = StringUtil.getTimeFormatString(time);
                    mRecordedTimeTxtv.setText(retVal);
                }
                break;
           case UEasyStreaming.State.MSG_NETWORK_DISCONNECT:
               updateStatusInfo("network disconnect.", 0);
               //当前网络状态处于断开状态
               Log.e(TAG, "lifecycle->demo->event->network disconnect.");
               break;
           case UEasyStreaming.State.MSG_PREPARED_FAILED:
               //推流prepare失败
               updateStatusInfo(event.toString(), 0);
               Log.e(TAG,  "lifecycle->demo->event->restart->after prepared error->"+ event.toString()+ ","+mEasyStreaming.isRecording());
               if (mEasyStreaming != null) {
                   mEasyStreaming.restart();
               }
               break;
           case UEasyStreaming.State.MSG_MUXER_FAILED:
               //推流写数据过程出现错误（如网络中断或其它未知的错误)
               updateStatusInfo("streaming failed:" + event.toString(), 0);
               Log.e(TAG,  "llifecycle->demo->event->restart->after write frame error->"+ event.toString() + ","+ mEasyStreaming.isRecording());
               if (mEasyStreaming != null) {
                   mEasyStreaming.restart();
               }
               break;
           case UEasyStreaming.State.MSG_NETWORK_RECONNECT:
               //网络重新连接
               Log.e(TAG, "lifecycle->demo->event->restart->after network reconnect:" + ","+mEasyStreaming.isRecording());
               updateStatusInfo("network reconnect....", 0);
              if (mEasyStreaming != null) {
                   mEasyStreaming.restart();
               }
               break;
           case UEasyStreaming.State.MSG_STOP:
               updateStatusInfo("streaming stop.", 0);
               Log.e(TAG, "lifecycle->demo->event->stop->");
               break;
        }
    }

    private class UiHandler extends Handler {
        public UiHandler(Looper looper) {
            super(looper);
        }
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_COUNTDOWN:
                    handleUpdateCountdown(msg.arg1);
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSettings = new Settings(this);
        setContentView(R.layout.live_layout_live_room_view);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (mSettings.getVideoCaptureOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        init();
        new Thread(){
            public void run() {
                int i = COUNTDOWN_START_INDEX;
                do {
                    try {
                        Thread.sleep(COUNTDOWN_DELAY);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message msg = Message.obtain();
                    msg.what = MSG_UPDATE_COUNTDOWN;
                    msg.arg1 = i;
                    uiHandler.sendMessage(msg);
                    i--;
                }while(i >= COUNTDOWN_END_INDEX);
            }
        }.start();
    }

    private void updateStatusInfo(final CharSequence text, long delay) {
        if (uiHandler != null && mStatusInfoTxtv != null) {
            uiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mStatusInfoTxtv.append(text + "\n");
                    mStatusInfoTxtv.setTextColor(Color.GREEN);
                }
            }, delay);
        }
    }

    private void init() {
        uiHandler = new UiHandler(getMainLooper());
        mPermissionsChecker = new PermissionsChecker(this);
        initView();
        initEnv();
    }

    private void initView() {
        mCameraToggleIv = (ImageView) findViewById(R.id.img_bt_switch_camera);
        mLampToggleIv = (ImageView) findViewById(R.id.img_bt_lamp);
        mCloseRecorderImgBtn = (ImageButton) findViewById(R.id.img_bt_close_record);
        mFocusIndex = findViewById(R.id.focus_index);
        mBitrateTxtv = (TextView) findViewById(R.id.bitrate_txtv);
        mPreviewContainer = (UAspectFrameLayout)findViewById(R.id.container);
        mCountDownTxtv = (TextView) findViewById(R.id.countdown_txtv);
        mRecordedTimeTxtv = (TextView) findViewById(R.id.recorded_time_txtv);
        mOutputStreamInfoTxtv = (TextView) findViewById(R.id.output_url_txtv);

        mBufferOverfloInfoTxtv = (TextView) findViewById(R.id.network_overflow_count);
        mBackImgBtn = (Button) findViewById(R.id.btn_finish);
        mContainer = (ViewGroup) findViewById(R.id.live_finish_container);
        mStatusInfoTxtv = (TextView) findViewById(R.id.status_info);
        mScrollView = (ScrollView) findViewById(R.id.scrollview);
        mStatusInfoTxtv.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

        mCameraToggleIv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mEasyStreaming != null) {
                    mEasyStreaming.switchCamera();
                }
            }
        });
        mLampToggleIv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               /* if (!isMute) {
                    isMute = true;
                } else {
                    isMute = false;
                }
                mEasyStreaming.mute(isMute);*/
                 if (mEasyStreaming != null) {
                     boolean retVal = mEasyStreaming.toggleFlashMode();
                     updateStatusInfo( "toggleFlashMode:" + (retVal ? "successed" : "failed"), 0);
                 }
            }

        });
        mCloseRecorderImgBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                isShutDownCountdown = true;
                mCloseRecorderImgBtn.setEnabled(false);
                if (mEasyStreaming != null) {
                    mEasyStreaming.stopRecording();
                }
                mContainer.setVisibility(View.VISIBLE);
            }
        });

        mBackImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "lifecycle->demo->activity->onPause");
        //if UEasyStermaing no inited in activity onCreate method , you need call this method after inited -> function as camera stopPreivew()
        mEasyStreaming.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        } else {
            mEasyStreaming.onResume();
        }
        Log.e(TAG, "lifecycle->demo->activity->onResume");
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    @Override protected
    void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        } else {
            mEasyStreaming.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mSettings.isOpenLogRecoder()) {
            Log2FileUtil.getInstance().stopLog();
        }
        Log.e(TAG, "lifecycle->demo->activity->onDestroy");
        mEasyStreaming.onDestroy();
    }

    public String videoBitrateMode(int value) {
        switch (value) {
            case UStreamingProfile.VIDEO_BITRATE_LOW: return "VIDEO_BITRATE_LOW";
            case UStreamingProfile.VIDEO_BITRATE_NORMAL: return "VIDEO_BITRATE_NORMAL";
            case UStreamingProfile.VIDEO_BITRATE_MEDIUM: return "VIDEO_BITRATE_MEDIUM";
            case UStreamingProfile.VIDEO_BITRATE_HIGH: return "VIDEO_BITRATE_HIGH";
            default: return value +"";
        }
    }

    public String audioBitrateMode(int value) {
        switch (value) {
            case UStreamingProfile.AUDIO_BITRATE_NORMAL: return "AUDIO_BITRATE_NORMAL";
            default: return value +"";
        }
    }

    public void handleShowStreamingInfo(UEasyStreaming.UEncodingType encodingType) {
        if (mOutputStreamInfoTxtv != null) {
            mOutputStreamInfoTxtv.setVisibility(View.VISIBLE);
            String streamId = mStreamingProfile.getStream().getStreamId();
            String domain = mStreamingProfile.getStream().getPublishDomain();
            String info = "video width:" + mStreamingProfile.getVideoOutputWidth()+ "\n" +
                    "video height:" + mStreamingProfile.getVideoOutputHeight()+ "\n" +
                    "video bitrate:" + videoBitrateMode(mStreamingProfile.getVideoBitrate()) + "\n" +
                    "audio bitrate:" + audioBitrateMode(mStreamingProfile.getAudioBitrate()) + "\n" +
                    "video fps:" + mStreamingProfile.getVideoFrameRate() + "\n" +
                    "url:" + "rtmp://" + domain + ((streamId.startsWith("/") ? streamId  : ("/" + streamId)) + "\n" +
                    "brand:" + DeviceUtils.getDeviceBrand() + "_" + DeviceUtils.getDeviceModel() + "\n" +
                    "sdk version:" + com.ucloud.live.Build.VERSION + "\n" +
                    "android sdk version:" + Build.VERSION.SDK_INT + "\n" +
                    "codec type:" + (encodingType == UEasyStreaming.UEncodingType.MEDIA_CODEC ? "mediacodec" : "x264"));
            mOutputStreamInfoTxtv.setText(info);
            Log.e(TAG, "@@" + info);
        }
        mToggleFilterImgBtn = (ImageButton) findViewById(R.id.img_bt_filter);
        if (encodingType == UEasyStreaming.UEncodingType.MEDIA_CODEC) {

            if (mSettings.getEncoderType() == UEasyStreaming.UEncodingType.MEDIA_CODEC) {
                mToggleFilterImgBtn.setVisibility(View.VISIBLE);
                mToggleFilterImgBtn.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                if (mEasyStreaming != null) {
                                    mEasyStreaming.applyFilter(UEasyStreaming.FILTER_NONE);
                                }
                                break;
                            case MotionEvent.ACTION_UP:
                                if (mEasyStreaming != null) {
                                    mEasyStreaming.applyFilter(UEasyStreaming.FILTER_BEAUTIFY);
                                    if (mCurrentBeautifyLevelBtn != null) {
                                        if (mEasyStreaming != null) {

                                            mEasyStreaming.applyFilterLevel(
                                                    BEAUTIFY_LEVEL[mCurrentBeautifyLevelBtn.getId() - 1][0],
                                                    BEAUTIFY_LEVEL[mCurrentBeautifyLevelBtn.getId() - 1][1],
                                                    BEAUTIFY_LEVEL[mCurrentBeautifyLevelBtn.getId() - 1][2],
                                                    BEAUTIFY_LEVEL[mCurrentBeautifyLevelBtn.getId() - 1][3]);
                                        }
                                    } else {
                                        mEasyStreaming.applyFilterLevel(
                                                BEAUTIFY_LEVEL[DEFAULT_BEAUTIFY_FILTER_LEVEL - 1][0],
                                                BEAUTIFY_LEVEL[DEFAULT_BEAUTIFY_FILTER_LEVEL - 1][1],
                                                BEAUTIFY_LEVEL[DEFAULT_BEAUTIFY_FILTER_LEVEL - 1][2],
                                                BEAUTIFY_LEVEL[DEFAULT_BEAUTIFY_FILTER_LEVEL - 1][3]);
                                    }
                                }
                                break;
                        }
                        return true;
                    }
                });
                initLevelView();
            }
        } else {
            mToggleFilterImgBtn.setVisibility(View.GONE);
            LinearLayout beautyLevel = (LinearLayout)findViewById(R.id.beautify_level_bar);
            beautyLevel.removeAllViews();
            beautyLevel.setVisibility(View.GONE);
        }
    }

    public void handleUpdateCountdown(final int count) {
        if (mCountDownTxtv != null) {
            mCountDownTxtv.setVisibility(View.VISIBLE);
            mCountDownTxtv.setText(String.format("%d", count));
            ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f,0f, 1.0f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setDuration(COUNTDOWN_DELAY);
            scaleAnimation.setFillAfter(false);
            scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mCountDownTxtv.setVisibility(View.GONE);

                    if (count == COUNTDOWN_END_INDEX && mEasyStreaming != null && !isShutDownCountdown) {
                        mEasyStreaming.startRecording();
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            if (!isShutDownCountdown) {
                mCountDownTxtv.startAnimation(scaleAnimation);
            } else {
                mCountDownTxtv.setVisibility(View.GONE);
            }
        }
    }

    private void initLevelView() {
        LinearLayout beautyLevel = (LinearLayout)findViewById(R.id.beautify_level_bar);
        for (int i = 0; i < BEAUTIFY_LEVEL.length ; i++) {
            Button button = new Button(this);
            button.setId(i + 1);
            button.setText(String.valueOf(i + 1));
            button.setBackgroundResource(R.drawable.live_selector_room_beautify_level);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.weight = 1.0f;
            button.setLayoutParams(params);
            button.setOnClickListener(this);
            if(i + 1 == DEFAULT_BEAUTIFY_FILTER_LEVEL) {
                button.setSelected(true);
                mCurrentBeautifyLevelBtn = button;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                button.setActivated(true);
            }
            beautyLevel.addView(button);
        }
    }

    public void initEnv() {
        if (mSettings.isOpenLogRecoder()) {
            Log2FileUtil.getInstance().setLogCacheDir(mSettings.getLogCacheDir());
            Log2FileUtil.getInstance().startLog();
        }

        UStreamingProfile.Stream stream = new UStreamingProfile.Stream(rtmpPushStreamDomain, "live/" + mSettings.getPusblishStreamId());

        mPreviewContainer.setShowMode(UAspectFrameLayout.Mode.FULL);

        mStreamingProfile = new UStreamingProfile.Builder()
                .setContext(this)
                .setPreviewContainerLayout(mPreviewContainer)
                .setEncodeType(mSettings.getEncoderType())
                .setCameraId(whichCamera)
                .setResolution(UStreamingProfile.Resolution.RATIO_AUTO)
                .setVideoBitrate(mSettings.getVideoBitRate())
                .setVideoFrameRate(mSettings.getVideoFrameRate())
                .setAudioBitrate(UStreamingProfile.AUDIO_BITRATE_NORMAL)
                .setVideoCaptureOrientation(mSettings.getVideoCaptureOrientation())//UStreamingProfile.ORIENTATION_LANDSCAPE or UStreamingProfile.ORIENTATION_PORTRAIT
                .setStream(stream).build();

        handleShowStreamingInfo(mStreamingProfile.getEncodeType());
        mEasyStreaming = UEasyStreaming.Factory.newInstance(mStreamingProfile);

        mEasyStreaming.addListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v instanceof Button) {
            v.setSelected(true);
            if (mCurrentBeautifyLevelBtn != null) {
                mCurrentBeautifyLevelBtn.setSelected(false);
            }
            mCurrentBeautifyLevelBtn = (Button) v;
            if (mEasyStreaming != null) {
                mEasyStreaming.applyFilterLevel(
                        BEAUTIFY_LEVEL[v.getId() -1][0],
                        BEAUTIFY_LEVEL[v.getId() -1][1],
                        BEAUTIFY_LEVEL[v.getId() -1][2],
                        BEAUTIFY_LEVEL[v.getId() -1][3]);
            }
        }
    }
}