package com.hdcy.app.fragment.third.second;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.hdcy.app.model.ActivityContent;
import com.hdcy.app.model.LeaderInfo;
import com.hdcy.app.model.RootListInfo;
import com.hdcy.app.view.NetworkImageHolderView;
import com.hdcy.base.utils.SizeUtils;
import com.hdcy.base.utils.net.NetHelper;
import com.hdcy.base.utils.net.NetRequestCallBack;
import com.hdcy.base.utils.net.NetRequestInfo;
import com.hdcy.base.utils.net.NetResponseInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by Chiwenheng on 2016-09-12.
 */

public class SecondFragment extends BaseLazyMainFragment implements BGARefreshLayout.BGARefreshLayoutDelegate
                                ,OnItemClickListener
{

    private Toolbar mToolbar;
    private TextView title;

    private BGARefreshLayout mRefreshLayout;

    private CommonAdapter mAdapter;

    private ListView mListView;

    private List<ActivityContent> activityContentList = new ArrayList<>();

    private List<String> mDatas = new ArrayList<>();

    private RootListInfo rootListInfo = new RootListInfo();
    private List<LeaderInfo> leaderInfoList = new ArrayList<>();

    private boolean isLast;

    private int pagecount = 0;

    int bgimgWidth;
    int bgimgHeight;


    private ConvenientBanner convenientBanner;//顶部广告栏控件

    private ArrayList<Integer> localImages = new ArrayList<Integer>();
    private List<String> networkImages;
    private String[] images = {"http://img2.imgtn.bdimg.com/it/u=3093785514,1341050958&fm=21&gp=0.jpg",
            "http://img2.3lian.com/2014/f2/37/d/40.jpg",
            "http://d.3987.com/sqmy_131219/001.jpg",
            "http://img2.3lian.com/2014/f2/37/d/39.jpg",
            "http://www.8kmm.com/UploadFiles/2012/8/201208140920132659.jpg",
            "http://f.hiphotos.baidu.com/image/h%3D200/sign=1478eb74d5a20cf45990f9df460b4b0c/d058ccbf6c81800a5422e5fdb43533fa838b4779.jpg",
            "http://f.hiphotos.baidu.com/image/pic/item/09fa513d269759ee50f1971ab6fb43166c22dfba.jpg"
    };



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
        //网络加载
        networkImages= Arrays.asList(images);
        convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        },networkImages)
        //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
        .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                //设置指示器的方向
        .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
        .setOnItemClickListener(SecondFragment.this);

        mAdapter = new CommonAdapter<String>(getActivity(), mDatas,R.layout.item_second_fragment) {
            @Override
            public void convert(ViewHolder holder, String o) {

            }
        };

        mListView.addHeaderView(headview);
        mListView.setAdapter(mAdapter);



    }

    private void initData(){
        GetActivityList();
        GetLeaderInfo();
    }

    private void setListener(){
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                showToast("position = "+position);
            }
        });
    }

    private void setData(){
        for (int i = 0; i < 10; i++) {
            mDatas.add("");
        }

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


    //图片点击事件
    @Override
    public void onItemClick(int position) {

        showToast("onclick");
    }

    public  void showToast(String content){
        Toast.makeText(getActivity(),content,Toast.LENGTH_SHORT).show();

    }

}
