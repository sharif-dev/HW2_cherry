package com.example.homework2;


import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

public class ShakeEventListener extends Service implements SensorEventListener {
    private static int SHAKE_LIMIT = 2;
    public void setLimit(int shake_limit){
        SHAKE_LIMIT = shake_limit;
    }
    private SensorManager mSensorManager;
    private float mAccel = 0.00f;
    private float mAccelCurrent = SensorManager.GRAVITY_EARTH;
    private float mAccelLast = SensorManager.GRAVITY_EARTH;



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.setLimit(16-(intent.getExtras().getInt("limit")));
//        Toast.makeText(getApplicationContext() , "service 2 start" , Toast.LENGTH_SHORT).show();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI ,new Handler());
        return START_STICKY;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public void onSensorChanged(SensorEvent se) {
        float x = se.values[0];
        float y = se.values[1];
        float z = se.values[2];
        mAccelLast = mAccelCurrent;
        mAccelCurrent = (float) Math.sqrt(x*x + y*y + z*z);
        float delta = mAccelCurrent - mAccelLast;
        mAccel = mAccel * 0.9f + delta;
        if(mAccel > SHAKE_LIMIT) {
            System.out.println("shaaaake");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                boolean isScreenOn = pm.isInteractive();
                if (isScreenOn == false)
                {
                    PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, getPackageName()+"shake,s1");
                    wl.acquire();
                    wl.release();
                }
                else{
//                    Toast.makeText(getApplicationContext() , "sensitive = "+SHAKE_LIMIT , Toast.LENGTH_SHORT).show();

                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext() , "service 2 destroy" , Toast.LENGTH_SHORT).show();
        mSensorManager.unregisterListener(this);


    }


}