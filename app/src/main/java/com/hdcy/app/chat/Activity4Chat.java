package com.hdcy.app.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.ui.EaseChatFragment;
import com.easemob.exceptions.EaseMobException;
import com.hdcy.app.R;
import com.hdcy.app.vedio.preference.Settings;
import com.ucloud.common.logger.L;
import com.ucloud.player.widget.v2.UVideoView;

import java.util.ArrayList;
import java.util.List;


/**
 * 聊天主界面
 * chiwenheng
 *
 * */
public class Activity4Chat extends AppCompatActivity implements View.OnClickListener ,UVideoView.Callback {

	private static final String TAG = "Activity4Chat";


	private UVideoView mVideoView;

	String rtmpPlayStreamUrl = "http://rtmp3.usmtd.ucloud.com.cn/live/%s.flv";
	Settings mSettings;



	private String groupid ="";

	public static void getInstance(Context context,String streamId){

		Intent intent =new Intent();
		Settings mSettings = new Settings(context);
		mSettings.setPublishStreamId(streamId);
		intent.setClass(context,Activity4Chat.class);
		context.startActivity(intent);
	}



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_chat_v2);
		

		initIM();

		mVideoView = (UVideoView) findViewById(R.id.videoview);

		mSettings = new Settings(this);

		mVideoView.setPlayType(UVideoView.PlayType.LIVE);
		mVideoView.setPlayMode(UVideoView.PlayMode.NORMAL);
		mVideoView.setRatio(UVideoView.VIDEO_RATIO_FILL_PARENT);
		mVideoView.setDecoder(UVideoView.DECODER_VOD_SW);

		mVideoView.registerCallback(this);

		mVideoView.setVideoPath(String.format(rtmpPlayStreamUrl, mSettings.getPusblishStreamId()));

	}


	private void initIM() {

		String userName="chiwenheng";
		String password="123456";


		EMChatManager.getInstance().login(userName,password,new EMCallBack() {//回调
			@Override
			public void onSuccess() {
				runOnUiThread(new Runnable() {
					public void run() {
						EMGroupManager.getInstance().loadAllGroups();
						EMChatManager.getInstance().loadAllConversations();
						Log.d("main", "登录聊天服务器成功！");
					}
				});
			}

			@Override
			public void onProgress(int progress, String status) {

			}

			@Override
			public void onError(int code, String message) {
				Log.d("main", "登录聊天服务器失败！");
			}
		});

		String groudId="1474647524640";

		//如果群开群是自由加入的，即group.isMembersOnly()为false，直接join
		try {
			EMGroupManager.getInstance().joinGroup(groudId);//需异步处理
		} catch (EaseMobException e) {
			e.printStackTrace();
		}

		//new出EaseChatFragment或其子类的实例
		EaseChatFragment chatFragment = new EaseChatFragment();
		//传入参数
		Bundle args = new Bundle();
		args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP);
		args.putString(EaseConstant.EXTRA_USER_ID, groudId);
		chatFragment.setArguments(args);
		getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();


	}


	@Override
	public void onClick(View view) {
		if(view.getId()==R.id.sendBtn){




		}

	}



	private void updateView(List<EMMessage> messages) {


	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
//		记得在不需要的时候移除listener，如在activity的onDestroy()时

		if (mVideoView != null) {
			mVideoView.setVolume(0,0);
			mVideoView.stopPlayback();
			mVideoView.release(true);
		}
	}

	private List<Message> getMyData(){
		
		List<Message> msgList = new ArrayList<Message>();
		Message msg;
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
		msg.setValue("100");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_TIME_TIP);
		msg.setValue("2012-12-23 下午2:23");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
		msg.setValue("99");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
		msg.setValue("98");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_TIME_TIP);
		msg.setValue("2012-12-23 下午2:25");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
		msg.setValue("97");
		msgList.add(msg);

		msg = new Message();
		msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
		msg.setValue("96");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
		msg.setValue("95");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_TIME_TIP);
		msg.setValue("2012-12-23 下午3:25");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
		msg.setValue("94");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_LEFT_IMAGE);
		msg.setValue("93");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
		msg.setValue("92");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
		msg.setValue("91");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_LEFT_IMAGE);
		msg.setValue("0");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
		msg.setValue("1");
		msgList.add(msg);
		
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_LEFT_IMAGE);
		msg.setValue("2");
		msgList.add(msg);
		
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
		msg.setValue("3");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_LEFT_AUDIO);
		msg.setValue("4");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
		msg.setValue("5");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
		msg.setValue("6");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
		msg.setValue("7");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
		msg.setValue("8");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_RIGHT_IMAGE);
		msg.setValue("9");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
		msg.setValue("10");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
		msg.setValue("11");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
		msg.setValue("12");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);
		msg.setValue("13");
		msgList.add(msg);
		
		msg = new Message();
		msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
		msg.setValue("14");
		msgList.add(msg);
		return msgList;
		
	}



	@Override
	public void onEvent(int what, String message) {
		Log.d(TAG, "what:" + what + ", message:" + message);
		switch (what) {
			case UVideoView.Callback.EVENT_PLAY_START:
				break;
			case UVideoView.Callback.EVENT_PLAY_PAUSE:
				break;
			case UVideoView.Callback.EVENT_PLAY_STOP:
				break;
			case UVideoView.Callback.EVENT_PLAY_COMPLETION:
				Toast.makeText(this, "EVENT_PLAY_COMPLETION", Toast.LENGTH_SHORT).show();
				break;
			case UVideoView.Callback.EVENT_PLAY_DESTORY:
				break;
			case UVideoView.Callback.EVENT_PLAY_ERROR:
				Toast.makeText(this, "EVENT_PLAY_ERROR:" + message, Toast.LENGTH_SHORT).show();
				break;
			case UVideoView.Callback.EVENT_PLAY_RESUME:
				break;
			case UVideoView.Callback.EVENT_PLAY_INFO_BUFFERING_START:
				L.e(TAG, "network block start....");
//              Toast.makeText(VideoActivity.this, "unstable network", Toast.LENGTH_SHORT).show();
				break;
			case UVideoView.Callback.EVENT_PLAY_INFO_BUFFERING_END:
				L.e(TAG, "network block end....");
				break;
		}
	}

	public void close(View view) {
		finish();
	}



}

