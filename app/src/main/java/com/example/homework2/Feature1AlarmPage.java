package com.example.homework2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.marcinmoskala.arcseekbar.ArcSeekBar;

public class Feature1AlarmPage extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;
    private ArcSeekBar arcSeekBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature1_alarm_page);

        //get shake strength needed to dismiss the alarm
        SharedPreferences.Editor editor = getSharedPreferences("savedTime", MODE_PRIVATE).edit();
        SharedPreferences prefs = getSharedPreferences("savedTime", MODE_PRIVATE);
        int strength = prefs.getInt("strength", 100);
        final int temp = (int) (strength/20);

        //setting vibration
        final Vibrator vibrator = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
        vibrator.vibrate(600000);

        //playing ringtone
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        final Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            r.setLooping(true);
        }

        //setting gyroscope sensor
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if(event.values[2] > 0.5f*temp || event.values[2] < -0.5f*temp){
                    vibrator.cancel();
                    r.stop();
                    sensorManager.unregisterListener(sensorEventListener);
                    Intent startIntent = new Intent(getApplicationContext(), Feature1Setting.class);
                    startActivity(startIntent);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        sensorManager.registerListener(sensorEventListener, sensor, sensorManager.SENSOR_DELAY_NORMAL);

        //dismiss button for caution and after dismiss will return to feature1 activity
        Button dismiss = (Button) findViewById(R.id.dismiss);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.cancel();
                r.stop();
                sensorManager.unregisterListener(sensorEventListener);
                Intent startIntent = new Intent(getApplicationContext(), Feature1Setting.class);
                startActivity(startIntent);
            }
        });







    }
}
