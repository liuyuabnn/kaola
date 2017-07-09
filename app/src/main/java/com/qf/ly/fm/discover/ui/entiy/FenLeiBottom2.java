package com.qf.ly.fm.discover.ui.entiy;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/18 0018.9:46
 * 版权所有 盗版必究
 */

public class FenLeiBottom2 {

    /**
     * categoryId : 201
     * title : 亲子
     * type : 5
     * parentId : -1
     * parentCategoryName :
     * label : -1
     * icon : http://image.kaolafm.net/mz/images/201512/6e237188-d4b0-4155-b30d-2c947a0435c2/default.png
     * backgroundPic : http://image.kaolafm.net/mz/images/201510/f07c85a7-dd0d-41e6-93d2-b181642bdcd2/default.png
     */

    private int categoryId;
    private String title;
    private int type;
    private int parentId;
    private String parentCategoryName;
    private int label;
    private String icon;
    private String backgroundPic;

    public static FenLeiBottom2 objectFromData(String str) {

        return new Gson().fromJson(str, FenLeiBottom2.class);
    }

    public static FenLeiBottom2 objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getJSONObject(key).toString(), FenLeiBottom2.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<FenLeiBottom2> arrayFenLeiBottom2FromData(String str) {

        Type listType = new TypeToken<ArrayList<FenLeiBottom2>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<FenLeiBottom2> arrayFenLeiBottom2FromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<FenLeiBottom2>>() {
            }.getType();

            return new Gson().fromJson(jsonObject.getJSONArray(key).toString(), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getParentCategoryName() {
        return parentCategoryName;
    }

    public void setParentCategoryName(String parentCategoryName) {
        this.parentCategoryName = parentCategoryName;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getBackgroundPic() {
        return backgroundPic;
    }

    public void setBackgroundPic(String backgroundPic) {
        this.backgroundPic = backgroundPic;
    }
}
