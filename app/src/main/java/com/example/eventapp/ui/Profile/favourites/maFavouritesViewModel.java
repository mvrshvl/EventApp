package com.example.eventapp.ui.Profile.favourites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.eventapp.Event;
import com.example.eventapp.Favourites;
import com.example.eventapp.MainActivity;
import com.example.eventapp.ui.Profile.DataAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.eventapp.MainActivity.mDatabaseReference;
import static com.example.eventapp.MainActivity.mFirebaseDatabase;
import static com.example.eventapp.ui.Profile.favourites.myFavourites.activity;
import static com.example.eventapp.ui.Profile.favourites.myFavourites.is_loaded;
import static com.example.eventapp.ui.Profile.favourites.myFavourites.recyclerView;

public class maFavouritesViewModel extends ViewModel {
    private static MutableLiveData<List> listData;
    private static List<Favourites> list_events;
    private List<Event> events;
    private String currentUser;


    public maFavouritesViewModel() {
        listData = new MutableLiveData<>();
        list_events = new ArrayList<>();
        currentUser = MainActivity.getCurrentUser().getUid();
        addEventFirebaseListener();

    }
    public static void setData(List<Event> list){

        listData.setValue(list);
    }
    public static List<Favourites> getFavData(){
        return list_events;
    }

    public LiveData<List> getText() {
        return listData;
    }

    private void addEventFirebaseListener() {
        //показываем View загрузки
        mDatabaseReference.child("favourites")
                .addValueEventListener(new ValueEventListener() {
                    //если данные в БД меняются
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (list_events.size() > 0) {
                            list_events.clear();
                        }
                        //проходим по всем записям и помещаем их в list_users в виде класса User
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Favourites favourites = postSnapshot.getValue(Favourites.class);
                            String user = favourites.getUser();
                            if(user.equals(currentUser)){
                                list_events.add(favourites);
                            }
                        }


                        mDatabaseReference=mFirebaseDatabase.getReference();
                        events = new ArrayList<>();
                        for (Favourites fav :list_events){
                            String eventId = fav.getEvent();

                            mDatabaseReference.child("events").orderByChild("id").equalTo(eventId).addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    events.add(dataSnapshot.getValue(Event.class));
                                    setData(events);
                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                }

                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                                }

                                @Override
                                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                        }

                        if(!is_loaded) {



                            //публикуем данные в ListView
                            DataAdapter adapter = new DataAdapter(activity, events);
                            DataAdapter.type_screen = true;
                            setData(events);

                            recyclerView.setAdapter(adapter);
                            is_loaded=true;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });
    }
}
