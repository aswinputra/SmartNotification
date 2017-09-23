package com.kasmartnotification.smartnotification.Controller;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.kasmartnotification.smartnotification.LocationTools.LocationHelper;
import com.kasmartnotification.smartnotification.LocationTools.MyGPSCallback;
import com.kasmartnotification.smartnotification.LocationTools.MyGoogleLocationApiClient;
import com.kasmartnotification.smartnotification.Model.Facet;
import com.kasmartnotification.smartnotification.Services.LocationService;
import com.kasmartnotification.smartnotification.Tools.CalendarHelper;
import com.kasmartnotification.smartnotification.Constants;
import com.kasmartnotification.smartnotification.Model.Setting;
import com.kasmartnotification.smartnotification.Model.Status;
import com.kasmartnotification.smartnotification.Interfaces.OnSmartNotiStartListener;
import com.kasmartnotification.smartnotification.Tools.NotificationHelper;
import com.kasmartnotification.smartnotification.R;
import com.kasmartnotification.smartnotification.Services.BreakPeriodService;
import com.kasmartnotification.smartnotification.Services.FocusPeriodService;
import com.kasmartnotification.smartnotification.Services.NotificationListener;
import com.kasmartnotification.smartnotification.Services.SmartNotiService;
import com.kasmartnotification.smartnotification.Tools.SugarHelper;
import com.kasmartnotification.smartnotification.Tools.Utility;


import java.text.DecimalFormat;

