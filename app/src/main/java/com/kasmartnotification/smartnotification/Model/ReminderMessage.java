package com.kasmartnotification.smartnotification.Model;

import com.orm.SugarRecord;

/**
 * Created by kiman on 19/9/17.
 */

public class ReminderMessage extends SugarRecord {
    private String name;
    private String toturnon;

    public ReminderMessage() {
    }

    public ReminderMessage(String name, String toTurnOn) {
        this.name = name;
        this.toturnon = toTurnOn;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getToTurnOn() {
        return toturnon;
    }

    public boolean is(String name){
        return this.name.equals(name);
    }
}
