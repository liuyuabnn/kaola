package com.qf.ly.fm.discover.ui.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.qf.ly.fm.R;
import com.qf.ly.fm.other.utils.LogUtil;

import java.io.IOException;

public class SurfaceActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private MediaPlayer mediaPlayer;
    private SurfaceView surfaceView;
    private float fromX, fromY;
    private final float slop = 100;//允许横向或纵向滑动的操作误差
    private AudioManager audioManager;//音频管理
    private TextView tv_voice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface);

        //获取音频管理服务
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        surfaceView = (SurfaceView) findViewById(R.id.surface_view);
        tv_voice = (TextView) findViewById(R.id.play_voive);
        //获取SurfaceHolder
        SurfaceHolder holder = surfaceView.getHolder();
        holder.addCallback(this);


        mediaPlayer = new MediaPlayer();
        //准备监听
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.start();
            }
        });
        //完成监听
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.start();
            }
        });
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.dream);
        try {
            mediaPlayer.setDataSource(this, uri);
            //异步缓冲
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //设置控件的触摸监听器
        surfaceView.setOnTouchListener(new View.OnTouchListener() {

            /**
             * ACTION_DOWN 和ACTION_UP只会执行一次，ACTION_MOVE可以执行n次
             *
             * @param view
             * @param event
             * @return
             */
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int action = event.getAction();
                //任何事件都是从按下开始
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        //记录按下的坐标
                        fromX = event.getX();
                        fromY = event.getY();
                        //按下事件必须返回true,否则其他的事件都不会执行,返回true表示对触摸事件感兴趣
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //记录滑动时刻的坐标
                        float endX = event.getX();
                        float endY = event.getY();
                        //获取滑动的方向
                        Orientation orientation = getOrientation(endX, endY);

                        float distance = orientation.getDistance();

                        if (orientation == Orientation.Top || orientation == Orientation.Bottom) {
                            tv_voice.setVisibility(View.VISIBLE);
                            toChangeVoice(orientation, distance);
                        } else if (orientation == Orientation.Left || orientation == Orientation.Right) {
                            toChangePlayProgress(orientation, distance);
                        } else {
                            Toast.makeText(SurfaceActivity.this, "你要干啥？", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        tv_voice.setVisibility(View.GONE);
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 根据上滑还是下滑，改变音量大小
     *
     * @param orientation
     * @param distance
     */
    private void toChangeVoice(Orientation orientation, float distance) {
        //获取当前媒体音量
        int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int v = (int) (distance / slop);
        //如果是向下滑动，表示要减小音量
        if (orientation == Orientation.Bottom) {

            volume = volume - v * 10;
            if (volume <= 0) {
                volume=0;
                tv_voice.setText(volume+"%");
            } else {
                tv_voice.setText(volume + "%");
            }
        } else {
            volume = volume + v * 10;
            if (volume >= 100) {
                volume=100;
                tv_voice.setText(volume+"%");
            } else {
                tv_voice.setText(volume + "%");
            }
        }
        //设置音量(类型，音量大小，播放声音)
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_PLAY_SOUND);
    }

    /**
     * 根据上滑还是下滑改变播放进度
     *
     * @param orientation
     */
    private void toChangePlayProgress(Orientation orientation, float distance) {

    }


    //SurfaceView在创建时的回调
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //设置显示内容的控件l
        mediaPlayer.setDisplay(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    //SurfaceView在销毁时的回调
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //停止解码
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
        //清除绘制缓存
        surfaceView.destroyDrawingCache();
    }

    /**
     * 切换屏幕
     *
     * @param btn
     */
    public void changeScreen(View btn) {
        Button bt = (Button) btn;
        //获取屏幕的方向
        int orientation = getRequestedOrientation();//-1
        //如果是横屏，那么切换成竖屏
        if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            //设置屏幕方向
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            bt.setText("切换至横屏");
        } else {
            //如果是竖屏，那么切换到横屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            bt.setText("切换至竖屏");
        }

    }

    /**
     * 如果在清单文件里配置了Activity的属性 android:configChanges="orientation|screenSize"
     * 那么Activity在切换屏幕的时候不会调用生命周期方法，只会调用当前的onConfigurationChanged
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LogUtil.d("onConfigurationChanged = " + newConfig.orientation);
    }

    /**
     * 手势的方向
     */
    enum Orientation {
        Top,

        Bottom,

        Left,

        Right,

        //未知方向
        Unknown;

        float distance;

        public float getDistance() {
            return distance;
        }

        public void setDistance(float distance) {
            this.distance = distance;
        }
    }

    private Orientation getOrientation(float endX, float endY) {
        Orientation orientation = Orientation.Unknown;
        //如果是向上
        if (Math.abs(fromX - endX) < slop && endY < fromY && Math.abs(endY - fromY) > slop) {
            //设置方向和滑动的距离
            orientation = Orientation.Top;
            orientation.setDistance(Math.abs(endY - fromY));
        }
        //向下
        else if (Math.abs(fromX - endX) < slop && endY > fromY && Math.abs(endY - fromY) > slop) {
            //设置方向和滑动的距离
            orientation = Orientation.Bottom;
            orientation.setDistance(Math.abs(endY - fromY));
        }
        //向左
        else if (Math.abs(fromY - endY) < slop && endX < fromX && Math.abs(endX - fromX) > slop) {
            //设置方向和滑动的距离
            orientation = Orientation.Left;
            orientation.setDistance(Math.abs(endX - fromX));
        }
        //向右
        else if (Math.abs(fromY - endY) < slop && endX > fromX && Math.abs(endX - fromX) > slop) {
            //设置方向和滑动的距离
            orientation = Orientation.Right;
            orientation.setDistance(Math.abs(endX - fromX));
        }

        return orientation;
    }
}
