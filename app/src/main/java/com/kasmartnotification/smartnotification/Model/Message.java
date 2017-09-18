package com.kasmartnotification.smartnotification.Model;

import com.orm.SugarRecord;

/**
 * Created by kiman on 18/9/17.
 */

public class Message extends SugarRecord{
    private String content;

    public Message() {
    }

    public Message(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
