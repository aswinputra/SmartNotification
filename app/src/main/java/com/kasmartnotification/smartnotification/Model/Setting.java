package com.kasmartnotification.smartnotification.Model;

import android.support.annotation.Nullable;

import com.orm.SugarRecord;

import java.util.Calendar;

/**
 * Created by kiman toggle 29/8/17.
 */

public class Setting extends SugarRecord {

    private String name;
    private String value;
    private Calendar calendar;
    private boolean toggle;

    public Setting(){

    }

    public Setting(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public Setting(String name, @Nullable String value, @Nullable Calendar calendar) {
        this.name = name;
        if(value != null) {
            this.value = value;
        }
        if(calendar != null){
            this.calendar = calendar;
        }
    }

    public void set(@Nullable String value, @Nullable Calendar calendar) {
        if(value != null) {
            this.value = value;
        }
        if(calendar != null){
            this.calendar = calendar;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public boolean is(String name){
        return this.name.equals(name);
    }
}
