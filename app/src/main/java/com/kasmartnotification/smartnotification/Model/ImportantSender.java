package com.kasmartnotification.smartnotification.Model;

/**
 * Created by aswinhartono on 14/9/17.
 */

public class ImportantSender {
    private String mName;

    public ImportantSender(String name){
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }
}
