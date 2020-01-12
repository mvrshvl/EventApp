package com.example.eventapp;

public class Favourites {
    private String user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;

    public Favourites(String user, String event,String id) {
        this.user = user;
        this.event = event;
        this.id = id;
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
