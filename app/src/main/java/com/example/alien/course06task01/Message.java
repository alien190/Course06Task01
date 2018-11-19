package com.example.alien.course06task01;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Message {
    private String mText;
    @ServerTimestamp
    Date time;
    private String mUsername;

    public Message(String text, String username) {
        mText = text;
        mUsername = username;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }
}
