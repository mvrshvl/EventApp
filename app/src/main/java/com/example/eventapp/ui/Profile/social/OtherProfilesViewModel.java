package com.example.eventapp.ui.Profile.social;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.eventapp.CircularTransformation;
import com.example.eventapp.Event;
import com.example.eventapp.MainActivity;
import com.example.eventapp.User;
import com.example.eventapp.ui.Profile.DataAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.eventapp.MainActivity.mDatabaseReference;


public class OtherProfilesViewModel extends ViewModel {
    public static List<Event> list_events;
    public static User user_global;
    private static MutableLiveData<List> listData;
   public OtherProfilesViewModel(){
        list_events = new ArrayList<>();
        listData = new MutableLiveData<>();
    }
    // TODO: Implement the ViewModel
    public  static void loadUser(String id, final TextView name, final TextView city, final ImageView photo,final List<Event> list){
        mDatabaseReference.child("user").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_global = dataSnapshot.getValue(User.class);
                if(list.isEmpty())
                addEventFirebaseListener();
                name.setText(user_global.name);
                city.setText(user_global.city);
                Picasso.get()
                        .load(user_global.photo)
                        .transform(new CircularTransformation(0))
                        .into(photo);
                list.addAll(list_events);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public static void setData(List<Event> list){

        listData.setValue(list);
    }
    public LiveData<List> getData() {
        return listData;
    }

    public static void addEventFirebaseListener() {

        mDatabaseReference.child("events")
                .addValueEventListener(new ValueEventListener() {
                    //если данные в БД меняются
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //проходим по всем записям и помещаем их в list_users в виде класса fav
                        list_events.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Event event = postSnapshot.getValue(Event.class);
                            String user = event.getUser();
                            if(user.equals(user_global.id)){
                                list_events.add(event);
                            }
                        }
                        setData(list_events);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });
    }

}
