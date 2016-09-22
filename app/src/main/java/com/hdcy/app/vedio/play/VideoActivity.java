package com.hdcy.app.vedio.play;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.easemob.chat.EMMessage;
import com.hdcy.app.R;
import com.hdcy.app.chat.ChatAdapter;
import com.hdcy.app.chat.Message;
import com.hdcy.app.vedio.preference.Settings;
import com.hdcy.base.utils.BaseUtils;
import com.ucloud.common.logger.L;
import com.ucloud.player.widget.v2.UVideoView;

import java.util.ArrayList;
import java.util.List;


public class VideoActivity extends AppCompatActivity implements UVideoView.Callback ,View.OnClickListener {

    private static final String TAG = "VideoActivity";

    private UVideoView mVideoView;

    String rtmpPlayStreamUrl = "http://rtmp3.usmtd.ucloud.com.cn/live/%s.flv";
    Settings mSettings;



    private ListView mlistview;
    private List<Message> mDatas=new ArrayList<>();
    private EditText etInput;
    private Button mBtn4Send;
    private ChatAdapter mAdapter;
    private String groupid ="";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_v2);
        mVideoView = (UVideoView) findViewById(R.id.videoview);

        mSettings = new Settings(this);

        mVideoView.setPlayType(UVideoView.PlayType.LIVE);
        mVideoView.setPlayMode(UVideoView.PlayMode.NORMAL);
        mVideoView.setRatio(UVideoView.VIDEO_RATIO_FILL_PARENT);
        mVideoView.setDecoder(UVideoView.DECODER_VOD_SW);

        mVideoView.registerCallback(this);

        mVideoView.setVideoPath(String.format(rtmpPlayStreamUrl, mSettings.getPusblishStreamId()));



        mlistview = (ListView)findViewById(R.id.lv_data);
        mBtn4Send= (Button) this.findViewById(R.id.sendBtn);
        etInput= (EditText) this.findViewById(R.id.sendMsgEdt);
        mAdapter=new ChatAdapter(this,mDatas);

        mBtn4Send.setOnClickListener(this);
        mlistview.setAdapter(mAdapter);

        initIM();

    }

    private void initIM() {



    }



    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.sendBtn){
            String content =etInput.getText().toString();
            if(TextUtils.isEmpty(content)){
                return;
            }
            etInput.setText("");

        }

    }



    private void updateView(List<EMMessage> messages) {
        //收到消息
        for(EMMessage item:messages){
            Message msg;
            msg = new Message();
            if(item.getTo().equals(BaseUtils.userName)){// 左边的文字
                msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
            }else{
                msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);// 右边的文字
            }

            msg.setValue(item.getBody().toString().replace("txt:",""));
            mDatas.add(msg);
        }

        mAdapter.notifyDataSetChanged();
        mlistview.setSelection(mDatas.size());
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoView != null) {
            mVideoView.setVolume(0,0);
            mVideoView.stopPlayback();
            mVideoView.release(true);
        }
    }

    @Override
    public void onEvent(int what, String message) {
        Log.d(TAG, "what:" + what + ", message:" + message);
        switch (what) {
            case UVideoView.Callback.EVENT_PLAY_START:
                break;
            case UVideoView.Callback.EVENT_PLAY_PAUSE:
                break;
            case UVideoView.Callback.EVENT_PLAY_STOP:
                break;
            case UVideoView.Callback.EVENT_PLAY_COMPLETION:
                Toast.makeText(this, "EVENT_PLAY_COMPLETION", Toast.LENGTH_SHORT).show();
                break;
            case UVideoView.Callback.EVENT_PLAY_DESTORY:
                break;
            case UVideoView.Callback.EVENT_PLAY_ERROR:
                Toast.makeText(this, "EVENT_PLAY_ERROR:" + message, Toast.LENGTH_SHORT).show();
                break;
            case UVideoView.Callback.EVENT_PLAY_RESUME:
                break;
            case UVideoView.Callback.EVENT_PLAY_INFO_BUFFERING_START:
                L.e(TAG, "network block start....");
//              Toast.makeText(VideoActivity.this, "unstable network", Toast.LENGTH_SHORT).show();
                break;
            case UVideoView.Callback.EVENT_PLAY_INFO_BUFFERING_END:
                L.e(TAG, "network block end....");
                break;
        }
    }

    public void close(View view) {
        finish();
    }
}
