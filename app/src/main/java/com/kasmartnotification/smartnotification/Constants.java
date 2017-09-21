package com.kasmartnotification.smartnotification;

/**
 * Created by kiman on 27/8/17.
 */

public class Constants {
    public static final int ONE_MINUTE = 60;
    public static final long COUNTDOWN_INTERVAL = 1000;

    public static final String PERIOD_TIME = "PERIOD_TIME";
    public static final String REMAINING_TIME = "REMAINING_TIME";
    public static final String END_TIMER = "END_TIMER";
    public static final String ZERO = "0";

    public static final String SERVICE_LOG = "SERVICE_LOG";
    public static final String STATUS_LOG = "STATUS_LOG";
    public static final String NOTI_LISTENER_LOG = "NOTI_LISTENER_LOG";
    public static final String MISC = "MISC_LOG";
    public static final String EXCEPTION = "EXCEPTION_LOG";
    public static final String TEST = "TEST_LOG";
    public static final String LOCATION_LOG = "LOCATION_LOG";

    public static final String SMART_NOTIFICATION = "SMART_NOTIFICATION";
    public static final String SMART_NOTIFICATION_UNBOUNDED = "SMART_NOTIFICATION_UNBOUNDED";
    public static final String SMART_NOTIFICATION_END = "SMART_NOTIFICATION_END";
    public static final String SMART_NOTIFICATION_END_TIME = "SMART_NOTIFICATION_END_TIME";
    public static final String FOCUS_TIMER = "FOCUS_TIMER";
    public static final String BREAK_TIMER = "BREAK_TIMER";
    public static final String PREVIOUS_TIMER = "PREVIOUS_TIMER";

    public static final String LOCAL_BROADCAST_REGISTERED = "LOCAL_BROADCAST_REGISTERED";

    public static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    public static final String NOTIFICATION_ACCESS_SETTING = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";
    public static final String NOTIFICATION_TITLE = "android.title";
    public static final String NOTIFICATION_MESSAGE = "android.text";

    public static final String Important = "Important";

    public static final String IMPORTANT = "IMPORTANT";
    public static final String STATUS = "STATUS";
    public static final int REMINDER_NOTIFICATION_ID = 7700;
    public static final int STATUS_NOTIFICATION_ID = 7701;
    public static final int SMART_NOTIFICATION_ON_CODE = 7702;
    public static final int SMART_NOTIFICATION_OFF_CODE = 7703;
    public static final String TURN_ON = "TURN ON";
    public static final String TURN_OFF = "TURN OFF";
    public static final String TURN_ON_OFF = "TURN ON OFF";

    public static String PACKAGE_NAME;

    public static final int BOTTOM_SHEET_COLLAPSE_HEIGHT = 700;
    public static final int INCREMENT_BY_HOUR = 30;
    public static final int INCREMENT_BY_MINUTE = 5;

    public static final String FOCUS_TIME = "FOCUS_TIME_VALUE";
    public static final String BREAK_DURATION = "BREAK_DURATION_VALUE";
    public static final String SMART_REMINDER = "LOCATION_REMINDER";
    public static final String IMPORTANT_SENDER = "IMPORTANT_SENDER";
    public static final String AUTOMATIC_RESPONSE = "AUTOMATIC_RESPONSE";
    public static final String IMPORTANT_KEYWORDS = "IMPORTANT_KEYWORD";

    public static final int PERMISSION_ACCESS_COARSE_LOCATION = 7705;
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 7706;
    public static final int FINE_LOCATION_REQUEST_CODE = 7707;
    public static final int GPS_REQUEST_CODE = 7708;

    public static final int LOCATION_REFRESH_TIME = 3 * 60000; //60000 millis = 1 min
    public static final int LOCATION_FAST_REFRESH_TIME = 6000; //60000 millis = 1 min

    public static final int LOCATION_ACCURACY = 65; //metres
    public static final String USER_LOCATION = "USER_LOCATION";

    public static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 7709;
    public static final double BOUND_BIAS_RADIUS_METERS = 50000; //50KM
    public static final String NOTIFICATION_REQUEST_CODE = "NOTIFICATION REQUEST";
}
