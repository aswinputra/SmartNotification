package com.kasmartnotification.smartnotification;

import android.content.ComponentName;
import android.content.Context;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.kasmartnotification.smartnotification.Controller.SettingsKeywords;
import com.kasmartnotification.smartnotification.Model.BlackListPackage;
import com.kasmartnotification.smartnotification.Model.ImportantSender;
import com.kasmartnotification.smartnotification.Model.Keyword;
import com.kasmartnotification.smartnotification.Model.Notifications;
import com.kasmartnotification.smartnotification.Model.Section;
import com.kasmartnotification.smartnotification.Model.Setting;
import com.kasmartnotification.smartnotification.Model.Status;
import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Created by kiman on 27/8/17.
 */

public class Utility {

    public static long getSecondFromMillis(long millis){
        return TimeUnit.MILLISECONDS.toSeconds(millis);
    }

    public static long getMinFromMillis(long millis) {
        return TimeUnit.MILLISECONDS.toMinutes(millis) + 1;
    }

    public static String getMinutesStr(long millis) {
        return String.valueOf(getMinFromMillis(millis));
    }

    public static Calendar getAddedCalendar(int hourToAdd) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, hourToAdd);
        return calendar;
    }

    public static String getCurrentMinute(Calendar endTime) {
        return String.format(Locale.UK, "%02d", endTime.get(Calendar.MINUTE));
    }

    public static int timeDiffInMinute(long timeToCompare) {
        long now = Calendar.getInstance().getTimeInMillis();
        long diff = now - timeToCompare;
        return (int) (diff / (60 * 1000) % 60);
    }

    public static int timeDiffInHour(int minutes) {
        return minutes / 60;
    }

    public static <T extends SugarRecord> T findFromDB(Class<T> type, String name) {
        try {
            List<T> objects = T.find(type, "name = ?", name);
            return objects.get(0);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> void createOrSetDBObject(Class<T> type, String name, @Nullable Boolean running, @Nullable String content, @Nullable Calendar calendar) {
        if (type == Setting.class) {
            Setting setting = findFromDB(Setting.class, name);
            if (setting == null) {
                setting = new Setting(name, content, calendar);
            } else {
                setting.set(content, calendar);
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

    public static String getTimeString(Calendar endTime) {
        int hour = endTime.get(Calendar.HOUR_OF_DAY);
        String minute = getCurrentMinute(endTime);
        String amPM = "";
        if (hour < 12) {
            amPM = "AM";
        } else {
            hour = hour - 12;
            amPM = "PM";
        }

        return hour + ":" + minute + " " + amPM;
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

    public static long getMillisDiff(Calendar endTime) {
        Calendar now = Calendar.getInstance();
        return endTime.getTimeInMillis() - now.getTimeInMillis();
    }

    public static boolean isSmartNotiInUse() {
        Status smartNotiStatus = Utility.findFromDB(Status.class, Constants.SMART_NOTIFICATION);
        Status smartNotiUnbounded = Utility.findFromDB(Status.class, Constants.SMART_NOTIFICATION_UNBOUNDED);
        return smartNotiStatus != null && smartNotiStatus.isRunning() || smartNotiUnbounded != null;
    }

    public static boolean isBroadcastReceiverRegistered() {
        Status registeredLocalBroadcast = Utility.findFromDB(Status.class, Constants.LOCAL_BROADCAST_REGISTERED);
        return registeredLocalBroadcast != null && registeredLocalBroadcast.isRunning();
    }

    public static boolean isNotificationListenEnabled(Context context) {
        return isNotificationListenEnabled(context, context.getPackageName());
    }

    private static boolean isNotificationListenEnabled(Context context, String pkgName) {
        final String flat = Settings.Secure.getString(context.getContentResolver(), Constants.ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static Section[] getSortedSectionArray(Notifications notifications) throws NullPointerException {
        if (notifications != null) {
            String[][] headers = notifications.getAllCategoryWithAmount();

            List<Section> sectionsList = new ArrayList<>();
            int sectionIndex = 0;
            for (int i = 0; i < headers.length; i++) {
                sectionsList.add(new Section(sectionIndex, headers[i][0], i));
                sectionIndex += Integer.parseInt(headers[i][1]);
            }

            Section[] sectionsArray = new Section[sectionsList.size()];
            for (int i = 0; i < sectionsList.size(); i++) {
                sectionsArray[i] = sectionsList.get(i);
            }

            return sectionsArray;
        } else {
            throw new NullPointerException("Argument notifications is null");
        }
    }

    public static <T> ArrayList<String> getNamesList(List<T> lists){
        ArrayList<String> names = new ArrayList<>();
        if (lists!=null &&lists.size()>0){
            if (lists.size()>=4){
                if (lists.get(0) instanceof Keyword){
                    for(int i = 0; i<4; i++){
                        names.add(((Keyword) lists.get(i)).getName());
                    }
                }else if(lists.get(0) instanceof ImportantSender){
                    for(int i = 0; i<4; i++){
                        names.add(((ImportantSender) lists.get(i)).getName());
                    }
                }
            }else{
                if (lists.get(0) instanceof Keyword){
                    for(int i = 0; i<lists.size(); i++){
                        names.add(((Keyword) lists.get(i)).getName());
                    }
                }else if(lists.get(0) instanceof ImportantSender){
                    for(int i = 0; i<lists.size(); i++){
                        names.add(((ImportantSender) lists.get(i)).getName());
                    }
                }
            }

        }
        return names;
    }
}
