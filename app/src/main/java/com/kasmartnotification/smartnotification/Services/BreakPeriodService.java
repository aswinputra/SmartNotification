package com.kasmartnotification.smartnotification.Services;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.kasmartnotification.smartnotification.Model.Setting;
import com.kasmartnotification.smartnotification.Tools.CalendarHelper;
import com.kasmartnotification.smartnotification.Constants;
import com.kasmartnotification.smartnotification.Model.Status;
import com.kasmartnotification.smartnotification.Tools.NotificationHelper;
import com.kasmartnotification.smartnotification.Tools.SugarHelper;
import com.kasmartnotification.smartnotification.Tools.Utility;

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
        Log.i(Constants.STATUS_LOG, "Break Period Service: onStartCommand");
        NotificationHelper.notifyAll(this);
        Setting breakPeriodSetting = SugarHelper.findFromDB(Setting.class, Constants.BREAK_DURATION);
        long breakPeriodMilli;
        if (breakPeriodSetting!=null){
            breakPeriodMilli = Utility.minToMillisecond(breakPeriodSetting.getTime());
        }else{
            breakPeriodMilli = Utility.minToMillisecond(Constants.INCREMENT_BY_MINUTE);
        }
         timer = new CountDownTimer(breakPeriodMilli, Constants.COUNTDOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.i(Constants.SERVICE_LOG, "BreakPeriod onTick: " + CalendarHelper.getSecondFromMillis(millisUntilFinished));
                updateTimerView(CalendarHelper.getMinutesStr(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                Log.i(Constants.STATUS_LOG, "BreakPeriod Timer is finished");

                SugarHelper.createOrSetDBObject(Status.class, Constants.BREAK_TIMER, false, null, null,0);

                updateTimerView(Constants.END_TIMER);
                if(!Utility.isBroadcastReceiverRegistered()){
                    continueToFocus();
                }
            }
        };
        timer.start();
        SugarHelper.createOrSetDBObject(Status.class, Constants.PREVIOUS_TIMER, null, Constants.BREAK_TIMER, null,0);
        SugarHelper.createOrSetDBObject(Status.class, Constants.BREAK_TIMER, true, null, null,0);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(Constants.STATUS_LOG, "Break Period Service is destroyed");
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
        if(SugarHelper.isSmartNotiInUse()) {
            Intent intent = new Intent(getApplicationContext(), FocusPeriodService.class);
            startService(intent);
        }
    }
}
