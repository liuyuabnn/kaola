package com.qf.ly.fm.other.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by Administrator on 2016/10/29 0029.20:36
 * 版权所有 盗版必究
 */
public class ShakeSensorListener implements SensorEventListener {
    private Sensor sensor; //传感器实例
    private SensorManager sensorManager;//传感器管理者
    private double slop = 15;//摇一摇临界值，自己把握

    public ShakeSensorListener(Context context) {
        //获取传感器服务
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        //获取加速度传感器实例
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //注册监听
        sensorManager.registerListener(this, sensor, Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] values = event.values;
        float x = values[0];//X轴坐标
        float y = values[1];
        float z = values[2];

        double speed = Math.sqrt(x * x + y * y + z * z);

        if (speed >= slop) {
            onShake();
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    //此方法在外面实现。相当于接口回调的简单用法
    public void onShake() {

    }

    public void unRegister() {
        sensorManager.unregisterListener(this);
    }
}
