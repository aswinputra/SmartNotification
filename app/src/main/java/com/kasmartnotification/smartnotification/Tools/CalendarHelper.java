package com.kasmartnotification.smartnotification.Tools;

import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by kiman on 17/9/17.
 */

public class CalendarHelper {
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

    public static long getMillisDiff(Calendar endTime) {
        Calendar now = Calendar.getInstance();
        return endTime.getTimeInMillis() - now.getTimeInMillis();
    }
}
