package com.hdcy.base.utils.net;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.hdcy.app.model.Content;
import com.hdcy.app.model.NewsArticleInfo;
import com.hdcy.app.model.NewsCategory;

import org.json.JSONArray;
import org.json.JSONObject;
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
        Log.e("nettest","new");
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

    /**
     * 获取资讯列表
     * @param pagecount 每页显示数量
     * @param tagId 资讯类别id
     * @param callBack
     * @return
     */
    public Callback.Cancelable GetNewsArticleContent(int pagecount,String tagId,final NetRequestCallBack callBack){
        NetRequest request = new NetRequest("/article/");
        request.addParam("enable","true");
        request.addParam("page",pagecount);
        request.addParam("size",2);
        request.addParam("sort","pagecount");
        request.addParam("tagid",tagId);
        return request.postarray(new NetRequestCallBack() {
            @Override
            public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                JSONArray dataObj = responseInfo.getDataArr();
                if (dataObj != null){
                    responseInfo.setContentList(JSON.parseArray(dataObj.toString(), Content.class));
                }
                callBack.onSuccess(requestInfo, responseInfo);
                Log.e("article2","sucess");
            }

            @Override
            public void onError(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                Log.e("article2","onfailure");

            }

            @Override
            public void onFailure(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                Log.e("article2","onfailure");

            }
        });
    }


}
