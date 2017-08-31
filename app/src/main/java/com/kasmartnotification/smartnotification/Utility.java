package com.kasmartnotification.smartnotification;

import android.database.sqlite.SQLiteException;
import android.support.annotation.Nullable;

import com.kasmartnotification.smartnotification.Model.Setting;
import com.kasmartnotification.smartnotification.Model.Status;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by kiman on 27/8/17.
 */

public class Utility {

    public static long getMinFromMillis(long millis){
        return TimeUnit.MILLISECONDS.toMinutes(millis) + 1;
    }

    public static String getMinutesStr(long millis){
        return String.valueOf(getMinFromMillis(millis));
    }

    public static Calendar getAddedCalendar(int hourToAdd){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, hourToAdd);
        return calendar;
    }

    public static String getCurrentMinute(Calendar endTime){
        return String.format(Locale.UK, "%02d", endTime.get(Calendar.MINUTE));
    }

    public static Status findStatusFromDB(String name){
        try {
            List<Status> statuses = Status.listAll(Status.class);
            for (Status status : statuses) {
                if (status.is(name)) {
                    return status;
                }
            }
            return null;
        }catch (SQLiteException e){
            return null;
        }
    }

    public static Setting findSettingFromDB(String name){
        try {
            List<Setting> settings = Setting.listAll(Setting.class);
            for (Setting setting : settings) {
                if (setting.is(name)) {
                    return setting;
                }
            }
            return null;
        }catch (SQLiteException e){
            return null;
        }
    }

    public static <T> void createOrSetDBObject(Class<T> type, String name, @Nullable Boolean running, @Nullable String content, @Nullable Calendar calendar){
        if(type == Setting.class){
            Setting setting = findSettingFromDB(name);
            if(setting == null) {
                setting = new Setting(name, content, calendar);
            }else{
                setting.set(content, calendar);
            }
            setting.save();
        }else if(type == Status.class){
            Status status = findStatusFromDB(name);
            if(status == null) {
                status = new Status(name, running, content);
            }else{
                status.set(running, content);
            }
            status.save();
        }
    }

    public static String getTimeString(Calendar endTime){
        int hour = endTime.get(Calendar.HOUR_OF_DAY);
        String minute = getCurrentMinute(endTime);
        String amPM = "";
        if(hour < 12){
            amPM = "AM";
        }else{
            hour = hour -12;
            amPM = "PM";
        }

        return hour + ":" + minute + " " + amPM;
    }

    public static void deleteStatusEntity(){
        Status.deleteAll(Status.class);
        deleteEndTime();
    }

    public static void deleteEndTime(){
        Setting setting = findSettingFromDB(Constants.SMART_NOTIFICATION_END_TIME);
        if(setting != null){
            setting.delete();
        }
    }

    public static long getMillisDiff(Calendar endTime){
        Calendar now = Calendar.getInstance();
        return endTime.getTimeInMillis() - now.getTimeInMillis();
    }

    public static boolean isSmartNotiInUse(){
        Status smartNotiStatus = Utility.findStatusFromDB(Constants.SMART_NOTIFICATION);
        Status smartNotiUnbounded = Utility.findStatusFromDB(Constants.SMART_NOTIFICATION_UNBOUNDED);
        return smartNotiStatus != null && smartNotiStatus.isRunning() || smartNotiUnbounded != null;
    }

    public static boolean isBroadcastReceiverRegistered() {
        Status registeredLocalBroadcast = Utility.findStatusFromDB(Constants.LOCAL_BROADCAST_REGISTERED);
        if (registeredLocalBroadcast != null) {
            return registeredLocalBroadcast.isRunning();
        }
        return false;
    }
}
