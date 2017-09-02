package com.kasmartnotification.smartnotification.Interfaces;

import com.kasmartnotification.smartnotification.Model.Notification;

/**
 * Created by kiman on 1/9/17.
 */

public interface NotificationUpdateListener {
    void onNotiAdded(Notification notification);
    void onNotiRemoveAll();
}
