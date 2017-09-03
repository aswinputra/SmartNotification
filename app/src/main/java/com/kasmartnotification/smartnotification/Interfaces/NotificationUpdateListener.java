package com.kasmartnotification.smartnotification.Interfaces;

import com.kasmartnotification.smartnotification.Model.Notification;

/**
 * Created by kiman on 1/9/17.
 * This interface is used to communicate between the NotificationAdatper, NotiBottomSheet and the Notifications
 * It is done this way so that the communication between Java Object and DB Entity is wrapped in one class
 * so that properly synchronization between POJO and DB Entity is ensured
 */

public interface NotificationUpdateListener {
    void onNotiAdded(Notification notification);
    void onNotiRemoveAll();
    void onRemove(Notification notification);
}
