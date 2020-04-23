package com.example.homework2;


import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class ShakeEventListener extends Service implements SensorEventListener {
    private static int SHAKE_LIMIT = 9;
    public void setLimit(int shake_limit){
        SHAKE_LIMIT = shake_limit;
    }
    private SensorManager mSensorManager;
    private float mAccel = 0.00f;
    private float mAccelCurrent = SensorManager.GRAVITY_EARTH;
    private float mAccelLast = SensorManager.GRAVITY_EARTH;

    private ShakeListener listener;



    public int onStartCommand(Intent intent, int flags, int startId) {

        // get sensor manager on starting the service
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // have a default sensor configured
        int sensorType = Sensor.TYPE_LIGHT;


//        mSensorManager.registerListener(this, sensor,
//                SensorManager.SENSOR_DELAY_NORMAL);

        return START_STICKY;
    }




    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public interface ShakeListener {
        public void onShake();
    }


    public ShakeEventListener(Activity a, ShakeListener l) {
        mSensorManager = (SensorManager) a.getSystemService(Context.SENSOR_SERVICE);
        listener = l;
        registerListener();
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
        if(mAccel > SHAKE_LIMIT)
            listener.onShake();
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