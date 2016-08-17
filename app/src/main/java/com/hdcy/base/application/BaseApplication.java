package com.hdcy.base.application;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.multidex.MultiDexApplication;

import com.hdcy.base.utils.BaseUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.xutils.common.util.LogUtil;
import org.xutils.x;

public class BaseApplication extends MultiDexApplication {

    private static BaseApplication instance;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initData();
        handler.post(new Runnable() {
            @Override
            public void run() {
                initDataThread();
            }
        });
    }

    private void initData() {
        // 崩溃异常初始化
//        CrashHandler.getInstance().init(instance);

        // xUtils 3.0初始化
        x.Ext.init(instance);

        // 创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(instance);
        ImageLoader.getInstance().init(configuration);
    }

    private void initDataThread() {
        //百度地图
///*        SDKInitializer.initialize(getApplicationContext());
//
        x.Ext.setDebug(true); // 是否输出debug日志, 开启debug会影响性能.
//
//        // 友盟统计
//        MobclickAgent.setDebugMode(false);// 设置开启日志,发布时请关闭日志
//        MobclickAgent.openActivityDurationTrack(false);// 禁止默认的页面统计方式(可以自定义名字)
//
//        // IM初始化
//        IMHelper.init(instance);*/

        LogUtil.e("DeviceInfo：" + getDeviceInfo(instance));
    }

    public static BaseApplication getInstance() {
        return instance;
    }

    public static Context getAppContext() {
        return getInstance();
    }

    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            String device_id = tm.getDeviceId();

            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            String mac = wifi.getConnectionInfo().getMacAddress();
            json.put("mac", mac);

            if (BaseUtils.isEmptyString(device_id)) {
                device_id = mac;
            }

            if (BaseUtils.isEmptyString(device_id)) {
                device_id = android.provider.Settings.Secure.getString(
                        context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }

            json.put("device_id", device_id);

            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
