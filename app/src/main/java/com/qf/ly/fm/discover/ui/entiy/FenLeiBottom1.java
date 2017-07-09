package com.qf.ly.fm.discover.ui.entiy;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/18 0018.9:43
 * 版权所有 盗版必究
 */

public class FenLeiBottom1 {

    /**
     * name : 分类
     * divisionLineColor : #5493e6
     * dataList : []
     */

    private String name;
    private String divisionLineColor;
    private List<FenLeiBottom2> dataList;

    public static FenLeiBottom1 objectFromData(String str) {

        return new Gson().fromJson(str, FenLeiBottom1.class);
    }

    public static FenLeiBottom1 objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getJSONObject(key).toString(), FenLeiBottom1.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<FenLeiBottom1> arrayFenLeiBottom1FromData(String str) {

        Type listType = new TypeToken<ArrayList<FenLeiBottom1>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<FenLeiBottom1> arrayFenLeiBottom1FromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<FenLeiBottom1>>() {
            }.getType();

            return new Gson().fromJson(jsonObject.getJSONArray(key).toString(), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDivisionLineColor() {
        return divisionLineColor;
    }

    public void setDivisionLineColor(String divisionLineColor) {
        this.divisionLineColor = divisionLineColor;
    }

    public List<FenLeiBottom2> getDataList() {
        return dataList;
    }

    public void setDataList(List<FenLeiBottom2> dataList) {
        this.dataList = dataList;
    }
}
