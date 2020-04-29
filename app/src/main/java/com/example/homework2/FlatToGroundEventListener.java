package com.example.homework2;


import android.annotation.SuppressLint;
import android.app.Service;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class FlatToGroundEventListener extends Service implements SensorEventListener {
    private int angle = 10;
    private SensorManager mSensorManager;
    private float mAccel = 0.00f;



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.setAngle(intent.getExtras().getInt("angle"));
//        Toast.makeText(getApplicationContext() , "service 3 start" , Toast.LENGTH_SHORT).show();

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
    public void onSensorChanged(SensorEvent event) {
        float[] g = new float[3];
        g = event.values.clone();

        double norm_Of_g = Math.sqrt(g[0] * g[0] + g[1] * g[1] + g[2] * g[2]);

// Normalize the accelerometer vector
        g[0] = (float) (g[0] / norm_Of_g);
        g[1] =(float) (g[1] / norm_Of_g);
        g[2] = (float) (g[2] / norm_Of_g);
        int inclination = (int) Math.round(Math.toDegrees(Math.acos(g[2])));

        if (inclination < angle || inclination > 180-angle)
        {
            lock();
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(this);
        Toast.makeText(getApplicationContext() , "service 3 stop" , Toast.LENGTH_SHORT).show();

    }


public void setAngle (int angle ){
        this.angle = angle;
}

    private void lock() {
        PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
        if (pm.isScreenOn()) {
            DevicePolicyManager policy = (DevicePolicyManager)
                    getSystemService(Context.DEVICE_POLICY_SERVICE);
            try {
                policy.lockNow();
            } catch (SecurityException ex) {
                Toast.makeText(
                        this,
                        "must enable device administrator",
                        Toast.LENGTH_LONG).show();

            }
        }
    }
}

