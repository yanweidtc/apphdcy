package com.hdcy.app.fragment.second;

import android.content.Intent;
import android.net.ConnectivityManager;
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

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.hdcy.app.R;
import com.hdcy.app.adapter.CommonAdapter;
import com.hdcy.app.adapter.ViewHolder;
import com.hdcy.app.basefragment.BaseLazyMainFragment;
import com.hdcy.app.chat.Activity4Chat;
import com.hdcy.app.model.Bean4VedioBanner;
import com.hdcy.app.model.RootListInfo;
import com.hdcy.app.uvod.impl.Activity4VedioDetailV2;
import com.hdcy.app.view.NetworkImageHolderView;
import com.hdcy.base.BaseInfo;
import com.hdcy.base.utils.DateUtil;
import com.hdcy.base.utils.SizeUtils;
import com.hdcy.base.utils.net.NetHelper;
import com.hdcy.base.utils.net.NetRequestCallBack;
import com.hdcy.base.utils.net.NetRequestInfo;
import com.hdcy.base.utils.net.NetResponseInfo;
import com.squareup.picasso.Picasso;
import com.ucloud.common.util.SystemUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by Chiwenheng on 2016-09-12.
 */

public class SecondFragment extends BaseLazyMainFragment implements BGARefreshLayout.BGARefreshLayoutDelegate
                                ,OnItemClickListener
{

    private static final String TAG = "SecondFragment";

    private Toolbar mToolbar;
    private TextView title;

    private BGARefreshLayout mRefreshLayout;

    private CommonAdapter mAdapter;

    private ListView mListView;

    private List<Bean4VedioBanner> mDatas4ListView = new ArrayList<>();

    private List<Bean4VedioBanner> mDatas4Banner = new ArrayList<>();




    private RootListInfo rootListInfo = new RootListInfo();

    private boolean isLast;

    private int pagecount = 0;

    int bgimgWidth;
    int bgimgHeight;


    private ConvenientBanner convenientBanner;//顶部广告栏控件

    private ArrayList<Integer> localImages = new ArrayList<Integer>();



    public static SecondFragment newInsatance(){
        Bundle args = new Bundle();
        SecondFragment fragment = new SecondFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second_pager, container, false);
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
        title.setText("视频");
        mListView = (ListView) view.findViewById(R.id.lv_mine_activity);
        mRefreshLayout = (BGARefreshLayout) view.findViewById(R.id.refreshLayout);
        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(getContext(),true));
        View headview = View.inflate(getContext(),R.layout.item_headerview_second,null);

        convenientBanner = (ConvenientBanner) headview.findViewById(R.id.convenientBanner);


        mAdapter = new CommonAdapter<Bean4VedioBanner>(getActivity(), mDatas4ListView,R.layout.item_second_fragment) {
            @Override
            public void convert(ViewHolder holder, Bean4VedioBanner bean) {

                holder.setText(R.id.tv_activity_title,bean.name);
                Date date= new Date(bean.startTime);
                holder.setText(R.id.tv_activity_desc, DateUtil.date2Str(date,"yyyy-MM-dd / HH:mm"));

                ImageView iv=holder.getView(R.id.iv_activity_background);
                Picasso.with(getActivity()).load(bean.image)
                        .placeholder(BaseInfo.PICASSO_PLACEHOLDER)
                        .error(BaseInfo.PICASSO_ERROR)
//                        .resize(240,240)
//                        .centerCrop()
                        .into(iv);

                // 是否直播的标志
                holder.setVisible(R.id.tv_is_live,bean.live);//是直播的标志





            }
        };

        mListView.addHeaderView(headview);
        mListView.setAdapter(mAdapter);



    }

    private void initData(){
        getBannerDatas();
        getContentList();
    }

    private void setListener(){
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // 这里header为banner index 从1 开始
                Bean4VedioBanner bean=mDatas4ListView.get(position-1);

                goToOneDetail(bean);

            }
        });
    }

    private void goToOneDetail(Bean4VedioBanner bean) {

        if(!SystemUtil.isNetworkConnected(getActivity())) {
			Toast.makeText(getActivity(), "当前网络不可用.", Toast.LENGTH_SHORT).show();
			return;
		}
        switch (SystemUtil.getConnectedType(getActivity())) {
			case ConnectivityManager.TYPE_MOBILE:
				Toast.makeText(getActivity(), "当前网络: mobile", Toast.LENGTH_SHORT).show();
				break;
			case ConnectivityManager.TYPE_ETHERNET:
				Toast.makeText(getActivity(), "当前网络: ehternet", Toast.LENGTH_SHORT).show();
				break;
			case ConnectivityManager.TYPE_WIFI:
				Toast.makeText(getActivity(), "当前网络: wifi", Toast.LENGTH_SHORT).show();
				break;
		}

        if(bean!=null){
//            if(bean.live){// 是否为直播
            if(bean.id==631533){// 让第一个条数据 进行直播
				// 开始直播
                Intent intent =new Intent();
                String streamId="12345";// 这里视频聊天的窗口固定死
                Activity4Chat.getInstance(getActivity(),streamId);

//                Settings mSettings = new Settings(getActivity());
//                mSettings.setPublishStreamId(streamId);
//                intent.setClass(getActivity(),VideoActivity.class);
//                startActivity(intent);



			}else{// 点播
                showToast(" 点播 点播");
				Activity4VedioDetailV2.getInstance(getActivity(),bean);

			}

        }
    }

    private void setData(){

        mAdapter.notifyDataSetChanged();
        mRefreshLayout.endLoadingMore();
    }
    private void setData1(){
//        Picasso.with(getContext()).load(leaderInfoList.get(0).getTopImage())
//                .placeholder(BaseInfo.PICASSO_PLACEHOLDER)
//                .resize(bgimgWidth,bgimgHeight)
//                .centerCrop()
//                .config(Bitmap.Config.RGB_565)
//                .into(iv_leader_background);
//        tv_leader_name.setText(leaderInfoList.get(0).getNickname()+"");

    }

    @Override
    public void onResume() {
        super.onResume();
        //开始自动翻页
        convenientBanner.startTurning(5000);
    }

    @Override
    public void onPause() {
        super.onPause();
        //停止翻页
        convenientBanner.stopTurning();
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        mDatas4ListView.clear();
        pagecount = 0;
        getContentList();
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
            getContentList();
            return true;
        }

    }
    /** 获取轮播图的数据*/
    private void getBannerDatas(){
        NetHelper.getInstance().GetVedioTopBanner(new NetRequestCallBack() {
            @Override
            public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                List<Bean4VedioBanner> tempList = responseInfo.vedioBannerList;
                mDatas4Banner.addAll(tempList);
                Log.d(TAG,mDatas4Banner.size()+"");

                //网络加载
                convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
                    @Override
                    public NetworkImageHolderView createHolder() {
                        return new NetworkImageHolderView();
                    }
                },mDatas4Banner)
                        //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                        .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                        //设置指示器的方向
                        .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                        .setOnItemClickListener(SecondFragment.this);

            }

            @Override
            public void onError(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {

            }

            @Override
            public void onFailure(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {

            }
        });
    }
    /** 获取列表的数据*/
    private void getContentList(){
        NetHelper.getInstance().getVedioListDatas(pagecount,new NetRequestCallBack() {
            @Override
            public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                List<Bean4VedioBanner> tempList = responseInfo.vedioBannerList;
                rootListInfo = responseInfo.getRootListInfo();
                mDatas4ListView.addAll(tempList);
                isLast = rootListInfo.isLast();
                Log.d(TAG,mDatas4ListView.size()+"");
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


    @Override
    protected void initLazyView(@Nullable Bundle savedInstanceState) {

    }


    //图片点击事件
    @Override
    public void onItemClick(int position) {
        showToast(" banner  item  onclick");
        goToOneDetail(mDatas4Banner.get(position));

    }

    public  void showToast(String content){
        Toast.makeText(getActivity(),content,Toast.LENGTH_SHORT).show();


    }

}
