package com.kasmartnotification.smartnotification.Controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.kasmartnotification.smartnotification.Constants;
import com.kasmartnotification.smartnotification.Model.Status;
import com.kasmartnotification.smartnotification.OnSmartNotiStartListener;
import com.kasmartnotification.smartnotification.R;
import com.kasmartnotification.smartnotification.Services.BreakPeriodService;
import com.kasmartnotification.smartnotification.Services.FocusPeriodService;
import com.kasmartnotification.smartnotification.Services.SmartNotiService;
import com.kasmartnotification.smartnotification.SmartNotiDialog;
import com.kasmartnotification.smartnotification.Utility;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnSmartNotiStartListener {

    private FloatingActionButton smartNotiFab;
    private BroadcastReceiver broadcastReceiver;
    private TextView nextBreakInTV;
    private TextView timerTV;
    private TextView minutesTV;
    private TextView SmartNotiTV;
    private TextView finishesTV;
    private TextView endTimeTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);

        nextBreakInTV = findViewById(R.id.activity_main_next_break_in);
        timerTV = findViewById(R.id.activity_main_focus_period_timer);
        minutesTV = findViewById(R.id.activity_main_minutes);
        SmartNotiTV = findViewById(R.id.activity_main_smartnotification);
        finishesTV = findViewById(R.id.activity_main_finishes);
        endTimeTV = findViewById(R.id.activity_main_end_time);
        smartNotiFab = findViewById(R.id.activity_main_fab_smartnotification);
        smartNotiFab.setOnClickListener(this);

        toggleFeedbackVis(false);
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
                if(Constants.UPDATE_PERIOD_TIME.equals(intent.getAction())) {
                    String time = intent.getStringExtra(Constants.REMAINING_TIME);
                    timerTV.setText(time);
                    if (time.equals(Constants.END_TIMER)) {
                        timerTV.setText(Constants.END_TIMER);
                        Status status = Utility.findStatusFromDB(Constants.PREVIOUS_TIMER);
                        if (status != null && status.getContent().equals(Constants.BREAK_TIMER)) {
                            prepareForOtherTimer(Constants.FOCUS_TIMER);
                        } else if (status != null && status.getContent().equals(Constants.FOCUS_TIMER)) {
                            prepareForOtherTimer(Constants.BREAK_TIMER);
                        }
                    }
                }
                if(Constants.SMART_NOTIFICATION_END.equals(intent.getAction())){
                    stopSmartNoti();
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.UPDATE_PERIOD_TIME);
        intentFilter.addAction(Constants.SMART_NOTIFICATION_END);
        LocalBroadcastManager.getInstance(this).registerReceiver((broadcastReceiver), intentFilter);

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
                Status status = Utility.findStatusFromDB(Constants.SMART_NOTIFICATION);
                if (status != null && status.isRunning()) {
                    stopSmartNoti();
                } else {
                    showDialog();
                }
            }
            break;
        }
    }

    private void stopSmartNoti() {
        Log.i(Constants.STATUS, "stopSmartNoti()");
        stopService(new Intent(MainActivity.this, SmartNotiService.class));
        stopService(new Intent(MainActivity.this, FocusPeriodService.class));
        stopService(new Intent(MainActivity.this, BreakPeriodService.class));
        toggleFeedbackVis(false);
        smartNotiFab.setImageResource(R.drawable.ic_noti);
    }

    @Override
    public void onSmartNotiStart() {
        Status prvTimerStatus = Utility.findStatusFromDB(Constants.PREVIOUS_TIMER);
        if(prvTimerStatus == null) {
            prvTimerStatus = new Status(Constants.PREVIOUS_TIMER, Constants.FOCUS_TIMER);
        }else{
            prvTimerStatus.setContent(Constants.FOCUS_TIMER);
        }
        prvTimerStatus.save();

        smartNotiFab.setImageResource(R.drawable.ic_no_noti);
        toggleFeedbackVis(true);
    }

    private void prepareForOtherTimer(String whichTimer) {
        Status status = Utility.findStatusFromDB(Constants.SMART_NOTIFICATION);
        Status prvTimerStatus = Utility.findStatusFromDB(Constants.PREVIOUS_TIMER);
        //TODO: is this supposed to be here?
        //if SMART NOTIFICATION IS STILL ON
        if (status != null && status.isRunning()) {
            switch (whichTimer) {
                case (Constants.FOCUS_TIMER): {
                    nextBreakInTV.setText(getResources().getString(R.string.next_break_in));
                    startService(new Intent(getApplicationContext(), FocusPeriodService.class));
                    if(prvTimerStatus!=null) {
                        prvTimerStatus.setContent(Constants.FOCUS_TIMER);
                        prvTimerStatus.save();
                    }
                }
                break;
                case (Constants.BREAK_TIMER): {
                    nextBreakInTV.setText(getResources().getString(R.string.next_focus_in));
                    startService(new Intent(getApplicationContext(), BreakPeriodService.class));
                    if(prvTimerStatus!=null) {
                        prvTimerStatus.setContent(Constants.BREAK_TIMER);
                        prvTimerStatus.save();
                    }
                }
                break;
            }

        }
    }

    private void toggleFeedbackVis(boolean visible) {
        if (visible) {
            nextBreakInTV.setVisibility(View.VISIBLE);
            timerTV.setVisibility(View.VISIBLE);
            minutesTV.setVisibility(View.VISIBLE);
            SmartNotiTV.setVisibility(View.VISIBLE);
            finishesTV.setVisibility(View.VISIBLE);
            endTimeTV.setVisibility(View.VISIBLE);
        } else {
            nextBreakInTV.setVisibility(View.GONE);
            timerTV.setVisibility(View.GONE);
            minutesTV.setVisibility(View.GONE);
            SmartNotiTV.setVisibility(View.GONE);
            finishesTV.setVisibility(View.GONE);
            endTimeTV.setVisibility(View.GONE);
        }
    }
}
