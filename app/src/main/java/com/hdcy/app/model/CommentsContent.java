package com.hdcy.app.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by WeiYanGeorge on 2016-08-23.
 */

public class CommentsContent implements Serializable {
    private static final long serialVersionUID = 1L;


    private int id;

    private Date createdTime;

    private String target;

    private long targetId;

    private long createrId;

    private String createrName;

    private String createrHeadimgurl;

    private String replyToId;

    private String replyToName;

    private String content;

    private int praiseCount;

    public List<Replys> getReplysList() {
        return replysList;
    }

    public void setReplysList(List<Replys> replysList) {
        this.replysList = replysList;
    }

    private List<Replys> replysList;


    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setCreatedTime(Date createdTime){
        this.createdTime = createdTime;
    }
    public Date getCreatedTime(){
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
    public long getTargetId(){
        return this.targetId;
    }
    public void setCreaterId(int createrId){
        this.createrId = createrId;
    }
    public long getCreaterId(){
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
