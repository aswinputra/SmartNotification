package com.kasmartnotification.smartnotification;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by kiman on 27/8/17.
 */

public class Utility {

    public static long roundSecond(long millis, long precisionTH){
        return TimeUnit.MILLISECONDS.toSeconds(millis);
    }

    public static String roundedSecondStr(long millis, long precisionTH){
        return String.valueOf(roundSecond(millis, precisionTH));
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

}
