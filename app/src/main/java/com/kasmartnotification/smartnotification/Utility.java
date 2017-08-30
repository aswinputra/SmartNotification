package com.kasmartnotification.smartnotification;

import android.database.sqlite.SQLiteException;

import com.kasmartnotification.smartnotification.Model.Setting;
import com.kasmartnotification.smartnotification.Model.Status;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by kiman on 27/8/17.
 */

public class Utility {

    public static long roundSecond(long millis){
        return TimeUnit.MILLISECONDS.toSeconds(millis);
    }

    public static String roundedSecondStr(long millis){
        return String.valueOf(roundSecond(millis));
    }

    public static int getCurrentHour(int hourToAdd){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, hourToAdd);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static String getCurrentMinute(){
        Calendar calendar = Calendar.getInstance();
        return String.format(Locale.UK, "%02d", calendar.get(Calendar.MINUTE));
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
}
