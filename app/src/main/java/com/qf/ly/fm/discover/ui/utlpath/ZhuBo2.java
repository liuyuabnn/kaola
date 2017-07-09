package com.qf.ly.fm.discover.ui.utlpath;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LY on 2016/10/10.21:29
 * 版权所有 盗版必究
 */

public class ZhuBo2 {

    /**
     * weburl :
     * albumName :
     * host : []
     * rid : 2052665
     * num : 0
     * tip :
     * rvalue : 2052665
     * rname : DJ小强
     * mp3PlayUrl :
     * area : null
     * listenNum : 0
     * rtype : 13
     * expoUrl : []
     * adId :
     * pic : http://image.kaolafm.net/mz/images/201609/6e59c786-cd36-476b-bb34-c54674220900/default.jpg
     * reportUrl : []
     * adType : -1
     * adUserId :
     * des : DJ小强
     * cornerMark : 0
     * followedNum : 0
     * fansCount : 2623
     * recommendReson : 讲起鬼来自己都害怕
     * isVanchor : 1
     * likedNum : 1533028
     * avatar : http://image.kaolafm.net/mz/images/201512/ca6222c7-a3ea-43bd-9d52-a06b41a81dde/default.jpg
     * liveStatus : 0
     * relation : 0
     * gender : 0
     * extraAttributes :
     * desc : 一个讲段子的帅哥
     * nickName : 小胆
     * uid : 1015105
     */

    private String weburl;
    private String albumName;
    private long rid;
    private int num;
    private String tip;
    private String rvalue;
    private String rname;
    private String mp3PlayUrl;
    private Object area;
    private int listenNum;
    private int rtype;
    private String adId;
    private String pic;
    private int adType;
    private String adUserId;
    private String des;
    private int cornerMark;
    private int followedNum;
    private int fansCount;
    private String recommendReson;
    private int isVanchor;
    private int likedNum;
    private String avatar;
    private int liveStatus;
    private int relation;
    private int gender;
    private String extraAttributes;
    private String desc;
    private String nickName;
    private int uid;
    private List<?> host;
    private List<?> expoUrl;
    private List<?> reportUrl;

    public static ZhuBo2 objectFromData(String str) {

        return new Gson().fromJson(str, ZhuBo2.class);
    }

    public static ZhuBo2 objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getJSONObject(key).toString(), ZhuBo2.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<ZhuBo2> arrayZhuBo2FromData(String str) {

        Type listType = new TypeToken<ArrayList<ZhuBo2>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<ZhuBo2> arrayZhuBo2FromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<ZhuBo2>>() {
            }.getType();

            return new Gson().fromJson(jsonObject.getJSONArray(key).toString(), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


    }

    public String getWeburl() {
        return weburl;
    }

    public void setWeburl(String weburl) {
        this.weburl = weburl;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public long getRid() {
        return rid;
    }

    public void setRid(long rid) {
        this.rid = rid;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getRvalue() {
        return rvalue;
    }

    public void setRvalue(String rvalue) {
        this.rvalue = rvalue;
    }

    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }

    public String getMp3PlayUrl() {
        return mp3PlayUrl;
    }

    public void setMp3PlayUrl(String mp3PlayUrl) {
        this.mp3PlayUrl = mp3PlayUrl;
    }

    public Object getArea() {
        return area;
    }

    public void setArea(Object area) {
        this.area = area;
    }

    public int getListenNum() {
        return listenNum;
    }

    public void setListenNum(int listenNum) {
        this.listenNum = listenNum;
    }

    public int getRtype() {
        return rtype;
    }

    public void setRtype(int rtype) {
        this.rtype = rtype;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getAdType() {
        return adType;
    }

    public void setAdType(int adType) {
        this.adType = adType;
    }

    public String getAdUserId() {
        return adUserId;
    }

    public void setAdUserId(String adUserId) {
        this.adUserId = adUserId;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public int getCornerMark() {
        return cornerMark;
    }

    public void setCornerMark(int cornerMark) {
        this.cornerMark = cornerMark;
    }

    public int getFollowedNum() {
        return followedNum;
    }

    public void setFollowedNum(int followedNum) {
        this.followedNum = followedNum;
    }

    public int getFansCount() {
        return fansCount;
    }

    public void setFansCount(int fansCount) {
        this.fansCount = fansCount;
    }

    public String getRecommendReson() {
        return recommendReson;
    }

    public void setRecommendReson(String recommendReson) {
        this.recommendReson = recommendReson;
    }

    public int getIsVanchor() {
        return isVanchor;
    }

    public void setIsVanchor(int isVanchor) {
        this.isVanchor = isVanchor;
    }

    public int getLikedNum() {
        return likedNum;
    }

    public void setLikedNum(int likedNum) {
        this.likedNum = likedNum;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getLiveStatus() {
        return liveStatus;
    }

    public void setLiveStatus(int liveStatus) {
        this.liveStatus = liveStatus;
    }

    public int getRelation() {
        return relation;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getExtraAttributes() {
        return extraAttributes;
    }

    public void setExtraAttributes(String extraAttributes) {
        this.extraAttributes = extraAttributes;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public List<?> getHost() {
        return host;
    }

    public void setHost(List<?> host) {
        this.host = host;
    }

    public List<?> getExpoUrl() {
        return expoUrl;
    }

    public void setExpoUrl(List<?> expoUrl) {
        this.expoUrl = expoUrl;
    }

    public List<?> getReportUrl() {
        return reportUrl;
    }

    public void setReportUrl(List<?> reportUrl) {
        this.reportUrl = reportUrl;
    }
}
