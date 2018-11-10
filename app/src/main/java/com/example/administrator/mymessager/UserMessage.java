package com.example.administrator.mymessager;

import java.util.Date;

/**
 * Created by Administrator on 2/16/2018.
 */

public class UserMessage {
    String message;
    String sender;
    Date CreatedAt;

    public UserMessage(String message, String sender, Date createdAt) {
        this.message = message;
        this.sender = sender;
        this.CreatedAt = createdAt;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setCreatedAt(Date createdAt) {
        this.CreatedAt = createdAt;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

    public Date getCreatedAt() {
        return CreatedAt;
    }
}
