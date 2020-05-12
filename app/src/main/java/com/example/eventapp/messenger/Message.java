package com.example.eventapp.messenger;

import java.util.Date;

public class Message {

    private String textMessage;
    private String autor;
    private long timeMessage;
    private int type;

    public Message(String textMessage, String autor, int type) {
        this.textMessage = textMessage;
        this.autor = autor;
        this.type = type;
        timeMessage = new Date().getTime();
    }

    public Message() {
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public String getAutor() {
        return autor;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public long getTimeMessage() {
        return timeMessage;
    }

    public void setTimeMessage(long timeMessage) {
        this.timeMessage = timeMessage;
    }
}