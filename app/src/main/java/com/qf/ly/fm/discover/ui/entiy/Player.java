package com.qf.ly.fm.discover.ui.entiy;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/24 0024.18:42
 * 版权所有 盗版必究
 */

public class Player {


    /**
     * id : 1600000000676
     * name : 西藏科教广播 FM106.3
     * pic : http://image.kaolafm.net/mz/images/201609/7a71dabd-c118-41a0-8ca1-b86d606d804c/default.jpg
     * classifyName : 省市台
     * isSubscribe : 0
     * playUrl : http://trslbs.kaolafm.com/016f63815d64d4db/1600000000676/playlist.m3u8
     * shareUrl : http://m.kaolafm.com/share/liveplay/index.html
     * onLineNum : 0
     * likedNum : 0
     * webPic : null
     * status : 1
     * classifyId : 2
     * cityId : null
     * cityName : null
     * roomId : 0
     */

    private long id;
    private String name;
    private String pic;
    private String classifyName;
    private int isSubscribe;
    private String playUrl;
    private String shareUrl;
    private int onLineNum;
    private int likedNum;
    private Object webPic;
    private int status;
    private int classifyId;
    private Object cityId;
    private Object cityName;
    private int roomId;

    public static Player objectFromData(String str) {

        return new Gson().fromJson(str, Player.class);
    }

    public static Player objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getJSONObject(key).toString(), Player.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<Player> arrayPlayerFromData(String str) {

        Type listType = new TypeToken<ArrayList<Player>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<Player> arrayPlayerFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<Player>>() {
            }.getType();

            return new Gson().fromJson(jsonObject.getJSONArray(key).toString(), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
    }

    public int getIsSubscribe() {
        return isSubscribe;
    }

    public void setIsSubscribe(int isSubscribe) {
        this.isSubscribe = isSubscribe;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public int getOnLineNum() {
        return onLineNum;
    }

    public void setOnLineNum(int onLineNum) {
        this.onLineNum = onLineNum;
    }

    public int getLikedNum() {
        return likedNum;
    }

    public void setLikedNum(int likedNum) {
        this.likedNum = likedNum;
    }

    public Object getWebPic() {
        return webPic;
    }

    public void setWebPic(Object webPic) {
        this.webPic = webPic;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(int classifyId) {
        this.classifyId = classifyId;
    }

    public Object getCityId() {
        return cityId;
    }

    public void setCityId(Object cityId) {
        this.cityId = cityId;
    }

    public Object getCityName() {
        return cityName;
    }

    public void setCityName(Object cityName) {
        this.cityName = cityName;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
}
