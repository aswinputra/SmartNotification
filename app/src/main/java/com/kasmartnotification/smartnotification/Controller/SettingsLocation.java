package com.kasmartnotification.smartnotification.Controller;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.kasmartnotification.smartnotification.Constants;
import com.kasmartnotification.smartnotification.LocationTools.LocationHelper;
import com.kasmartnotification.smartnotification.Model.Setting;
import com.kasmartnotification.smartnotification.R;
import com.kasmartnotification.smartnotification.Tools.SugarHelper;
import com.kasmartnotification.smartnotification.Tools.Utility;

import com.kasmartnotification.smartnotification.Model.Place;
import com.kasmartnotification.smartnotification.Model.ReminderMessage;
import com.kasmartnotification.smartnotification.Model.Setting;
import com.kasmartnotification.smartnotification.R;
import com.kasmartnotification.smartnotification.Services.LocationService;
import com.kasmartnotification.smartnotification.Tools.SugarHelper;
import com.kasmartnotification.smartnotification.Tools.Utility;
import com.orm.SugarRecord;

import java.util.List;

import static com.kasmartnotification.smartnotification.Constants.PERMISSION_ACCESS_COARSE_LOCATION;
import static com.kasmartnotification.smartnotification.Constants.TURN_ON;

public class SettingsLocation extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout smartNotiReminder, places, reminderMessage;
    private TextView reminderMessageTV, placesTV;
    private Switch smartNotiReminderSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_location);

        boolean booleanSmartReminder = false;

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        smartNotiReminder=findViewById(R.id.activity_settings_location_smart_noti_reminders_linear_layout);
        places = findViewById(R.id.activity_settings_location_places_linear_layout);
        placesTV = findViewById(R.id.activity_settings_location_places_text_view);

        List<Place> placesList = SugarRecord.listAll(Place.class);
        if (!placesList.isEmpty()){
            placesTV.setText(Utility.getNames(placesList));
        }else{
            placesTV.setText("Add your Places here");
        }

        reminderMessage = findViewById(R.id.activity_settings_location_reminder_messages_linear_layout);
        reminderMessageTV = findViewById(R.id.activity_settings_location_reminder_messages_text_view);

        List<ReminderMessage> messages = ReminderMessage.find(ReminderMessage.class, "toturnon = ?", TURN_ON);
        if (!messages.isEmpty()){
            reminderMessageTV.setText(Utility.getNames(messages));
        }else {
            reminderMessageTV.setText("Add your own messages here");
        }

        smartNotiReminderSwitch = findViewById(R.id.activity_settings_location_smart_noti_reminders_switch);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            SugarHelper.createOrSetDBObject(Setting.class, Constants.SMART_REMINDER, null,null,null,0,false);
        }

        Setting smartReminderSetting = SugarHelper.findFromDB(Setting.class, Constants.SMART_REMINDER);
        booleanSmartReminder = Utility.getSwitchValue(smartReminderSetting, Constants.SMART_REMINDER);
        //remove the listener so it does not go to Change Listener when the activity start running
        smartNotiReminderSwitch.setOnCheckedChangeListener(null);
        smartNotiReminderSwitch.setChecked(booleanSmartReminder);

        if (booleanSmartReminder){
            places.setVisibility(View.VISIBLE);
            reminderMessage.setVisibility(View.VISIBLE);
        }else{
            places.setVisibility(View.GONE);
            reminderMessage.setVisibility(View.GONE);
        }

        smartNotiReminderSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                //TODO: Do something when checked is true
                if (checked){
                    LocationHelper.getLocationPermission(getApplicationContext(), SettingsLocation.this);
                    if (!Utility.isServiceRunning(getApplicationContext(),LocationService.class)) {
                        startService(new Intent(SettingsLocation.this, LocationService.class));
                    }
                    places.setVisibility(View.VISIBLE);
                    reminderMessage.setVisibility(View.VISIBLE);
                }else{
                    stopService(new Intent(SettingsLocation.this, LocationService.class));
                    places.setVisibility(View.GONE);
                    reminderMessage.setVisibility(View.GONE);
                }
                SugarHelper.createOrSetDBObject(Setting.class, Constants.SMART_REMINDER, null, null, null,0, checked);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // All good!
                } else {
                    Toast.makeText(this, "Need your location to activate this feature", Toast.LENGTH_SHORT).show();
                    SugarHelper.createOrSetDBObject(Setting.class, Constants.SMART_REMINDER, null,null,null,0,false);
                    smartNotiReminderSwitch.setChecked(false);
                }
                break;
        }
    }

}
