package com.hdcy.app.fragment.fourth.childpagers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hdcy.app.R;
import com.hdcy.app.adapter.ThirdPageFragmentAdapter;
import com.hdcy.app.adapter.ThirdPagesFragmentAdapter;
import com.hdcy.app.basefragment.BaseBackFragment;
import com.hdcy.app.event.StartBrotherEvent;
import com.hdcy.app.fragment.third.OfflineActivityFragment;
import com.hdcy.app.model.ActivityContent;
import com.hdcy.app.model.RootListInfo;
import com.hdcy.app.view.NoScrollListView;
import com.hdcy.base.utils.SizeUtils;
import com.hdcy.base.utils.net.NetHelper;
import com.hdcy.base.utils.net.NetRequestCallBack;
import com.hdcy.base.utils.net.NetRequestInfo;
import com.hdcy.base.utils.net.NetResponseInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by WeiYanGeorge on 2016-09-10.
 */

public class PersonalActivityFragment extends BaseBackFragment implements BGARefreshLayout.BGARefreshLayoutDelegate{

    private Toolbar mToolbar;
    private TextView title;

    private BGARefreshLayout mRefreshLayout;

    private ThirdPageFragmentAdapter mAdapter;

    private NoScrollListView mListView;


    private List<ActivityContent> activityContentList = new ArrayList<>();
    private RootListInfo rootListInfo = new RootListInfo();

    private boolean isLast;

    private int pagecount = 0;

    int bgimgWidth;
    int bgimgHeight;

    public static PersonalActivityFragment newInstance(){
        PersonalActivityFragment fragment = new PersonalActivityFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_activity, container, false);
        initView(view);
        bgimgWidth = SizeUtils.getScreenWidth();
        bgimgHeight = SizeUtils.dpToPx(200);
        initData();
        setListener();
        return view;
    }

    private void initView(View view){
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        title = (TextView) view.findViewById(R.id.toolbar_title);
        title.setText("我的活动");
        initToolbarNav(mToolbar);
        mListView = (NoScrollListView) view.findViewById(R.id.lv_mine_activity);
        mRefreshLayout = (BGARefreshLayout) view.findViewById(R.id.refreshLayout);
        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(getContext(),true));

    }

    private void setListener(){
/*        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(activityContentList.get(position).getType().equalsIgnoreCase("ACTIVITY")){
                    String ActivityId = activityContentList.get(position).getId()+"";
                    EventBus.getDefault().post(new StartBrotherEvent(OfflineActivityFragment.newInstance(ActivityId)));
                }else {
                    return;
                }
            }
        });*/


    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        activityContentList.clear();
        pagecount = 0;
        initData();
        mRefreshLayout.endRefreshing();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        pagecount++;
        if(isLast){
            mRefreshLayout.endLoadingMore();
            Toast.makeText(getActivity(), "没有更多的数据了", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            initData();
            mRefreshLayout.endLoadingMore();
            return true;
        }

    }

    private void initData(){
        GetMineActivityList();
    }

    private void setData(){
        mAdapter = new ThirdPageFragmentAdapter(getActivity(), activityContentList);
        mListView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new ThirdPageFragmentAdapter.OnItemClickListener() {
            @Override
            public void onItem(int position) {
                String ActivityId = activityContentList.get(position).getId()+"";
                EventBus.getDefault().post(new StartBrotherEvent(OfflineActivityFragment.newInstance(ActivityId)));
            }
        });

        mRefreshLayout.endLoadingMore();
    }

    public void GetMineActivityList(){
        NetHelper.getInstance().GetMineActivityList("ACTIVITY", pagecount, new NetRequestCallBack() {
            @Override
            public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                List<ActivityContent> temp = responseInfo.getActivityContentList();
                activityContentList.addAll(temp);
                rootListInfo = responseInfo.getRootListInfo();
                isLast = rootListInfo.isLast();
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