import static com.kasmartnotification.smartnotification.Constants.TURN_OFF;
import static com.kasmartnotification.smartnotification.Constants.TURN_ON;
import static com.kasmartnotification.smartnotification.Constants.PERMISSION_ACCESS_COARSE_LOCATION;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener,
        OnSmartNotiStartListener,
        MyGPSCallback {


    private FloatingActionButton smartNotiFab;
    private BroadcastReceiver broadcastReceiver;
    private TextView nextBreakInTV;
    private TextView timerTV;
    private TextView minutesTV;
    private TextView smartNotiTV;
    private TextView untilTV;
    private TextView endTimeTV;
    private NotiBottomSheet bottomSheet;

    private static DecimalFormat timeFormat = new DecimalFormat("#.#");

    private SmartNotiDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);

        Constants.PACKAGE_NAME = getPackageName();

        nextBreakInTV = findViewById(R.id.activity_main_next_break_in);
        timerTV = findViewById(R.id.activity_main_focus_period_timer);
        minutesTV = findViewById(R.id.activity_main_minutes);
        smartNotiTV = findViewById(R.id.activity_main_smartnotification);
        untilTV = findViewById(R.id.activity_main_until);
        endTimeTV = findViewById(R.id.activity_main_end_time);
        smartNotiFab = findViewById(R.id.activity_main_fab_smartnotification);
        smartNotiFab.setOnClickListener(this);

        toggleFeedbackVis(false);
        setUpBroadcastReceiver();

        bottomSheet = new NotiBottomSheet(getApplicationContext(), this);

        if (!Facet.hasSetDefaultValues()) {
            Facet.setDefaultValues();
        }

        Setting firstTimeOpen = SugarHelper.findFromDB(Setting.class, Constants.FIRST_TIME_OPEN);

        if (firstTimeOpen == null) {
            new WelcomeDialog(this);
            SugarHelper.createOrSetDBObject(Setting.class, Constants.FIRST_TIME_OPEN, null, null, null, 0, true);
        }

        MyGoogleLocationApiClient client = new MyGoogleLocationApiClient(this);
        client.setUpGPS(this, this);

        handleIntentFromNotification();
    }

    private void handleIntentFromNotification() {
        Intent intent = getIntent();
        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {
                if (action.equals(TURN_ON)) {
                    showSmartNotiDialog();
                } else if (action.equals(TURN_OFF)) {
                    stopSmartNoti();
                }
                NotificationHelper.cancelNotification(this, Constants.REMINDER_NOTIFICATION_ID);
            }
        }
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
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_report_feedback) {
            sendFeedback();
        }

        return super.onOptionsItemSelected(item);
    }

    public void showSmartNotiDialog() {
        dialog = new SmartNotiDialog(this, this);
        dialog.setContentView(R.layout.smart_notification_dialog);
        dialog.show();
        Utility.getNotificationAccessPermission(this);
    }

    /**
     * This functions sets up the BroadcastReceiver to receive broadcast data from the Services:
     * BreakPeriodService, FocusPeriodService, and SmartNotiService
     * <p>
     * PERIOD_TIME is a signal from Break and Focus Period Services to inform to update the time
     * on this UI thread
     * When it's OnTick of the Timer is called in the Service, it will send REMAINING_TIME as a string to display
     * But when it's OnFinish is called, it will send END_TIMER to signal that the timer from a Service is finished
     * and therefore needs to call the alternative Service, which needs the status flag of the PREVIOUS_TIMER
     * so when the previous timer is a break timer, the focus timer will be called and vice versa
     * <p>
     * SMART_NOTIFICATION_END is a signal from SmartNotiService to notify that the Smart Notification has ended
     * so that the view can be updated accordingly
     */
    public void setUpBroadcastReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    if (intent.getAction().equals(Constants.PERIOD_TIME)) {
                        String time = intent.getStringExtra(Constants.REMAINING_TIME);
                        double theTime = Integer.parseInt(time);
                        if (theTime <= 60) {
                            String focusTimer = Integer.toString((int) theTime);
                            timerTV.setText(focusTimer);
                            minutesTV.setText(R.string.minutes);
                        } else {
                            String focusTimer;
                            if (getMinute(theTime) != 0) {
                                focusTimer = String.format("%d:%02d", getHour(theTime), getMinute(theTime));
                            } else {
                                focusTimer = Integer.toString(getHour(theTime));
                            }
                            timerTV.setText(focusTimer);
                            if (getHour(theTime) > 1) {
                                minutesTV.setText(R.string.main_hour);
                            }
                            minutesTV.setText(R.string.main_hours);
                        }
                        if (time.equals(Constants.END_TIMER)) {
                            timerTV.setText(Constants.ZERO);
                            Status status = SugarHelper.findFromDB(Status.class, Constants.PREVIOUS_TIMER);
                            if (status != null && status.getContent().equals(Constants.BREAK_TIMER)) {
                                prepareForOtherTimer(Constants.FOCUS_TIMER);
                            } else if (status != null && status.getContent().equals(Constants.FOCUS_TIMER)) {
                                prepareForOtherTimer(Constants.BREAK_TIMER);
                            }
                        }
                    }
                    if (intent.getAction().equals(Constants.SMART_NOTIFICATION_END)) {
                        stopSmartNoti();
                    }
                } catch (Exception e) {
                    Log.e(Constants.EXCEPTION, e.getLocalizedMessage());
                }
            }
        };
    }

    private void prepareForOtherTimer(String whichTimer) {
        if (SugarHelper.isSmartNotiInUse()) {
            switch (whichTimer) {
                case (Constants.FOCUS_TIMER): {
                    nextBreakInTV.setText(getResources().getString(R.string.next_break_in));
                    startService(new Intent(getApplicationContext(), FocusPeriodService.class));
                }
                break;
                case (Constants.BREAK_TIMER): {
                    nextBreakInTV.setText(getResources().getString(R.string.next_focus_in));
                    startService(new Intent(getApplicationContext(), BreakPeriodService.class));
                }
                break;
            }

        }
    }

    /**
     * LocalBroadcastManager needs to be registered in onStart() and unregistered in onStop()
     * IntentFilter is used to listen for specific intent action: PERIOD_TIME and SMART_NOTIFICATION_END
     * <p>
     * When the broadcast is registered, its status is saved in the database so that we check to see
     * if the MainActivity is visible to users
     * <p>
     * Because we can't update the UI when the Activity is not visible, so when the Activity is back to visible
     * we need to refresh the view to show the correct information
     */
    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.PERIOD_TIME);
        intentFilter.addAction(Constants.SMART_NOTIFICATION_END);
        LocalBroadcastManager.getInstance(this).registerReceiver((broadcastReceiver), intentFilter);

        SugarHelper.createOrSetDBObject(Status.class, Constants.LOCAL_BROADCAST_REGISTERED, true, null, null, 0);

        refreshView();

    }

    /**
     * LocalBroadcastManager needs to be registered in onStart() and unregistered in onStop()
     * <p>
     * When the broadcast is unregistered, its status is saved in the database so that we check to see
     * if the MainActivity is invisible to users
     */
    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        SugarHelper.createOrSetDBObject(Status.class, Constants.LOCAL_BROADCAST_REGISTERED, false, null, null, 0);

        //this is to prevent leaking window
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        super.onStop();
    }

    /**
     * Since once the app is started and smart notification ends, there are no entry of
     * SMART_NOTIFICATION and SMART_NOTIFICATION_UNBOUNDED in the Database, we can use that to check
     * whether the button is clicked to disable to to enable Smart Notification
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_main_fab_smartnotification: {
                if (isNotStarted()) {
                    showSmartNotiDialog();
                } else {
                    stopSmartNoti();
                }
            }
            break;
        }
    }

    /**
     * When a stop signal is given, the Smart Notification needs to stop, therefore the 3 Services are called to stop
     * if a Service is not started, calling a stopService to it, will do nothing to it
     * <p>
     * Once it's finished, we clear all the status, which store the status of each timer, and the PERVIOUS_TIMER
     * And also, update the view accordingly
     */
    private void stopSmartNoti() {
        Log.i(Constants.STATUS_LOG, "stopSmartNoti()");
        stopService(new Intent(MainActivity.this, SmartNotiService.class));
        stopService(new Intent(MainActivity.this, FocusPeriodService.class));
        stopService(new Intent(MainActivity.this, BreakPeriodService.class));
        stopService(new Intent(MainActivity.this, NotificationListener.class));
        SugarHelper.deleteStatusEntity();
        setSmartNotiOffView();
    }

    @Override
    public void onSmartNotiStart() {
        NotificationHelper.notifySmartNoti(this);
        setSmartNotiOnView();
    }

    private void toggleFeedbackVis(boolean visible) {
        if (visible) {
            nextBreakInTV.setVisibility(View.VISIBLE);
            timerTV.setVisibility(View.VISIBLE);
            minutesTV.setVisibility(View.VISIBLE);
            smartNotiTV.setVisibility(View.VISIBLE);
            untilTV.setVisibility(View.VISIBLE);
            endTimeTV.setVisibility(View.VISIBLE);
        } else {
            nextBreakInTV.setVisibility(View.GONE);
            timerTV.setVisibility(View.GONE);
            minutesTV.setVisibility(View.GONE);
            smartNotiTV.setVisibility(View.GONE);
            untilTV.setVisibility(View.GONE);
            endTimeTV.setVisibility(View.GONE);
        }
    }

    /**
     * @return true means there are no service running at the moment or started even started yet
     */
    private boolean isNotStarted() {
        Status smartNotiStatus = SugarHelper.findFromDB(Status.class, Constants.SMART_NOTIFICATION);
        Status smartNotiUnbounded = SugarHelper.findFromDB(Status.class, Constants.SMART_NOTIFICATION_UNBOUNDED);
        return smartNotiStatus == null && smartNotiUnbounded == null;
    }

    private void refreshView() {
        Status focusTimer = SugarHelper.findFromDB(Status.class, Constants.FOCUS_TIMER);
        Status breakTimer = SugarHelper.findFromDB(Status.class, Constants.BREAK_TIMER);

        if (SugarHelper.isSmartNotiInUse()) {
            setSmartNotiOnView();
            if (focusTimer != null && focusTimer.isRunning()) {
                nextBreakInTV.setText(getResources().getString(R.string.next_break_in));
            } else if (breakTimer != null && breakTimer.isRunning()) {
                nextBreakInTV.setText(getResources().getString(R.string.next_focus_in));
            }
        } else {
            stopSmartNoti();
        }
        bottomSheet.update();
    }

    private void setSmartNotiOnView() {
        smartNotiFab.setImageResource(R.drawable.ic_no_noti);
        toggleFeedbackVis(true);
        Setting endTime = SugarHelper.findFromDB(Setting.class, Constants.SMART_NOTIFICATION_END_TIME);
        if (endTime != null) {
            String test = CalendarHelper.getTimeString(endTime.getCalendar());
            endTimeTV.setText(test);
        } else {
            endTimeTV.setText(getResources().getString(R.string.turned_off));
        }
    }

    private void setSmartNotiOffView() {
        toggleFeedbackVis(false);
        smartNotiFab.setImageResource(R.drawable.ic_noti);
        NotificationHelper.cancelSmartNotiNotification(this);
    }


    @Override
    public void onBackPressed() {
        if (bottomSheet.isBottomSheetCollapsed()) {
            super.onBackPressed();
        } else {
            bottomSheet.collapseBottomSheet();
        }
    }

    private int getMinute(double time) {
        return (int) time % 60;
    }

    private int getHour(double time) {
        return (int) time / 60 % 24;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // All good!
                } else {
                    Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
                    SugarHelper.createOrSetDBObject(Setting.class, Constants.SMART_REMINDER, null, null, null, 0, false);
                }
                break;
        }
    }

    @Override
    public void OnGPSResult(int statusCode) {
        if (statusCode == LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE) {
            Toast.makeText(this, "GPS is unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendFeedback() {
        String subject = "Smart Notification feedback";
        String bodyText = "Please provide your feedback below. \n Please include images if applicable";
        String email = "mailto:11883170@student.uts.edu.au" +
                "?cc=" + "11836210@student.uts.edu.au" +
                "&subject=" + Uri.encode(subject) +
                "&body=" + Uri.encode(bodyText);
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse(email));
        try {
            startActivity(Intent.createChooser(emailIntent, "Pick your app..."));
        } catch (Exception e) {
            //nothing;
        }
    }

}
