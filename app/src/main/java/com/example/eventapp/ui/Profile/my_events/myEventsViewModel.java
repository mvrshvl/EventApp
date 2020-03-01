package com.example.eventapp.ui.Profile.my_events;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.eventapp.Event;
import com.example.eventapp.MainActivity;
import com.example.eventapp.ui.Profile.DataAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.eventapp.MainActivity.mDatabaseReference;
import static com.example.eventapp.ui.Profile.my_events.myEvents.activity;
import static com.example.eventapp.ui.Profile.my_events.myEvents.is_loaded;
import static com.example.eventapp.ui.Profile.my_events.myEvents.recyclerView;


public class myEventsViewModel extends ViewModel {
    private static MutableLiveData<List> listData;
    private List<Event> list_events;
    private String currentUser;


    public myEventsViewModel() {
        listData = new MutableLiveData<>();
        list_events = new ArrayList<>();
        currentUser = MainActivity.getCurrentUser().getUid();
        addEventFirebaseListener();

    }
    public static void setData(List<Event> list){

        listData.setValue(list);
    }

    public LiveData<List> getText() {
        return listData;
    }

    private void addEventFirebaseListener() {

        mDatabaseReference.child("events")
                .addValueEventListener(new ValueEventListener() {
                    //если данные в БД меняются
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (list_events.size() > 0) {
                            list_events.clear();
                        }
                        //проходим по всем записям и помещаем их в list_users в виде класса fav
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Event event = postSnapshot.getValue(Event.class);
                            String user = event.getUser();
                            if(user.equals(currentUser)){
                                list_events.add(event);
                            }
                        }
                        if(!is_loaded) {
                            //публикуем данные в ListView
                            DataAdapter adapter = new DataAdapter(activity,list_events);
                            DataAdapter.type_screen = false;
                            recyclerView.setAdapter(adapter);
                            setData(list_events);
                            is_loaded=true;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });
    }


}
