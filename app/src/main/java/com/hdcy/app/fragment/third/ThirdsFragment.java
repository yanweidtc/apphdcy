package com.hdcy.app.fragment.third;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hdcy.app.R;
import com.hdcy.app.adapter.ThirdPageFragmentAdapter;
import com.hdcy.app.basefragment.BaseLazyMainFragment;
import com.hdcy.app.event.StartBrotherEvent;
import com.hdcy.app.fragment.fourth.FourthFragment;
import com.hdcy.app.model.ActivityContent;
import com.hdcy.app.model.LeaderInfo;
import com.hdcy.app.model.RootListInfo;
import com.hdcy.base.BaseInfo;
import com.hdcy.base.utils.SizeUtils;
import com.hdcy.base.utils.net.NetHelper;
import com.hdcy.base.utils.net.NetRequestCallBack;
import com.hdcy.base.utils.net.NetRequestInfo;
import com.hdcy.base.utils.net.NetResponseInfo;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by WeiYanGeorge on 2016-09-10.
 */

public class ThirdsFragment extends BaseLazyMainFragment implements BGARefreshLayout.BGARefreshLayoutDelegate{

    private Toolbar mToolbar;
    private TextView title;

    private BGARefreshLayout mRefreshLayout;

    private ThirdPageFragmentAdapter mAdapter;

    private ListView mListView;

    private List<ActivityContent> activityContentList = new ArrayList<>();
    private RootListInfo rootListInfo = new RootListInfo();
    private List<LeaderInfo> leaderInfoList = new ArrayList<>();

    private boolean isLast;

    private int pagecount = 0;

    int bgimgWidth;
    int bgimgHeight;

    private ImageView iv_leader_background;
    private TextView tv_leader_name;

    public static ThirdsFragment newInsatance(){
        Bundle args = new Bundle();
        ThirdsFragment fragment = new ThirdsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thirds_pager, container, false);
        bgimgWidth = SizeUtils.getScreenWidth();
        bgimgHeight = SizeUtils.dpToPx(200);
        initView(view);
        initData();
        setListener();
        return view;
    }

    private void initView(View view){
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        title = (TextView) view.findViewById(R.id.toolbar_title);
        title.setText("活动");
        mListView = (ListView) view.findViewById(R.id.lv_mine_activity);
        mRefreshLayout = (BGARefreshLayout) view.findViewById(R.id.refreshLayout);
        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(getContext(),true));
        View headview = View.inflate(getContext(),R.layout.item_leader,null);
        iv_leader_background =(ImageView) headview.findViewById(R.id.iv_leader_background);
        tv_leader_name = (TextView) headview.findViewById(R.id.tv_leader_name);
        mListView.addHeaderView(headview);
    }

    private void initData(){
        GetActivityList();
        GetLeaderInfo();
    }

    private void setListener(){
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(activityContentList.get(position).getType().equalsIgnoreCase("ACTIVITY")){
                    String ActivityId = activityContentList.get(position).getId()+"";
                    EventBus.getDefault().post(new StartBrotherEvent(OfflineActivityFragment.newInstance(ActivityId)));
                }else {
                    return;
                }
            }
        });
    }

    private void setData(){
        mAdapter = new ThirdPageFragmentAdapter(getActivity(), activityContentList);
        mListView.setAdapter(mAdapter);
        mRefreshLayout.endLoadingMore();
    }
    private void setData1(){
        Picasso.with(getContext()).load(leaderInfoList.get(0).getTopImage())
                .placeholder(BaseInfo.PICASSO_PLACEHOLDER)
                .resize(bgimgWidth,bgimgHeight)
                .centerCrop()
                .config(Bitmap.Config.RGB_565)
                .into(iv_leader_background);
        tv_leader_name.setText(leaderInfoList.get(0).getNickname()+"");

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
            return true;
        }

    }

    private void GetActivityList(){
        NetHelper.getInstance().GetPaticipationList("ACTIVITY", pagecount, new NetRequestCallBack() {
            @Override
            public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                List<ActivityContent> activityContentstemp = responseInfo.getActivityContentList();
                rootListInfo = responseInfo.getRootListInfo();
                activityContentList.addAll(activityContentstemp);
                isLast = rootListInfo.isLast();
                Log.e("ActivityContentList",activityContentList.size()+"");
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

    public void GetLeaderInfo(){
        NetHelper.getInstance().GetLeaderInfo(new NetRequestCallBack() {
            @Override
            public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                leaderInfoList = responseInfo.getLeaderInfo();
                setData1();
            }

            @Override
            public void onError(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {

            }

            @Override
            public void onFailure(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {

            }
        });
    }


    @Override
    protected void initLazyView(@Nullable Bundle savedInstanceState) {

    }
}
