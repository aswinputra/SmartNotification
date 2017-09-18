package com.kasmartnotification.smartnotification.Model;

import com.orm.SugarRecord;

/**
 * Created by aswinhartono on 14/9/17.
 */

public class ImportantSender extends SugarRecord{
    private String name;

    public ImportantSender() {
    }

    public ImportantSender(String name){
        this.name = name.toLowerCase();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toLowerCase();
    }

    public boolean is(String name){
        return this.name.equals(name);
    }
}
