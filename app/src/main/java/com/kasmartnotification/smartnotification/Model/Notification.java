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
        this.pkgName = pkgName;
        this.title = title;
        this.message = message;
        this.postedTime = postedTime;
        determineImportance();
        appIcon = icon;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
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
            Log.i(Constants.EXCEPTION, e.getMessage());
            is = true;
        }
        return is;
    }

    private void determineImportance() {
        try {
            if (message.contains("urgent")) {
                setImportant(true);
            } else {
                setImportant(false);
            }
        } catch (Exception e) {
            Log.i(Constants.EXCEPTION, e.getMessage());
            setImportant(false);
        }
    }

    public boolean isBlackListed() {
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
        return !title.isEmpty() && !message.isEmpty() && !isBlackListed();
    }

    public Icon getAppIcon() {
        return appIcon;
    }
}
