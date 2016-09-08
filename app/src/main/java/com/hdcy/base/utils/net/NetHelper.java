package com.hdcy.base.utils.net;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.hdcy.app.model.ActivityContent;
import com.hdcy.app.model.ActivityDetails;
import com.hdcy.app.model.ArticleInfo;
import com.hdcy.app.model.RootListInfo;
import com.hdcy.app.model.CommentsContent;
import com.hdcy.app.model.Content;
import com.hdcy.app.model.NewsCategory;
import com.hdcy.app.model.PraiseResult;
import com.hdcy.app.model.Replys;
import com.hdcy.app.model.Result;

import org.json.JSONArray;
import org.json.JSONException;
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
        return request.getarray(new NetRequestCallBack() {
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

    public Callback.Cancelable GetWholeNewsArticleContent(int pagecount,final NetRequestCallBack callBack){
        NetRequest request = new NetRequest("/article/");
        request.addParam("enable","true");
        request.addParam("page",pagecount);
        request.addParam("size",10);
        request.addParam("sort","pagecount");
        return request.postarray(new NetRequestCallBack() {
            @Override
            public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                JSONArray dataObj = responseInfo.getDataArr();
                JSONObject dataObj1 = responseInfo.getDataObj();
                if (dataObj != null){
                    responseInfo.setContentList(JSON.parseArray(dataObj.toString(), Content.class));
                    responseInfo.setRootListInfo(JSON.parseObject(dataObj1.toString(),RootListInfo.class));
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

    /**
     * 获取资讯列表
     * @param pagecount 每页显示数量
     * @param tagId 资讯类别id
     * @param callBack
     * @return
     */
    public Callback.Cancelable GetNewsArticleContent(int pagecount,int tagId,final NetRequestCallBack callBack){
        NetRequest request = new NetRequest("/article/");
        request.addParam("enable","true");
        request.addParam("page",pagecount);
        request.addParam("size","20");
        request.addParam("sort","pagecount");
        request.addParam("tagId",tagId);
        return request.postarray(new NetRequestCallBack() {
            @Override
            public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                JSONArray dataObj = responseInfo.getDataArr();
                JSONObject dataObj1 = responseInfo.getDataObj();
                if (dataObj != null){
                    responseInfo.setContentList(JSON.parseArray(dataObj.toString(), Content.class));
                    responseInfo.setRootListInfo(JSON.parseObject(dataObj1.toString(), RootListInfo.class));
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

    /**
     * 对资讯文章发布评论
     * @param targetid
     * @param content
     * @param callBack
     * @return
     */
    public Callback.Cancelable PublishComments(String targetid,String content,String target,String replyid,final NetRequestCallBack callBack){
        NetRequest request = new NetRequest("/comment/");
        request.addHeader("Authorization","Basic MToxMjM0NTY=");
        request.addHeader("Content-Type", "application/json;charset=UTF-8");
        JSONObject obj = new JSONObject();
        try {
                obj.put("replyToId",replyid);
                obj.put("target", target);
                obj.put("targetId", targetid);
                obj.put("content", content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addParamjson(obj.toString());

        return request.postinfo(new NetRequestCallBack() {
            @Override
            public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                JSONObject dataObj = responseInfo.getDataObj();
                if (dataObj != null){
                    responseInfo.setCommentsContent(JSON.parseObject(dataObj.toString(), CommentsContent.class));
                    responseInfo.setReplys(JSON.parseObject(dataObj.toString(), Replys.class));
                }
                callBack.onSuccess(requestInfo, responseInfo);
            }

            @Override
            public void onError(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {

            }

            @Override
            public void onFailure(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {

            }
        });
    }

    /**
     * 点赞功能
     */
    public Callback.Cancelable  DoPraise( String targetId, final NetRequestCallBack callBack){
        NetRequest request = new NetRequest("/praise/");
        request.addHeader("Authorization","Basic MToxMjM0NTY=");
        request.addHeader("Content-Type", "application/json;charset=UTF-8");
        JSONObject obj = new JSONObject();
        try {
            obj.put("target", "comment");
            obj.put("targetId", targetId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addParamjson(obj.toString());
        return request.postinfo(new NetRequestCallBack() {
            @Override
            public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                JSONObject dataObj = responseInfo.getDataObj();
                if (dataObj != null){
                    responseInfo.setPraiseResult(JSON.parseObject(dataObj.toString(), PraiseResult.class));
                }
                Log.e("点赞成功",dataObj.toString());
                callBack.onSuccess(requestInfo, responseInfo);
            }

            @Override
            public void onError(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {

            }

            @Override
            public void onFailure(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {

            }
        });
    }

    /**
     * 取消点赞
     */

    public Callback.Cancelable UnDoPraise( String targetId, final NetRequestCallBack callBack){
        NetRequest request = new NetRequest("/praise/");
        request.addHeader("Authorization","Basic MToxMjM0NTY=");
        request.addHeader("Content-Type", "application/json;charset=UTF-8");

        JSONObject obj = new JSONObject();
        try {
            obj.put("target", "comment");
            obj.put("targetId", targetId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addParamjson(obj.toString());
        return request.putinfo(new NetRequestCallBack() {
            @Override
            public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                callBack.onSuccess(requestInfo, responseInfo);

            }

            @Override
            public void onError(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {

            }

            @Override
            public void onFailure(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {

            }
        });
    }



    /**
     * 得到文章内容详情
     * @param tagId
     * @param callBack
     * @return
     */
    public Callback.Cancelable GetArticleInfo(String tagId,final NetRequestCallBack callBack){
        NetRequest request = new NetRequest("/article/"+tagId);
        return request.postobject(new NetRequestCallBack() {
            @Override
            public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                JSONObject dataObj = responseInfo.getDataObj();
                if (dataObj != null){
                    responseInfo.setArticleInfo(JSON.parseObject(dataObj.toString(), ArticleInfo.class));
                }
                callBack.onSuccess(requestInfo, responseInfo);
            }

            @Override
            public void onError(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {

            }

            @Override
            public void onFailure(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {

            }
        });
    }

    /**
     *
     * @param tagId
     * @param callBack
     * 得到资讯评论列表
     * @return
     */

    public Callback.Cancelable GetCommentsList(String tagId,String target,int pagecount,final NetRequestCallBack callBack){
        NetRequest request = new NetRequest("/comments/");
        request.addParam("targetId",tagId);
        request.addParam("page",pagecount);
        request.addParam("size",30);
        request.addParam("sort","createdTime,desc");
        request.addParam("target",target);
        return request.postarray(new NetRequestCallBack() {
            @Override
            public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                JSONArray dataObj = responseInfo.getDataArr();
                JSONObject dataObj1 = responseInfo.getDataObj();
                if (dataObj != null){
                    responseInfo.setCommentsContentList(JSON.parseArray(dataObj.toString(), CommentsContent.class));
                    responseInfo.setRootListInfo(JSON.parseObject(dataObj1.toString(), RootListInfo.class));
                }
                callBack.onSuccess(requestInfo, responseInfo);
                Log.e("comments","sucess");
            }

            @Override
            public void onError(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                Log.e("comments","onfailure");

            }

            @Override
            public void onFailure(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                Log.e("comments","onfailure");

            }
        });
    }

    /**
     * 得到活动列表
     * 全部null 线上为ONLINE 线下为ACTIVTY
     * @param activityType
     * @param callBack
     * @return
     */

    public Callback.Cancelable GetPaticipationList(String activityType,int pagecount,final NetRequestCallBack callBack){
        NetRequest request = new NetRequest("/participation/");
        request.addParam("page",pagecount);
        request.addParam("enable","true");
        request.addParam("actType",activityType);
        request.addParam("size","10");
        request.addParam("sort","createdTime,desc");
        return request.postarray(new NetRequestCallBack() {
            @Override
            public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                JSONArray dataObj = responseInfo.getDataArr();
                JSONObject dataObj1 = responseInfo.getDataObj();
                if (dataObj != null){
                    responseInfo.setActivityContentList(JSON.parseArray(dataObj.toString(), ActivityContent.class));
                    responseInfo.setRootListInfo(JSON.parseObject(dataObj1.toString(), RootListInfo.class));
                }
                callBack.onSuccess(requestInfo, responseInfo);
                Log.e("activity","sucess");
            }

            @Override
            public void onError(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                Log.e("activity","onfailure");

            }

            @Override
            public void onFailure(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                Log.e("activity","onfailure");

            }
        });
    }

    /**
     * 得到轮播图信息
     *
     * @param callBack
     * @return
     */

    public Callback.Cancelable GetActivityTopBanner( final NetRequestCallBack callBack){
        NetRequest request = new NetRequest("/participation/");
        request.addParam("page",0);
        request.addParam("enable","true");
        request.addParam("size","10");
        request.addParam("sort","createdTime,desc");
        request.addParam("top","true");
        return request.postarray(new NetRequestCallBack() {
            @Override
            public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                JSONArray dataObj = responseInfo.getDataArr();
                if (dataObj != null){
                    responseInfo.setActivityContentList(JSON.parseArray(dataObj.toString(), ActivityContent.class));
                }
                callBack.onSuccess(requestInfo, responseInfo);
                Log.e("activityhot","sucess");
            }

            @Override
            public void onError(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                Log.e("activityhot","onfailure");

            }

            @Override
            public void onFailure(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                Log.e("activityhot","onfailure");

            }
        });
    }

    public Callback.Cancelable GetActivityRecommended( final NetRequestCallBack callBack){
        NetRequest request = new NetRequest("/participation/");
        request.addParam("page",0);
        request.addParam("enable","true");
        request.addParam("size","10");
        request.addParam("sort","createdTime,desc");
        request.addParam("recommend","true");
        return request.postarray(new NetRequestCallBack() {
            @Override
            public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                JSONArray dataObj = responseInfo.getDataArr();
                if (dataObj != null){
                    responseInfo.setActivityContentList(JSON.parseArray(dataObj.toString(), ActivityContent.class));
                }
                callBack.onSuccess(requestInfo, responseInfo);
                Log.e("activityhot","sucess");
            }

            @Override
            public void onError(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                Log.e("activityhot","onfailure");

            }

            @Override
            public void onFailure(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                Log.e("activityhot","onfailure");

            }
        });
    }

    public Callback.Cancelable GetActivityDeatil(String activityId,final NetRequestCallBack callBack){
        NetRequest request = new NetRequest("/activity/"+activityId);
        return request.postobject(new NetRequestCallBack() {
            @Override
            public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                JSONObject dataObj = responseInfo.getDataObj();
                if (dataObj != null){
                    responseInfo.setActivityDetails(JSON.parseObject(dataObj.toString(), ActivityDetails.class));
                }
                callBack.onSuccess(requestInfo, responseInfo);
                Log.e("ActivityDeatils","success");
            }

            @Override
            public void onError(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                Log.e("ActivityDeatils","failed");

            }

            @Override
            public void onFailure(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                Log.e("ActivityDeatils","failed");
            }
        });
    }

    public Callback.Cancelable GetCurrentPaticipationStatus(String activityid,final NetRequestCallBack callBack){
        NetRequest request = new NetRequest("/participator/member");
        request.addHeader("Authorization","Basic MToxMjM0NTY=");
        request.addHeader("Content-Type", "application/json;charset=UTF-8");

        request.addParam("participationId",activityid);

        return request.postobject(new NetRequestCallBack() {
            @Override
            public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                JSONObject dataObj = responseInfo.getDataObj();
                if (dataObj != null){
                    responseInfo.setResultinfo(JSON.parseObject(dataObj.toString(), Result.class));
                }
                Log.e("aticipationStatus", "success");
                callBack.onSuccess(requestInfo, responseInfo);
            }

            @Override
            public void onError(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {

            }

            @Override
            public void onFailure(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {

            }
        });
    }

    public Callback.Cancelable RegisterOfflineActivity(String activityId,String message,final NetRequestCallBack callBack){
        NetRequest request = new NetRequest("/activityParticipator/");
        request.addHeader("Authorization","Basic MToxMjM0NTY=");
        request.addHeader("Content-Type", "application/json;charset=UTF-8");
        JSONObject obj = new JSONObject();
        try {
            obj.put("activityId",activityId);
            obj.put("message", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        request.addParamjson(obj.toString());

        return request.postinfo(new NetRequestCallBack() {
            @Override
            public void onSuccess(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {
                Log.e("报名状态","success");
                JSONObject dataObj = responseInfo.getDataObj();
                if (dataObj != null){

                }
                callBack.onSuccess(requestInfo, responseInfo);
            }

            @Override
            public void onError(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {

            }

            @Override
            public void onFailure(NetRequestInfo requestInfo, NetResponseInfo responseInfo) {

            }
        });
    }






}
