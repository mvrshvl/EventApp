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

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import static com.example.eventapp.MainActivity.mDatabaseReference;
import static com.example.eventapp.MainActivity.moderator_mode;
import static com.example.eventapp.ui.home.HomeFragment.is_loaded;
import static com.example.eventapp.ui.home.HomeFragment.recyclerView;

public class HomeViewModel extends ViewModel {

    private static MutableLiveData<List> listData;
    private static HashMap<String,List> filter_hash;
    private static List<Event> list_events= new ArrayList<>();
    private static String order = "date";
    //переменные фильтра
    static int type_index;
    static String type;
    static long dateStart;
    static long dateEnd ;
    static int priceMin;
    static int priceMax;
    static int priceMin_k;
    static int priceMax_k;
    static boolean relevant = false;
    //переменные экрана фильтра для его восстановления
    static String minPrice_et = "";
    static String maxPrice_et = "";
    static String minPrice_k_et = "";
    static String maxPrice_k_et = "";
    static String dateSt_b = "Начало";
    static String dateEn_b = "Конец";
    static boolean price_cb =false;
    static boolean price_kids_cb = false;
    static boolean today_cb = false;



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

    public static LiveData<List> getData() {
        return listData;
    }

    public static void setOrder(String ord){
        order = orderType(ord);
        is_loaded = false;
        addEventFirebaseListener();
    }

    public static void addEventFirebaseListener() {
        //показываем View загрузки
        mDatabaseReference.child("events")
                .orderByChild(order)
                .addValueEventListener(new ValueEventListener() {
                    //если данные в БД меняются
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (list_events.size() > 0) {
                            list_events.clear();
                        }
                        //проходим по всем записям и помещаем их в list_users в виде класса User
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            relevant = true;
                            Event event = postSnapshot.getValue(Event.class);
                            //Удаляем устаревшие
                            Calendar calendar = new GregorianCalendar();
                            long today = calendar.getTimeInMillis();
                            if(event.getDate_end()<today)
                                Utils.deletePost(event);
                            else if(event.getCity().equals(User.getCity())) {
                                //Фильтруем

                                if (type_index > 0 & event.getType().equals(type))
                                    relevant = true;
                                else if (type_index == 0)
                                    relevant = true;
                                else relevant = false;
                                if (event.getPrice() >= priceMin & event.getPrice() <= priceMax & relevant)
                                    relevant = true;
                                else relevant = false;
                                if (event.getPrice_kids() >= priceMin_k & event.getPrice_kids() <= priceMax_k & relevant)
                                    relevant = true;
                                else relevant = false;
                                if (event.getDate() >= dateStart & event.getDate() <= dateEnd & relevant)
                                    relevant = true;
                                else relevant = false;
                                //режим модератора
                                if(event.getState()==1 && relevant) relevant = true;
                                else{
                                    if(moderator_mode & event.getState() < 3 & relevant)
                                        relevant = true;
                                    else relevant = false;

                                }
                                if (relevant)
                                    list_events.add(event);
                            }
                        }
                        if(!is_loaded) {
                            //публикуем данные в ListView
                            //делаем список по возрастанию если этой по лайкам
                            //во избежание страшного нагромождения динамики(((
                            if(order == "like")
                                Collections.reverse(list_events);
                            setData(list_events);
                            recyclerView.scrollToPosition(MainActivity.recyclerPosition);
                            //убираем View загрузки

                            is_loaded=true;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });
    }

    public static String orderType(String type){
        String tmp = "date";
        switch (type){
            case "По дате":
                tmp = "date";
                break;
            case "По популярности":
                tmp = "like";
                break;
            case "По цене":
                tmp = "price";
                break;
        }
        return tmp;
    }
     public static int getOrderIndex(){
         switch (order){
             case "date":
                 return  0;
             case "like":
                 return  1;
             case "price":
                return 2;
             default:
                return 0;
         }
     }



}