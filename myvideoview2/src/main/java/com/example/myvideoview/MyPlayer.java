package com.example.myvideoview;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.VideoView;

/**
 * 封装的视屏播放器*****************使用指南
 * 导入Vitamio
 * 修改主题样式，NoActionBar
 * 重写Activity onConfigurationChanged方法
 * 给要使用的Activity 设置属性android:configChanges="keyboardHidden|orientation|screenSize
 * Created by Administrator on 2016/10/30 0030.14:08
 */

public class MyPlayer extends RelativeLayout implements View.OnClickListener {
    //activity对象
    private Activity activity;

    private VideoView videoView;//播放器对象
    private String url;//播放的路径
    private int scaleType = VideoView.VIDEO_LAYOUT_STRETCH;//默认缩放模式.拉伸
    /**
     * 控件宽高
     */
    private int viedoWidth;
    private int videoHeight;
    /**
     * 底部控制栏
     */
    private LinearLayout bottomLayout;
    private ImageView ivPlay;//播放按钮
    private TextView tvBtimer, tvEtimer;
    private SeekBar seekbar;//进度
    private ImageView ivFullScreen;//切屏按钮

    /**
     * 是否全屏
     */
    private boolean isFullScreen = false;
    /**
     * 是否触摸拖动
     */
    private boolean isTouch = false;

    /**
     * 中间缓冲层
     */
    private LinearLayout centerBufferLayout;
    private ProgressBar centerProgressBar;
    private TextView centertv;
    /**
     * 手势检测器
     */
    private GestureDetector gestureDetector;
    /**
     * TODO Handler
     */
    private static final int SEND_MESSAGE_SETPROGRESS = 1;//设置当前的播放进度
    private static final int SEND_MESSAGE_HIDE_BOTTOM = 2;//隐藏底部的控件

