package com.hdcy.base.utils.net;


import com.hdcy.app.model.Article;
import com.hdcy.app.model.ArticleInfo;
import com.hdcy.app.model.Comments;
import com.hdcy.app.model.CommentsContent;
import com.hdcy.app.model.Content;
import com.hdcy.app.model.NewsArticleInfo;
import com.hdcy.app.model.NewsCategory;
import com.nostra13.universalimageloader.core.assist.deque.LIFOLinkedBlockingDeque;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by WeiYanGeorge on 2016-08-10.
 *
 */

public class NetResponseInfo {
    private String code;
    private String message;
    private String result;
    private JSONObject dataObj;
    private JSONArray dataArr;
    private List<NewsCategory> newsCategoryList;
    private List<Content> contentList;
    private List<Comments> commentsList;

    public List<CommentsContent> getCommentsContentList() {
        return commentsContentList;
    }


    public ArticleInfo getArticleInfo() {
        return articleInfo;
    }

    public void setArticleInfo(ArticleInfo articleInfo) {
        this.articleInfo = articleInfo;
    }

    public ArticleInfo articleInfo;



    public void setCommentsContentList(List<CommentsContent> commentsContentList) {
        this.commentsContentList = commentsContentList;
    }

    private List<CommentsContent> commentsContentList;


    public List<Comments> getCommentsList() {
        return commentsList;
    }

    public void setCommentsList(List<Comments> commentsList) {
        this.commentsList = commentsList;
    }


    public List<NewsArticleInfo> getNewsArticleInfoList() {
        return newsArticleInfoList;
    }

    public void setNewsArticleInfoList(List<NewsArticleInfo> newsArticleInfoList) {
        this.newsArticleInfoList = newsArticleInfoList;
    }

    private List<NewsArticleInfo> newsArticleInfoList;



    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public JSONObject getDataObj() {
        return dataObj;
    }

    public void setDataObj(JSONObject dataObj) {
        this.dataObj = dataObj;
    }

    public JSONArray getDataArr() {
        return dataArr;
    }

    public void setDataArr(JSONArray dataArr) {
        this.dataArr = dataArr;
    }

    public List<NewsCategory> getNewsCategoryList(){
        return newsCategoryList;
    }

    public void setNewsCategoryList(List<NewsCategory> newsCategoryList){
        this.newsCategoryList = newsCategoryList;
    }
    public List<Content> getContentList() {
        return contentList;
    }

    public void setContentList(List<Content> contentList) {
        this.contentList = contentList;
    }


}
