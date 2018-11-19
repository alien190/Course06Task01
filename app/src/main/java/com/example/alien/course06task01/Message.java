package com.example.alien.course06task01;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Message {
    private String mText;
    @ServerTimestamp
    Date time;
    private String mUserUid;

    public Message(String text, String userUid) {
        mText = text;
        mUserUid = userUid;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public String getUserUid() {
        return mUserUid;
    }

    public void setUserUid(String userUid) {
        mUserUid = userUid;
    }
}
