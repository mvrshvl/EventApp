package com.example.eventapp.ui.home;

import android.graphics.Point;
import android.view.Display;

import com.example.eventapp.Event;
import com.example.eventapp.MainActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.example.eventapp.ui.Profile.add.AddEventViewModel.getDate;

public class filter {
public static List<Event> filtration(List<Event> l_events, HashMap<String,List> rules){
    List<Event> out_events = new ArrayList<>();
    boolean flag = false;
    for (Event event : l_events)
    {
        if(rules.get("type")!= null & rules.get("type").size()!=0){
            List<String> local_rule = rules.get("type");
            if(local_rule.get(0).equals(event.getType())) flag = true;
            else continue;
        }
        if(rules.get("price")!=null & rules.get("price").size()==2){
            List<Integer> local_rule = rules.get("price");
            if (event.getPrice() >= (int)local_rule.get(0) & event.getPrice()<= (int) local_rule.get(1))
                flag = true;
            else continue;
        }
        if(rules.get("price_kids")!=null & rules.get("price_kids").size()==2){
            List<Integer> local_rule = rules.get("price_kids");
            if (event.getPrice_kids() >= local_rule.get(0) & event.getPrice_kids()<=local_rule.get(1))
                flag = true;
            else continue;
        }
        if(rules.get("date")!=null & rules.get("date").size()==2){
            List<Long> local_rule = rules.get("date");
            Calendar calendar = new Calendar() {
                @Override
                protected void computeTime() {

                }

                @Override
                protected void computeFields() {

                }

                @Override
                public void add(int field, int amount) {

                }

                @Override
                public void roll(int field, boolean up) {

                }

                @Override
                public int getMinimum(int field) {
                    return 0;
                }

                @Override
                public int getMaximum(int field) {
                    return 0;
                }

                @Override
                public int getGreatestMinimum(int field) {
                    return 0;
                }

                @Override
                public int getLeastMaximum(int field) {
                    return 0;
                }
            };
            calendar.setTimeInMillis(event.getDate());
            DateFormat date1 = new SimpleDateFormat("dd.MM.yyyy HH : mm");

            long l2 = local_rule.get(0);
            long l1 = event.getDate();
            long l3 = local_rule.get(1);
            if(l1>l2){
                l2 = l1;
            }
            if(l1<l3){
                l3=l1;
            }
            if (event.getDate() >= local_rule.get(0) & event.getDate_end()<=local_rule.get(1))
                flag = true;
            else continue;
        }
        if (flag) out_events = distinct(out_events,event);
    }

    return out_events;
    }

    private static List <Event> distinct(List <Event> events,Event event){
        boolean flag = false;
        if(events.size()==0)
        {
            events.add(event);
        }
        else {
            for (Event check_event : events) {
                if (event.getId().equals(check_event.getId())) flag = true;
            }
            if(!flag) events.add(event);
        }
        return events;
    }

    public static List<Event> goSearch(List <Event> events, String text){
        List<Event> out_events = new ArrayList<>();
        for(Event event : events){
            if(event.getName().matches("(.*)"+text+"(.*)"))
                out_events.add(event);
        }
        return out_events;
    }

}
