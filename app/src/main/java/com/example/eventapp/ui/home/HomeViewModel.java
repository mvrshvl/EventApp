package com.example.eventapp.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.eventapp.Event;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<List> listData;

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
}