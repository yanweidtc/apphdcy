package com.hdcy.app.fragment.fourth.childpagers;

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

import com.hdcy.app.R;
import com.hdcy.app.adapter.GiftFragmentAdapter;
import com.hdcy.app.adapter.ThirdPageFragmentAdapter;
import com.hdcy.app.basefragment.BaseBackFragment;
import com.hdcy.app.event.StartBrotherEvent;
import com.hdcy.app.fragment.fourth.SuperBrowserFragment;
import com.hdcy.app.model.GiftContent;
import com.hdcy.app.model.RootListInfo;
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
 * Created by WeiYanGeorge on 2016-09-19.
 */

public class PersonalGiftFragment extends BaseBackFragment implements BGARefreshLayout.BGARefreshLayoutDelegate{

    private Toolbar mToolbar;
    private TextView title;

    private BGARefreshLayout mRefreshLayout;

    private GiftFragmentAdapter mAdapter;

    private ListView mListView;

    private List<GiftContent> giftContentList = new ArrayList<>();
    private RootListInfo rootListInfo = new RootListInfo();

    private boolean isLast;

    private int pagecount = 0 ;

    public static PersonalGiftFragment newInstance(){
        PersonalGiftFragment fragment = new PersonalGiftFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_gift, container, false);
        initView(view);
        initData();
        setListener();
        return view;
    }

    private void initView(View view){
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        title = (TextView) view.findViewById(R.id.toolbar_title);
        title.setText("我的积分");
        initToolbarNav(mToolbar);
        mListView = (ListView) view.findViewById(R.id.lv_mine_gift);
        mRefreshLayout = (BGARefreshLayout) view.findViewById(R.id.refreshLayout);
        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setRefreshViewHolder(new BGANormalRefreshViewHolder(getContext(),true));

        mAdapter = new GiftFragmentAdapter(getContext(), giftContentList);
        Log.e("giftsize",giftContentList.size()+"");
        mListView.setAdapter(mAdapter);

    }

    private void setListener(){
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventBus.getDefault().post(new StartBrotherEvent(SuperBrowserFragment.newInstance(1,giftContentList.get(position).getId()+"","积分兑换")));
            }
        });
    }

    private void initData(){
        GetMineGiftList();
    }

    private void setData(){
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        giftContentList.clear();
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

    public void GetMineGiftList(){
        NetHelper.getInstance().GetMineGiftList(pagecount, new NetRequestCallBack() {
            @Override
            public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                List<GiftContent> temp = responseInfo.getGiftContent();
                giftContentList.addAll(temp);
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
