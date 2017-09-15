package com.kasmartnotification.smartnotification.Services;

import android.app.Notification;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.kasmartnotification.smartnotification.Constants;
import com.kasmartnotification.smartnotification.Model.Notifications;
import com.kasmartnotification.smartnotification.Utility;

/**
 * Created by kiman on 1/9/17.
 */

public class NotificationListener extends NotificationListenerService {

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        Log.i(Constants.STATUS_LOG, "Notification Lister: connected");
    }

    @Override
    public void onListenerDisconnected() {
        super.onListenerDisconnected();
        Log.i(Constants.STATUS_LOG, "Notification Lister: disconnected");
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        if (Utility.isFocusTimerOn()) {
            Notification incomingNoti = sbn.getNotification();
            String details = "package: " + sbn.getPackageName() + "\n";
            Bundle extras = incomingNoti.extras;
            String title = extras.getString(Constants.NOTIFICATION_TITLE);
            CharSequence message = extras.getCharSequence(Constants.NOTIFICATION_MESSAGE);

            details += "sbn.getKey(): " + sbn.getKey() + "\n";

            Icon appIcon = incomingNoti.getSmallIcon();
            Icon largeIcon = incomingNoti.getLargeIcon();
            int color = incomingNoti.color;

            details += "title: " + title + "\n";
            if (message != null) {
                details += "message: " + message;
            }
            Log.i(Constants.NOTI_LISTENER_LOG, details);

            Notifications notifications = Notifications.getInstance();

            com.kasmartnotification.smartnotification.Model.Notification newNoti = new com.kasmartnotification.smartnotification.Model.Notification(
                    sbn.getPackageName(),
                    title,
                    String.valueOf(message),
                    sbn.getPostTime(),
                    appIcon,
                    largeIcon,
                    color);

            if (newNoti.isValid()) {
                notifications.add(newNoti);
            }

            if(!sbn.getPackageName().equals(Constants.PACKAGE_NAME)) {
                cancelNotification(sbn.getKey());
            }
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        //this is used to synchronise the notifications in our bottomsheet and notification bar
        if(sbn.getPackageName().equals(Constants.PACKAGE_NAME)) {
            Notifications.getInstance().remove(sbn.getId());
        }
        super.onNotificationRemoved(sbn);
    }

    @Override
    public void onDestroy() {
        Log.i(Constants.STATUS_LOG, "Notification Lister: destroyed");
        super.onDestroy();
    }
}
