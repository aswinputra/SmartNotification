package com.kasmartnotification.smartnotification.Model;

import android.graphics.drawable.Icon;
import android.util.Log;

import com.kasmartnotification.smartnotification.Constants;
import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by kiman on 1/9/17.
 */

public class Notification extends SugarRecord{
    private String pkgName;
    private String title;
    private String message;
    private long postedTime;
    private boolean important;
    private String importance;
    private Icon appIcon;

    public Notification() {
    }

    public Notification(String pkgName, String title, String message, long postedTime,  Icon icon) {
        this.pkgName = pkgName  == null ? "": pkgName;
        this.title = title == null ? "": title;
        this.message = message  == null ? "": message;
        this.postedTime = postedTime;
        determineImportance();
        appIcon = icon;
    }

    public String getPkgName() {
        return pkgName;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public long getPostedTime() {
        return postedTime;
    }

    public boolean isImportant() {
        return important;
    }

    public String getImportance() {
        return importance;
    }

    private void setImportant(boolean important) {
        this.important = important;
        importance = important ? "Important" : "Unimportant";
    }

    public boolean is(Notification notification) {
        boolean is;
        try {
            is = this.pkgName.equals(notification.pkgName) &&
                    this.title.equals(notification.title) &&
                    this.message.equals(notification.message) &&
                    this.important == notification.important;
        } catch (Exception e) {
            Log.e(Constants.EXCEPTION, e.getMessage());
            is = true;
        }
        return is;
    }

    private void determineImportance() {
        try {
            if (determinedImportant(message)) {
                setImportant(true);
            } else {
                setImportant(false);
            }
        } catch (Exception e) {
            Log.e(Constants.EXCEPTION, e.getMessage());
            setImportant(false);
        }
    }

    private boolean isBlackListed() {
        try {
            List<BlackListPackage> blackListPackageList = BlackListPackage.listAll(BlackListPackage.class);
            for (BlackListPackage bLPackage : blackListPackageList) {
                if (this.pkgName.equals(bLPackage.getName())) {
                    return true;
                }
            }
        } catch (Exception e) {
            Log.e(Constants.EXCEPTION, e.getMessage());
        }
        return false;
    }

    public boolean isValid(){
        return isTitleValid() &&
                isMessageValid() &&
                !isBlackListed();
    }

    public Icon getAppIcon() {
        return appIcon;
    }

    private boolean determinedImportant(String message){
        //TODO: we will need a database for this
        return message.toLowerCase().contains("urgent");
    }

    private boolean isTitleValid(){
        //TODO: we might need a database for this or a list
        String title = this.title.toLowerCase();
        return !title.isEmpty() &&
                !(title.contains("null")||
                        title.contains("chat heads active")
                );
    }

    private boolean isMessageValid(){
        //TODO: we might need a database for this or a list
        String message = this.message.toLowerCase();
        return !message.isEmpty() &&
                !message.equals("null");
    }
}
