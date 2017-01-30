package com.touristadev.tourista.dataModels;

/**
 * Created by Shanyl Jimenez on 11/30/2016.
 */

public class TourRequest {

    private String RequestTouristID;
    private String RequestStartDate;
    private int RequestNumberOfGuest;
    private TouristaPackages RequestPackage;
    private String RequestCarType;


    public TourRequest() {
    }

    public TourRequest(String requestTouristID, String requestStartDate, int requestNumberOfGuest, TouristaPackages requestPackageID, String requestCarType) {
        RequestTouristID = requestTouristID;
        RequestStartDate = requestStartDate;
        RequestNumberOfGuest = requestNumberOfGuest;
        RequestPackage = requestPackageID;
        RequestCarType = requestCarType;
    }

    public String getRequestTouristID() {
        return RequestTouristID;
    }

    public void setRequestTouristID(String requestTouristID) {
        RequestTouristID = requestTouristID;
    }

    public String getRequestStartDate() {
        return RequestStartDate;
    }

    public void setRequestStartDate(String requestStartDate) {
        RequestStartDate = requestStartDate;
    }

    public int getRequestNumberOfGuest() {
        return RequestNumberOfGuest;
    }

    public void setRequestNumberOfGuest(int requestNumberOfGuest) {
        RequestNumberOfGuest = requestNumberOfGuest;
    }

    public TouristaPackages getRequestPackage() {
        return RequestPackage;
    }

    public void setRequestPackage(TouristaPackages requestPackage) {
        RequestPackage = requestPackage;
    }

    public String getRequestCarType() {
        return RequestCarType;
    }

    public void setRequestCarType(String requestCarType) {
        RequestCarType = requestCarType;
    }
}

