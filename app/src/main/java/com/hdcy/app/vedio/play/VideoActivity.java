package com.hdcy.app.vedio.play;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.hdcy.app.R;
import com.hdcy.app.chat.ChatAdapter;
import com.hdcy.app.chat.Message;
import com.hdcy.app.vedio.preference.Settings;
import com.hdcy.base.utils.BaseUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.ucloud.common.logger.L;
import com.ucloud.player.widget.v2.UVideoView;

import java.util.ArrayList;
import java.util.List;


public class VideoActivity extends AppCompatActivity implements UVideoView.Callback ,View.OnClickListener {

    private static final String TAG = "VideoActivity";

    private UVideoView mVideoView;

    String rtmpPlayStreamUrl = "http://rtmp3.usmtd.ucloud.com.cn/live/%s.flv";
    Settings mSettings;



    private ListView mlistview;
    private List<Message> mDatas=new ArrayList<>();
    private EditText etInput;
    private Button mBtn4Send;
    private ChatAdapter mAdapter;
    private String groupid ="";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        mVideoView = (UVideoView) findViewById(R.id.videoview);

        mSettings = new Settings(this);

        mVideoView.setPlayType(UVideoView.PlayType.LIVE);
        mVideoView.setPlayMode(UVideoView.PlayMode.NORMAL);
        mVideoView.setRatio(UVideoView.VIDEO_RATIO_FILL_PARENT);
        mVideoView.setDecoder(UVideoView.DECODER_VOD_SW);

        mVideoView.registerCallback(this);

        mVideoView.setVideoPath(String.format(rtmpPlayStreamUrl, mSettings.getPusblishStreamId()));



        mlistview = (ListView)findViewById(R.id.lv_data);
        mBtn4Send= (Button) this.findViewById(R.id.sendBtn);
        etInput= (EditText) this.findViewById(R.id.sendMsgEdt);
        mAdapter=new ChatAdapter(this,mDatas);

        mBtn4Send.setOnClickListener(this);
        mlistview.setAdapter(mAdapter);

        initIM();

    }

    private void initIM() {
        //如果群开群是自由加入的，即group.isMembersOnly()为false，直接join
        try {
//			EMClient.getInstance().groupManager().joinGroup(groupid);//需异步处理
//			//需要申请和验证才能加入的，即group.isMembersOnly()为true，调用下面方法
//			EMClient.getInstance().groupManager().applyJoinToGroup(groupid, "求加入");//需异步处理

            EMClient.getInstance().groupManager().addGroupChangeListener(new EMGroupChangeListener() {
                @Override
                public void onUserRemoved(String groupId, String groupName) {
                    //当前用户被管理员移除出群组
                }

                @Override
                public void onGroupDestroyed(String s, String s1) {

                }

                @Override
                public void onAutoAcceptInvitationFromGroup(String s, String s1, String s2) {

                }

                @Override
                public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {
                    //收到加入群组的邀请
                }
                @Override
                public void onInvitationDeclined(String groupId, String invitee, String reason) {
                    //群组邀请被拒绝
                }
                @Override
                public void onApplicationReceived(String groupId, String groupName, String applyer, String reason) {
                    //收到加群申请
                }
                @Override
                public void onApplicationAccept(String groupId, String groupName, String accepter) {
                    //加群申请被同意
                }
                @Override
                public void onApplicationDeclined(String groupId, String groupName, String decliner, String reason) {
                    // 加群申请被拒绝
                }

                @Override
                public void onInvitationAccepted(String s, String s1, String s2) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(BaseUtils.userName);
        if(conversation!=null){
            //获取此会话的所有消息
            List<EMMessage> messages = conversation.getAllMessages();
            //SDK初始化加载的聊天记录为20条，到顶时需要去DB里获取更多
            //获取startMsgId之前的pagesize条消息，此方法获取的messages SDK会自动存入到此会话中，APP中无需再次把获取到的messages添加到会话中
//		List<EMMessage> messages = conversation.loadMoreMsgFromDB(startMsgId, 20);
            if(messages!=null){
                updateView(messages);
            }
        }


        EMClient.getInstance().chatManager().addMessageListener(msgListener);


    }



    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.sendBtn){
            String content =etInput.getText().toString();
            if(TextUtils.isEmpty(content)){
                return;
            }
            etInput.setText("");
            //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
            EMMessage message = EMMessage.createTxtSendMessage(content, "chiwenheng");
            //如果是群聊，设置chattype，默认是单聊
//			if (chatType == CHATTYPE_GROUP)
//				message.setChatType(ChatType.GroupChat);
            message.setMessageStatusCallback(new EMCallBack(){
                @Override
                public void onSuccess() {
                    Log.d(TAG, "onSuccess() called with: " + "");
                }

                @Override
                public void onError(int i, String s) {

                    Log.d(TAG, "onError() called with: " + "i = [" + i + "], s = [" + s + "]");
                }

                @Override
                public void onProgress(int i, String s) {
                    Log.d(TAG, "onProgress() called with: " + "i = [" + i + "], s = [" + s + "]");

                }
            });
            //发送消息
            EMClient.getInstance().chatManager().sendMessage(message);

        }

    }


    private EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(final  List<EMMessage> messages) {
            Log.d(TAG, "onMessageReceived() called with: " + "messages = [" + messages + "]");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateView(messages);

                }
            });


        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //收到透传消息
            Log.d(TAG, "onCmdMessageReceived() called with: " + "messages = [" + messages + "]");
        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> messages) {
            //收到已读回执
            Log.d(TAG, "onMessageReadAckReceived() called with: " + "messages = [" + messages + "]");
        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> message) {
            //收到已送达回执
            Log.d(TAG, "onMessageDeliveryAckReceived() called with: " + "message = [" + message + "]");
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
            Log.d(TAG, "onMessageChanged() called with: " + "message = [" + message + "], change = [" + change + "]");
        }
    };

    private void updateView(List<EMMessage> messages) {
        //收到消息
        for(EMMessage item:messages){
            Message msg;
            msg = new Message();
            if(item.getTo().equals(BaseUtils.userName)){// 左边的文字
                msg.setType(ChatAdapter.VALUE_LEFT_TEXT);
            }else{
                msg.setType(ChatAdapter.VALUE_RIGHT_TEXT);// 右边的文字
            }

            msg.setValue(item.getBody().toString().replace("txt:",""));
            mDatas.add(msg);
        }

        mAdapter.notifyDataSetChanged();
        mlistview.setSelection(mDatas.size());
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoView != null) {
            mVideoView.setVolume(0,0);
            mVideoView.stopPlayback();
            mVideoView.release(true);
        }
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
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
