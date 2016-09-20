package com.hdcy.app.chat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.ui.EaseChatFragment;
import com.hdcy.app.R;

import java.util.ArrayList;
import java.util.List;


/**
 * 聊天主界面
 * chiwenheng
 *
 * */
public class Activity4Chat extends AppCompatActivity implements View.OnClickListener{

	private static final String TAG = "Activity4Chat";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_chat_v2);
		

		initIM();

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

		//new出EaseChatFragment或其子类的实例
		EaseChatFragment chatFragment = new EaseChatFragment();
//传入参数
		Bundle args = new Bundle();
		args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP);
		args.putString(EaseConstant.EXTRA_USER_ID, "zw123");
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



}

