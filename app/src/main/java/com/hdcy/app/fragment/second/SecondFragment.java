package com.hdcy.app.fragment.second;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hdcy.app.R;
import com.hdcy.app.basefragment.BaseLazyMainFragment;
import com.hdcy.app.fragment.second.child.ViewPagerFragment;
import com.hdcy.app.fragment.second.child.childpager.FirstPagerFragment;
import com.hdcy.app.fragment.second.child.childpager.OtherPagerFragment;

/**
 * Created by WeiYanGeorge on 2016-08-15.
 */

public class SecondFragment extends BaseLazyMainFragment{

    public static SecondFragment newInstance() {

        Bundle args = new Bundle();

        SecondFragment fragment = new SecondFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        initView(savedInstanceState);
        return view;
    }

    private void initView(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            loadRootFragment(R.id.fl_second_container, ViewPagerFragment.newInstance());
        }
    }

    @Override
    protected void initLazyView(@Nullable Bundle savedInstanceState) {
        // 这里可以不用懒加载,因为Adapter的场景下,Adapter内的子Fragment只有在父Fragment是show状态时,才会被Attach,Create
    }
}
