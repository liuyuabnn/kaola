package com.qf.ly.fm.other.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.qf.ly.fm.R;
import com.qf.ly.fm.discover.ui.activity.ComActivity;
import com.qf.ly.fm.discover.ui.activity.SurfaceActivity;
import com.qf.ly.fm.discover.ui.entiy.TuiJian2;
import com.qf.ly.fm.discover.ui.utlpath.widget.StackView;
import com.qf.ly.fm.offline.ui.db.DownLoadManager;
import com.qf.ly.fm.offline.ui.entiy.DownLoadEntiy;
import com.qf.ly.fm.other.utils.DeviceUtil;
import com.qf.ly.fm.other.utils.FileUitl;
import com.qf.ly.fm.other.utils.HttpUtil;
import com.qf.ly.fm.other.utils.ImageUtil;
import com.qf.ly.fm.other.utils.KaolaTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.sharesdk.onekeyshare.OnekeyShare;

public class PlayActivity extends AppCompatActivity {
    private ImageView ivPlay, btnplay, ivtop, ivDownLoad, ivNext, iv_comments, iv_more, iv_share;
    private TextView tvnow, tvall, tvtittle, tv_dy;
    private MediaPlayer mediaPlayer;
    private LinearLayout imagell;
    private KaolaTask task1, task2;
    private SeekBar seekbar;
    private StackView stackView;
    private boolean isPause;
    private TuiJian2 tuijian2;
    private PlayMode mode = PlayMode.SINGLE_LOOP;
    private PopupWindow pw;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mediaPlayer == null) {
                return;
            }
            //获取当前播放时间
            int currentPosition = mediaPlayer.getCurrentPosition();
            String currtime = getTime(currentPosition);
            //更新时间
            tvnow.setText(currtime);
            //进度条的最大长度
            int max = seekbar.getMax();
            //得到总时间
            int duration = mediaPlayer.getDuration();
            int progress = max * currentPosition / duration;
            //更新进度
            seekbar.setProgress(progress);
            //从现在开始一秒后更新一下当前时间
            handler.sendEmptyMessageDelayed(0, 1000);

        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kaola_play);
        ivPlay = (ImageView) findViewById(R.id.middle_play_iv);
        btnplay = (ImageView) findViewById(R.id.middle_play_edit);
        tvnow = (TextView) findViewById(R.id.middle_play_time_now);
        tvall = (TextView) findViewById(R.id.middle_play_time_all);
        seekbar = (SeekBar) findViewById(R.id.middle_play_seekbar);
        ivtop = (ImageView) findViewById(R.id.top_play_iv);
        tvtittle = (TextView) findViewById(R.id.top_title_tv);
        ivDownLoad = (ImageView) findViewById(R.id.bottom_download);
        imagell = (LinearLayout) findViewById(R.id.image_ll);
        stackView = (StackView) findViewById(R.id.middle_donghua);
        ivNext = (ImageView) findViewById(R.id.middle_play_next);
        iv_comments = (ImageView) findViewById(R.id.bottom_comments);
        iv_more = (ImageView) findViewById(R.id.bottom_live_more);
        tv_dy = (TextView) findViewById(R.id.top_dy_tv);
        iv_share = (ImageView) findViewById(R.id.bottom_share);
        //初始化popupWindow
        showPopup();
        //得到数据源
        Intent intent = getIntent();
        tuijian2 = intent.getParcelableExtra("tuiJian2");

        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnekeyShare oks = new OnekeyShare();
                //设置标题
                oks.setTitle(tuijian2.getAlbumName());
                //标题的网络连接
                oks.setTitleUrl(tuijian2.getMp3PlayUrl());
                //分享的文本
                oks.setText(tuijian2.getRname());
                //设置分享的网络图片
                oks.setImageUrl(tuijian2.getPic());
                //设置评论
                oks.setComment(tuijian2.getRvalue());
                //启动分享页面
                oks.show(PlayActivity.this);
            }
        });

        //初始化MediaPlayer
        mediaPlayer = new MediaPlayer();
        //设置准备监听
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //开始播放
                mediaPlayer.start();
                //发送一个标识为0的空的消息
                handler.sendEmptyMessage(0);
            }
        });
        //缓冲监听
        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                //设置seekBar第二层进度，通常用来表示视频或者音频缓冲进度
                seekbar.setSecondaryProgress(percent);
            }
        });
        //设置完成监听
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //如果是单曲循环
                if (mode == PlayMode.SINGLE_LOOP) {
                    mediaPlayer.start();
                } else if (mode == PlayMode.RANDOM) {//如果是随机播放

                }
            }
        });
        //SeekBar设置监听
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int duration = mediaPlayer.getDuration();
                String time = getTime(duration * progress / 100);
                tvnow.setText(time);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //跳到指定地点播放
                int progress = seekBar.getProgress();
                int duration = mediaPlayer.getDuration();

                int position = duration * progress / 100;
                mediaPlayer.seekTo(position);
            }
        });
        /**
         * 下载的监听
         */
        ivDownLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(PlayActivity.this, "已添加至离线下载", Toast.LENGTH_SHORT).show();

                task1 = new KaolaTask(new KaolaTask.IDownFile() {
                    @Override
                    public Object downLoadFile() {

                        return HttpUtil.getFile(tuijian2.getMp3PlayUrl(), FileUitl.DIR_AUDIO);
                    }
                }, new KaolaTask.IRequestCallback() {
                    @Override
                    public void onSuccess(Object object) {

                        ivDownLoad.setImageResource(R.drawable.btn_download_disable);

                        File file = (File) object;
                        DownLoadEntiy entiy = new DownLoadEntiy();
                        entiy.setImage(tuijian2.getPic());
                        entiy.setAlbumName(tuijian2.getAlbumName());
                        entiy.setCount(1);
                        long length = file.length();
                        entiy.setSize(FileUitl.getFileSize(length));

                        DownLoadManager manager = DownLoadManager.getInstance();
                        manager.insert(entiy);
                    }

                    @Override
                    public void onError() {

                        Toast.makeText(PlayActivity.this, "MP3下载失败了", Toast.LENGTH_SHORT).show();
                    }
                });

                task1.execute();
            }
        });
        /**
         * 显示动画
         */
        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stackView.showAnim();
            }
        });
        /**
         * 评论页面
         */
        iv_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(PlayActivity.this, ComActivity.class);
                intent1.putExtra("tuijian2", tuijian2);
                startActivity(intent1);

            }
        });
        /**
         * popupWindow
         */
        iv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 显示在某个地方，参数二就是控制PopupWindow的位置，参数一指指定的组件，然后找这个组件的最终父容器，参数三和参数四表示偏移量。
                pw.showAtLocation(iv_more, Gravity.BOTTOM, 0, 0);
                // 显示在参数一的组件正下方，参数二和参数三表示偏移量。
                //pw.showAsDropDown(view, 0, 0);
            }
        });
        /**
         * 订阅，播放视频
         */
        tv_dy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(PlayActivity.this, SurfaceActivity.class);
                startActivity(intent1);

            }
        });

    }

    /**
     * 初始化窗口
     */
    private void showPopup() {
        //new一个窗口对象
        pw = new PopupWindow();
        // 必须设置宽和高。否则不会显示
        pw.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        pw.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        //将布局文件转换为控件对象，并设置给窗口
        View v = getLayoutInflater().inflate(R.layout.pouper_window_more, null);
        pw.setContentView(v);
        //设置窗口可获取焦点
        pw.setFocusable(true);
        //设置点击窗口外面消失掉窗口，一定要和setBackgroundDrawable一起使用，否则没有效果
        pw.setOutsideTouchable(true);
        pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    /**
     * 取消按钮
     *
     * @param view
     */
    public void moreClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                pw.dismiss();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        tvtittle.setText(tuijian2.getAlbumName());

        task2 = new KaolaTask(new KaolaTask.IDownIamge() {
            @Override
            public Bitmap loadImage() {
                return HttpUtil.getBitmap(tuijian2.getPic());
            }
        }, new KaolaTask.IRequestCallback() {
            @Override
            public void onSuccess(Object object) {
                Bitmap image_bg = (Bitmap) object;
                //设置背景虚化图片
                imagell.setBackground(ImageUtil.BlurImages(image_bg, PlayActivity.this));

                int swidth = image_bg.getWidth();
                int sheight = image_bg.getHeight();
                //设置ivtop图片
                Matrix matrix1 = new Matrix();
                float scale1x = 1.0f * DeviceUtil.getPxFromDp(PlayActivity.this, 50) / swidth;
                float scale1y = 1.0f * DeviceUtil.getPxFromDp(PlayActivity.this, 50) / sheight;
                matrix1.setScale(scale1x, scale1y);
                Bitmap image_top = Bitmap.createBitmap(image_bg, 0, 0, swidth, sheight, matrix1, false);
                ivtop.setImageBitmap(image_top);
                //设置ivPlay的图片
                Matrix matrix2 = new Matrix();
                float scale2x = 1.0f * DeviceUtil.getPxFromDp(PlayActivity.this, 350) / swidth;
                float scale2y = 1.0f * DeviceUtil.getPxFromDp(PlayActivity.this, 350) / sheight;
                matrix2.setScale(scale2x, scale2y);
                Bitmap image_play = Bitmap.createBitmap(image_bg, 0, 0, swidth, sheight, matrix2, false);
                ivPlay.setImageBitmap(image_play);
            }

            @Override
            public void onError() {

                Toast.makeText(PlayActivity.this, "下载图片失败", Toast.LENGTH_SHORT).show();
            }
        });
        task2.execute();

        btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                start(tuijian2.getMp3PlayUrl());
            }
        });

    }

    private void start(String mp3PlayUrl) {

        //判断是否正在播放
        if (mediaPlayer.isPlaying()) {
            //暂停
            mediaPlayer.pause();
            isPause = true;
            btnplay.setImageResource(R.drawable.anchor_edit_play);
        } else {
            //如果是暂停状态
            if (isPause) {
                //直接接着上次播放
                mediaPlayer.start();
                isPause = false;
                btnplay.setImageResource(R.drawable.anchor_edit_play_pause);
            } else {
                //开始从头播放
                playMp3(mp3PlayUrl);
                isPause = false;
                btnplay.setImageResource(R.drawable.anchor_edit_play_pause);
            }

        }

    }

    private void playMp3(String mp3PlayUrl) {
        //重置
        mediaPlayer.reset();
        Uri uri = Uri.parse(mp3PlayUrl);
        try {
            //设置播放路径
            mediaPlayer.setDataSource(this, uri);
            //准备播放
            mediaPlayer.prepare();//准备监听

            //获取总共的播放时长,单位是毫秒
            int duration = mediaPlayer.getDuration();
            String time = getTime(duration);
            tvall.setText(time);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /***
     * 时间格式转换
     *
     * @param duration
     * @return
     */
    private String getTime(int duration) {
        Date date = new Date(duration);
        SimpleDateFormat format = new SimpleDateFormat("mm:ss");
        return format.format(date);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //停止并释放
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
        handler.removeMessages(0);
        //取消下载任务
        if (task1 != null) {
            task1.cancel(true);
            task1 = null;
        }
        if (task2 != null) {
            task2.cancel(true);
            task2 = null;
        }
    }

    /**
     * 播放模式
     */
    public enum PlayMode {
        /**
         * 列表循环
         */
        LIST_LOOP,

        /**
         * 单曲循环
         */
        SINGLE_LOOP,

        /**
         * 随机播放
         */
        RANDOM
    }
}
