package com.hdcy.app.fragment.first.childpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hdcy.app.OnItemClickListener;
import com.hdcy.app.R;
import com.hdcy.app.activity.MainActivity;
import com.hdcy.app.adapter.FirsPagersFragmentAdapter;
import com.hdcy.app.basefragment.BaseFragment;
import com.hdcy.app.event.StartBrotherEvent;
import com.hdcy.app.event.TabSelectedEvent;
import com.hdcy.app.fragment.first.InfoDetailFragment;
import com.hdcy.app.model.Content;
import com.hdcy.base.utils.net.NetHelper;
import com.hdcy.base.utils.net.NetRequestCallBack;
import com.hdcy.base.utils.net.NetRequestInfo;
import com.hdcy.base.utils.net.NetResponseInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WeiYanGeorge on 2016-08-17.
 */

public class FirstPagersFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{
    private RecyclerView mRecy;
    private SwipeRefreshLayout mRefreshLayout;

    private FirsPagersFragmentAdapter mAdapter;

    private boolean mAtTop = true;

    private int mScrollTotal;

    private List<Content> contentList = new ArrayList<>();

    private int tagId;

    //加载更多页数,默认第一页为0
    private int pagecount = 0;

    public static FirstPagersFragment newInstance(int tagId) {

        Bundle args = new Bundle();
        args.putInt("param",tagId);
        FirstPagersFragment fragment = new FirstPagersFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second_pager_first, container, false);
        EventBus.getDefault().register(this);
        tagId = getArguments().getInt("param");
        Log.e("TagValue",tagId+"");
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        mRecy = (RecyclerView) view.findViewById(R.id.recy);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);

        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.setOnRefreshListener(this);

        mAdapter = new FirsPagersFragmentAdapter(_mActivity);

        LinearLayoutManager manager = new LinearLayoutManager(_mActivity);
        mRecy.setLayoutManager(manager);

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view, RecyclerView.ViewHolder vh) {

                EventBus.getDefault().post(new StartBrotherEvent(InfoDetailFragment.newInstance(mAdapter.getItem(position).getId()+"")));

            }
        });



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
        if(tagId == 1011){
        getWholeNewsArticleInfo();
        }else {
            getNewsArticleInfo();
        }
    }

    private void setData(){
        mAdapter.setDatas(contentList);

        mRecy.setAdapter(mAdapter);


    }

    @Override
    public void onRefresh() {
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }

    private void scrollToTop() {
        mRecy.smoothScrollToPosition(0);
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

    private void getNewsArticleInfo(){
        NetHelper.getInstance().GetNewsArticleContent(pagecount,tagId,new NetRequestCallBack() {
            @Override
            public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
            if(contentList.isEmpty()){
                List<Content> contentListtemp = responseInfo.getContentList();
                contentList.addAll(contentListtemp);
                Log.e("Articlesize",contentList.size()+"");
                }
                setData();

            }

            @Override
            public void onError(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {

            }

            @Override
            public void onFailure(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {

            }
        });
    }

    private void getWholeNewsArticleInfo(){
        NetHelper.getInstance().GetWholeNewsArticleContent(pagecount,new NetRequestCallBack() {
            @Override
            public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                if(contentList.isEmpty()){
                    List<Content> contentListtemp = responseInfo.getContentList();
                    contentList.addAll(contentListtemp);
                    Log.e("Articlesize",contentList.size()+"");
                }
                setData();

            }

            @Override
            public void onError(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {

            }

            @Override
            public void onFailure(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {

            }
        });
    }
}
