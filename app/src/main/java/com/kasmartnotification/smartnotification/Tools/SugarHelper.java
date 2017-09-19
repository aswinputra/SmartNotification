package com.kasmartnotification.smartnotification.Tools;

import android.support.annotation.Nullable;

import com.kasmartnotification.smartnotification.Constants;
import com.kasmartnotification.smartnotification.Model.BlackListPackage;
import com.kasmartnotification.smartnotification.Model.Message;
import com.kasmartnotification.smartnotification.Model.Setting;
import com.kasmartnotification.smartnotification.Model.Status;
import com.orm.SugarRecord;

import java.util.Calendar;
import java.util.List;

/**
 * Created by kiman on 17/9/17.
 */

public class SugarHelper {

    public static <T> void createOrSetDBObject(Class<T> type, String name, @Nullable Boolean running, @Nullable String content, @Nullable Calendar calendar, @Nullable long milliseconds, @Nullable boolean toggle) {
        if (type == Setting.class) {
            Setting setting = findFromDB(Setting.class, name);
            if (setting == null) {
                setting = new Setting(name, content, calendar, milliseconds, toggle);
            } else {
                setting.set(content, calendar,milliseconds, toggle);
            }
            setting.save();
        } else if (type == Status.class) {
            Status status = findFromDB(Status.class, name);
            if (status == null) {
                status = new Status(name, running, content);
            } else {
                status.set(running, content);
            }
            status.save();
        } else if (type == BlackListPackage.class) {
            BlackListPackage pkg = findFromDB(BlackListPackage.class, name);
            if (pkg == null) {
                pkg = new BlackListPackage(name);
                pkg.save();
            }
        }
    }

    public static <T> void createOrSetDBObject(Class<T> type, String name, @Nullable Boolean running, @Nullable String content, @Nullable Calendar calendar, @Nullable long milliseconds) {
        if (type == Setting.class) {
            Setting setting = findFromDB(Setting.class, name);
            if (setting == null) {
                setting = new Setting(name, content, calendar, milliseconds);
            } else {
                setting.set(content, calendar,milliseconds);
            }
            setting.save();
        } else if (type == Status.class) {
            Status status = findFromDB(Status.class, name);
            if (status == null) {
                status = new Status(name, running, content);
            } else {
                status.set(running, content);
            }
            status.save();
        } else if (type == BlackListPackage.class) {
            BlackListPackage pkg = findFromDB(BlackListPackage.class, name);
            if (pkg == null) {
                pkg = new BlackListPackage(name);
                pkg.save();
            }
        }
    }

    public static void deleteStatusEntity() {
        Status.deleteAll(Status.class);
        deleteEndTime();
    }

    public static void deleteEndTime() {
        Setting setting = findFromDB(Setting.class, Constants.SMART_NOTIFICATION_END_TIME);
        if (setting != null) {
            setting.delete();
        }
    }

    public static boolean isSmartNotiInUse() {
        Status smartNotiStatus = findFromDB(Status.class, Constants.SMART_NOTIFICATION);
        Status smartNotiUnbounded = findFromDB(Status.class, Constants.SMART_NOTIFICATION_UNBOUNDED);
        return smartNotiStatus != null && smartNotiStatus.isRunning() || smartNotiUnbounded != null;
    }

    public static boolean isFocusTimerOn(){
        Status focusTimerStatus = findFromDB(Status.class, Constants.FOCUS_TIMER);
        return focusTimerStatus != null && focusTimerStatus.isRunning();
    }

    public static boolean isBreakTimerOn(){
        Status breakTimerStatus = findFromDB(Status.class, Constants.BREAK_TIMER);
        return breakTimerStatus != null && breakTimerStatus.isRunning();
    }

    //TODO: we might need to safe guard this from sql injection
    public static <T extends SugarRecord> T findFromDB(Class<T> type, String name) {
        try {
            List<T> objects = T.find(type, "name = ?", name);
            return objects.get(0);
        } catch (Exception e) {
            return null;
        }
    }
}
