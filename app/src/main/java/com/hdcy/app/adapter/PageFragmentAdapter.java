package com.hdcy.app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hdcy.app.fragment.second.child.childpager.FirstPagerFragment;
import com.hdcy.app.fragment.second.child.childpager.OtherPagerFragment;

/**
 * Created by WeiYanGeorge on 2016-08-15.
 */

public class PageFragmentAdapter extends FragmentPagerAdapter {
    private String[] mTab = new String[]{"推荐", "热门", "收藏"};

    public PageFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return FirstPagerFragment.newInstance();
        } else {
            return OtherPagerFragment.newInstance(position);
        }
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
