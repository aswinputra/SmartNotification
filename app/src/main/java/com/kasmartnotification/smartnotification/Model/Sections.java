package com.kasmartnotification.smartnotification.Model;

import com.kasmartnotification.smartnotification.Utility;

/**
 * Created by kiman on 2/9/17.
 */

public class Sections {
    private static Sections instance =null;
    private Section[] sectionArray = null;

    public static Sections getInstance() {
        if (instance == null) {
            synchronized (Notifications.class) {
                if (instance == null) {
                    instance = new Sections();
                }
            }
        }
        return instance;
    }

    private Sections(){
        update();
    }

    public Section[] getSectionArray() {
        return sectionArray;
    }

    public boolean update(){
        Notifications notifications = Notifications.getInstance();
        sectionArray =  Utility.getSortedSectionArray(notifications);
        return true;
    }
}
