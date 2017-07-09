package com.qf.ly.fm.other.utils;

import android.content.Context;

/**
 * Created by LY on 2016/10/12.12:17
 * 版权所有 盗版必究
 */

public class DeviceUtil {
    /**
     * 获取屏幕宽度
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context){
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 把dp转化成px
     * @param context
     * @param dp
     * @return
     */
    public static float getPxFromDp(Context context,int dp){

        //获取屏幕密度
        float mip=context.getResources().getDisplayMetrics().density;
        //像素=dp*密度，再加上0.5f的误差
        float px=dp*mip+0.5f;
        return px;
    }

}
