package com.touristadev.tourista.models;

/**
 * Created by Christian on 11/25/2016.
 */

public class ExploreCard {
    private String Title;
    private int Rating;
    private String Price;
    private String NoSpots;
    private String NoHours; //Date
    private String type;
    private int imgView;
    private String CompanyName;

    public ExploreCard(String title, int rating, String price, String noSpots, String noHours, String type, int imgView, String companyName) {
        Title = title;
        Rating = rating;
        Price = price;
        NoSpots = noSpots;
        NoHours = noHours;
        this.type = type;
        this.imgView = imgView;
        CompanyName = companyName;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getRating() {
        return Rating;
    }

    public void setRating(int rating) {
        Rating = rating;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getNoSpots() {
        return NoSpots;
    }

    public void setNoSpots(String noSpots) {
        NoSpots = noSpots;
    }

    public String getNoHours() {
        return NoHours;
    }

    public void setNoHours(String noHours) {
        NoHours = noHours;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getImgView() {
        return imgView;
    }

    public void setImgView(int imgView) {
        this.imgView = imgView;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }
}
