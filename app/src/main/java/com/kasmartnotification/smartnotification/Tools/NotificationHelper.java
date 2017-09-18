package com.kasmartnotification.smartnotification.Tools;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.kasmartnotification.smartnotification.Constants;
import com.kasmartnotification.smartnotification.Controller.MainActivity;
import com.kasmartnotification.smartnotification.Model.Notification;
import com.kasmartnotification.smartnotification.Model.Notifications;
import com.kasmartnotification.smartnotification.R;

/**
 * Created by kiman on 14/9/17.
 */

public class NotificationHelper {

    public static void notifyAll(Context context){
        Notifications notifications = Notifications.getInstance();
        for(Notification notification: notifications.getNotificationList()){
            notify(context, notification, notification.isImportant());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void notify(Context context, Notification notification, boolean important) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(notification.getPkgName());

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri soundPath;
        if(important) {
            soundPath = Uri.parse("android.resource://com.kasmartnotification.smartnotification/raw/rhea");
        }else{
            soundPath = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        long[] vibrationPattern = {0, 200, 100, 200}; //sleep, vibrate, sleep, vibrate
        int color = ContextCompat.getColor(context, R.color.colorPrimary);

        NotificationCompat.Builder importantNoti =  new NotificationCompat.Builder(context, Constants.IMPORTANT)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getMessage())
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setColor(color)
                .setColorized(true)
                .setSound(soundPath)
                .setVibrate(vibrationPattern);

        Icon appIcon = notification.getAppIcon();
        if (appIcon != null) {
            Drawable drawable = appIcon.loadDrawable(context);
            Bitmap bitmap = ImageDecoder.convertDrawableToBitmap(drawable);
            bitmap = ImageDecoder.changeImageColor(bitmap, notification.getColor());
            importantNoti.setLargeIcon(bitmap);
        }

        if (notificationManager != null) {
            notificationManager.notify(notification.getShortId(), importantNoti.build());
        }
    }

    public static void notifySmartNoti(Context context){
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent mainActivityIntent = new Intent(context, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(mainActivityIntent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT);

        int color = ContextCompat.getColor(context, R.color.colorPrimary);

        NotificationCompat.Builder importantNoti =  new NotificationCompat.Builder(context, Constants.STATUS)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle("Smart Notification is running...")
                .setContentInfo("testing")
                .setContentText("Tap to open app")
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setColor(color)
                .setColorized(true);

        if (notificationManager != null) {
            notificationManager.notify(Constants.STATUS_NOTIFICATION_ID, importantNoti.build());
        }
    }

    public static void cancelSmartNotiNotification(Context context){
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null) {
            notificationManager.cancel(Constants.STATUS_NOTIFICATION_ID);
        }
    }

    public static void cancelNotification(Context context, int shortId){
        //remove that notification
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        try{
            if (notificationManager != null) {
                notificationManager.cancel(shortId);
            }
        }catch (Exception e){
            Log.e(Constants.EXCEPTION, e.getLocalizedMessage());
        }
    }
}
