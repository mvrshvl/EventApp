package com.example.eventapp.ui.home;

import android.graphics.Point;
import android.view.Display;

import com.example.eventapp.Event;
import com.example.eventapp.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class filter {
public static List<Event> filtration(List<Event> l_events, HashMap<String,List> rules){
    List<Event> out_events = new ArrayList<>();
    for (Event event : l_events)
    {
        if(rules.get("type")!= null & rules.get("type").size()!=0){
            List<String> local_rule = rules.get("type");
            if(local_rule.get(0).equals(event.getType())){
                out_events.add(event);
            }
        }
        if(rules.get("price")!=null & rules.get("price").size()==2){
            List<Integer> local_rule = rules.get("price");
            if (event.getPrice() >= local_rule.get(0) & event.getPrice()<=local_rule.get(1))
                out_events.add(event);
        }
        if(rules.get("price_kids")!=null & rules.get("price_kids").size()==2){
            List<Integer> local_rule = rules.get("price_kids");
            if (event.getPrice_kids() >= local_rule.get(0) & event.getPrice_kids()<=local_rule.get(1))
                out_events.add(event);
        }
        if(rules.get("date")!=null & rules.get("date").size()==2){
            List<Integer> local_rule = rules.get("date");
            if (event.getDate() >= local_rule.get(0) & event.getDate_end()<=local_rule.get(1))
                out_events.add(event);
        }
    }
    return out_events;
    }
    public static List<Event> search(List <Event> events, String text){
        List<Event> out_events = new ArrayList<>();
        for(Event event : events){
            if(event.getName().matches("(.*)"+text+"(.*)"));
            out_events.add(event);
        }
        return out_events;
    }

}
