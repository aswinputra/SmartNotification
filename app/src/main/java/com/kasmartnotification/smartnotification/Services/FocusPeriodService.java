package com.kasmartnotification.smartnotification.Services;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.kasmartnotification.smartnotification.Tools.CalendarHelper;
import com.kasmartnotification.smartnotification.Constants;
import com.kasmartnotification.smartnotification.Model.Status;
import com.kasmartnotification.smartnotification.Tools.SugarHelper;
import com.kasmartnotification.smartnotification.Tools.Utility;

/**
 * Created by kiman on 27/8/17.
 */

public class FocusPeriodService extends Service {
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
        Log.i(Constants.STATUS_LOG, "Focus Period Service: onStartCommand");
        timer = new CountDownTimer(120000, Constants.COUNTDOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.i(Constants.SERVICE_LOG, "FocusPeriod onTick: " + CalendarHelper.getSecondFromMillis(millisUntilFinished));
                updateTimerView(CalendarHelper.getMinutesStr(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                Log.i(Constants.STATUS_LOG, "FocusPeriod Timer is finished");
                SugarHelper.createOrSetDBObject(Status.class, Constants.FOCUS_TIMER, false, null, null);
                updateTimerView(Constants.END_TIMER);
                if(!Utility.isBroadcastReceiverRegistered()){
                    continueToBreak();
                }
            }
        };
        timer.start();
        SugarHelper.createOrSetDBObject(Status.class, Constants.PREVIOUS_TIMER, null, Constants.FOCUS_TIMER, null);
        SugarHelper.createOrSetDBObject(Status.class, Constants.FOCUS_TIMER, true, null, null);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(Constants.STATUS_LOG, "Focus Period Service is destroyed");
        timer.onFinish();
        timer.cancel();
        stopSelf();
        super.onDestroy();
    }

    /**
     * Using Intent and Local Broadcast to send the time information from Service thread to
     * the UI thread for updating timer value
     *
     * @param time
     */
    private void updateTimerView(String time) {
        Intent intent = new Intent(Constants.PERIOD_TIME);
        if (time != null)
            intent.putExtra(Constants.REMAINING_TIME, time);
        broadcastManager.sendBroadcast(intent);
    }

    private void continueToBreak() {
        if (SugarHelper.isSmartNotiInUse()) {
            Intent intent = new Intent(getApplicationContext(), BreakPeriodService.class);
            startService(intent);
        }
    }


}
