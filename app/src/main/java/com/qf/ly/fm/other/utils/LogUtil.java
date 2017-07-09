package com.qf.ly.fm.other.utils;

/**
 * Created by LY on 2016/10/10.11:14
 * 版权所有 盗版必究
 */

public class LogUtil {
    /**
     * 是否是调试版本
     */
    public static final boolean isDebug = true;

    private static final String TAG = "print";

    public static void d(String msg)
    {
        if (isDebug)
        {
            android.util.Log.d(TAG, msg);
        }
    }

    public static void w(String msg)
    {
        if (isDebug)
        {
            android.util.Log.w(TAG, msg);
        }
    }

    public static void e(String msg)
    {
        if (isDebug)
        {
            android.util.Log.e(TAG, msg);
        }
    }
}
