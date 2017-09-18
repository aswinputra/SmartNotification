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
import com.kasmartnotification.smartnotification.Model.Setting;
import com.kasmartnotification.smartnotification.Model.Status;
import com.kasmartnotification.smartnotification.Tools.SugarHelper;

import static java.lang.Math.round;

/**
 * Created by kiman on 27/8/17.
 */

public class SmartNotiService extends Service {

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
        Log.i(Constants.STATUS_LOG, "Smart Noti Service: onStartCommand");
        Setting endTime = SugarHelper.findFromDB(Setting.class, Constants.SMART_NOTIFICATION_END_TIME);
        if(endTime!=null) {
            long millisInFuture = CalendarHelper.getMillisDiff(endTime.getCalendar());
            timer = new CountDownTimer(millisInFuture, Constants.COUNTDOWN_INTERVAL) {
                @Override
                public void onTick(long millisUntilFinished) {
                    Log.i(Constants.SERVICE_LOG, "Smart Noti onTick: " + CalendarHelper.getSecondFromMillis(millisUntilFinished));
                }

                @Override
                public void onFinish() {
                    Log.i(Constants.SERVICE_LOG, "Smart Noti Timer is finished");

                    SugarHelper.createOrSetDBObject(Status.class, Constants.SMART_NOTIFICATION, false, null, null);

                    sendEndFlag();
                }
            };
            timer.start();

            SugarHelper.createOrSetDBObject(Status.class, Constants.SMART_NOTIFICATION, true, null, null);
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(Constants.STATUS_LOG, "Smart Noti Service is destroyed");
        try {
            timer.onFinish();
            timer.cancel();
        }catch (Exception e){
            Log.e(Constants.EXCEPTION, e.getLocalizedMessage());
        }
        stopSelf();
        super.onDestroy();
    }

    /**
     * Using Intent and Local Broadcast to send the time information from Service thread to
     * the UI thread for update views
     */
    private void sendEndFlag(){
        Intent intent = new Intent(Constants.SMART_NOTIFICATION_END);
        broadcastManager.sendBroadcast(intent);
        stopSelf();
    }

}
