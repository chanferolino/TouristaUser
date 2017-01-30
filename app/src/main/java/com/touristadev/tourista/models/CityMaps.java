package com.touristadev.tourista.models;

import android.widget.RatingBar;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Christian on 11/20/2016.
 */

public class CityMaps {

    private String CityName;
    private LatLng latlng;
    private int Rating;
    private String Description;
    private int NoSpots;

    public CityMaps(String cityName, LatLng latlng, int rating, String description, int noSpots) {
        CityName = cityName;
        this.latlng = latlng;
        Rating = rating;
        Description = description;
        NoSpots = noSpots;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }

    public int getRating() {
        return Rating;
    }

    public void setRating(int rating) {
        Rating = rating;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getNoSpots() {
        return NoSpots;
    }

    public void setNoSpots(int noSpots) {
        NoSpots = noSpots;
    }
}
