package com.qf.ly.fm.discover.ui.entiy;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LY on 2016/10/14.18:45
 * 版权所有 盗版必究
 */

public class TuiJian2 implements Parcelable{

    protected TuiJian2(Parcel in) {
        weburl = in.readString();
        albumName = in.readString();
        rid = in.readLong();
        num = in.readInt();
        tip = in.readString();
        rvalue = in.readString();
        rname = in.readString();
        mp3PlayUrl = in.readString();
        listenNum = in.readInt();
        rtype = in.readInt();
        adId = in.readString();
        pic = in.readString();
        adType = in.readInt();
        adUserId = in.readString();
        des = in.readString();
        cornerMark = in.readInt();
        followedNum = in.readInt();
    }

    public static final Creator<TuiJian2> CREATOR = new Creator<TuiJian2>() {
        @Override
        public TuiJian2 createFromParcel(Parcel in) {
            return new TuiJian2(in);
        }

        @Override
        public TuiJian2[] newArray(int size) {
            return new TuiJian2[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(weburl);
        dest.writeString(albumName);
        dest.writeLong(rid);
        dest.writeInt(num);
        dest.writeString(tip);
        dest.writeString(rvalue);
        dest.writeString(rname);
        dest.writeString(mp3PlayUrl);
        dest.writeInt(listenNum);
        dest.writeInt(rtype);
        dest.writeString(adId);
        dest.writeString(pic);
        dest.writeInt(adType);
        dest.writeString(adUserId);
        dest.writeString(des);
        dest.writeInt(cornerMark);
        dest.writeInt(followedNum);
    }

    /**
     * weburl :
     * albumName : 郭德纲-话说北京[29段]
     * host : []
     * rid : 1100000051325
     * num : 0
     * tip :
     * rvalue : 1100000051325
     * rname : 郭德纲-话说北京[29段]
     * mp3PlayUrl :
     * area : null
     * listenNum : 0
     * rtype : 0
     * expoUrl : []
     * adId :
     * pic : http://image.kaolafm.net/mz/images/201610/8d96a555-3a64-44cc-90f1-b9b13ec4570b/default.jpg
     * reportUrl : []
     * adType : -1
     * adUserId :
     * des : 名家之作，不容错过
     * cornerMark : 0
     * followedNum : 0
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
    private List<?> host;
    private List<?> expoUrl;
    private List<?> reportUrl;

    public static TuiJian2 objectFromData(String str) {

        return new Gson().fromJson(str, TuiJian2.class);
    }

    public static TuiJian2 objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getJSONObject(key).toString(), TuiJian2.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<TuiJian2> arrayTuiJian2FromData(String str) {

        Type listType = new TypeToken<ArrayList<TuiJian2>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<TuiJian2> arrayTuiJian2FromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<TuiJian2>>() {
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
