package com.kasmartnotification.smartnotification;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by kiman on 28/8/17.
 */

public class MyPreferences {
    private static MyPreferences theInstance = null;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    public static MyPreferences getInstance(Context appContext) {
        if(theInstance != null) {
            return theInstance;
        }else{
            theInstance= new MyPreferences(appContext);
            return theInstance;
        }
    }

    private MyPreferences(Context context){
        sharedPreferences = context.getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public void setSmartNotiServiceStatus(boolean running){
        editor = sharedPreferences.edit();
        editor.putBoolean(Constants.SMART_NOTI_SERVICE_RUNNING, running);
        editor.apply();
    }

    public boolean getSmartNotiServiceStatus(){
        return sharedPreferences.getBoolean(Constants.SMART_NOTI_SERVICE_RUNNING, false);
    }
}
