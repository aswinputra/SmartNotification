package com.kasmartnotification.smartnotification;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private FloatingActionButton smartNotiFab;
    private BroadcastReceiver broadcastReceiver;
    private TextView timerTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);

        smartNotiFab = findViewById(R.id.activity_main_fab_smartnotification);
        timerTv = findViewById(R.id.activity_main_focus_period_timer);

        smartNotiFab.setOnClickListener(this);

        setUpBroadcastReceiver();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showDialog(){
        SmartNotiDialog dialog = new SmartNotiDialog(this);
        dialog.setContentView(R.layout.smart_notification_dialog);
        dialog.show();
    }

    public void setUpBroadcastReceiver(){
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String time = intent.getStringExtra(Constant.REMAINING_TIME);
                timerTv.setText(time);
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((broadcastReceiver),
                new IntentFilter(Constant.UPDATE_TIME)
        );
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onStop();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_main_fab_smartnotification: {
                showDialog();
            }break;
        }

    }
}
