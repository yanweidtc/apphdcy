package com.hdcy.app.fragment.third.child;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hdcy.app.OnItemClickListener;
import com.hdcy.app.R;
import com.hdcy.app.adapter.FirsPagersFragmentAdapter;
import com.hdcy.app.adapter.ThirdPageFragmentAdapter;
import com.hdcy.app.adapter.ThirdPagesFragmentAdapter;
import com.hdcy.app.basefragment.BaseFragment;
import com.hdcy.app.event.StartBrotherEvent;
import com.hdcy.app.event.TabSelectedEvent;
import com.hdcy.app.fragment.first.InfoDetailFragment;
import com.hdcy.app.fragment.third.OfflineActivityFragment;
import com.hdcy.app.model.ActivityContent;
import com.hdcy.app.model.RootListInfo;
import com.hdcy.app.view.NoScrollListView;
import com.hdcy.base.BaseInfo;
import com.hdcy.base.utils.net.NetHelper;
import com.hdcy.base.utils.net.NetRequestCallBack;
import com.hdcy.base.utils.net.NetRequestInfo;
import com.hdcy.base.utils.net.NetResponseInfo;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;


/**
 * Created by WeiYanGeorge on 2016-08-31.
 */

public class ThirdPagesFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate{
    private NoScrollListView mRecy;
    private BGARefreshLayout mRefreshLayout;
    private BGABanner mBanner;

    private ImageView iv_recommend_first, iv_recommend_second;

    private ThirdPageFragmentAdapter mAdapter;

    private boolean mAtTop = true;
    private boolean isDatacompleted =false;



    private List<ActivityContent> activityContentList = new ArrayList<>();
    private List<ActivityContent> activityHotList = new ArrayList<>();
    private List<ActivityContent> activityRecommend = new ArrayList<>();

    private List<String> imgurls = new ArrayList<>();
    private List<String> tips = new ArrayList<>();

    private RootListInfo rootListInfo = new RootListInfo();

    private boolean isLast;
    private boolean isLoadMore;

    private int pagecount = 0;

    private String activityType;

    public static ThirdPagesFragment newInstance(String activityType){
        Bundle args = new Bundle();
        args.putString("param", activityType);
        ThirdPagesFragment fragment = new ThirdPagesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_third_pager_first, container, false);
        mRecy = (NoScrollListView) view.findViewById(R.id.lv_activity);
        mRecy.setFocusable(false);
        EventBus.getDefault().register(this);
        activityType = getArguments().getString("param");
        Log.e("activityType",activityType+"");
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        mBanner =(BGABanner) view.findViewById(R.id.banner);
        iv_recommend_first = (ImageView) view.findViewById(R.id.iv_rd_first);
        iv_recommend_second = (ImageView) view.findViewById(R.id.iv_rd_second);
        mRefreshLayout = (BGARefreshLayout) view.findViewById(R.id.refreshLayout);
        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(getContext(),true));

    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        isLoadMore = false;
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
            GetActivityList();
            return true;
        }
    }

    private void initData(){
        GetActivityList();
        GetTopActivityListBanner();
        GetRecommendActivity();
    }

    private void setData(){

        mAdapter = new ThirdPageFragmentAdapter(getActivity(),activityContentList);
        mRecy.setAdapter(mAdapter);
        mRecy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("点击时的位置",activityContentList.get(position).getId()+"");
                if(activityContentList.get(position).getType().equalsIgnoreCase("ACTIVITY")){
                    String ActivityId = activityContentList.get(position).getId()+"";
                EventBus.getDefault().post(new StartBrotherEvent(OfflineActivityFragment.newInstance(ActivityId)));
                }else {
                    return;
                }
            }
        });
        mRefreshLayout.endLoadingMore();




    }

    private void setData1(){
        mBanner.setAdapter(new BGABanner.Adapter() {
            @Override
            public void fillBannerItem(BGABanner banner, View view, Object model, int position) {
                Glide.with(banner.getContext()).load(model).placeholder(R.mipmap.icon_chat_camera).error(R.mipmap.icon_chat_camera).dontAnimate().thumbnail(0.1f).into((ImageView) view);
            }
        });
        mBanner.setData(imgurls,tips);
    }

    private void setData2(){
        Picasso.with(getContext()).load(activityRecommend.get(0).getImage())
                .placeholder(BaseInfo.PICASSO_PLACEHOLDER)
                .resize(115,115)
                .centerCrop()
                .into(iv_recommend_first);
        Picasso.with(getContext()).load(activityRecommend.get(1).getImage())
                .placeholder(BaseInfo.PICASSO_PLACEHOLDER)
                .resize(115,115)
                .centerCrop()
                .into(iv_recommend_second);
    }



    private void scrollToTop(){

    }

    /**
     * 选择tab事件
     */
    @Subscribe
    public void onTabSelectedEvent(TabSelectedEvent event) {
        if (mAtTop) {
            mRefreshLayout.beginRefreshing();
        } else {
           // scrollToTop();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRecy.setAdapter(null);
        EventBus.getDefault().unregister(this);
    }

    public void GetActivityList(){
        NetHelper.getInstance().GetPaticipationList(activityType, pagecount,new NetRequestCallBack() {
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

    public void GetTopActivityListBanner(){
        NetHelper.getInstance().GetActivityTopBanner( new NetRequestCallBack() {
            @Override
            public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                if(activityHotList.isEmpty()){
                List<ActivityContent> hottemp = responseInfo.getActivityContentList();
                activityHotList.addAll(hottemp);
                for(int i= 0; i< activityHotList.size() ; i++){
                    imgurls.add(i,activityHotList.get(i).getImage());
                    tips.add(i,activityHotList.get(i).getName());
                 }
                }
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

    public void GetRecommendActivity(){
        NetHelper.getInstance().GetActivityRecommended(new NetRequestCallBack() {
            @Override
            public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                if(activityRecommend.isEmpty()){
                    List<ActivityContent> rectemp = responseInfo.getActivityContentList();
                    activityRecommend.addAll(rectemp);
                }
                setData2();
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
