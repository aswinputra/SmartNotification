package com.kasmartnotification.smartnotification.Model;

import com.orm.SugarRecord;

/**
 * Created by aswinhartono on 14/9/17.
 */

public class Keyword extends SugarRecord{

    private String name;

    public Keyword(){

    }

    public Keyword(String keyword) {
        this.name = keyword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean is(String name){
        return this.name.equals(name);
    }
}
