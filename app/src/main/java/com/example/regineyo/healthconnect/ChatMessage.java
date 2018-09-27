package com.example.regineyo.healthconnect;

import android.text.format.DateFormat;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ChatMessage {

    private String id;
    private String text;
    private String name;
    private String photoUrl;
    private String imageUrl;

    public ChatMessage() {
    }

    public ChatMessage(String text, String name, String photoUrl, String imageUrl) {
        this.text = text;
        this.name = name;
        this.photoUrl = photoUrl;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getText() {
        return text;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}


//public class ChatMessage {
//    private String messageText;
//    private String messageUser;
//    private long messageTime;
//
//    public ChatMessage(String messageText, String messageUser) {
//        this.messageText = messageText;
//        this.messageUser = messageUser;
//
//        // Initialize to current time
//        messageTime = new Date().getTime();
//    }
//
//    public ChatMessage(){
//
//    }
//
//    public String getMessageText() {
//        return messageText;
//    }
//
//    public void setMessageText(String messageText) {
//        this.messageText = messageText;
//    }
//
//    public String getMessageUser() {
//        return messageUser;
//    }
//
//    public void setMessageUser(String messageUser) {
//        this.messageUser = messageUser;
//    }
//
//    public long getMessageTime() {
//        return messageTime;
//    }
//
//    public void setMessageTime(long messageTime) {
//        this.messageTime = messageTime;
//    }
//}
