package com.qf.ly.fm.other.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by LY on 2016/10/11.9:55
 * 版权所有 盗版必究
 */

public class SavePrefernce {
    /**
     * 保存请求json服务器的字符串到指定的SharedPreferences 文件中
     * @param context
     * @param fileName
     * @param flag
     * @return
     */

    public static String getCacheFromPreference(Context context, String fileName, String flag) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(flag, "");
        return json;
    }

    /**
     * 获取指定的SharedPreferences文件里指定标签内容保存的json字符串
     * @param context
     * @param fileName
     * @param flag
     * @param json
     */
    public static void saveCacheToPreference(Context context, String fileName, String flag,String json) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(flag,json);
        editor.commit();
    }
    /**
     * 获取指定的SharedPreferences文件里指定标签内容保存的时间
     *
     * @param context 上下文
     * @param fileName 文件名
     * @param flagName 标签名
     * @return 指定的json
     */
    public static long getCacheTimeFromPreference(Context context, String fileName, String flagName)
    {
        SharedPreferences preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        long value = preferences.getLong(flagName, 0);
        return value;
    }

    /**
     * 保存请求json服务器的时间到指定的SharedPreferences 文件中
     *
     * @param context 上下文
     * @param fileName 文件名
     * @param flagName 标签名
     * @param time 服务器时间
     */
    public static void saveCacheTimeToPreference(Context context, String fileName, String flagName, long time)
    {
        SharedPreferences preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(flagName, time);
        editor.commit();
    }

}
