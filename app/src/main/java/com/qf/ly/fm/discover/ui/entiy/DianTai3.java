package com.qf.ly.fm.discover.ui.entiy;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/18 0018.14:16
 * 版权所有 盗版必究
 */

public class DianTai3 {

    /**
     * type : 1
     * pic :
     * name : 国家台
     * id : 1
     */

    private int type;
    private String pic;
    private String name;
    private int id;

    public static DianTai3 objectFromData(String str) {

        return new Gson().fromJson(str, DianTai3.class);
    }

    public static DianTai3 objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getJSONObject(key).toString(), DianTai3.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<DianTai3> arrayDianTai3FromData(String str) {

        Type listType = new TypeToken<ArrayList<DianTai3>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<DianTai3> arrayDianTai3FromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<DianTai3>>() {
            }.getType();

            return new Gson().fromJson(jsonObject.getJSONArray(key).toString(), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
