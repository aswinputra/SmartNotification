package com.kasmartnotification.smartnotification.Services;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.kasmartnotification.smartnotification.Constants;
import com.kasmartnotification.smartnotification.Model.Status;
import com.kasmartnotification.smartnotification.Utility;

/**
 * Created by kiman on 29/8/17.
 */

public class BreakPeriodService extends Service {
    private LocalBroadcastManager broadcastManager;
    private CountDownTimer timer;

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
         timer = new CountDownTimer(60000, Constants.COUNTDOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.i(Constants.SERVICE_LOG, "BreakPeriod onTick: " + Utility.getSecondFromMillis(millisUntilFinished));
                updateTimerView(Utility.getMinutesStr(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                Log.i(Constants.SERVICE_LOG, "BreakPeriod Timer is finished");

                Utility.createOrSetDBObject(Status.class, Constants.BREAK_TIMER, false, null, null);

                updateTimerView(Constants.END_TIMER);
                if(!Utility.isBroadcastReceiverRegistered()){
                    continueToFocus();
                }
            }
        };
        timer.start();
        Utility.createOrSetDBObject(Status.class, Constants.PREVIOUS_TIMER, null, Constants.BREAK_TIMER, null);
        Utility.createOrSetDBObject(Status.class, Constants.BREAK_TIMER, true, null, null);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(Constants.SERVICE_LOG, "Break Period Service is destroyed");
        timer.onFinish();
        timer.cancel();
        stopSelf();
        super.onDestroy();
    }

    /**
     * Using Intent and Local Broadcast to send the time information from Service thread to
     * the UI thread for updating timer value
     * @param time
     */
    private void updateTimerView(String time){
        Intent intent = new Intent(Constants.PERIOD_TIME);
        if(time != null)
            intent.putExtra(Constants.REMAINING_TIME, time);
        broadcastManager.sendBroadcast(intent);
    }

    private void continueToFocus(){
        if(Utility.isSmartNotiInUse()) {
            Intent intent = new Intent(getApplicationContext(), FocusPeriodService.class);
            startService(intent);
        }
    }
}
