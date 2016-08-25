package com.hdcy.app.fragment.first;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hdcy.app.R;
import com.hdcy.app.basefragment.BaseFragment;
import com.hdcy.app.fragment.first.childpager.FirstPagersFragment;
import com.hdcy.app.model.NewsCategory;
import com.hdcy.base.utils.net.NetHelper;
import com.hdcy.base.utils.net.NetRequestCallBack;
import com.hdcy.base.utils.net.NetRequestInfo;
import com.hdcy.base.utils.net.NetResponseInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yanweigeorge on 16/8/16.
 */
public class ViewPagerFragment extends BaseFragment {
    private TabLayout mTab;
    private ViewPager mViewPager;
    private List<NewsCategory> newsCategoryList = new ArrayList<>();


    public static ViewPagerFragment newInstance() {

        Bundle args = new Bundle();

        ViewPagerFragment fragment = new ViewPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second_pager, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        mTab = (TabLayout) view.findViewById(R.id.tab);
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        mTab.addTab(mTab.newTab());
        mTab.addTab(mTab.newTab());
        mTab.addTab(mTab.newTab());
        mTab.addTab(mTab.newTab());
        mTab.addTab(mTab.newTab());

    }


    private void initData() {
        getNewsCategory();
    }

    private void setAdapter(){
        mViewPager.setAdapter(new ViewPageFragmentAdapter(getChildFragmentManager(),newsCategoryList));
        mTab.setupWithViewPager(mViewPager);
    }

    private void getNewsCategory(){
        NetHelper.getInstance().GetNewsCategoryList(new NetRequestCallBack() {
            @Override
            public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                NewsCategory newsCategory = new NewsCategory();
                newsCategory.setName("全部");
                newsCategory.setId(1011);
                Log.e("全部id",newsCategory.getId()+"");
                newsCategoryList.add(newsCategory);
                List<NewsCategory> newsCategoryListTemp = responseInfo.getNewsCategoryList();
                newsCategoryList.addAll(newsCategoryListTemp);
                setAdapter();
            }

            @Override
            public void onError(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {

            }

            @Override
            public void onFailure(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {

            }

        });
    }

    public class ViewPageFragmentAdapter extends FragmentPagerAdapter {

        private List<NewsCategory> data;




        public ViewPageFragmentAdapter(FragmentManager fm, List<NewsCategory> data) {
            super(fm);
            this.data = data;
        }

        @Override
        public Fragment getItem(int position) {
            return FirstPagersFragment.newInstance(data.get(position).getId());
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


    /**
     * Created by WeiYanGeorge on 2016-08-18.
     */

}