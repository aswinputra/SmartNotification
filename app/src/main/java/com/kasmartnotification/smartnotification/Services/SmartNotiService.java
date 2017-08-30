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
        //TODO: get millisInFuture from Setting
        timer = new CountDownTimer(20000, Constants.COUNTDOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.i(Constants.SERVICE_LOG, "Smart Noti onTick: " + Utility.roundSecond(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                Log.i(Constants.SERVICE_LOG, "Smart Noti Timer is finished");
                Status smartNotiStatus = Utility.findStatusFromDB(Constants.SMART_NOTIFICATION);
                if(smartNotiStatus != null) {
                    smartNotiStatus.setRunning(false);
                    smartNotiStatus.save();
                }
                sendEndFlag();
            }
        };
        timer.start();

        Status smartNotiStatus = Utility.findStatusFromDB(Constants.SMART_NOTIFICATION);
        if(smartNotiStatus == null) {
            smartNotiStatus = new Status(Constants.SMART_NOTIFICATION, true);
        }else{
            smartNotiStatus.setRunning(true);
        }
        smartNotiStatus.save();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(Constants.SERVICE_LOG, "Smart Noti Timer Service is destroyed");
        timer.onFinish();
        timer.cancel();
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
    }

}
