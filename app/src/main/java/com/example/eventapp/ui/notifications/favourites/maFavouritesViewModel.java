package com.example.eventapp.ui.notifications.favourites;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.eventapp.Event;
import com.example.eventapp.Favourites;
import com.example.eventapp.MainActivity;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.eventapp.MainActivity.mDatabaseReference;
import static com.example.eventapp.ui.notifications.favourites.myFavourites.activity;
import static com.example.eventapp.ui.notifications.favourites.myFavourites.circular_progress;
import static com.example.eventapp.ui.notifications.favourites.myFavourites.is_loaded;
import static com.example.eventapp.ui.notifications.favourites.myFavourites.loading;
import static com.example.eventapp.ui.notifications.favourites.myFavourites.recyclerView;

public class maFavouritesViewModel extends ViewModel {
    private MutableLiveData<List> listData;
    private List<Favourites> list_events;
    private String currentUser;


    public maFavouritesViewModel() {
        listData = new MutableLiveData<>();
        list_events = new ArrayList<>();
        currentUser = MainActivity.getCurrentUser().getUid();
        addEventFirebaseListener();

    }
    public void setData(List<Favourites> list){

        listData.setValue(list);
    }

    public LiveData<List> getText() {
        return listData;
    }

    private void addEventFirebaseListener() {
        //показываем View загрузки

        loading(true);

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
                        if(!is_loaded) {
                            //публикуем данные в ListView
                            DataAdapter adapter = new DataAdapter(activity, list_events);
                            recyclerView.setAdapter(adapter);
                            setData(list_events);
                            //убираем View загрузки
                            loading(false);
                            is_loaded=true;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });
    }
}
