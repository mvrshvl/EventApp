package com.example.eventapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.eventapp.MainActivity.getCurrentUser;
import static com.example.eventapp.MainActivity.mDatabaseReference;
import static com.example.eventapp.MainActivity.mFirebaseDatabase;

public class Utils {
    public static List<Event> list_event;
    public static boolean flag_moderator;
    public static void setNewCity(String city){
        mDatabaseReference.child("user").child(User.getId()).child("city").setValue(city);
        User.setCity(city);
    }

    public static void deleteLike(Event event){
        DatabaseReference reference = mFirebaseDatabase.getInstance().getReference("favourites").child(event.getId()+getCurrentUser().getUid());
        reference.getRef().removeValue();
        reference = mFirebaseDatabase.getInstance().getReference("events").child(event.getId()).child("like");
        reference.getRef().setValue(event.getLike()-1);
    }
    public static void sendLike(Event event){
        if (getCurrentUser()!=null) {
            Favourites fav = new Favourites(getCurrentUser().getUid(), event.getId());
            mDatabaseReference.child("favourites").child(event.getId()+getCurrentUser().getUid()).setValue(fav);

            DatabaseReference ref = mFirebaseDatabase.getInstance().getReference("events").child(event.getId());
            ref.child("like").setValue(event.getLike()+1);
//            tv.setText(count + "");
        }else {
            //toast
        }
    }
    public static void verify(){
            MainActivity.getCurrentUser().sendEmailVerification();
    }
    public static void deletePost(final Event e) {
        DatabaseReference reference = mFirebaseDatabase.getInstance().getReference("events").child(e.getId());
        reference.getRef().removeValue();
        mDatabaseReference.child("favourites")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            Favourites fav = ds.getValue(Favourites.class);
                            if(fav.getEvent().equals(e.getId())){
                                DatabaseReference reference = ds.getRef();
                                reference.getRef().removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
    public static long getTime(String date, String time){
        DateFormat date1 = new SimpleDateFormat("dd.MM.yyyy HH : mm");
        long msStart = 0;
        try {
            Date nDate1 = date1.parse(date+" "+time);
            msStart=nDate1.getTime();
        }
        catch (Exception e){

        }

        return msStart;
    }
    public static long getDate(String date){
        DateFormat date1 = new SimpleDateFormat("dd.MM.yyyy");
        long msStart = 0;
        try {
            Date nDate1 = date1.parse(date);
            msStart=nDate1.getTime();
        }
        catch (Exception e){
        }
        return msStart;
    }

    public static String checkDate(long start,long end){
        Calendar calendar = new GregorianCalendar();
        long today = calendar.getTimeInMillis();
        long month = (long) 26784000*100;
    if(start>end){
            return "Дата конца должна быть позже начала";
        }
        else if(start < today){
            return "Событие не может начинаться ранее текущей даты";
        }
        else if(end - start > month){
            return "Событие не может длиться более месяца";
        }
        else {
            return "OK";
        }

    }
    public static long getEndToday(long start){
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(start);
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        return calendar.getTimeInMillis();
    }
    public static long getStartToday(long end){
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(end);
        calendar.set(Calendar.HOUR_OF_DAY,00);
        calendar.set(Calendar.MINUTE,00);
        return calendar.getTimeInMillis();
    }
    //with year
    public static String[] normalizeDate (long ms){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(ms);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        month ++;
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        int minute = cal.get(Calendar.MINUTE);

        String[] returning = {format(day)+"."+format(month)+"."+ cal.get(Calendar.YEAR),format(hour)+" : "+ format(minute)};

        return returning;
    }
    //without year
    public static String setTime(long ms){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(ms);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        month ++;
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        int minute = cal.get(Calendar.MINUTE);
        return format(day)+"."+format(month)+" "+format(hour)+" : "+ format(minute);
    }

    public static String format(int n){
        if(n<10)
            return "0"+n;
        else
            return ""+n;
    }
    //показываем загрузку
    public static void loading(RecyclerView recyclerView, ProgressBar circular_progress,
                               TextView tv_empty, ImageView iv_empty, boolean a, boolean b){
        if(a){
            circular_progress.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }
        else{
            circular_progress.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        if(b){
            tv_empty.setVisibility(View.INVISIBLE);
            iv_empty.setVisibility(View.INVISIBLE);
        }
        else{
            tv_empty.setVisibility(View.VISIBLE);
            iv_empty.setVisibility(View.VISIBLE);
        }
    }
    public static void isModerator(final LinearLayout block){

        mDatabaseReference.child("moderator")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            Moderator fav = ds.getValue(Moderator.class);
                            if(fav.getId().equals(User.getMail())){
                                block.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

}
