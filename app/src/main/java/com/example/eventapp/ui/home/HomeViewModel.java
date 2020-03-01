package com.example.eventapp.ui.home;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.eventapp.Event;
import com.example.eventapp.MainActivity;
import com.example.eventapp.User;
import com.example.eventapp.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import static com.example.eventapp.MainActivity.mDatabaseReference;
import static com.example.eventapp.ui.home.HomeFragment.is_loaded;
import static com.example.eventapp.ui.home.HomeFragment.recyclerView;

public class HomeViewModel extends ViewModel {

    private static MutableLiveData<List> listData;
    private static HashMap<String,List> filter_hash;
    private static List<Event> list_events= new ArrayList<>();
    private static boolean price =false;
    private static boolean price_kids = false;
    private static boolean today = false;



    public HomeViewModel() {
        listData = new MutableLiveData<>();
        filter_hash = new HashMap<>();
        //List<List> list_hm = new ArrayList<List>();
    }

    public static HashMap<String, List> getFilter_hash() {
        return filter_hash;
    }

    public static void setFilter_hash(HashMap<String, List> filter_hash) {
        HomeViewModel.filter_hash = filter_hash;
    }

    public static void setData(List<Event> list){
        listData.setValue(list);
    }

    public LiveData<List> getData() {
        return listData;
    }

    public static boolean isPrice() {
        return price;
    }

    public static void setPrice(boolean price) {
        HomeViewModel.price = price;
    }
    public static boolean isPrice_kids() {
        return price_kids;
    }

    public static void setPrice_kids(boolean price_kids) {
        HomeViewModel.price_kids = price_kids;
    }
    public static boolean isToday() {
        return today;
    }

    public static void setToday(boolean today) {
        HomeViewModel.today = today;
    }

    public static void addEventFirebaseListener() {
        //показываем View загрузки

        mDatabaseReference.child("events")
                .addValueEventListener(new ValueEventListener() {
                    //если данные в БД меняются
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (list_events.size() > 0) {
                            list_events.clear();
                        }
                        //проходим по всем записям и помещаем их в list_users в виде класса User
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Event event = postSnapshot.getValue(Event.class);
                            //Удаляем устаревшие
                            Calendar calendar = new GregorianCalendar();
                            long today = calendar.getTimeInMillis();
                            if(event.getDate_end()<today)
                                Utils.deletePost(event);
                            else if(event.getCity().equals(User.getCity()))
                                list_events.add(event);
                        }
                        if(!is_loaded) {
                            //публикуем данные в ListView


                            setData(list_events);
                            recyclerView.scrollToPosition(MainActivity.recyclerPosition);
                            //убираем View загрузки

                            is_loaded=true;
                        }
//                        if(list_events.size()!=0)
//                        HomeFragment.loading(false,true);
//                        else
//                            HomeFragment.loading(false,false);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });
    }


}