package com.hdcy.app.fragment.third.child;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hdcy.app.OnItemClickListener;
import com.hdcy.app.R;
import com.hdcy.app.adapter.FirsPagersFragmentAdapter;
import com.hdcy.app.adapter.ThirdPagesFragmentAdapter;
import com.hdcy.app.basefragment.BaseFragment;
import com.hdcy.app.event.StartBrotherEvent;
import com.hdcy.app.event.TabSelectedEvent;
import com.hdcy.app.fragment.first.InfoDetailFragment;
import com.hdcy.app.model.ActivityContent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WeiYanGeorge on 2016-08-31.
 */

public class ThirdPagesFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{
    private RecyclerView mRecy;
    private SwipeRefreshLayout mRefreshLayout;

    private ThirdPagesFragmentAdapter mAdapter;

    private boolean mAtTop = true;

    private int mScrollTotal;

    private List<ActivityContent> activityContentList = new ArrayList<>();

    private int pagecount = 0;

    public static ThirdPagesFragment newInstance(int tagId){
        Bundle args = new Bundle();

        ThirdPagesFragment fragment = new ThirdPagesFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_third_pager_first, container, false);
        EventBus.getDefault().register(this);

        return view;
    }

    private void initView(View view) {

        mRecy = (RecyclerView) view.findViewById(R.id.recy);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);

        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.setOnRefreshListener(this);

        mAdapter = new ThirdPagesFragmentAdapter(_mActivity);


        LinearLayoutManager manager = new LinearLayoutManager(_mActivity);
        mRecy.setLayoutManager(manager);

/*        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view, RecyclerView.ViewHolder vh) {

                EventBus.getDefault().post(new StartBrotherEvent(InfoDetailFragment.newInstance(mAdapter.getItem(position).getId()+"")));

            }
        });*/



        mRecy.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mScrollTotal += dy;
                if (mScrollTotal <= 0) {
                    mAtTop = true;
                } else {
                    mAtTop = false;
                }
            }
        });

    }

    private void initData(){

    }

    @Override
    public void onRefresh() {

    }

    private void scrollToTop(){

    }

    /**
     * 选择tab事件
     */
    @Subscribe
    public void onTabSelectedEvent(TabSelectedEvent event) {
        if (mAtTop) {
            mRefreshLayout.setRefreshing(true);
            onRefresh();
        } else {
            scrollToTop();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRecy.setAdapter(null);
        EventBus.getDefault().unregister(this);
    }
}
