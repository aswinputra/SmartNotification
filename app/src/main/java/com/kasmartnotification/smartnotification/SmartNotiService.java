package com.kasmartnotification.smartnotification;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import static java.lang.Math.round;

/**
 * Created by kiman on 27/8/17.
 */

public class SmartNotiService extends Service {

    private LocalBroadcastManager broadcastManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        broadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        CountDownTimer timer = new CountDownTimer(20000, Constants.COUNTDOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.i(Constants.SERVICE_LOG, "Smart Noti onTick: " + Utility.roundSecond(millisUntilFinished));
                updateTimerView(Utility.roundedSecondStr(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                String finishTime = Constants.END_TIMER;
                Log.i(Constants.SERVICE_LOG, "Smart Noti Timer is finished");
                MyPreferences preferences = MyPreferences.getInstance(getApplicationContext());
                preferences.setSmartNotiServiceStatus(false);
                updateTimerView(finishTime);
            }
        };
        timer.start();
        MyPreferences preferences = MyPreferences.getInstance(getApplicationContext());
        preferences.setSmartNotiServiceStatus(true);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * Using Intent and Local Broadcast to send the time information from Service thread to
     * the UI thread for updating timer value
     * @param time
     */
    private void updateTimerView(String time){
        Intent intent = new Intent(Constants.UPDATE_TIME);
        if(time != null)
            intent.putExtra(Constants.REMAINING_TIME, time);
        broadcastManager.sendBroadcast(intent);
    }

}
