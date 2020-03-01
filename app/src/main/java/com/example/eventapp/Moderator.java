package com.example.eventapp;

public class Moderator {
    public Moderator(String id,String city){
        this.id = id;
        this.city = city;
    }
    public Moderator(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String id;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String city;

}
