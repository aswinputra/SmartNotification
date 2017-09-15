package com.kasmartnotification.smartnotification.Controller;

import android.content.Intent;
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

public class SettingsLocation extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout smartNotiReminder, places, reminderMessage;
    private Switch smartNotiReminderSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_location);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        smartNotiReminder=findViewById(R.id.activity_settings_location_smart_noti_reminders_linear_layout);
        places = findViewById(R.id.activity_settings_location_places_linear_layout);
        reminderMessage = findViewById(R.id.activity_settings_location_reminder_messages_linear_layout);
        smartNotiReminderSwitch = findViewById(R.id.activity_settings_location_smart_noti_reminders_switch);

        smartNotiReminderSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //TODO: Do something when b is true
                if (b){
                    Toast.makeText(getApplicationContext(), "On",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Off", Toast.LENGTH_SHORT).show();
                }
            }
        });

        smartNotiReminder.setOnClickListener(this);
        places.setOnClickListener(this);
        reminderMessage.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.activity_settings_location_smart_noti_reminders_linear_layout:
                if (smartNotiReminderSwitch.isChecked()){
                    smartNotiReminderSwitch.setChecked(false);
                }else {
                    smartNotiReminderSwitch.setChecked(true);
                }
                break;
            case R.id.activity_settings_location_places_linear_layout:
                intent = new Intent(this, SettingsPlaces.class);
                startActivity(intent);
                break;
            case R.id.activity_settings_location_reminder_messages_linear_layout:
                intent = new Intent(this, SettingsReminderMessages.class);
                startActivity(intent);
                break;
        }
    }
}
