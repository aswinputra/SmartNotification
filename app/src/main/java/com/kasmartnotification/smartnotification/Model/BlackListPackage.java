package com.kasmartnotification.smartnotification.Model;

import com.orm.SugarRecord;

/**
 * Created by kiman on 2/9/17.
 */

public class BlackListPackage extends SugarRecord{

    private String name;
    private String test;

    public BlackListPackage() {
    }

    public BlackListPackage(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
