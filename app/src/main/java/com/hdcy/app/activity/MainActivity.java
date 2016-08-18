package com.hdcy.app.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.WindowManager;


import com.hdcy.app.R;
import com.hdcy.app.basefragment.BaseLazyMainFragment;
import com.hdcy.app.fragment.second.SecondFragment;
import com.hdcy.app.model.NewsCategory;
import com.hdcy.app.view.BottomBar;
import com.hdcy.app.view.BottomBarTab;
import com.hdcy.base.utils.net.NetHelper;
import com.hdcy.base.utils.net.NetRequestCallBack;
import com.hdcy.base.utils.net.NetRequestInfo;
import com.hdcy.base.utils.net.NetResponseInfo;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.util.LogUtil;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

public class MainActivity extends SupportActivity implements BaseLazyMainFragment.OnBackToFirstListener{
    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;
    public static final int FOURTH = 3;

    private SupportFragment[] mFragments = new SupportFragment[4];
    private BottomBar mBottomBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null){
            mFragments[FIRST] = SecondFragment.newInstance();
            loadMultipleRootFragment(R.id.fl_container, FIRST,mFragments[FIRST]);
        }else {
            mFragments[FIRST] = findFragment(SecondFragment.class);
        }


        initView();
    }

    @Override
    protected FragmentAnimator onCreateFragmentAnimator() {
        return super.onCreateFragmentAnimator();
    }
        private void initView(){
            mBottomBar = (BottomBar) findViewById(R.id.bottomBar);

            mBottomBar.addItem(new BottomBarTab(this, R.mipmap.ic_home_white_24dp))
                    .addItem(new BottomBarTab(this,R.mipmap.ic_format_list_numbered_white_24dp));

            mBottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
                @Override
                public void onTabSelected(int position, int prePosition) {
                    showHideFragment(mFragments[position], mFragments[prePosition]);
                }

                @Override
                public void onTabUnselected(int position) {

                }

                @Override
                public void onTabReselected(int position) {
                    SupportFragment currentFragment = mFragments[position];
                    int count = currentFragment.getChildFragmentManager().getBackStackEntryCount();

                }
            });
        }
    @Override
    public void onBackPressedSupport() {
        // 对于 4个类别的主Fragment内的回退back逻辑,已经在其onBackPressedSupport里各自处理了
        super.onBackPressedSupport();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackToFirstFragment() {
        mBottomBar.setCurrentItem(0);
    }



}
