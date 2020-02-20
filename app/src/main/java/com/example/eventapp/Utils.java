package com.example.eventapp;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.eventapp.MainActivity.getCurrentUser;
import static com.example.eventapp.MainActivity.mDatabaseReference;
import static com.example.eventapp.MainActivity.mFirebaseDatabase;

public class Utils {
    public static List<Event> list_event;

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
}
