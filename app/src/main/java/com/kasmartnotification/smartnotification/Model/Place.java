package com.kasmartnotification.smartnotification.Model;

import com.orm.SugarRecord;

/**
 * Created by aswinhartono on 14/9/17.
 */

public class Place extends SugarRecord{
    private String name;
    private String address;

    public Place(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
