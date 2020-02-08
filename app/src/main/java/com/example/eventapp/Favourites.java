package com.example.eventapp;

public class Favourites {
    private String user;



    public Favourites(String user, String event) {
        this.user = user;
        this.event = event;
    }
    public Favourites() {
    }
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    private String event;
}
