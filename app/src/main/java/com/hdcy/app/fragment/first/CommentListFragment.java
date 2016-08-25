package com.hdcy.app.fragment.first;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hdcy.app.R;
import com.hdcy.app.activity.MainActivity;
import com.hdcy.app.adapter.CommentListFragmentAdapter;
import com.hdcy.app.basefragment.BaseBackFragment;
import com.hdcy.app.event.TabSelectedEvent;
import com.hdcy.app.model.CommentsContent;
import com.hdcy.base.utils.net.NetHelper;
import com.hdcy.base.utils.net.NetRequestCallBack;
import com.hdcy.base.utils.net.NetRequestInfo;
import com.hdcy.base.utils.net.NetResponseInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

//import com.hdcy.app.adapter.CommentListFragmentAdapter;

/**
 * Created by WeiYanGeorge on 2016-08-23.
 */

public class CommentListFragment extends BaseBackFragment implements SwipeRefreshLayout.OnRefreshListener{

    private RecyclerView mRecy;
    private SwipeRefreshLayout mRefreshLayout;
    private boolean mAtTop = true;

    private int mScrollTotal;

    private String content;

    private boolean isEdit;//是否编辑过


    private CommentListFragmentAdapter mAdapter;

    //加载更多页数,默认第一页为0
    private int pagecount = 0;

    private String tagId;


    private List<CommentsContent>  commentsList = new ArrayList<>();

    public static CommentListFragment newInstance(String tagId) {
        CommentListFragment fragment = new CommentListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("param",tagId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_commentlist,container,false);
        EventBus.getDefault().register(this);
        Bundle bundle = getArguments();
        if(bundle !=null){
            tagId = bundle.getString("param");
        }
        initView(view);
        initData();
        return view;
    }

    private void initView(View view){
        mRecy = (RecyclerView) view. findViewById(R.id.recy);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);

        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.setOnRefreshListener(this);

        mAdapter = new CommentListFragmentAdapter(_mActivity);

        LinearLayoutManager manager = new LinearLayoutManager(_mActivity);

        mRecy.setLayoutManager(manager);

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



    private  void initData(){
        GetCommentsList();
    }

    private void setData(){
        mAdapter.setDatas(commentsList);

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

    public void GetCommentsList(){
        NetHelper.getInstance().GetCommentsList( tagId, new NetRequestCallBack() {
            @Override
            public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                if(commentsList.isEmpty()){
                    List<CommentsContent> commentListFragmentListtemp = responseInfo.getCommentsContentList();
                    commentsList.addAll(commentListFragmentListtemp);
                    Log.e("CommentListsize",commentsList.size()+"");
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
