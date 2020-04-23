package com.example.homework2;


import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class ShakeEventListener extends Service implements SensorEventListener {
    private static int SHAKE_LIMIT = 2;
    public void setLimit(int shake_limit){
        SHAKE_LIMIT = shake_limit;
    }
    private SensorManager mSensorManager;
    private float mAccel = 0.00f;
    private float mAccelCurrent = SensorManager.GRAVITY_EARTH;
    private float mAccelLast = SensorManager.GRAVITY_EARTH;

//    private ShakeListener listener;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("servive start");
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        return START_STICKY;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void registerListener() {
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unregisterListener() {
        mSensorManager.unregisterListener(this);
    }

    public void onSensorChanged(SensorEvent se) {
        float x = se.values[0];
        float y = se.values[1];
        float z = se.values[2];
        mAccelLast = mAccelCurrent;
        mAccelCurrent = (float) Math.sqrt(x*x + y*y + z*z);
        float delta = mAccelCurrent - mAccelLast;
        mAccel = mAccel * 0.9f + delta;
        if(mAccel > SHAKE_LIMIT) {
            System.out.println("shaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaake");
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
//                   setTurnScreenOn(true);
//               } else {
//                   final Window window = getWindow();
//                   window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
//               }
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        System.out.println("mcell = "+mAccel);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }





    public void onResume() {
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onPause() {
        mSensorManager.unregisterListener(this);
    }
}