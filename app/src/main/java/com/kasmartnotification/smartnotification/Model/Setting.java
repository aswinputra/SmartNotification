package com.kasmartnotification.smartnotification.Model;

import android.support.annotation.Nullable;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by kiman on 29/8/17.
 */

public class Setting extends SugarRecord {

    private String name;
    private String value;
    private long time;
    private Calendar calendar;
    private boolean toggle;
//    private ArrayList<Object> Lists;

    public Setting(){

    }

    public Setting(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public Setting(String name, long time){
        this.name = name;
        this.time = time;
    }

    public Setting(String name, @Nullable String value, @Nullable Calendar calendar, @Nullable long time) {
        this.name = name;
        if(value != null) {
            this.value = value;
        }
        if(calendar != null){
            this.calendar = calendar;
        }
        this.time = time;
    }

    public Setting(String name, @Nullable String value, @Nullable Calendar calendar, @Nullable long time, @Nullable boolean toggle) {
        this.name = name;
        if(value != null) {
            this.value = value;
        }
        if(calendar != null){
            this.calendar = calendar;
        }
        this.time = time;
        this.toggle = toggle;
    }

    public void set(@Nullable String value, @Nullable Calendar calendar, @Nullable long time, boolean toggle) {
        if(value != null) {
            this.value = value;
        }
        if(calendar != null){
            this.calendar = calendar;
        }
        this.time = time;
        this.toggle = toggle;
    }

    public void set(@Nullable String value, @Nullable Calendar calendar, @Nullable long time) {
        if(value != null) {
            this.value = value;
        }
        if(calendar != null){
            this.calendar = calendar;
        }
        this.time = time;
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isToggle() {
        return toggle;
    }

    public void setToggle(boolean toggle) {
        this.toggle = toggle;
    }
}
