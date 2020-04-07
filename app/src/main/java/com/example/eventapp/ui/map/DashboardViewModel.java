package com.example.eventapp.ui.map;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.eventapp.Event;
import com.example.eventapp.EventOnMap;
import com.example.eventapp.User;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class DashboardViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private static List<EventOnMap> coord;

    public DashboardViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
    public static List<EventOnMap> getCoords(List<Event> events, Context context){
        coord = new ArrayList<>();

        for(Event e :events){
            Geocoder geocoder = new Geocoder(context);
            List<Address> add;
            double x = 0.0;
            double y = 0.0;
            try {
                add = geocoder.getFromLocationName(e.getCity()+"," + e.getAddress(),1);
                if(add.size() > 0) {
                    x = add.get(0).getLatitude();
                    y = add.get(0).getLongitude();
                }
            }catch (Exception exc){

            }
            EventOnMap eom = new EventOnMap(e,x,y);
            coord.add(eom);
        }
        return coord;
    }
    public static LatLng getCamera(Context context){
        List<Address> add;
        Geocoder geocoder = new Geocoder(context);
        LatLng xy = new LatLng(0.0,0.0);
        try {
            add = geocoder.getFromLocationName(User.getCity(),1);
            if(add.size() > 0) {
                xy = new LatLng(add.get(0).getLatitude(),add.get(0).getLongitude());
            }
        }catch (Exception exc){

        }
        return xy;
    }
}