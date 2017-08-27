package com.kasmartnotification.smartnotification;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * Created by kiman on 27/8/17.
 */

public class FocusPeriodService extends Service {
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
        CountDownTimer timer = new CountDownTimer(5000, Constants.COUNTDOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.i(Constants.SERVICE_LOG, "FocusPeriod onTick: " + Utility.roundSecond(millisUntilFinished));
                updateTimerView(Utility.roundedSecondStr(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                String finishTime = "0";
                Log.i(Constants.SERVICE_LOG, "FocusPeriod Timer is finished");
                updateTimerView(finishTime);
            }
        };
        timer.start();
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
        Intent intent = new Intent(Constants.UPDATE_FOCUS_TIME);
        if(time != null)
            intent.putExtra(Constants.REMAINING_TIME, time);
        broadcastManager.sendBroadcast(intent);
    }

}
