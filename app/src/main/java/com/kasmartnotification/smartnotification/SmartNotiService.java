package com.kasmartnotification.smartnotification;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.math.MathUtils;
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
        CountDownTimer timer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.i(Constant.SERVICE_LOG, "onTick: " + Utility.roundSecond(millisUntilFinished, 1000));
                updateTimerView(Utility.roundedSecondStr(millisUntilFinished, 1000));
            }

            @Override
            public void onFinish() {
                String finishTime = "0";
                Log.i(Constant.SERVICE_LOG, "Timer is finished");
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
        Intent intent = new Intent(Constant.UPDATE_TIME);
        if(time != null)
            intent.putExtra(Constant.REMAINING_TIME, time);
        broadcastManager.sendBroadcast(intent);
    }

}
