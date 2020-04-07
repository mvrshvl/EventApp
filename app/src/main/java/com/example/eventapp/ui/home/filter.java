package com.example.eventapp.ui.home;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.eventapp.Event;
import com.example.eventapp.MainActivity;
import com.example.eventapp.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import static com.example.eventapp.ui.Profile.add.AddEventViewModel.getDate;

public class filter extends HomeViewModel{


    public static List<Event> goSearch(List<Event> events, String text) {
        List<Event> out_events = new ArrayList<>();
        for (Event event : events) {
            if (event.getName().matches("(.*)" + text + "(.*)"))
                out_events.add(event);
        }
        return out_events;
    }

    public static int getType(String[] array, String type) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(type)) return i;
        }
        return 0;
    }
    public static void clearFilter(){
        type = "Тип";
        type_index = 0;
        dateStart = Long.MIN_VALUE;
        dateEnd = Long.MAX_VALUE;
        priceMin = 0;
        priceMax = Integer.MAX_VALUE;
        priceMin_k = priceMin;
        priceMax_k = priceMax;

        minPrice_et = "";
        maxPrice_et = "";
        minPrice_k_et = "";
        maxPrice_k_et = "";
        price_cb = false;
        price_kids_cb = false;
        dateSt_b = "Начало";
        dateEn_b = "Конец";
        today_cb = false;

    }
    public static  void setType(String s,String[] array){
        if(!s.equals("Тип")) {
            type_index = getType(array, s);
            type = s;
        }
        else {
            type = "Тип";
            type_index = 0;
        }
    }
    public static void setPrice(boolean free,boolean free_k,String min,String max,String min_k,String max_k){
        //запоминаем фильтры
        minPrice_et =min;
        maxPrice_et =max;
        minPrice_k_et = min_k;
        maxPrice_k_et = max_k;
        price_cb = free;
        price_kids_cb = free_k;

        if(free)
        {
            priceMin = 0;
            priceMax = 0;
            priceMin_k = 0;
            priceMax_k = 0;
        }
        else{
            if(min.isEmpty()) priceMin = 0;
            else priceMin = Integer.parseInt(min);

            if(max.isEmpty()) priceMax = Integer.MAX_VALUE;
            else priceMax = Integer.parseInt(max);

            if(free_k){
                priceMin_k = Integer.MIN_VALUE;
                priceMax_k = Integer.MAX_VALUE;
            }
            else{
                if(min_k.isEmpty()) priceMin_k = 0;
                else priceMin_k = Integer.parseInt(min_k);

                if(min.isEmpty()) priceMax_k = Integer.MAX_VALUE;
                else priceMax_k = Integer.parseInt(max_k);
            }
        }

    }

    public static void setDate(boolean today,String min,String max){
        dateSt_b = min;
        dateEn_b = max;
        today_cb = today;
        if(today){
            Calendar calendar = new GregorianCalendar();
            dateStart = Utils.getStartToday(calendar.getTimeInMillis());
            dateEnd = Utils.getEndToday(calendar.getTimeInMillis());
        }else{
            if(min.isEmpty() || min.toLowerCase().equals("начало")) dateStart = Long.MIN_VALUE;
            else dateStart = Utils.getDate(min);

            if(max.isEmpty() || max.toLowerCase().equals("конец")) dateEnd = Long.MAX_VALUE;
            else dateEnd = Utils.getDate(max);
        }

    }
}
