package com.hdcy.app.model;

import java.io.Serializable;

/**
 * Created by WeiYanGeorge on 2016-08-23.
 */

public class Replys implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;

    private int createdTime;

    private String target;

    private int targetId;

    private int createrId;

    private String createrName;

    private String createrHeadimgurl;

    private String replyToId;

    private String replyToName;

    private String content;

    private int praiseCount;

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
    public void setTarget(String target){
        this.target = target;
    }
    public String getTarget(){
        return this.target;
    }
    public void setTargetId(int targetId){
        this.targetId = targetId;
    }
    public int getTargetId(){
        return this.targetId;
    }
    public void setCreaterId(int createrId){
        this.createrId = createrId;
    }
    public int getCreaterId(){
        return this.createrId;
    }
    public void setCreaterName(String createrName){
        this.createrName = createrName;
    }
    public String getCreaterName(){
        return this.createrName;
    }
    public void setCreaterHeadimgurl(String createrHeadimgurl){
        this.createrHeadimgurl = createrHeadimgurl;
    }
    public String getCreaterHeadimgurl(){
        return this.createrHeadimgurl;
    }
    public void setReplyToId(String replyToId){
        this.replyToId = replyToId;
    }
    public String getReplyToId(){
        return this.replyToId;
    }
    public void setReplyToName(String replyToName){
        this.replyToName = replyToName;
    }
    public String getReplyToName(){
        return this.replyToName;
    }
    public void setContent(String content){
        this.content = content;
    }
    public String getContent(){
        return this.content;
    }
    public void setPraiseCount(int praiseCount){
        this.praiseCount = praiseCount;
    }
    public int getPraiseCount(){
        return this.praiseCount;
    }
}
