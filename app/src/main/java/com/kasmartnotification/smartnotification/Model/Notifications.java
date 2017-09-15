package com.kasmartnotification.smartnotification.Model;

import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.kasmartnotification.smartnotification.Constants;
import com.kasmartnotification.smartnotification.Interfaces.NotificationUpdateListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by kiman on 1/9/17.
 */

public class Notifications {
    private List<Notification> notificationList = null;
    private static Notifications instance = null;
    private NotificationUpdateListener listener = null;

    public static Notifications getInstance() {
        if (instance == null) {
            synchronized (Notifications.class) {
                if (instance == null) {
                    instance = new Notifications();
                }
            }
        }
        return instance;
    }

    private Notifications() {
        notificationList = new ArrayList<>();
        notificationList = Notification.listAll(Notification.class);
        sort();
    }

    public void add(Notification notification) {
        if (size() != 0) {
            if (!has(notification)) {
                notificationList.add(notification);
                sort();
                notification.save();
                if (listener != null) {
                    listener.onNotiAdded(notification);
                }
            }
        } else {
            notificationList.add(notification);
            sort();
            notification.save();
            if (listener != null) {
                listener.onNotiAdded(notification);
            }
        }
    }

    private boolean has(Notification notification) {
        for (Notification noti : notificationList) {
            if (noti.is(notification)) {
                return true;
            }
        }
        return false;
    }

    public Notification get(int position) {
        return notificationList.get(position);
    }

    public List<Notification> getNotificationList() {
        return notificationList;
    }

    public int size() {
        return notificationList.size();
    }

    private Notification getById(int shortId){
        for(Notification notification: notificationList){
            if(notification.getShortId() == shortId){
                return notification;
            }
        }
        return null;
    }

    public void remove(Notification notification) {
        Log.d(Constants.TEST,
                "notifications size before: " + size() + "\n" +
                        "notification db size before: " + Notification.listAll(Notification.class).size());

        boolean success = notificationList.remove(notification);
        if(!success){
            Notification noti = getById(notification.getShortId());
            if(noti!=null){
                notificationList.remove(noti);
                noti.delete();
                listener.onRemove(noti);
            }
        }else {
            notification.delete();
            listener.onRemove(notification);
        }

        Log.d(Constants.TEST,
                        "notifications size: " + size() + "\n" +
                        "notification db size: " + Notification.listAll(Notification.class).size());
    }

    public void remove(int shortId) {
        Notification notification = Notification.findById(Notification.class, shortId);
        if (notification != null) {
            remove(notification);
        }

    }

    public void removeAll() {
        if (notificationList != null) {
            notificationList.clear();
        }
        try {
            Notification.deleteAll(Notification.class);
        } catch (SQLiteException e) {
            Log.i(Constants.MISC, e.getMessage());
        }
        listener.onNotiRemoveAll();
    }

    //importance as category of notification
    public String[][] getAllCategoryWithAmount() {
        Map<String, Integer> importanceCategory = new HashMap<>();
        for (Notification notification : notificationList) {
            if (importanceCategory.containsKey(notification.getImportance())) {
                Integer count = importanceCategory.get(notification.getImportance());
                importanceCategory.put(notification.getImportance(), ++count);
            } else {
                importanceCategory.put(notification.getImportance(), 1);
            }
        }

        //sorting
        String[][] sortedCategories = new String[importanceCategory.size()][];
        String[] values = new String[2];
        int index = 0;
        SortedSet<String> references = new TreeSet<>(importanceCategory.keySet());
        for (String name : references) {
            values[0] = name;
            values[1] = String.valueOf(importanceCategory.get(name));

            sortedCategories[index] = values.clone();
            index++;
        }

        return sortedCategories;
    }

    public void setListener(NotificationUpdateListener listener) {
        this.listener = listener;
    }

    private void sort() {
        //sorting importance
        Comparator<Notification> importanceComparator = new Comparator<Notification>() {
            @Override
            public int compare(Notification n, Notification n1) {
                return n.getImportance().compareTo(n1.getImportance());
            }
        };
        Collections.sort(notificationList, importanceComparator);

        /*wanted to sort by time, but it doesn't need to be done because when new notification comes
         * will added to the end of the list, and therefore showing below the the older notification
         */
    }

}
