package com.hdcy.app.vedio;

import android.app.Application;

import com.ucloud.live.UEasyStreaming;

import java.util.Random;

public class AppContext extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		UEasyStreaming.initStreaming("publish3-key");
		UEasyStreaming.syncMobileConfig(this, 3600 * 24);
	}

	public static int getRandomStreamId() {
		Random random = new Random();
		int randint =(int)Math.floor((random.nextDouble()*10000.0 + 10000.0));
		return randint;
	}
}