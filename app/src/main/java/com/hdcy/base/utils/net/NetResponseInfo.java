package com.hdcy.base.utils.net;


import com.hdcy.app.model.NewsCategory;

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

}
