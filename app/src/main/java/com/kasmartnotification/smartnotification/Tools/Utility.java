package com.kasmartnotification.smartnotification.Tools;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.kasmartnotification.smartnotification.Constants;
import com.kasmartnotification.smartnotification.Model.ImportantSender;
import com.kasmartnotification.smartnotification.Model.Keyword;
import com.kasmartnotification.smartnotification.Model.Notifications;
import com.kasmartnotification.smartnotification.Model.Place;
import com.kasmartnotification.smartnotification.Model.ReminderMessage;
import com.kasmartnotification.smartnotification.Model.Section;
import com.kasmartnotification.smartnotification.Model.Setting;
import com.kasmartnotification.smartnotification.Model.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by kiman on 27/8/17.
 */

public class Utility {

    public static boolean isBroadcastReceiverRegistered() {
        Status registeredLocalBroadcast = SugarHelper.findFromDB(Status.class, Constants.LOCAL_BROADCAST_REGISTERED);
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
                }else if (lists.get(0)instanceof ReminderMessage){
                    for(int i = 0; i<4; i++){
                        names.add(((ReminderMessage) lists.get(i)).getName());
                    }
                }else if (lists.get(0)instanceof Place){
                    for(int i = 0; i<4; i++){
                        names.add(((Place) lists.get(i)).getName());
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
                }else if (lists.get(0)instanceof ReminderMessage){
                    for(int i = 0; i<lists.size(); i++){
                        names.add(((ReminderMessage) lists.get(i)).getName());
                    }
                }else if (lists.get(0)instanceof Place){
                    for(int i = 0; i<lists.size(); i++){
                        names.add(((Place) lists.get(i)).getName());
                    }
                }
            }

        }
        return names;
    }

    public static long minToMillisecond(long minute){
        return TimeUnit.MINUTES.toMillis(minute);
    }

    public static long hourToMillisecond(int hour){
        return TimeUnit.HOURS.toMillis(hour);
    }

    public static long millisecondToMin(long milli){
        return TimeUnit.MILLISECONDS.toMinutes(milli);
    }

    public static  <T> boolean getSwitchValue(T setting, final String type) {
        boolean toggle;
        if (setting != null) {
            toggle = ((Setting) setting).isToggle();
        } else {
            toggle = false;
            SugarHelper.createOrSetDBObject(Setting.class, type, null, null, null, 0, toggle);
        }
        return toggle;
    }

    public static boolean hasPlayServices(Context context, Activity activity) {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(context);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(activity, result, Constants.PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            Toast.makeText(context, "Can't connect to Google play services", Toast.LENGTH_SHORT).show();
            return false;
        }
        Log.i(Constants.MISC, "It supports google play services");
        return true;
    }

    public static <T> String getNames(List<T> list){
        String name = "";
        int count = 0;
        for (String names : getNamesList(list)){
            if (count <= 3){
                if (count==getNamesList(list).size()-1||count==3){
                    name = name + names;
                }else{
                    name = name + names + ", ";
                }
            }else if(count == list.size()-1||count==4){
                name = name + names;
            }
            count++;
        }
        return name;

    }

    public static boolean isServiceRunning(Context appContext, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) appContext.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
