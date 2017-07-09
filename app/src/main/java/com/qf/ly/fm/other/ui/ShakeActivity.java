package com.qf.ly.fm.other.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;

import com.qf.ly.fm.R;
import com.qf.ly.fm.other.utils.MediaPlayerUtil;
import com.qf.ly.fm.other.utils.ShakeSensorListener;

public class ShakeActivity extends AppCompatActivity {

    private Vibrator vibrator;//手机震动对象
    private ShakeSensorListener sensorListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);

        //手机震动服务
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        final Uri normalUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.shake_sound_male);
//        Uri matchUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.shake_match);
//        Uri noMatch = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.shake_nomatch);

        sensorListener = new ShakeSensorListener(this) {
            @Override
            public void onShake() {
                MediaPlayerUtil.getInstance().play(normalUri.toString());
                //震动10秒
//                vibrator.vibrate(10000);//0一直震， -1震动一次
                //震动节奏：奇数位表示停止时间， 偶数位表示震动持续时间
                long[] array = new long[]{200, 300, 400, 300, 400, 500};
                vibrator.vibrate(array, 0);//一直保持这个节奏震动

            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorListener.unRegister();
        vibrator.cancel();//取消震动
    }
}
