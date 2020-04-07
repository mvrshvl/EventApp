package com.example.eventapp;

import android.graphics.Bitmap;
import android.net.Uri;

public class Event {
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice_kids() {
        return price_kids;
    }

    public void setPrice_kids(int price_kids) {
        this.price_kids = price_kids;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getDate_end() {
        return date_end;
    }

    public void setDate_end(long date_end) {
        this.date_end = date_end;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String type;
    private String name;
    private String about;
    private int price;
    private int price_kids;
    private long date;
    private long date_end;
    private String address;
    private String id;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    private int state;

//4 состояния 0 - ожидает проверки,1 - допущено, 2- заблокировано,3 - истекло
    private String city;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    private String user;

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    private int like;



    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    public String getImages_path1() {
        return images_path1;
    }

    public void setImages_path1(String images_path1) {
        this.images_path1 = images_path1;
    }

    public String getImages_path2() {
        return images_path2;
    }

    public void setImages_path2(String images_path2) {
        this.images_path2 = images_path2;
    }

    public String getImages_path3() {
        return images_path3;
    }

    public void setImages_path3(String images_path3) {
        this.images_path3 = images_path3;
    }
    private String images_path1;
    private String images_path2;
    private String images_path3;

    public int getDiss() {
        return diss;
    }

    public void setDiss(int diss) {
        this.diss = diss;
    }

    private int diss;

    public Event(String type,String name, String about, int price,int price_kids,long date,long date_end,String address,String id,String images_path1,String images_path2,String images_path3,int like,String user,String city, int state,int diss) {
        this.type = type;
        this.name = name;
        this.about = about;
        this.price = price;
        this.date = date;
        this.price_kids = price_kids;
        this.date_end = date_end;
        this.address = address;
        this.id = id;
        this.images_path1 = images_path1;
        this.images_path2 = images_path2;
        this.images_path3=images_path3;
        this.like = like;
        this.user = user;
        this.city = city;
        this.state = state;
        this.diss = diss;
    }
    public Event(){

    }

}


