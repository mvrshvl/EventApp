package com.example.eventapp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public static String getMail() {
        return _mail;
    }

    public static void setMail(String mail) {
        User._mail = mail;
    }

    public static String getCity() {
        return _city;
    }

    public static void setCity(String city) {
        User._city = city;
    }

    public static String getName() {
        return _name;
    }

    public static void setName(String name) {
        User._name = name;
    }

    private static String _mail;
    private static String _city;
    private static String _name;
    private static String _id;
    public String mail;
    public  String city;
    public String name;
    public String id;
    private DatabaseReference mDatabase;

    public User(String mail, String city, String name, String id) {
        this.mail = mail;
        this.city = city;
        this.name = name;
        this.id = id;
    }
    public User(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }


    public static String getId() {
        return _id;
    }

    public static void setId(String id) {
        User._id = id;
    }



}
