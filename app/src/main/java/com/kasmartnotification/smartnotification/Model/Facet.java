package com.kasmartnotification.smartnotification.Model;

import com.google.android.gms.maps.model.LatLng;
import com.kasmartnotification.smartnotification.Constants;
import com.kasmartnotification.smartnotification.Tools.SugarHelper;

import static com.kasmartnotification.smartnotification.Constants.TURN_OFF;
import static com.kasmartnotification.smartnotification.Constants.TURN_ON;

/**
 * Created by kiman on 19/9/17.
 * This class aims to initialise some default value in the database
 */

public class Facet {

    private static boolean hasSetDefaultValues = false;
    private static LatLng userLocation = null;

    public static void setDefaultValues() {
        SugarHelper.createOrSetDBObject(BlackListPackage.class, "android", null, null, null);
        SugarHelper.createOrSetDBObject(BlackListPackage.class, Constants.PACKAGE_NAME, null, null, null);

        SugarHelper.createOrSetDBObject(ReminderMessage.class, "Would you like to turn on Smart Notification?", null, TURN_ON, null);
        SugarHelper.createOrSetDBObject(ReminderMessage.class, "Are you sure you don't want to turn it on?", null, TURN_ON, null);
        SugarHelper.createOrSetDBObject(ReminderMessage.class, "Would you like to turn off Smart Notification?", null, TURN_OFF, null);

        hasSetDefaultValues = true;
    }

    public static boolean hasSetDefaultValues() {
        return hasSetDefaultValues;
    }

    public static LatLng getUserLocation() {
        return userLocation;
    }

    public static void setUserLocation(LatLng userLocation) {
        Facet.userLocation = userLocation;
    }
}
