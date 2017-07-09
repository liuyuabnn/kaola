package com.qf.ly.fm.other.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by LY on 2016/10/10.14:07
 * 版权所有 盗版必究
 * 文件工具类
 */

public class FileUitl {
    /**
     * 项目根目录
     */
    public static final String APP_DIR = "KaoLaFM";

    /**
     * 获取 KaoLaFM/image 目录
     */
    public static final File DIR_IMAGE = getDir("image");

    /**
     * 音频目录
     */
    public static final File DIR_AUDIO = getDir("audio");
    /**
     * m3u8目录
     */
    public static final File DIR_M3U8 = getDir("m3u8");

    /**
     * 视频目录
     */
    public static final File DIR_VIDEO = getDir("video");

    /**
     * Picasso图片目录
     */
    public static final File DIR_PICASSO_IMAGE = getDir("picasso_image");
    /**
     * SDK目录
     */
    public static final File DIR_SDK = getDir("sdk");

    /**
     * 获取sd卡下项目目录
     *
     * @return
     */
    public static File getAppDir() {
        //获取sd卡的状态
        String state = Environment.getExternalStorageState();
        //如果sd卡已挂载
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            //获取sd卡根目录
            File gen = Environment.getExternalStorageDirectory();
            File dir = new File(gen, APP_DIR);

            if (!dir.exists()) {
                dir.mkdirs();
            }
            return dir;
        }

        return null;
    }

    /**
     * 获取项目根目录下指定的子目录
     *
     * @param dirName
     * @return
     */
    public static File getDir(String dirName) {
        File dir = new File(getAppDir(), dirName);

        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 将字节转换为兆
     */
    public static String getFileSize(long length) {
        if (length > 1024 && length / 1024 < 1024) {
            return length / 1024 + "KB";
        } else if (length / 1024 / 1024 > 1 && length / 1024 / 1024 < 1024) {
            return length / 1024 / 1024 + "MB";
        }
        return length + "B";
    }


    /**
     * 删除缓存文件
     *
     * @param dir
     * @param rename
     */
    public static void deleteCacheFile(File dir, String rename) {
        File target = new File(dir, rename);
        if (target.exists()) {
            target.delete();
        }
    }

    /**
     * 清空缓存
     *
     * @param dir
     */
    public static void clearCacheFromDir(File dir) {
        if (!dir.exists()) {
            return;
        }
        //清空该目录下面的所有子文件
        File[] files = dir.listFiles();

        for (int i = 0; i < files.length; i++) {
            files[i].delete();
        }
    }


}
