package com.qf.ly.fm.other.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.qf.ly.fm.R;

/**
 * Created by Administrator on 2016/10/28 0028.9:50
 * 版权所有 盗版必究
 */

public class MyService extends Service {
    private NotificationManager notificationManager;
    private Notification notification;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    private void initNotification() {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //远程视图，没有显示在app内部，而是显示在通知栏
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_layout);
        //PendingIntent是执行点击事件的延迟意图
        //设置点击播放按钮时发送的通知
        PendingIntent playIntent = PendingIntent.getBroadcast(this, 0, new Intent("com.kaola.play"), 0);
        remoteViews.setOnClickPendingIntent(R.id.koala_notification_play_tv, playIntent);
        PendingIntent closeIntent = PendingIntent.getBroadcast(this, 0, new Intent("com.kaola.close"), 0);
        remoteViews.setOnClickPendingIntent(R.id.notifition_close, closeIntent);

        notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notification_icon)
                .setCustomBigContentView(remoteViews)//设置自定义通知布局
                .setOngoing(true)//通知删不掉，表示通知正在进行,比如下载
                .setAutoCancel(true)//点击进入通知后，通知自动消失
                .build();
        //发起通知
        notificationManager.notify(0x001, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initNotification();
        return super.onStartCommand(intent, flags, startId);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBind();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class MyBind extends Binder {

    }
}
