package com.kasmartnotification.smartnotification.Model;

import android.support.annotation.Nullable;

import com.orm.SugarRecord;

/**
 * Created by kiman on 29/8/17.
 */

public class Status extends SugarRecord{
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

    public Status(String name, @Nullable Boolean running, @Nullable String content) {
        this.name = name;
        if(running!= null) {
            this.running = running;
        }
        if(content != null) {
            this.content = content;
        }
    }

    public void set(@Nullable Boolean running, @Nullable String content) {
        if(running!= null) {
            this.running = running;
        }
        if(content != null) {
            this.content = content;
        }
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