    private Handler hander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SEND_MESSAGE_SETPROGRESS:
                    setProgress();
                    //每隔一秒设置一下播放进度
                    this.sendEmptyMessageDelayed(SEND_MESSAGE_SETPROGRESS, 1000);
                    break;
                case SEND_MESSAGE_HIDE_BOTTOM:
                    hideBottom();
                    break;
            }
        }
    };

    public MyPlayer(Context context) {
        this(context, null);
    }

    public MyPlayer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     * 设置伸缩模式
     * 构造者模式
     *
     * @param scale
     * @return
     */
    public MyPlayer setScaleType(int scale) {
        this.scaleType = scale;
        return this;
    }

    /**
     * 设置Activity
     * 横竖屏切换必须依赖一个Activity
     * 构造者模式
     *
     * @param activity
     */
    public MyPlayer setActivity(Activity activity) {
        this.activity = activity;
        return this;
    }

    /**
     * TODO 初始化方
     */
    private void initView() {

        //初始化维他蜜
        Vitamio.isInitialized(getContext());
        inflate(getContext(), R.layout.videoplay_layout, this);
        videoView = (VideoView) findViewById(R.id.videoView);
        //准备监听
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.setVideoLayout(scaleType, 0);
                //获得宽高
                viedoWidth = getWidth();
                videoHeight = getHeight();
            }
        });
        //播放信息改变的监听
        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                switch (what) {
                    case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                        //正在播放
                        break;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                        //正在缓冲
                        centerBufferLayout.setVisibility(VISIBLE);
                        break;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                        //缓冲结束
                        centerBufferLayout.setVisibility(GONE);
                        break;
                }
                return false;
            }
        });
        //播放缓冲进度的监听
        videoView.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                centertv.setText(percent + "%");
            }
        });

        //播放完成的监听
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //移除消息
                hander.removeMessages(SEND_MESSAGE_SETPROGRESS);
            }
        });

        //底部控件
        bottomLayout = (LinearLayout) findViewById(R.id.view_bottom_layout);
        ivPlay = (ImageView) findViewById(R.id.view_bottom_play);
        ivFullScreen = (ImageView) findViewById(R.id.view_bottom_fullscreen);
        tvBtimer = (TextView) findViewById(R.id.view_bottom_btime);
        tvEtimer = (TextView) findViewById(R.id.view_bottom_etime);
        seekbar = (SeekBar) findViewById(R.id.view_bottom_seek);
        //底部空间点击事件
        ivPlay.setOnClickListener(this);
        ivFullScreen.setOnClickListener(this);
        //拖动条监听
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvBtimer.setText(getTimer(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isTouch = true;
                //撤销隐藏底部控件的消息
                hander.removeMessages(SEND_MESSAGE_HIDE_BOTTOM);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isTouch = false;
                int progress = seekBar.getProgress();
                videoView.seekTo(progress);
                //发送隐藏底部控件的消息
                hander.sendEmptyMessageDelayed(SEND_MESSAGE_HIDE_BOTTOM, 2000);
            }
        });

        //中间控件
        centerBufferLayout = (LinearLayout) findViewById(R.id.view_center_buffer);
        centertv = (TextView) findViewById(R.id.view_center_jd);

        //初始化手势检测器
        gestureDetector = new GestureDetector(getContext(), new GestureListener());

    }

    /**
     * 控件点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_bottom_play:
                //播放和暂停
                if (videoView != null && videoView.isPlaying()) {
                    videoView.pause();
                    ivPlay.setImageResource(R.mipmap.ic_play);
                } else {
                    videoView.start();
                    ivPlay.setImageResource(R.mipmap.ic_pause);
                }
                break;
            case R.id.view_bottom_fullscreen:
                //横竖屏切换
                toggleScreen();
                break;
        }

    }

    /**
     * TODO 设置横竖屏
     */
    public void toggleScreen() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //当前为横屏
            if (activity != null) {
                //设置屏幕为竖屏
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                ivFullScreen.setImageResource(R.mipmap.ic_enlarge);
                isFullScreen = false;
            }
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            //当前为竖屏
            if (activity != null) {
                //设置屏幕为横屏
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                ivFullScreen.setImageResource(R.mipmap.ic_not_fullscreen);
                isFullScreen = true;
            }
        }
    }

    /**
     * 横竖屏切换时回调
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        //隐藏底部状态栏
        hideBottom();
        if (isFullScreen) {
            //横屏
            //隐藏状态栏
            if (activity != null) {
                activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
            //设置播放器的宽度
            int screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
            int screenHeight = getContext().getResources().getDisplayMetrics().heightPixels;

            ViewGroup.LayoutParams layoutParams = this.getLayoutParams();
            layoutParams.width = screenWidth;
            layoutParams.height = screenHeight;
            setLayoutParams(layoutParams);
        } else {
            //竖屏
            //显示状态栏
            if (activity != null) {
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
            //设置宽高
            ViewGroup.LayoutParams layoutParams = this.getLayoutParams();
            layoutParams.width = viedoWidth;
            layoutParams.height = videoHeight;
            setLayoutParams(layoutParams);
        }
        //重新设置拉伸模式
        videoView.setVideoLayout(scaleType, 0);
    }

    /**
     * TODO 手势监听器
     */

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        //按下
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        //轻按
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            //显示或者隐藏底部的控件
            if (isShow(bottomLayout)) {
                hideBottom();
            } else {
                showBottom(3000);
            }
            return true;
        }

        //双击
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return true;
        }

        //滑动
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            //处理滑动事件
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }


    /**
     * 显示底部控件
     */
    private void showBottom() {
        showBottom(0);
    }

    /**
     * 显示底部控件多少毫秒
     *
     * @param timer
     */
    private void showBottom(long timer) {
        if (bottomLayout != null && bottomLayout.getVisibility() == GONE) {
            bottomLayout.setVisibility(VISIBLE);
            if (timer > 0) {
                hander.sendEmptyMessageDelayed(SEND_MESSAGE_HIDE_BOTTOM, timer);
            }
        }
    }

    /**
     * 隐藏底部控件
     */
    private void hideBottom() {
        if (bottomLayout != null && bottomLayout.getVisibility() == VISIBLE) {
            bottomLayout.setVisibility(GONE);
        }
    }

    /**
     * 是否显示
     *
     * @return
     */
    private boolean isShow(View view) {
        return view.getVisibility() == VISIBLE;
    }


    /**
     * 设置当前的播放进度
     */
    private void setProgress() {
        //设置进度条
        int position = (int) videoView.getCurrentPosition();//获得当前的进度
        int duration = (int) videoView.getDuration();//获得总耗时
        if (!isTouch) {//如果没有拖动
            if (seekbar != null) {
                seekbar.setMax(duration);
                seekbar.setProgress(position);
            }
            //设置文本
            tvBtimer.setText(getTimer(position));
            tvEtimer.setText(getTimer(duration));
        }
    }

    /**
     * 将触摸事件交付于手势检测器
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //有手势的时候交给手势检测器处理
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }
        //手势完成或抬起的时候自己处理
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                break;
        }
        return false;
    }


    /**
     * 把毫秒数转换成00:00:00
     *
     * @param timer
     * @return
     */
    private String getTimer(long timer) {
        int h = (int) (timer / 1000 / 60 / 60);
        int m = (int) ((timer / 1000 / 60 % 60));
        int s = (int) (timer / 1000 % 60);
        return (h >= 10 ? h : ("0" + h)) + ":" + (m >= 10 ? m : ("0" + m)) + ":" + (s >= 10 ? s : ("0" + s));
    }


    /**
     * 播放的方法
     */
    public void play(String url) {
        play(url, 0);
    }

    /**
     * 播放的方法的重载
     * 按进度播放
     *
     * @param url
     * @param position
     */
    public void play(String url, int position) {
        this.url = url;
        if (this.url != null) {
//            Uri uri=Uri.parse(url);
//            videoView.setVideoURI(uri);
            videoView.setVideoPath(url);
            if (position > 0) {
                //设置进度
                videoView.seekTo(position);
            }
            //开始播放
            videoView.start();
            //正在播放中
            ivPlay.setImageResource(R.mipmap.ic_pause);
            //设置当前的进度
            hander.sendEmptyMessageDelayed(SEND_MESSAGE_SETPROGRESS, 1000);
        }
    }


    /**
     * 声明周期方法
     */
    int currentPosition = -1;

    public void onPause() {
        if (videoView != null) {
            videoView.pause();
            //记录当前的播放位置
            currentPosition = (int) videoView.getCurrentPosition();
        }
    }

    public void onResume() {
        if (videoView != null) {
            videoView.resume();
            //根据记录的位置重新开始播放
            if (currentPosition != -1) {
                play(this.url, currentPosition);
            }
        }
    }

    public void onDestory() {
        if (videoView != null) {
            //清空handler中的所有事件
            hander.removeCallbacksAndMessages(null);
            videoView.stopPlayback();
        }
    }

    /**
     * 当为横屏的时候按返回键，返回竖屏
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && isFullScreen) {
            toggleScreen();
            return true;
        }
        return false;
    }

}
