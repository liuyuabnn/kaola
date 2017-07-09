package com.qf.ly.fm.other.utils;

import android.media.MediaPlayer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/10/25 0025.17:17
 * 版权所有 盗版必究
 */

public class ExecutorUtil {

    private ExecutorService service;
    private List<String> playList;
    private static ExecutorUtil instance;
    private boolean needDownload = true;//下载开关
    //由于是边下边播，所以要定时判断播放的地址列表下载好了没
    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            if (MediaPlayerUtil.getInstance().getStatus() == MediaPlayerUtil.Status.Playing) {
                return;
            }
            playFromList();
        }
    };
    private Timer timer = new Timer();

    private ExecutorUtil() {
        playList = new ArrayList<>();
        service = Executors.newFixedThreadPool(5);
    }

    /**
     * 单例模式
     *
     * @return
     */
    public static ExecutorUtil getInstance() {
        if (instance == null) {
            synchronized (ExecutorUtil.class) {
                if (instance == null) {
                    instance = new ExecutorUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 开始工作
     *
     * @param url
     */
    public void work(String url) {
        //执行下载
        download(url);
        //执行播放
        play();
    }

    //立即关闭线程池
    public void shutdown() {
        needDownload = false;
        //关闭线程池
        service.shutdownNow();
        FileUitl.clearCacheFromDir(FileUitl.DIR_VIDEO);
        task.cancel();
        task = null;
        timer.cancel();
        timer = null;
    }

    public void play() {
        service.submit(new Thread() {
            @Override
            public void run() {
                MediaPlayerUtil.getInstance().setICompletionListener(new MediaPlayerUtil.ICompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        playFromList();
                    }
                });
                timer.schedule(task, 0, 1000);
            }
        });

    }

    private int index;

    private void playFromList() {
        if (playList != null && !playList.isEmpty()) {
            index++;
            LogUtil.d(">>> 播放第 " + index + " 个片断");
            String firstUri = playList.remove(0);
            MediaPlayerUtil.getInstance().play(firstUri);
        }
    }


    /**
     * 连环下载，得到播放的地址列表playList
     *
     * @param url
     */
    public void download(final String url) {
        //把线程添加到线程池里执行
        service.submit(new Thread() {
            @Override
            public void run() {
                //持续下载
                while (needDownload) {
                    //获取第一个m3u8文件里的下载地址(也就是第二个m3u8的文件地址)
                    String m3u8Url = downloadM3u81(url);
                    //删除第二个m3u8文件
                    FileUitl.deleteCacheFile(FileUitl.DIR_M3U8, url.hashCode() + ".MP3");
                    //获取拼接后的ts下载地址
                    List<String> tsUrlList = downloadM3u82(m3u8Url);
                    //删除第二个m3u8文件
                    FileUitl.deleteCacheFile(FileUitl.DIR_M3U8, m3u8Url.hashCode() + ".MP3");
                    //下载ts文件
                    downloadTs(tsUrlList);
                }
            }
        });
    }


    /**
     * 下载m3u8File1文件操作
     *
     * @param url
     */
    public String downloadM3u81(String url) {
        File m3u8File1 = HttpUtil.getFile(url, FileUitl.DIR_M3U8);
        FileInputStream fis = null;
        BufferedReader bufferedReader = null;
        try {
            fis = new FileInputStream(m3u8File1);
            bufferedReader = new BufferedReader(new InputStreamReader(fis));
            String read = null;
            //获取以第一个m3u8 文件的url
            while ((read = bufferedReader.readLine()) != null) {
                if (read.startsWith("http")) {
                    return read;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    fis.close();
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    /**
     * 下载第二个m3u8文件
     *
     * @param url 从第一个m3u8文件里读取出来的
     * @return
     */
    public List<String> downloadM3u82(String url) {
        File m3u8File2 = HttpUtil.getFile(url, FileUitl.DIR_M3U8);
        FileInputStream fis = null;
        BufferedReader bufferedReader = null;
        List<String> list = null;
        try {
            fis = new FileInputStream(m3u8File2);
            bufferedReader = new BufferedReader(new InputStreamReader(fis));
            String read = null;
            list = new ArrayList<>();
            //最后一个"/",加一个"/"转译
            int lastIndex = url.lastIndexOf("/");
            //需要保留"/"
            url = url.substring(0, lastIndex + 1);
            //获取以.ts结尾的字符串
            while ((read = bufferedReader.readLine()) != null) {
                if (read.endsWith(".ts")) {
                    //拼接操作
                    String temp = url;
                    temp += read;
                    list.add(temp);
                }
            }
            return list;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    /**
     * 下载视频片段
     *
     * @param urlList
     */
    public void downloadTs(final List<String> urlList) {
        for (int i = 0; i < urlList.size(); i++) {
            String url = urlList.get(i);
            File file = HttpUtil.getFile(url, FileUitl.DIR_VIDEO);
            //添加文件地址（也就是播放路径）到播放列表里面
            playList.add(file.getAbsolutePath());
        }
    }
}


