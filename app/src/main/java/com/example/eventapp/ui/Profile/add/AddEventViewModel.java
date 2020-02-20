package com.example.eventapp.ui.Profile.add;


import android.graphics.Bitmap;
import android.net.Uri;

import androidx.lifecycle.ViewModel;

import com.example.eventapp.Event;
import com.example.eventapp.MainActivity;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class AddEventViewModel extends ViewModel {

    static private String type ;
    static private String name ;
    static private String about ;
    static private int price ;
    static private int price_kids ;
    static private String address;
    static private String date;
    static  private String date_end;
    static  private String time;
    static  private String time_end;
    static Bitmap[] images=new Bitmap[3];
    static Uri[] images_path=new Uri[3];


    public static Bitmap[] getImages() {
        return images;
    }

    public static void setImages(Bitmap[] images) {
        AddEventViewModel.images = images;
    }

    public static Uri[] getImages_path() {
        return images_path;
    }

    public static void setImages_path(Uri[] images_path) {
        AddEventViewModel.images_path = images_path;
    }


    private StorageReference mStorageRef;


    public static void initial(){
        type=null;
        name=null;
        about=null;
        price=-1;
        price_kids=-1;
        address=null;
        date=null;
        date_end=null;
        time=null;
        time_end=null;
    }

    public static void setType(String _type){
        type = _type;
    }
    public static void setName(String _name){name = _name;}
    public static void setAbout(String _about){about = _about;}
    public static void setDate(String _date){date = _date;}
    public static void setTime(String _time){time=_time;}
    public static void setTime_end(String _time_end){time_end=_time_end;}
    public static void setDate_end(String _date_end){date_end = _date_end;}
    public static void setPrice(int _price){price = _price;}
    public static void setPrice_kids(int _price_kids){price_kids = _price_kids;}
    public static void setAddress(String _address){address = _address;}

    public static String getName(){return name;}
    public static String getAbout(){return about;}
    public static String getDate(){return date;}
    public static String getDate_end(){return date_end;}
    public static String getAddress(){return address;}
    public static int getPrice(){return price;}
    public static int getPrice_kids(){return price_kids;}
    public static String getTime(){return time;}
    public static String getTime_end(){return time_end;}

    public static String getType() {
        return type;
    }

    public void sendData() throws ParseException {
        DateFormat date1 = new SimpleDateFormat("dd.MM.yyyy HH : mm");

        Date nDate1 = date1.parse(date+" "+time);
        ////////////////////////////////////


        long msStart=nDate1.getTime();
        long msEnd;
        if(time_end!=null||date_end!=null){
            Date nDate2 = date1.parse(date_end+" "+time_end);
             msEnd=nDate2.getTime();
        }
        else msEnd=0;
        String id = UUID.randomUUID().toString();

        mStorageRef = FirebaseStorage.getInstance().getReference();

        String images_path1=images_path[0].toString();
        String images_path2;
        if(images_path[1]!=null)
            images_path2=images_path[1].toString();
        else
            images_path2="";
        String images_path3;
        if(images_path[2]!=null)
         images_path3=images_path[2].toString();
        else
            images_path3="";
        Event event = new Event(type,name,about,price,price_kids,msStart,msEnd,address,id,images_path1,images_path2,images_path3,0,MainActivity.getCurrentUser().getUid());
        MainActivity.mDatabaseReference.child("events").child(event.getId()).setValue(event);
    }










}
