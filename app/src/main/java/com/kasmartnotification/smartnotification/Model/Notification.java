package com.kasmartnotification.smartnotification.Model;

import android.graphics.drawable.Icon;
import android.util.Log;

import com.kasmartnotification.smartnotification.Constants;
import com.kasmartnotification.smartnotification.Tools.SugarHelper;
import com.kasmartnotification.smartnotification.Tools.Utility;
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
    private Icon largeIcon;
    private int color;

    public Notification() {
    }

    public Notification(String pkgName, String title, String message, long postedTime,  Icon smallIcon, Icon largeIcon, int color) {
        this.pkgName = pkgName  == null ? "": pkgName;
        this.title = title == null ? "": title;
        this.message = message  == null ? "": message;
        this.postedTime = postedTime;
        determineImportance();
        appIcon = smallIcon;
        this.largeIcon = largeIcon;
        this.color = color;
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

    public Icon getLargeIcon() {
        return largeIcon;
    }

    public int getColor() {
        return color;
    }

    public boolean is(Notification notification) {
        boolean is;
        try {
            is = this.pkgName.equals(notification.pkgName) &&
                    this.title.equals(notification.title) &&
                    this.message.equals(notification.message) &&
                    this.important == notification.important;
        } catch (Exception e) {
            Log.e(Constants.EXCEPTION, e.getLocalizedMessage());
            is = true;
        }
        return is;
    }

    private void determineImportance() {
        try {
            if (determinedImportant()) {
                setImportant(true);
            } else {
                setImportant(false);
            }
        } catch (Exception e) {
            Log.e(Constants.EXCEPTION, e.getLocalizedMessage());
            setImportant(false);
        }
    }

    private boolean isBlackListed() {
        BlackListPackage matchedBLPkg = SugarHelper.findFromDB(BlackListPackage.class, pkgName.toLowerCase());
        return matchedBLPkg != null;
    }

    public boolean isValid(){
        return isTitleValid() &&
                isMessageValid() &&
                !isBlackListed();
    }

    public Icon getAppIcon() {
        return appIcon;
    }

    public int getShortId(){
        return getId().intValue();
    }

    private boolean determinedImportant(){
        return importantByKeyword() || importantBySender();
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

    public boolean importantByKeyword(){
        String message = this.message.toLowerCase();
        List<Keyword> keywords = Keyword.listAll(Keyword.class);
        for(Keyword keyword: keywords){
            if(message.contains(keyword.getName())){
                return true;
            }
        }
        return false;
    }

    public boolean importantBySender(){
        ImportantSender matchedSender = SugarHelper.findFromDB(ImportantSender.class, title.toLowerCase());
        return matchedSender != null;
    }
}
