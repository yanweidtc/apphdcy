package com.hdcy.app.model;

import java.io.Serializable;

/**
 * Created by WeiYanGeorge on 2016-08-31.
 */

public class ActivityKwlist implements Serializable {

    private static final long serialVersionUID = 1L;


    private int id;

    private int createdTime;

    private String modifyTime;

    private String keyWord;

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setCreatedTime(int createdTime){
        this.createdTime = createdTime;
    }
    public int getCreatedTime(){
        return this.createdTime;
    }
    public void setModifyTime(String modifyTime){
        this.modifyTime = modifyTime;
    }
    public String getModifyTime(){
        return this.modifyTime;
    }
    public void setKeyWord(String keyWord){
        this.keyWord = keyWord;
    }
    public String getKeyWord(){
        return this.keyWord;
    }

}
