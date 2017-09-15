package com.kasmartnotification.smartnotification.Controller;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.kasmartnotification.smartnotification.R;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout physicalActivity, phoneUsage;
    private Switch physicalActivitySwitch, phoneUsageSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_activity);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        physicalActivity = findViewById(R.id.activity_settings_activity_noti_during_physical_activity_linear_layout);
        phoneUsage = findViewById(R.id.activity_settings_activity_noti_during_phone_usage_linear_layout);
        physicalActivity.setOnClickListener(this);
        phoneUsage.setOnClickListener(this);

        physicalActivitySwitch = findViewById(R.id.activity_settings_activity_noti_during_physical_activity_switch);
        physicalActivitySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    Toast.makeText(getApplicationContext(), "physical activity On",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "physical activity Off", Toast.LENGTH_SHORT).show();
                }
            }
        });

        phoneUsageSwitch = findViewById(R.id.activity_settings_activity_noti_during_phone_usage_switch);
        phoneUsageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    Toast.makeText(getApplicationContext(), "phone usage On",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "phone usage Off", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.activity_settings_activity_noti_during_physical_activity_linear_layout:
                if (physicalActivitySwitch.isChecked()){
                    physicalActivitySwitch.setChecked(false);
                }else {
                    physicalActivitySwitch.setChecked(true);
                }
                break;
            case R.id.activity_settings_activity_noti_during_phone_usage_linear_layout:
                if (phoneUsageSwitch.isChecked()){
                    phoneUsageSwitch.setChecked(false);
                }else {
                    phoneUsageSwitch.setChecked(true);
                }
                break;
        }
    }
}
