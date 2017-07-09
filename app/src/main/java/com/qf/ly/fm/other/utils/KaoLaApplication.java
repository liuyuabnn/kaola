package com.qf.ly.fm.other.utils;

import android.app.Application;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.qf.ly.fm.offline.ui.db.DownLoadManager;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import cn.sharesdk.framework.ShareSDK;

/**
 * Created by LY on 2016/10/12.13:17
 * 版权所有 盗版必究
 */

public class KaoLaApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //配置 picasso
        Picasso picasso = new Picasso.Builder(this)
                .defaultBitmapConfig(Bitmap.Config.RGB_565)
                .memoryCache(new LruCache((int) (Runtime.getRuntime().maxMemory() / 8)))//运行内存的1/8
                .downloader(new OkHttpDownloader(FileUitl.DIR_IMAGE))
                .build();
//        Picasso picasso = new Picasso.Builder(this)
//                .defaultBitmapConfig(Bitmap.Config.RGB_565)//默认图片质量,选择RGB_565内存直接减半
//                .memoryCache(new LruCache(20 << 20))//位移 20 * 1024 * 1024
//                .downloader(new OkHttpDownloader(FileUitl.DIR_PICASSO_IMAGE))//指定源图存储位置
//                .build();
        //设置单例模式
        Picasso.setSingletonInstance(picasso);
        //数据库初始化
        DownLoadManager.init(this);
        //获取一个默认的配置
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        //初始化操作
        ImageLoader.getInstance().init(configuration);
        //MediaPlayerUtil初始化
        MediaPlayerUtil.init(this);
        //在ShareSDK任何操作之前都要初始化
        ShareSDK.initSDK(this);


    }
}
