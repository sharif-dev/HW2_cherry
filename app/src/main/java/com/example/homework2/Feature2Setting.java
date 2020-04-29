package com.example.homework2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.marcinmoskala.arcseekbar.ArcSeekBar;
import com.marcinmoskala.arcseekbar.ProgressListener;

public class Feature2Setting extends AppCompatActivity {
    boolean serviceIsAlive =false;


    ArcSeekBar  arcSeekBar ;
    Switch feat2Switch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature2_setting);
        SharedPreferences sharedPrefs = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPrefs.edit();
        feat2Switch = (Switch) findViewById(R.id.feature3Switch);
        final ArcSeekBar arcSeekBar  = (ArcSeekBar) findViewById(R.id.seekArc);
        serviceIsAlive = sharedPrefs.getBoolean("isAlive",false);
        feat2Switch.setChecked(serviceIsAlive);
        arcSeekBar.setProgress(sharedPrefs.getInt("LastLimit",100));
        if (feat2Switch.isChecked()){
            Intent intent = new Intent(Feature2Setting.this, ShakeEventListener.class);
            int j = arcSeekBar.getProgress();
            j = (int) (j/6);
            intent.putExtra("limit",j);
            startService(intent);
            serviceIsAlive = true;
        }






        int[] intArray = getResources().getIntArray(R.array.progressGradientColors);
        arcSeekBar.setProgressGradient(intArray);
        arcSeekBar.setOnProgressChangedListener(new ProgressListener() {
            @Override
            public void invoke(int i) {
                if (serviceIsAlive){
                Intent intent = new Intent(Feature2Setting.this, ShakeEventListener.class);
                int j = (int) (i/6);
                intent.putExtra("limit",j);
                startService(intent);
                }
                editor.putInt("LastLimit",i);
                editor.apply();
            }
    });

        feat2Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    int i = arcSeekBar.getProgress();
                    Intent intent = new Intent(Feature2Setting.this, ShakeEventListener.class);
                    int j = (int) (i/6);
                    intent.putExtra("limit",j);
                    startService(intent);
                    serviceIsAlive = true;
                    editor.putBoolean("isAlive",serviceIsAlive);
                }else{
                    stopService(new Intent(Feature2Setting.this, ShakeEventListener.class));
                    serviceIsAlive = false;
                    editor.putBoolean("isAlive",serviceIsAlive);
                }
                editor.apply();
            }
        });

    }

}
