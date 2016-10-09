package com.hdcy.app.fragment.second;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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

import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.hdcy.app.R;
import com.hdcy.app.adapter.CommonAdapter;
import com.hdcy.app.adapter.ViewHolder;
import com.hdcy.app.basefragment.BaseLazyMainFragment;
import com.hdcy.app.chat.Activity4Chat;
import com.hdcy.app.model.Bean4VedioBanner;
import com.hdcy.app.model.RootListInfo;
import com.hdcy.app.uvod.impl.Activity4VedioDetailV2;
import com.hdcy.app.view.ScaleInTransformer;
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

import static com.hdcy.base.utils.DateUtil.date2Str;

/**
 * Created by Chiwenheng on 2016-09-12.
 *
 *  v2 版本,此版本的listview 上面的viewpager轮播广告不一样
 */

public class SecondFragmentV2 extends BaseLazyMainFragment implements BGARefreshLayout.BGARefreshLayoutDelegate
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



    private ViewPager mViewPager;
    private PagerAdapter mAdapter4Banner;

    private RootListInfo rootListInfo = new RootListInfo();

    private boolean isLast;

    private int pagecount = 0;

    int bgimgWidth;
    int bgimgHeight;

    String mGroupId;

    public static SecondFragmentV2 newInsatance(){
        Bundle args = new Bundle();
        SecondFragmentV2 fragment = new SecondFragmentV2();
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


        //前面三个参数和创建私有群一致
//needApprovalRequired: 如果创建的公开群用需要户自由加入，就传false。否则需要申请，等群主批准后才能加入，传true
        try {
            EMGroup group = EMGroupManager.getInstance().createPublicGroup("chate", "desc", null, false);//需异步处理
            mGroupId =group.getGroupId();

            Log.e("group",group.toString());
            Log.e("group",mGroupId);

        } catch (Exception e) {
            e.printStackTrace();
        }


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
        View headview = View.inflate(getContext(),R.layout.item_headerview_second_v2,null);

        mViewPager = (ViewPager) headview.findViewById(R.id.id_viewpager);

        initHeadBanner();


        mAdapter = new CommonAdapter<Bean4VedioBanner>(getActivity(), mDatas4ListView,R.layout.item_second_fragment) {
            @Override
            public void convert(ViewHolder holder, Bean4VedioBanner bean) {

                holder.setText(R.id.tv_activity_title,bean.name);
                Date date= new Date(bean.startTime);
                holder.setText(R.id.tv_activity_desc, "播放时间:"+date2Str(date,"yyyy-MM-dd / HH:mm"));

                ImageView iv=holder.getView(R.id.iv_activity_background);
                Picasso.with(getActivity()).load(bean.image)
                        .placeholder(BaseInfo.PICASSO_PLACEHOLDER)
                        .error(BaseInfo.PICASSO_ERROR)
//                        .resize(240,240)
//                        .centerCrop()
                        .into(iv);

                // 是否直播的标志
                holder.setVisible(R.id.tv_is_live,bean.live);//是直播的标志

                if(bean.live){
                    if(bean.liveState.equals("直播中")){
                        holder.setText(R.id.tv_activity_desc, "观看人数:"+bean.viewCount);
                    }else if(bean.liveState.equals("未开始")){
                        holder.setText(R.id.tv_activity_desc, "播放时间:"+date2Str(date,"yyyy-MM-dd / HH:mm"));
                    }else if(bean.liveState.equals("点播")){
                        holder.setText(R.id.tv_activity_desc, "观看次数:"+bean.viewCountPlus+"／时长 "+bean.length);
                    }
                }



            }
        };

        mListView.addHeaderView(headview);
        mListView.setAdapter(mAdapter);



    }

    private void initHeadBanner() {


        mViewPager.setPageMargin(40);
        mViewPager.setOffscreenPageLimit(3);
        mAdapter4Banner = new PagerAdapter()
        {
            @Override
            public Object instantiateItem(ViewGroup container, int position)
            {

                Bean4VedioBanner bean =mDatas4Banner.get(position);

                View view=View.inflate(getActivity(),R.layout.item_banner_top,null);
//                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                view.setLayoutParams(lp);
                container.addView(view);
                TextView tvName= (TextView) view.findViewById(R.id.tv_activity_title);
                tvName.setText(bean.name);
//                R.id.tv_activity_title,bean.name);
                Date date= new Date(bean.startTime);
                TextView tvDate= (TextView) view.findViewById(R.id.tv_activity_desc);

                tvDate.setText("播放时间:"+DateUtil.date2Str(date,"yyyy-MM-dd / HH:mm"));
                ImageView iv= (ImageView) view.findViewById(R.id.iv_activity_background);
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                Picasso.with(getActivity()).load(bean.image)
                        .placeholder(BaseInfo.PICASSO_PLACEHOLDER)
                        .error(BaseInfo.PICASSO_ERROR)
//                        .resize(240,240)
//                        .centerCrop()
                        .into(iv);

                // 是否直播的标志
                View view4Live=view.findViewById(R.id.tv_is_live);
                if(bean.live){
                    view4Live.setVisibility(View.VISIBLE);
                    if(bean.liveState.equals("直播中")){
                        tvDate.setText( "观看人数:"+bean.viewCount);
                    }else if(bean.liveState.equals("未开始")){
                        tvDate.setText("播放时间:"+date2Str(date,"yyyy-MM-dd / HH:mm"));
                    }else if(bean.liveState.equals("点播")){
                        tvDate.setText("观看次数:"+bean.viewCountPlus+"／时长 "+bean.length);
                    }
                }else{
                    view4Live.setVisibility(View.GONE);
                }




                view.setTag(bean);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bean4VedioBanner bean= (Bean4VedioBanner) v.getTag();
                        if(bean!=null){
                            goToOneDetail(bean);
                        }
                    }
                });


                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object)
            {
                container.removeView((View) object);
            }

            @Override
            public int getCount()
            {
                return  mDatas4Banner.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object o)
            {
                return view == o;
            }
        };

        mViewPager.setAdapter(mAdapter4Banner);

        mViewPager.setPageTransformer(true, new ScaleInTransformer());


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
//				Toast.makeText(getActivity(), "当前网络: mobile", Toast.LENGTH_SHORT).show();
				break;
			case ConnectivityManager.TYPE_ETHERNET:
//				Toast.makeText(getActivity(), "当前网络: ehternet", Toast.LENGTH_SHORT).show();
				break;
			case ConnectivityManager.TYPE_WIFI:
//				Toast.makeText(getActivity(), "当前网络: wifi", Toast.LENGTH_SHORT).show();
				break;
		}

        if(bean!=null){
            if(bean.live){// 是否为直播
//            if(bean.id==631533){// 让第一个条数据 进行直播
				// 开始直播
                Intent intent =new Intent();
                String streamId=bean.steamId;// 这里视频聊天的窗口固定死
                Activity4Chat.getInstance(getActivity(),streamId,mGroupId);

//                Settings mSettings = new Settings(getActivity());
//                mSettings.setPublishStreamId(streamId);
//                intent.setClass(getActivity(),VideoActivity.class);
//                startActivity(intent);

			}else{// 点播

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
    }

    @Override
    public void onPause() {
        super.onPause();
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

                initHeadBanner();// 初始化banner


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
//        showToast(" banner  item  onclick");
        goToOneDetail(mDatas4Banner.get(position));

    }

    public  void showToast(String content){
        Toast.makeText(getActivity(),content,Toast.LENGTH_SHORT).show();


    }

}
