package com.touristadev.tourista.dataModels;

/**
 * Created by Shanyl Jimenez on 11/26/2016.
 */

public class Itinerary {

    private String  spotID;
    private String startTime;
    private String endTime;
    private String  spotName;

    public Itinerary() {
    }

    public Itinerary(String spotID, String startTime, String endTime, String spotName) {
        this.spotID = spotID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.spotName = spotName;
    }

    public String getSpotID() {
        return spotID;
    }

    public void setSpotID(String spotID) {
        this.spotID = spotID;
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

    public String getSpotName() {
        return spotName;
    }

    public void setSpotName(String spotName) {
        this.spotName = spotName;
    }
}
