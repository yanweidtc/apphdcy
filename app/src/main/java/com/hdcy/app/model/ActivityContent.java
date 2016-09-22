package com.hdcy.app.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by WeiYanGeorge on 2016-08-30.
 */

public class ActivityContent implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;

    private String name;

    private String image;

    private String type;

    private String sortType;

    private String actType;

    private Date startTime;

    private Date endTime;

    private int hot;

    private String address;

    private String desc;

    private String comment;

    private boolean finish;

    private boolean enable;

    private int sponsorId;

    private String sponsor;

    private int browseval;

    private int fansval;

    private boolean top;

    private int topIndex;

    private boolean recommend;

    private List<ActivityKwlist> kwlist ;

    private int weighting;

    private String sponsorName;

    private String sponsorImage;

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setImage(String image){
        this.image = image;
    }
    public String getImage(){
        return this.image;
    }
    public void setType(String type){
        this.type = type;
    }
    public String getType(){
        return this.type;
    }
    public void setSortType(String sortType){
        this.sortType = sortType;
    }
    public String getSortType(){
        return this.sortType;
    }
    public void setActType(String actType){
        this.actType = actType;
    }
    public String getActType(){
        return this.actType;
    }
    public void setStartTime(Date startTime){
        this.startTime = startTime;
    }
    public Date getStartTime(){
        return this.startTime;
    }
    public void setEndTime(Date endTime){
        this.endTime = endTime;
    }
    public Date getEndTime(){
        return this.endTime;
    }
    public void setHot(int hot){
        this.hot = hot;
    }
    public int getHot(){
        return this.hot;
    }
    public void setAddress(String address){
        this.address = address;
    }
    public String getAddress(){
        return this.address;
    }
    public void setDesc(String desc){
        this.desc = desc;
    }
    public String getDesc(){
        return this.desc;
    }
    public void setComment(String comment){
        this.comment = comment;
    }
    public String getComment(){
        return this.comment;
    }
    public void setFinish(boolean finish){
        this.finish = finish;
    }
    public boolean getFinish(){
        return this.finish;
    }
    public void setEnable(boolean enable){
        this.enable = enable;
    }
    public boolean getEnable(){
        return this.enable;
    }
    public void setSponsorId(int sponsorId){
        this.sponsorId = sponsorId;
    }
    public int getSponsorId(){
        return this.sponsorId;
    }
    public void setSponsor(String sponsor){
        this.sponsor = sponsor;
    }
    public String getSponsor(){
        return this.sponsor;
    }
    public void setBrowseval(int browseval){
        this.browseval = browseval;
    }
    public int getBrowseval(){
        return this.browseval;
    }
    public void setFansval(int fansval){
        this.fansval = fansval;
    }
    public int getFansval(){
        return this.fansval;
    }
    public void setTop(boolean top){
        this.top = top;
    }
    public boolean getTop(){
        return this.top;
    }
    public void setTopIndex(int topIndex){
        this.topIndex = topIndex;
    }
    public int getTopIndex(){
        return this.topIndex;
    }
    public void setRecommend(boolean recommend){
        this.recommend = recommend;
    }
    public boolean getRecommend(){
        return this.recommend;
    }
    public void setKwlist(List<ActivityKwlist> kwlist){
        this.kwlist = kwlist;
    }
    public List<ActivityKwlist> getKwlist(){
        return this.kwlist;
    }
    public void setWeighting(int weighting){
        this.weighting = weighting;
    }
    public int getWeighting(){
        return this.weighting;
    }

    public String getSponsorImage() {
        return sponsorImage;
    }

    public void setSponsorImage(String sponsorImage) {
        this.sponsorImage = sponsorImage;
    }


    public String getSponsorName() {
        return sponsorName;
    }

    public void setSponsorName(String sponsorName) {
        this.sponsorName = sponsorName;
    }



}
