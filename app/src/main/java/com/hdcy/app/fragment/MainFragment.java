package com.hdcy.app.fragment;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hdcy.app.R;
import com.hdcy.app.activity.MainActivity;
import com.hdcy.app.basefragment.BaseFragment;
import com.hdcy.app.event.StartBrotherEvent;
import com.hdcy.app.event.TabSelectedEvent;
import com.hdcy.app.fragment.first.FirstFragment;
import com.hdcy.app.fragment.fourth.FourthFragment;
import com.hdcy.app.fragment.third.ThirdsFragment;
import com.hdcy.app.fragment.second.SecondFragment;
import com.hdcy.app.view.BottomBar;
import com.hdcy.app.view.BottomBarTab;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by WeiYanGeorge on 2016-08-23.
 */

public class MainFragment extends BaseFragment {

    private static final int REQ_MSG = 10;

    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;
    public static final int FOURTH = 3;

    private SupportFragment[] mFragments = new SupportFragment[4];

    private BottomBar mBottomBar;


    public static MainFragment newInstance() {

        Bundle args = new Bundle();

        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,container,false);

        if(savedInstanceState == null){
            mFragments[FIRST] = FirstFragment.newInstance();
            mFragments[SECOND] = SecondFragment.newInsatance();
            mFragments[THIRD] = ThirdsFragment.newInstance();
            mFragments[FOURTH] = FourthFragment.newInstance();
            loadMultipleRootFragment(R.id.fl_tab_container,FIRST,mFragments[FIRST],
                    mFragments[SECOND],
                    mFragments[THIRD],
                    mFragments[FOURTH]);
        }else {
            mFragments[FIRST] = findChildFragment(FirstFragment.class);
            mFragments[SECOND] = findChildFragment(SecondFragment.class);
            mFragments[THIRD] = findChildFragment(ThirdsFragment.class);
            mFragments[FOURTH] = findChildFragment(FourthFragment.class);
        }

        initView(view);
        String[] mPermissionList = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE,Manifest.permission.READ_LOGS,Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.SET_DEBUG_APP,Manifest.permission.SYSTEM_ALERT_WINDOW,Manifest.permission.GET_ACCOUNTS};
        ActivityCompat.requestPermissions(getActivity(),mPermissionList, 100);
        return view;
    }

    private void initView(View view){
        EventBus.getDefault().register(this);
        mBottomBar = (BottomBar) view.findViewById(R.id.bottomBar);

        mBottomBar
                .addItem(new BottomBarTab(_mActivity, R.drawable.tab_icon_information_default, "资讯"))
                .addItem(new BottomBarTab(_mActivity, R.drawable.tab_icon_topic_default, "视频"))
                .addItem(new BottomBarTab(_mActivity, R.drawable.tab_icon_activity_default, "活动"))
                .addItem(new BottomBarTab(_mActivity, R.mipmap.tab_icon_user_default, "我的"));

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
                // 这里推荐使用EventBus来实现 -> 解耦
                // 在FirstPagerFragment,FirstHomeFragment中接收, 因为是嵌套的Fragment
                // 主要为了交互: 重选tab 如果列表不在顶部则移动到顶部,如果已经在顶部,则刷新
                EventBus.getDefault().post(new TabSelectedEvent(position));
            }
        });

    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (requestCode == REQ_MSG && resultCode == RESULT_OK) {

        }
    }

    /**
     * start other BrotherFragment
     */
    @Subscribe
    public void startBrother(StartBrotherEvent event) {
        start(event.targetFragment);
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}
