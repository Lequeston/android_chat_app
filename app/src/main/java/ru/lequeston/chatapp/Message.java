package ru.lequeston.chatapp;

import java.util.Date;

public class Message {
    private String mUserName;
    private String mTextMessage;

    private long mMessageTime;

    public Message(){}
    public Message(String userName, String textMessage){
        mUserName = userName;
        mTextMessage = textMessage;

        mMessageTime = new Date().getTime();
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getTextMessage() {
        return mTextMessage;
    }

    public void setTextMessage(String textMessage) {
        mTextMessage = textMessage;
    }

    public long getMessageTime() {
        return mMessageTime;
    }

    public void setMessageTime(long messageTime) {
        mMessageTime = messageTime;
    }
}
