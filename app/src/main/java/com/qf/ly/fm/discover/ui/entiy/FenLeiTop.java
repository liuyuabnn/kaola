package com.qf.ly.fm.discover.ui.entiy;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/17 0017.19:03
 * 版权所有 盗版必究
 */

public class FenLeiTop {

    /**
     * rid : 648
     * rtype : 16
     * rname : 搞笑
     * pic : http://image.kaolafm.net/mz/images/201609/72fb2249-1b44-4283-b023-dae6214b18f9/default.jpg
     * weburl :
     * des : 搞笑
     * albumName :
     * num : 0
     * host : []
     * mp3PlayUrl :
     * cornerMark : 0
     * rvalue : 648
     * tip :
     * followedNum : 0
     * listenNum : 0
     * area : null
     * reportUrl : []
     * expoUrl : []
     * adType : -1
     * adUserId :
     * adId :
     */

    private int rid;
    private int rtype;
    private String rname;
    private String pic;
    private String weburl;
    private String des;
    private String albumName;
    private int num;
    private String mp3PlayUrl;
    private int cornerMark;
    private String rvalue;
    private String tip;
    private int followedNum;
    private int listenNum;
    private Object area;
    private int adType;
    private String adUserId;
    private String adId;
    private List<?> host;
    private List<?> reportUrl;
    private List<?> expoUrl;

    public static FenLeiTop objectFromData(String str) {

        return new Gson().fromJson(str, FenLeiTop.class);
    }

    public static FenLeiTop objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getJSONObject(key).toString(), FenLeiTop.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<FenLeiTop> arrayFenLeiTopFromData(String str) {

        Type listType = new TypeToken<ArrayList<FenLeiTop>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<FenLeiTop> arrayFenLeiTopFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<FenLeiTop>>() {
            }.getType();

            return new Gson().fromJson(jsonObject.getJSONArray(key).toString(), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public int getRtype() {
        return rtype;
    }

    public void setRtype(int rtype) {
        this.rtype = rtype;
    }

    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getWeburl() {
        return weburl;
    }

    public void setWeburl(String weburl) {
        this.weburl = weburl;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getMp3PlayUrl() {
        return mp3PlayUrl;
    }

    public void setMp3PlayUrl(String mp3PlayUrl) {
        this.mp3PlayUrl = mp3PlayUrl;
    }

    public int getCornerMark() {
        return cornerMark;
    }

    public void setCornerMark(int cornerMark) {
        this.cornerMark = cornerMark;
    }

    public String getRvalue() {
        return rvalue;
    }

    public void setRvalue(String rvalue) {
        this.rvalue = rvalue;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public int getFollowedNum() {
        return followedNum;
    }

    public void setFollowedNum(int followedNum) {
        this.followedNum = followedNum;
    }

    public int getListenNum() {
        return listenNum;
    }

    public void setListenNum(int listenNum) {
        this.listenNum = listenNum;
    }

    public Object getArea() {
        return area;
    }

    public void setArea(Object area) {
        this.area = area;
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

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public List<?> getHost() {
        return host;
    }

    public void setHost(List<?> host) {
        this.host = host;
    }

    public List<?> getReportUrl() {
        return reportUrl;
    }

    public void setReportUrl(List<?> reportUrl) {
        this.reportUrl = reportUrl;
    }

    public List<?> getExpoUrl() {
        return expoUrl;
    }

    public void setExpoUrl(List<?> expoUrl) {
        this.expoUrl = expoUrl;
    }
}
