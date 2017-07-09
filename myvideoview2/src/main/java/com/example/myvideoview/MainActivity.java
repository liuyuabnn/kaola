package com.example.myvideoview;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import io.vov.vitamio.widget.VideoView;

public class MainActivity extends AppCompatActivity {
    private MyPlayer myPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myPlayer = (MyPlayer) findViewById(R.id.myPlayer);
        String Path = "android.resource://" + getPackageName() + "/" + R.raw.dream;
//        Log.d("print", "==========" + Path);
//        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.dream);
        myPlayer.setActivity(this)
                .setScaleType(VideoView.VIDEO_LAYOUT_STRETCH)
                .play("http://flv2.bn.netease.com/videolib3/1506/29/yIUnj3281/SD/yIUnj3281-mobile.mp4");
    }

    /**
     * 横竖屏切换时会调用该方法
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (myPlayer != null) {
            myPlayer.onConfigurationChanged(newConfig);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (myPlayer != null) {
            myPlayer.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (myPlayer != null) {
            myPlayer.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myPlayer != null) {
            myPlayer.onDestory();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (myPlayer != null && myPlayer.onKeyDown(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
