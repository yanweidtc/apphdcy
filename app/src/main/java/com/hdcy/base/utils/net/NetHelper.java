package com.hdcy.base.utils.net;

import com.alibaba.fastjson.JSON;
import com.hdcy.app.model.NewsCategory;

import org.json.JSONArray;
import org.xutils.common.Callback;

/**
 * Created by WeiYanGeorge on 2016-08-10.
 */

public class NetHelper {

    private static class NetHelperHolder {
        private static final NetHelper instance = new NetHelper();
    }

    public static NetHelper getInstance() {
        return NetHelperHolder.instance;
    }

    private NetHelper() {
        super();
    }

    /**
     * 获取资讯分类
     *
     * @param callBack 回调
     */
    public Callback.Cancelable GetNewsCategoryList(final NetRequestCallBack callBack){
        NetRequest request = new NetRequest("/tag/child");
        return request.post(new NetRequestCallBack() {
            @Override
            public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                JSONArray dataArr = responseInfo.getDataArr();
                if (dataArr != null){
                    responseInfo.setNewsCategoryList(JSON.parseArray(dataArr.toString(), NewsCategory.class));
                }
                callBack.onSuccess(requestInfo, responseInfo);
            }

            @Override
            public void onError(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                callBack.onError(requestInfo, responseInfo);

            }

            @Override
            public void onFailure(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                callBack.onFailure(requestInfo,responseInfo);
            }
        });
    }
}
