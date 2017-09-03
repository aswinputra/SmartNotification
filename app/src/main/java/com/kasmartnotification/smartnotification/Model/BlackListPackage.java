package com.kasmartnotification.smartnotification.Model;

import com.orm.SugarRecord;

/**
 * Created by kiman on 2/9/17.
 * This is a class for the packages that are blacklisted, because the information from these packages
 * doesn't benefits the actual end users
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
