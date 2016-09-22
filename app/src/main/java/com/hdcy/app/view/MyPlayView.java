package com.hdcy.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hdcy.app.R;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by chiwenheng on 16/9/20.
 */
public class MyPlayView extends JCVideoPlayerStandard {


	public View view4Share;

	private Context mContext;

	public MyPlayView(Context context) {
		super(context);
		mContext=context;
	}

	public MyPlayView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
	}


	@Override
	public int getLayoutId() {
		return  R.layout.part_activity_vedio_play_v2;
	}

	@Override
	public void init(Context context) {
		Log.d(TAG, "init() called with: " + "context = [" + context + "]");
		super.init(context);
		view4Share=this.findViewById(R.id.iv_share);
		view4Share.setOnClickListener(this);

		backButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Log.d(TAG, "onClick() called with: " + "v = [" + v + "]");
		int id=v.getId();
		if(id==R.id.iv_share){

			showToast("share");
		}else if(id == R.id.back){
			showToast("back");
		}else{
			super.onClick(v);
		}


	}

	public void showToast(String content){
		Toast.makeText(mContext,content,Toast.LENGTH_SHORT);
	}



}

