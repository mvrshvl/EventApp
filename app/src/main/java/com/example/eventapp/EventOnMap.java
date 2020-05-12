package com.example.eventapp;

public class EventOnMap extends Event{
    private double x;

    private double y;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public EventOnMap(Event event,double x,double y){
        super(event.getType(),event.getName(),
                event.getAbout(),event.getPrice(),event.getPrice_kids(),event.getDate(),event.getDate_end(),event.getAddress(),event.getId(),event.getImages_path1(),
                event.getImages_path2(), event.getImages_path3(),event.getLike(),event.getUser(),event.getCity(),event.getState(),event.getDiss(),event.getKids_age(),event.getType_age());
        this.x = x;
        this.y = y;
    }

}
