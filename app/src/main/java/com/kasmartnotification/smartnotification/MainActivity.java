package com.kasmartnotification.smartnotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnSmartNotiStartListener {

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

    public void showDialog() {
        SmartNotiDialog dialog = new SmartNotiDialog(this, this);
        dialog.setContentView(R.layout.smart_notification_dialog);
        dialog.show();
    }

    public void setUpBroadcastReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String time = intent.getStringExtra(Constants.REMAINING_TIME);
                timerTv.setText(time);
                if(time.equals(Constants.END_TIMER)){
                    prepareForOtherTimer(Constants.BREAK_TIMER);
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((broadcastReceiver),
                new IntentFilter(Constants.UPDATE_FOCUS_TIME)
        );

        //TODO: checks whether it timer is running or not, so that the icon and the view is properly done
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
                //TODO: checks whether it timer is running or not, so that the icon and the view is properly done
                showDialog();
            }
            break;
        }

    }

    @Override
    public void onSmartNotiStart() {
        smartNotiFab.setImageResource(R.drawable.ic_no_noti);
        //TODO: show the related info on the screen
    }

    private void prepareForOtherTimer(String whichTimer){
        MyPreferences preferences = MyPreferences.getInstance(getApplicationContext());
        if (preferences.getSmartNotiServiceStatus()) {
            if (whichTimer.equals(Constants.NEW_SMART_NOTI)) {
                smartNotiFab.setImageResource(R.drawable.ic_noti);
            } else if (whichTimer.equals(Constants.BREAK_TIMER)) {
                startService(new Intent(getApplicationContext(), FocusPeriodService.class));
            }
        }
    }
}
