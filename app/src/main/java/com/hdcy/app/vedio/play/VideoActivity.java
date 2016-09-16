package com.hdcy.app.vedio.play;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hdcy.app.R;
import com.hdcy.app.vedio.preference.Settings;
import com.ucloud.common.logger.L;
import com.ucloud.player.widget.v2.UVideoView;


public class VideoActivity extends AppCompatActivity implements UVideoView.Callback {

    private static final String TAG = "VideoActivity";

    private UVideoView mVideoView;

    String rtmpPlayStreamUrl = "http://rtmp3.usmtd.ucloud.com.cn/live/%s.flv";

    Settings mSettings;

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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoView != null) {
            mVideoView.setVolume(0,0);
            mVideoView.stopPlayback();
            mVideoView.release(true);
        }
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
