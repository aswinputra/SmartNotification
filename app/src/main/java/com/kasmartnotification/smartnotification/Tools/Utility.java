package com.kasmartnotification.smartnotification.Tools;

import android.content.ComponentName;
import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;

import com.kasmartnotification.smartnotification.Constants;
import com.kasmartnotification.smartnotification.Model.ImportantSender;
import com.kasmartnotification.smartnotification.Model.Keyword;
import com.kasmartnotification.smartnotification.Model.Notifications;
import com.kasmartnotification.smartnotification.Model.Section;
import com.kasmartnotification.smartnotification.Model.Status;

import java.util.ArrayList;
import java.util.List;

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
