package com.hdcy.app.fragment.third;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hdcy.app.R;
import com.hdcy.app.basefragment.BaseLazyMainFragment;
import com.hdcy.app.fragment.third.child.ThirdPagesFragment;

/**
 * Created by WeiYanGeorge on 2016-08-30.
 */

public class ThirdFragment extends BaseLazyMainFragment{
    private Toolbar mToolbar;
    private TabLayout mTab;
    private ViewPager mViewPager;

    private TextView title;

    public static ThirdFragment newInstance(){
        Bundle args = new Bundle();
        ThirdFragment fragment = new ThirdFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_third_pager, container,false);
        initView(view);
        initData();
        return view;

    }

    private void initView(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mTab = (TabLayout) view.findViewById(R.id.tab);
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        title = (TextView) view.findViewById(R.id.toolbar_title);
        title.setText("活动");

        mTab.addTab(mTab.newTab());
        mTab.addTab(mTab.newTab());
        mTab.addTab(mTab.newTab());

    }

    @Override
    protected void initLazyView(@Nullable Bundle savedInstanceState) {
        mViewPager.setAdapter(new ThirdFragment.SecondViewPageFragmentAdapter(getChildFragmentManager()));
        mTab.setupWithViewPager(mViewPager);

    }

    private void setData(){

    }

    private void initData(){

    }

    public class SecondViewPageFragmentAdapter extends FragmentPagerAdapter{

        private String[] mTab = new String[]{"全部","线上活动","线下活动"};
        private String[] activitytype = new String[]{null, "ONLINE","ACTIVITY"};

        public SecondViewPageFragmentAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ThirdPagesFragment.newInstance(activitytype[position]);
        }

        @Override
        public int getCount() {
             return mTab.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTab[position];
        }
    }
}
