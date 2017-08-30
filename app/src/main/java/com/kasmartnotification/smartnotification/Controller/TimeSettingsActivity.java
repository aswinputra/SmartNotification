package com.kasmartnotification.smartnotification.Controller;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.kasmartnotification.smartnotification.R;

public class TimeSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_settings);
        Toolbar toolbar = findViewById(R.id.activity_time_settings_toolbar);
        setSupportActionBar(toolbar);

    }

}
