package com.kasmartnotification.smartnotification.Services;

import android.annotation.TargetApi;
import android.app.Notification;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.kasmartnotification.smartnotification.Constants;
import com.kasmartnotification.smartnotification.Model.Notifications;
import com.kasmartnotification.smartnotification.Model.Status;
import com.kasmartnotification.smartnotification.Utility;

/**
 * Created by kiman on 1/9/17.
 */

public class NotificationListener extends NotificationListenerService{

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        //TODO: MUST change this to check for FOCUS TIMER is running instead
        if(Utility.isSmartNotiInUse()) {

            Notification notification = sbn.getNotification();
            String details = sbn.getPackageName() + "\n";
            Bundle extras = notification.extras;
            String title = extras.getString(Constants.NOTIFICATION_TITLE);
            CharSequence message = extras.getCharSequence(Constants.NOTIFICATION_MESSAGE);

            Icon appIcon = notification.getSmallIcon();

            details += "\n" + title;
            if (message != null) {
                details += "\n" + message;
            }
            Log.i(Constants.NOTI_LISTENER_LOG, details);

            Notifications notifications = Notifications.getInstance();

            com.kasmartnotification.smartnotification.Model.Notification myNoti = new com.kasmartnotification.smartnotification.Model.Notification(
                    sbn.getPackageName(), title, String.valueOf(message), sbn.getPostTime(), appIcon);

            if (myNoti.isValid()) {
                notifications.add(myNoti);
            }

            cancelNotification(sbn.getKey());
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }
}
