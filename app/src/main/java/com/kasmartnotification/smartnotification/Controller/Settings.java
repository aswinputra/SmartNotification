package com.kasmartnotification.smartnotification.Controller;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.kasmartnotification.smartnotification.R;
import com.kasmartnotification.smartnotification.Tools.Utility;

public class Settings extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout time, location, activity, keywords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        time = findViewById(R.id.activity_settings_time_linear_layout);
        location = findViewById(R.id.activity_settings_location_linear_layout);
//        activity = findViewById(R.id.activity_settings_activity_linear_layout);
        keywords = findViewById(R.id.activity_settings_keywords_linear_layout);
        time.setOnClickListener(this);
        location.setOnClickListener(this);
//        activity.setOnClickListener(this);
        keywords.setOnClickListener(this);

        Utility.getNotificationAccessPermission(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.activity_settings_time_linear_layout:
                intent = new Intent(this, SettingsTime.class);
                startActivity(intent);
                break;
            case R.id.activity_settings_location_linear_layout:
                intent = new Intent(this,SettingsLocation.class);
                startActivity(intent);
                break;
//            case R.id.activity_settings_activity_linear_layout:
//                intent = new Intent(this, SettingsActivity.class);
//                startActivity(intent);
//                break;
            case R.id.activity_settings_keywords_linear_layout:
                intent = new Intent(this, SettingsKeywordsResponse.class);
                startActivity(intent);
                break;
        }
    }
}
