package com.touristadev.tourista.models;

/**
 * Created by Shanyl Jimenez on 11/24/2016.
 */

public class Itinerary {

    private String startTime;
    private String endTime;
    private String spot;

    public Itinerary() {
    }

    public Itinerary(String startTime, String endTime, String spot) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.spot = spot;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSpot() {
        return spot;
    }

    public void setSpot(String spot) {
        this.spot = spot;
    }
}

