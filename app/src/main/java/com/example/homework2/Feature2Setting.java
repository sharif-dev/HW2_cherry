package com.example.homework2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import com.marcinmoskala.arcseekbar.ArcSeekBar;
import com.marcinmoskala.arcseekbar.ProgressListener;

import org.jetbrains.annotations.NotNull;

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
        feat2Switch = (Switch) findViewById(R.id.feature2Switch);
        final ArcSeekBar arcSeekBar  = (ArcSeekBar) findViewById(R.id.seekArc);
        serviceIsAlive = sharedPrefs.getBoolean("isAlive",false);
        feat2Switch.setChecked(serviceIsAlive);
        arcSeekBar.setProgress(sharedPrefs.getInt("LastLimit",100));
        if (feat2Switch.isChecked()){
            Intent intent = new Intent(Feature2Setting.this, ShakeEventListener.class);
            int j = arcSeekBar.getProgress();
            intent.putExtra("limit",j);
            startService(intent);
            serviceIsAlive = true;
            editor.putBoolean("isAlive",serviceIsAlive);
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

//        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//        dialog.setMessage("Allow app to control your lock screens");
//        dialog.setTitle("Sleep Mode alert");
//        dialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(getApplicationContext(),"hi",Toast.LENGTH_SHORT).show();
//            }
//        });
//        AlertDialog alertDialog = dialog.create();
//        alertDialog.show();

    }

}
