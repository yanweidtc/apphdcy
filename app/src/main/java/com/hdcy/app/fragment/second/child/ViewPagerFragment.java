package com.hdcy.app.fragment.second.child;

import android.content.Context;
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
import com.hdcy.app.activity.NewsActivity;
import com.hdcy.app.adapter.PageFragmentAdapter;
import com.hdcy.app.basefragment.BaseFragment;
import com.hdcy.app.fragment.second.child.childpager.FirstPagerFragment;
import com.hdcy.app.fragment.second.child.childpager.FirstPagersFragment;
import com.hdcy.app.fragment.second.child.childpager.OtherPagerFragment;
import com.hdcy.app.model.NewsCategory;
import com.hdcy.base.utils.net.NetHelper;
import com.hdcy.base.utils.net.NetRequestCallBack;
import com.hdcy.base.utils.net.NetRequestInfo;
import com.hdcy.base.utils.net.NetResponseInfo;

import org.xutils.common.util.LogUtil;

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
        int Tabcount = newsCategoryList.size();
        for(int i = 0;i <= Tabcount;i++) {
            mTab.addTab(mTab.newTab());
        }

    }

    private void initData() {
        getNewsCategory();
    }

    private void setAdapter(){
        mViewPager.setAdapter(new PageFragmentAdapter(getChildFragmentManager(),newsCategoryList));
        mTab.setupWithViewPager(mViewPager);
    }

    private void getNewsCategory(){
        NetHelper.getInstance().GetNewsCategoryList(new NetRequestCallBack() {
            @Override
            public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                NewsCategory newsCategory = new NewsCategory();
                List<NewsCategory> newsCategoryListTemp = responseInfo.getNewsCategoryList();
                newsCategory.setName("全部");
                newsCategory.setId(newsCategoryListTemp.get(0).getId()-1);
                newsCategoryList.add(newsCategory);
                newsCategoryList.addAll(newsCategoryListTemp);
                Log.e("listsize",newsCategoryList.size()+"");
                Log.e("listName",newsCategoryList.get(0).getName());
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




    public class PageFragmentAdapter extends FragmentPagerAdapter {

        private List<NewsCategory> data;




        public PageFragmentAdapter(FragmentManager fm, List<NewsCategory> data) {
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

}