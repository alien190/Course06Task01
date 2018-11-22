package com.example.alien.course06task01;

import android.support.annotation.Nullable;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Message {
    private String mText;
    @ServerTimestamp
    Date time;
    private String mUsername;

    public Message() {
    }

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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Message) {
            Message message = (Message) obj;
            return compare(time, message.time)
                    && compare(mText, message.mText)
                    && compare(mUsername, message.mUsername);
        }
        return false;
    }

    private boolean compare(@Nullable Object stringOne, @Nullable Object stringTwo) {
        return stringOne != null && stringOne.equals(stringTwo);
    }
}
