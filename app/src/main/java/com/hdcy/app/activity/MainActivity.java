package com.hdcy.app.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.hdcy.app.R;
import com.hdcy.app.fragment.MainFragment;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * Created by WeiYanGeorge on 2016-08-23.
 */

public class MainActivity extends SupportActivity {

    private static final String TAG = "MainActivity";
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        String username="chiwenheng";
        String pwd="123456";


        //注册失败会抛出HyphenateException
//        try {
//            EMClient.getInstance().createAccount(username, pwd);//同步方法
//        } catch (HyphenateException e) {
//            e.printStackTrace();
//        }

        EMClient.getInstance().login(username,pwd,new EMCallBack() {//回调
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


        if (savedInstanceState == null) {
            loadRootFragment(R.id.fl_container_activity1, MainFragment.newInstance());
        }

        //注册失败会抛出HyphenateException
//        EMClient.getInstance().createAccount(username, pwd);//同步方法

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

}
