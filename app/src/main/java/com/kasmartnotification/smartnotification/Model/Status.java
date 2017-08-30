package com.kasmartnotification.smartnotification.Model;

import com.orm.SugarRecord;

/**
 * Created by kiman on 29/8/17.
 */

public class Status extends SugarRecord<Status>{
    private String name;
    private boolean running;
    private String content;

    public Status(){

    }

    public Status(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public Status(String name, boolean running) {
        this.name = name;
        this.running = running;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean is(String name){
        return this.name.equals(name);
    }
}
