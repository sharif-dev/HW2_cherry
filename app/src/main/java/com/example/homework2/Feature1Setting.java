package com.example.homework2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.marcinmoskala.arcseekbar.ArcSeekBar;
import com.marcinmoskala.arcseekbar.ProgressListener;

import java.util.Calendar;

public class Feature1Setting extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private TextView timeView;
    private TextView timeItselfView;
    private TextView low;
    private TextView high;
    private Button changBtn;
    private Button cancelBtn;
    private  Boolean timeSet;
    private ArcSeekBar arcSeekBar;
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature1_setting);

        //Set activity elements pointers
        timeView = (TextView) findViewById(R.id.timeView);
        timeItselfView = (TextView) findViewById(R.id.timeItselfView);
        low = (TextView) findViewById(R.id.low);
        high = (TextView) findViewById(R.id.high);
        changBtn = (Button) findViewById(R.id.pickBtn);
        cancelBtn = (Button) findViewById(R.id.canBtn);
        arcSeekBar  = (ArcSeekBar) findViewById(R.id.strengthBar);

        //shared preference
        editor = getSharedPreferences("savedTime", MODE_PRIVATE).edit();
        prefs = getSharedPreferences("savedTime", MODE_PRIVATE);

        //Get possible past information
        int hour = prefs.getInt("hour", -1);
        int minute = prefs.getInt("minute", -1);
        boolean isSet = prefs.getBoolean("isSet", false);
        int strength = prefs.getInt("strength", 100);

        //set seek bar
        arcSeekBar.setProgress(strength);
        int[] intArray = getResources().getIntArray(R.array.progressGradientColors);
        arcSeekBar.setProgressGradient(intArray);

        //change strength by changing in seek bar appearance
        arcSeekBar.setOnProgressChangedListener(new ProgressListener() {
            @Override
            public void invoke(int i) {
                editor.putInt("strength", i);
                editor.apply();
            }
        });


        //If there is already an alarm set
        if (isSet == true){

            timeView.setText("ALARM SET FOR:");
            timeItselfView.setText(String.format("%02d", hour)  + " : " + String.format("%02d", minute));
            changBtn.setText("RESET TIME");
            cancelBtn.setVisibility(View.VISIBLE);
            low.setVisibility(View.VISIBLE);
            high.setVisibility(View.VISIBLE);
            arcSeekBar.setVisibility(View.VISIBLE);

        }
        //If there is no alarm
        else {
            timeView.setText("NO ALARM SET");
            timeItselfView.setText("");
            cancelBtn.setVisibility(View.INVISIBLE);
            low.setVisibility(View.INVISIBLE);
            high.setVisibility(View.INVISIBLE);
            arcSeekBar.setVisibility(View.INVISIBLE);
            changBtn.setText("PICK A TIME");

        }

        //back button returns to the main activity
        Button back = (Button) findViewById(R.id.backs1);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(startIntent);

            }
        });

        //changing the alarm time
        changBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.show(getSupportFragmentManager(), "Time Picker");

            }
        });

        //canceling the alarm
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm();
            }

        });
    }

    //when a new alarm time is set
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        timeView.setText("ALARM SET FOR:");
        timeItselfView.setText(String.format("%02d", hourOfDay)  + " : " + String.format("%02d", minute));
        changBtn.setText("RESET TIME");
        cancelBtn.setVisibility(View.VISIBLE);
        low.setVisibility(View.VISIBLE);
        high.setVisibility(View.VISIBLE);
        arcSeekBar.setVisibility(View.VISIBLE);

        editor.putBoolean("isSet", true);
        editor.putInt("hour", hourOfDay);
        editor.putInt("minute", minute);
        editor.apply();

        Calendar newCalendar = Calendar.getInstance();
        newCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        newCalendar.set(Calendar.MINUTE, minute);
        newCalendar.set(Calendar.SECOND, 0);

        createAlarm(newCalendar);

    }

    //creating the alarm for chosen time
    private void createAlarm(Calendar calendar){

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, myIntent, 0);

        alarmManager.cancel(pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

        Toast.makeText(this, "Alarm set", Toast.LENGTH_SHORT).show();

    }

    //canceling the alarm
    private void cancelAlarm(){

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, myIntent, 0);

        editor.putBoolean("isSet", false);
        editor.putInt("hour", -1);
        editor.putInt("minute", -1);
        editor.apply();

        alarmManager.cancel(pendingIntent);

        timeView.setText("NO ALARM SET");
        timeItselfView.setText("");
        cancelBtn.setVisibility(View.INVISIBLE);
        low.setVisibility(View.INVISIBLE);
        high.setVisibility(View.INVISIBLE);
        arcSeekBar.setVisibility(View.INVISIBLE);
        changBtn.setText("PICK A TIME");

        Toast.makeText(this, "Alarm canceled", Toast.LENGTH_SHORT).show();

    }

}




