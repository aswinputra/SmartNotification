package com.kasmartnotification.smartnotification.Model;

import com.orm.SugarRecord;

/**
 * Created by kiman on 29/8/17.
 */

public class Setting extends SugarRecord<Setting> {

    private String name;
    private String value;

    public Setting(){

    }

    public Setting(String name, String value) {
        this.name = name;
        this.value = value;
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

    public boolean is(String name){
        return this.name.equals(name);
    }
}
