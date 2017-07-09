package com.qf.ly.fm.other.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.ImageView;

import com.qf.ly.fm.R;

import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2016/10/25 0025.10:42
 * 版权所有 盗版必究
 */

public class MediaPlayerUtil {

    private static Context mContext;
    private MediaPlayer mediaPlayer;
    private PlayMode mode = PlayMode.CUSTOM;
    //播放列表
    private List<String> playList;
    private int playIndex;
    private static MediaPlayerUtil instance;
    private Status status = Status.Reset;

    /**
     * 初始化， 执行一次就够了
     *
     * @param context
     */
    public static void init(Context context) {
        mContext = context;
    }

    /**
     * 单例模式
     *
     * @return
     */

    public static MediaPlayerUtil getInstance() {
        if (instance == null) {
            synchronized (MediaPlayerUtil.class) {
                if (instance == null) {
                    instance = new MediaPlayerUtil();
                }
            }
        }
        return instance;
    }

    private MediaPlayerUtil() {
        initPlayer();
    }

    /**
     * 自定义接口
     */
    private ICompletionListener completionListener;

    public void setICompletionListener(ICompletionListener listener) {
        completionListener = listener;
    }

    public interface ICompletionListener {
        void onCompletion(MediaPlayer mediaPlayer);
    }


    /**
     * 初始化操作
     */
    private void initPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.start();
                status = Status.Playing;
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                //单曲循环
                if (mode == PlayMode.SINGLE_LOOP) {
                    mediaPlayer.start();
                }
                //列表循环
                else if (mode == PlayMode.LIST_LOOP) {
                    playIndex++;
                    String url = playList.get(playIndex);
                    next(url);
                }
                //随机模式
                else if (mode == PlayMode.RANDOM) {
                    int size = playList.size();
                    Random random = new Random();
                    int r = random.nextInt(size);
                    String url = playList.get(r);
                    next(url);
                } else {//自定义模式
                    if (completionListener != null) {
                        completionListener.onCompletion(mediaPlayer);
                    }
                }
            }
        });
    }

    public void play(String url) {
        if (mediaPlayer == null) {
            initPlayer();
        }
        mediaPlayer.reset();
        status = Status.Reset;
        Uri uri = Uri.parse(url);
        try {
            mediaPlayer.setDataSource(mContext, uri);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void next(String url) {
        play(url);
    }

    /**
     * 点击播放按钮
     *
     * @param url
     * @param btnplay
     */
    public void pause(String url, ImageView btnplay) {
        //判断是否正在播放
        if (mediaPlayer.isPlaying()) {
            //暂停
            mediaPlayer.pause();
            status = Status.Pause;
            btnplay.setImageResource(R.drawable.anchor_edit_play);
        } else {
            //开始从头播放
            play(url);
            status = Status.Playing;
            btnplay.setImageResource(R.drawable.anchor_edit_play_pause);
        }

    }

    public void stop() {
        if (mediaPlayer == null) {
            return;
        }
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            status = Status.Stop;
        }
        mediaPlayer.release();
        mediaPlayer = null;
    }

    public Status getStatus() {
        return status;
    }

    /**
     * 设置播放列表
     *
     * @param playList
     */
    public void setPlayerList(List<String> playList) {
        this.playList = playList;
    }

    /**
     * 设置播放模式
     *
     * @param mode
     */
    public void setPlayMode(PlayMode mode) {
        this.mode = mode;
    }

    /**
     * 播放模式
     */
    public enum PlayMode {
        /**
         * 列表循环
         */
        LIST_LOOP("列表循环"),

        /**
         * 单曲循环
         */
        SINGLE_LOOP("单曲循环"),

        /**
         * 随机播放
         */
        RANDOM("随机播放"),
        /**
         * 自定义模式
         */
        CUSTOM("自定义模式");

        private String name;

        PlayMode(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public enum Status {
        Playing,

        Pause,

        Stop,

        Reset;
    }

}
