package com.hdcy.app.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.hdcy.app.R;
import com.hdcy.app.fragment.BootFragment;
import com.hdcy.app.fragment.MainFragment;
import com.hdcy.base.utils.BaseUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.util.NetUtils;
import com.umeng.socialize.PlatformConfig;

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


        //注册失败会抛出HyphenateException
//        try {
//            EMClient.getInstance().createAccount(username, pwd);//同步方法
//        } catch (HyphenateException e) {
//            e.printStackTrace();
//        }

        EMClient.getInstance().login(BaseUtils.userName,BaseUtils.userPwd,new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.d(TAG, "登录聊天服务器成功！");
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Log.d(TAG, "onError() called with: " + "code = [" + code + "], message = [" + message + "]");
                Log.d(TAG, "登录聊天服务器失败！");
            }
        });

        //注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(new MyConnectionListener());

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
                    }else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        // 显示帐号在其他设备登录
                        showToast("显示帐号在其他设备登录");
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

}
