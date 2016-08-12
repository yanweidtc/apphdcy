package com.hdcy.base.utils.net;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.hdcy.base.BaseData;
import com.hdcy.base.BaseInfo;
import com.hdcy.base.utils.BaseUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

/**
 * Created by WeiYanGeorge on 2016-08-10.
 */

public class NetRequest implements BaseData {

    private RequestParams params;
    private String url;
    private NetRequestInfo netRequestInfo;
    private NetResponseInfo netResponseInfo;

    /**
     * 初始化请求
     * @param url 地址
     *
     */
    public NetRequest(String url) {
        this.url = url;

    }

    public void addHeader(String key, String value) {
        params.addHeader(key, value);
        netRequestInfo.setUrl(netRequestInfo.getUrl() + key + "=" + value + "&");
    }

    public void addParam(String key, String value) {
        params.addBodyParameter(key, value);
        netRequestInfo.setUrl(netRequestInfo.getUrl() + key + "=" + value + "&");
        LogUtil.e(key + " = " + value);
    }

    public void addParam(String key, int value) {
        addParam(key, value + "");
    }

    public void addParam(String key, double value) {
        addParam(key, value + "");
    }

    public void addParam(String key, File value) {
        params.setMultipart(true);
        params.addBodyParameter(key, value);
        netRequestInfo.setUrl(netRequestInfo.getUrl() + key + "=" + value + "&");
    }

    public void setTimeOut(int time) {
        params.setConnectTimeout(time);
    }
    /**
     * 请求/上传数据
     *
     * @param callBack 回调
     */
    public Callback.Cancelable post(final NetRequestCallBack callBack) {
        String str = netRequestInfo.getUrl();
        netRequestInfo.setUrl(str.substring(0, str.length() - 1));
        return x.http().post(params, new Callback.ProgressCallback<String>() {


            @Override
            public void onStarted() {
                LogUtil.e("onStart");
                if (callBack != null) {
                    callBack.onStart();
                }
            }

            @Override
            public void onWaiting() {
                LogUtil.e("onWaiting");
                callBack.onWaiting();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("onCancelled：" + cex.getMessage());
                cex.printStackTrace();
                callBack.onCancelled();
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                LogUtil.e("onLoading：" + current + " - " + total + " == " + (int) ((current * 1.0f / total) * 100) + "%");
                if (callBack != null) {
                    callBack.onLoading(total, current, current * 1.0f / total, isDownloading);
                }
            }

            @Override
            public void onSuccess(String result) {
                netResponseInfo.setResult(result);
                try {
                    JSONObject obj = new JSONObject(result);

                    int code = obj.optInt("code", -1);
                    netResponseInfo.setCode(code + "");
                    netResponseInfo.setMessage(obj.optString("message"));
                    if (code == 200) {
                        LogUtil.e("onSuccess：" + netRequestInfo.getUrl());
                        LogUtil.e("onSuccess：" + netResponseInfo.getResult());
                        netResponseInfo.setDataObj(obj.optJSONObject("data"));
                        netResponseInfo.setDataObj(obj.optJSONObject("data"));
                        netResponseInfo.setDataArr(obj.optJSONArray("data"));
                        if (callBack != null) {
                            callBack.onSuccess(netRequestInfo, netResponseInfo);
                        }
                    } else {
                        LogUtil.e("onError：" + netRequestInfo.getUrl());
                        LogUtil.e("onError：" + netResponseInfo.getMessage());
                        LogUtil.e("onError：" + netResponseInfo.getResult());
                        if (callBack != null) {
                            callBack.onError(netRequestInfo, netResponseInfo);
                        }
                    }
                } catch (JSONException e) {
                    LogUtil.e("onError：" + netRequestInfo.getUrl());
                    LogUtil.e("onError：" + netResponseInfo.getMessage());
                    LogUtil.e("onError：" + netResponseInfo.getResult());
                    if (callBack != null) {
                        callBack.onError(netRequestInfo, netResponseInfo);
                    }
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("onFailure：" + netRequestInfo.getUrl());
                LogUtil.e("onFailure：" + ex.getMessage());
                netResponseInfo.setMessage(ex.getMessage());
                if (callBack != null) {
                    callBack.onFailure(netRequestInfo, netResponseInfo);
                }
                ex.printStackTrace();
            }

            @Override
            public void onFinished() {
                LogUtil.e("onFinished");
                if (callBack != null) {
                    callBack.onFinished();
                }
            }
        });
    }

}
