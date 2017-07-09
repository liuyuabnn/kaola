package com.qf.ly.fm.other.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.qf.ly.fm.R;
import com.qf.ly.fm.other.utils.HttpUtil;
import com.qf.ly.fm.other.utils.SavePrefernce;
import com.qf.ly.fm.other.utils.UriPath;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class BannerActivity extends AppCompatActivity {
    private ImageView banner_iv;
    private int showTime = 3;
    private Timer timer = new Timer();
    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            showTime--;
            //倒计时三秒后自动跳转
            if (showTime == 0) {
                skip(null);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);

        banner_iv = (ImageView) findViewById(R.id.banner_iv);
        //获取上一次保存的json
        String json = SavePrefernce.getCacheFromPreference(this, "json", "flag");
        //获取上一次保存json的时间
        long saveTime = SavePrefernce.getCacheTimeFromPreference(this, "json", "time");
        //和现在的时间对比，如果超出半小时就请求网络，否则只使用缓存的json
        long currTime = System.currentTimeMillis();
        long time = (currTime - saveTime) / 1000 / 60;//得到分钟
        if ("".equals(json) || time > 30) {
            //表示没有数据，需要请求服务端
            new Thread() {
                @Override
                public void run() {
                    //在子线程中执行
                    String json = HttpUtil.getJson(UriPath.BANNERURL);
                    parseJson(json);
                }
            }.start();
        } else {
            parseJson(json);
        }


    }

    public void parseJson(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            //请求成功了，保存json到缓存
            SavePrefernce.saveCacheToPreference(BannerActivity.this, "json", "flag", json);
            long time = jsonObject.getLong("serverTime");
            SavePrefernce.saveCacheTimeToPreference(BannerActivity.this, "json", "time", time);

            String code = jsonObject.getString("code");
            String message = jsonObject.getString("message");
            if ("10000".equals(code) && "success".equals(message)) {
                JSONObject result = jsonObject.getJSONObject("result");

                //广告页面显示的时间
                showTime = result.getInt("showTime");
                //广告图片地址
                final String img = result.getString("img");
                new Thread() {
                    @Override
                    public void run() {
                        final Bitmap bitmap = HttpUtil.getBitmap(img);
                        if (bitmap != null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    banner_iv.setImageBitmap(bitmap);
                                }
                            });
                            //请求成功了，三秒后自动跳转到Home
                            startTimer();
                        }
                    }
                }.start();
            }
        } catch (JSONException e) {
            //请求失败了，三秒后自动跳转到Home
            startTimer();
            e.printStackTrace();
        }
    }

    public void startTimer() {
        if (timer != null) {
            timer.schedule(timerTask, 0, 2000);
        }
    }

    public void skip(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timerTask.cancel();
        timerTask = null;
        timer.cancel();
        timer = null;
    }
}
