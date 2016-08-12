package com.hdcy.base.utils.net;


import org.json.JSONArray;
import org.json.JSONObject;

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

}
