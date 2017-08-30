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
         timer = new CountDownTimer(5000, Constants.COUNTDOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.i(Constants.SERVICE_LOG, "BreakPeriod onTick: " + Utility.roundSecond(millisUntilFinished));
                updateTimerView(Utility.roundedSecondStr(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                Log.i(Constants.SERVICE_LOG, "BreakPeriod Timer is finished");
                Status breakTimerStatus = Utility.findStatusFromDB(Constants.BREAK_TIMER);
                if(breakTimerStatus != null) {
                    breakTimerStatus.setRunning(false);
                    breakTimerStatus.save();
                }
                updateTimerView(Constants.END_TIMER);
            }
        };
        timer.start();

        Status breakTimerStatus = Utility.findStatusFromDB(Constants.BREAK_TIMER);
        if(breakTimerStatus == null) {
            breakTimerStatus = new Status(Constants.BREAK_TIMER, true);
        }else{
            breakTimerStatus.setRunning(true);
        }
        breakTimerStatus.save();

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
        Intent intent = new Intent(Constants.UPDATE_PERIOD_TIME);
        if(time != null)
            intent.putExtra(Constants.REMAINING_TIME, time);
        broadcastManager.sendBroadcast(intent);
    }
}
