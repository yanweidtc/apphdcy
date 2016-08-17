package com.hdcy.app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hdcy.app.fragment.second.child.childpager.FirstPagersFragment;
import com.hdcy.app.model.NewsCategory;

import java.util.List;

/**
 * Created by WeiYanGeorge on 2016-08-17.
 */

public class ViewPageFragmentAdapter extends FragmentPagerAdapter {

    private List<NewsCategory> data;




    public ViewPageFragmentAdapter(FragmentManager fm, List<NewsCategory> data) {
        super(fm);
        this.data = data;
    }

    @Override
    public Fragment getItem(int position) {
                return null;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return data.get(position).getName();
    }


}
