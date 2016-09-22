package com.hdcy.app.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.util.NetUtils;
import com.hdcy.app.R;
import com.hdcy.app.fragment.BootFragment;
import com.hdcy.app.fragment.MainFragment;
import com.hdcy.app.view.SFProgressDialog;
import com.umeng.socialize.PlatformConfig;

import java.util.Timer;
import java.util.TimerTask;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Created by WeiYanGeorge on 2016-08-23.
 */

public class MainActivity extends SupportActivity {

    private static final String TAG = "MainActivity";

    private  final static int WAIT_TIME = 500; //启动页加载完成等待时间


    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg){

        }
    };
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
//        String username="chiwenheng";
//        String pwd="123456";

        //umeng

        PlatformConfig.setWeixin("wx6619f92e0cc550da","431c26c014b6ea3c4aab0b1d8016b2b9");
        if (savedInstanceState == null) {
            loadRootFragment(R.id.fl_container_activity1, BootFragment.newInstance());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadRootFragment(R.id.fl_container_activity1, MainFragment.newInstance());
                }
            }, 3000);

           // loadRootFragment(R.id.fl_container_activity1, MainFragment.newInstance());
        }

    }


    @Override
    public void onBackPressedSupport() {
        // 对于 4个类别的主Fragment内的回退back逻辑,已经在其onBackPressedSupport里各自处理了
        super.onBackPressedSupport();
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        // 设置横向(和安卓4.x动画相同)
        return new DefaultHorizontalAnimator();
    }

    public  void showToast(String content){
        Toast.makeText(this,content,Toast.LENGTH_SHORT).show();
    }

    //实现ConnectionListener接口
    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
        }
        @Override
        public void onDisconnected(final int error) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if(error == EMError.USER_REMOVED){
                        // 显示帐号已经被移除
                        showToast("显示帐号已经被移除");
//                    }else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
//                        // 显示帐号在其他设备登录
//                        showToast("显示帐号在其他设备登录");
                    } else {
                        if (NetUtils.hasNetwork(MainActivity.this)) {
                            //连接不到聊天服务器
                            showToast("连接不到聊天服务器");
                        }else{
                            //当前网络不可用，请检查网络设置
                            showToast("当前网络不可用，请检查网络设置");
                        }
                    }
                }
            });
        }
    }

    private final static long LOADING_WAITING_TIME = 2000;// 网络加载对话框出现等待时间
    private boolean isNeedShowProgress;// 是否需要显示加载对话框
    private SFProgressDialog mProgressDialog;
    private Timer timer;
    private Toast mToast;
    private Dialog mDialog;
    private Dialog pwdDialog;
    private PopupWindow pop;


    /**
     * 显示等待框
     *
     * @param str
     * @param isTouchHide
     * @param isNeedWait  是否需要等待一段时间再显示等待框(默认true)
     */
    public void showProgressDialog(String str, boolean isTouchHide,
                                   boolean isNeedWait) {
        isNeedShowProgress = true;
        if (mProgressDialog == null) {
            try {
                mProgressDialog = SFProgressDialog.createProgressDialog(this);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        if (timer == null) {
            timer = new Timer();
        }
        mProgressDialog.setMessage(str);
        mProgressDialog.setCanceledOnTouchOutside(isTouchHide);
        if (!mProgressDialog.isShowing() && !isFinishing()) {
            if (isNeedWait) {
                timer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                if (isNeedShowProgress) {
                                    mProgressDialog.show();
                                }
                            }
                        });
                    }
                }, LOADING_WAITING_TIME);
            } else {
                if (isNeedShowProgress) {
                    mProgressDialog.show();
                }
            }
        }
    }

    /**
     * 隐藏等待框
     */
    public void dismissProgressDialog() {
        isNeedShowProgress = false;
        if (mProgressDialog != null) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * 销毁等待框
     */
    public void cancelProgressDialog() {
        dismissProgressDialog();
        if (mProgressDialog != null) {
            mProgressDialog.cancel();
            mProgressDialog = null;
        }
    }


}
