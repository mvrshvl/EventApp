package com.example.eventapp.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.eventapp.Event;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<List> listData;



    private static boolean price =false;
    private static boolean price_kids = false;
    private static boolean today = false;



    public HomeViewModel() {
        listData = new MutableLiveData<>();
        //List<List> list_hm = new ArrayList<List>();
    }
    public void setData(List<Event> list){
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
}