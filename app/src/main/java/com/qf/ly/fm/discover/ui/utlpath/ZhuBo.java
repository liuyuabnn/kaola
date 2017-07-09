package com.qf.ly.fm.discover.ui.utlpath;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LY on 2016/10/10.21:17
 * 版权所有 盗版必究
 */

public class ZhuBo {

    /**
     * contentType : 1
     * relatedValue : -1
     * icon :
     * id : 254
     * componentType : 1
     * desc :
     * pic :
     * dataList : []
     * moreType : 3
     * count : 0
     * contentSourceId : 6
     * name : 轮播图
     * hasmore : 1
     */

    private int contentType;
    private String relatedValue;
    private String icon;
    private int id;
    private int componentType;
    private String desc;
    private String pic;
    private int moreType;
    private int count;
    private int contentSourceId;
    private String name;
    private int hasmore;
    private List<ZhuBo2> dataList;

    public static ZhuBo objectFromData(String str) {

        return new com.google.gson.Gson().fromJson(str, ZhuBo.class);
    }

    public static ZhuBo objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new com.google.gson.Gson().fromJson(jsonObject.getJSONObject(key).toString(), ZhuBo.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<ZhuBo> arrayZhuBoFromData(String str) {

        Type listType = new com.google.gson.reflect.TypeToken<ArrayList<ZhuBo>>() {
        }.getType();

        return new com.google.gson.Gson().fromJson(str, listType);
    }

    public static List<ZhuBo> arrayZhuBoFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new com.google.gson.reflect.TypeToken<ArrayList<ZhuBo>>() {
            }.getType();

            return new com.google.gson.Gson().fromJson(jsonObject.getJSONArray(key).toString(), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public String getRelatedValue() {
        return relatedValue;
    }

    public void setRelatedValue(String relatedValue) {
        this.relatedValue = relatedValue;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getComponentType() {
        return componentType;
    }

    public void setComponentType(int componentType) {
        this.componentType = componentType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getMoreType() {
        return moreType;
    }

    public void setMoreType(int moreType) {
        this.moreType = moreType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getContentSourceId() {
        return contentSourceId;
    }

    public void setContentSourceId(int contentSourceId) {
        this.contentSourceId = contentSourceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHasmore() {
        return hasmore;
    }

    public void setHasmore(int hasmore) {
        this.hasmore = hasmore;
    }

    public List<ZhuBo2> getDataList() {
        return dataList;
    }

    public void setDataList(List<ZhuBo2> dataList) {
        this.dataList = dataList;
    }
}
