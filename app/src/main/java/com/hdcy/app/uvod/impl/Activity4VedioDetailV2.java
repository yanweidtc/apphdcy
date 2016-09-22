package com.hdcy.app.uvod.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hdcy.app.R;
import com.hdcy.app.fragment.second.Fragment4TabComment;
import com.hdcy.app.fragment.second.Fragment4TabVedioBrief;
import com.hdcy.app.model.Bean4VedioBanner;
import com.hdcy.app.view.MyPlayView;
import com.hdcy.base.utils.net.NetHelper;
import com.hdcy.base.utils.net.NetRequestCallBack;
import com.hdcy.base.utils.net.NetRequestInfo;
import com.hdcy.base.utils.net.NetResponseInfo;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * 视频点播 需要的aciivty
 */
public class Activity4VedioDetailV2 extends SupportActivity {
	private static final String TAG = "Activity4VedioDetail";

	private static final int MSG_INIT_PLAY = 0;

	private Toolbar mToolbar;
	private TabLayout mTab;
	private ViewPager mViewPager;

	private List<Fragment> mFragments=new ArrayList<>();

	private Bean4VedioBanner mBean;

	private MyPlayView jcVideoPlayerStandard;

	public  static void getInstance(Context context, Bean4VedioBanner bean){

		Intent intent=new Intent();
//		intent.setAction("com.hdcy.app.uvod.impl.Activity4VedioDetail");
		intent.setClass(context,Activity4VedioDetailV2.class);

		Bundle bundle=new Bundle();

		bundle.putString("title",bean.name);
		if(TextUtils.isEmpty(bean.url2)){// 这里暂时用  一个默认地址
			bundle.putString("videoPath", "http://mediademo.ufile.ucloud.com.cn/ucloud_promo_140s.mp4");
		}else{
			bundle.putString("videoPath", bean.url2);

		}
		bundle.putSerializable("bean",bean);

//			intent.putExtra("videoPath", "http://ulive-record.ufile.ucloud.com.cn/101841470662011.m3u8");
//			intent.putExtra("videoPath", "http://uc-hls.ufile.ucloud.cn/1470744319684927_05v3.m3u8");
		intent.putExtras(bundle);
		context.startActivity(intent);
	}




	private void init() {
		initView();
		getDatas();
	}


	private void initView() {

		jcVideoPlayerStandard = (MyPlayView) findViewById(R.id.custom_videoplayer_standard);
		// 临时的视频地址
		String url4Vedio="http://mediademo.ufile.ucloud.com.cn/ucloud_promo_140s.mp4";
		if(!TextUtils.isEmpty(mBean.url2)){
			url4Vedio=mBean.url2;
		}

		jcVideoPlayerStandard.setUp(url4Vedio
				, JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, mBean.name);

		ImageLoader.getInstance().displayImage(mBean.image,
				jcVideoPlayerStandard.thumbImageView);

		jcVideoPlayerStandard.backButton.setVisibility(View.VISIBLE);
		jcVideoPlayerStandard.backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Activity4VedioDetailV2.this.finish();
			}
		});
		jcVideoPlayerStandard.view4Share.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showToast("share  share share  share share  share ");
			}
		});


		mTab = (TabLayout) this.findViewById(R.id.uvod_tab);
		mViewPager = (ViewPager) this.findViewById(R.id.uvod_viewPager);

		mTab.addTab(mTab.newTab());
		mTab.addTab(mTab.newTab());

		mFragments.add(Fragment4TabVedioBrief.newInstance(mBean.desc));
//		called with: tagId = [630240], target = [article]
		mFragments.add(Fragment4TabComment.newInstance("630240","article"));

		mViewPager.setAdapter(new ViewPageFragmentAdapter(getSupportFragmentManager()));
		mTab.setupWithViewPager(mViewPager);

	}

	@Override
	protected void onCreate(Bundle bundles) {
		super.onCreate(bundles);
		setContentView(R.layout.activity_video_detial_v2);
		mBean= (Bean4VedioBanner) getIntent().getSerializableExtra("bean");
		String intentAction = getIntent().getAction();
		init();

		IntentFilter filter = new IntentFilter();
		filter.setPriority(1000);
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mNetworkStateListener, filter);

	}


	@Override
	public void onBackPressed() {
		if (JCVideoPlayer.backPress()) {
			return;
		}
		super.onBackPressed();
	}
	@Override
	protected void onPause() {
		super.onPause();
		JCVideoPlayer.releaseAllVideos();
	}


	@Override
	protected void onResume() {
		super.onResume();
	}


	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mNetworkStateListener);
	}




	private BroadcastReceiver mNetworkStateListener = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo activeInfo = manager.getActiveNetworkInfo();
				if (activeInfo == null) {
					Toast.makeText(context, getString(R.string.error_current_network_disconnected), Toast.LENGTH_LONG).show();
				}
			}
		}
	};


	//###################################非视频业务#####################################################
	public class ViewPageFragmentAdapter extends FragmentPagerAdapter {



		public ViewPageFragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {

			return mFragments.get(position);
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			if(position==0){
				return "视频简介";
			}else {
				return "评论";
			}
		}
	}



	/** 获取视频详情 */
	private void getDatas() {
		if(mBean==null){
			return;
		}

		NetHelper.getInstance().getOneVedioDetail(mBean.id,new NetRequestCallBack() {
			@Override
			public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {

				Log.d(TAG, "onSuccess() called with: " + "requestInfo = [" + requestInfo + "], responseInfo = [" + responseInfo + "]");

				showToast(responseInfo.mBean4VedioDetail.toString());

			}

			@Override
			public void onError(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {

			}

			@Override
			public void onFailure(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {

			}
		});

	}

	private void showToast(String s) {
		Toast.makeText(this,s,Toast.LENGTH_SHORT).show();

	}


}

