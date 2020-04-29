package com.example.homework2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class Feature3Setting extends AppCompatActivity {
    ComponentName admin ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature3_setting);
        final EditText angletxt = findViewById(R.id.angleEdittxt);
        Button button = (Button) (findViewById(R.id.AngleBtn));
        Switch feature3EnableSwitch = (Switch) findViewById(R.id.feature3Switch);


        SharedPreferences sharedPrefs = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPrefs.edit();

        feature3EnableSwitch.setChecked(sharedPrefs.getBoolean("isAlive",false));
        System.out.println(sharedPrefs.getInt("LastAngle",10));
        angletxt.setText(Integer.toString(sharedPrefs.getInt("LastAngle",10)));

        if (feature3EnableSwitch.isChecked()){
            Intent intent = new Intent(Feature3Setting.this, ShakeEventListener.class);
            intent.putExtra("angle",Integer.parseInt(angletxt.getText().toString()));
            startService(intent);
        }


        feature3EnableSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Intent intent = new Intent(Feature3Setting.this , FlatToGroundEventListener.class);
                    intent.putExtra("angle",angletxt.getText());
                    startService(intent);

//                    get first permission
                    final DevicePolicyManager policy = (DevicePolicyManager)
                            getSystemService(Context.DEVICE_POLICY_SERVICE);
                    admin = new ComponentName(Feature3Setting.this, AdminReceiver.class);
                    if (!policy.isAdminActive(admin)) {

                        Intent intent2 = new Intent(
                                DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).putExtra(
                                DevicePolicyManager.EXTRA_DEVICE_ADMIN, admin);
                        Feature3Setting.this.startActivity(intent2);

                    }
                    editor.putBoolean("isAlive",true);
                }
                else{
                    Intent intent = new Intent(Feature3Setting.this , FlatToGroundEventListener.class);
                    stopService(intent);
                    editor.putBoolean("isAlive",false);
                }
                editor.apply();

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int angle = Integer.parseInt(angletxt.getText().toString());
                Intent intent = new Intent(Feature3Setting.this , FlatToGroundEventListener.class);
                intent.putExtra("angle",Integer.parseInt(angletxt.getText().toString()));
                startService(intent);
                editor.putInt("LastAngle",Integer.parseInt(angletxt.getText().toString()));
                editor.apply();
            }
        });

    }
}
