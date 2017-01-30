package com.touristadev.tourista.dataModels;

import java.util.ArrayList;

/**
 * Created by Christian on 1/11/2017.
 */

public class CustomizedPackage {
    private String packageName;
    private String packLanguage;
    private String packNumTourGuide;
    private String packNumPax;
    private String price;
    private ArrayList<Itinerary> packageItinerary;
    private int numberOfDays;
    private int packageImage;
    private String packDescription;
    private String startDate;
    public CustomizedPackage() {

    }

    public CustomizedPackage(String packageName, String packLanguage, String packNumTourGuide, String packNumPax, String price, ArrayList<Itinerary> packageItinerary, int numberOfDays, int packageImage, String packDescription, String startDate) {
        this.packageName = packageName;
        this.packLanguage = packLanguage;
        this.packNumTourGuide = packNumTourGuide;
        this.packNumPax = packNumPax;
        this.price = price;
        this.packageItinerary = packageItinerary;
        this.numberOfDays = numberOfDays;
        this.packageImage = packageImage;
        this.packDescription = packDescription;
        this.startDate = startDate;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackLanguage() {
        return packLanguage;
    }

    public void setPackLanguage(String packLanguage) {
        this.packLanguage = packLanguage;
    }

    public String getPackNumTourGuide() {
        return packNumTourGuide;
    }

    public void setPackNumTourGuide(String packNumTourGuide) {
        this.packNumTourGuide = packNumTourGuide;
    }

    public String getPackNumPax() {
        return packNumPax;
    }

    public void setPackNumPax(String packNumPax) {
        this.packNumPax = packNumPax;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public ArrayList<Itinerary> getPackageItinerary() {
        return packageItinerary;
    }

    public void setPackageItinerary(ArrayList<Itinerary> packageItinerary) {
        this.packageItinerary = packageItinerary;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public int getPackageImage() {
        return packageImage;
    }

    public void setPackageImage(int packageImage) {
        this.packageImage = packageImage;
    }

    public String getPackDescription() {
        return packDescription;
    }

    public void setPackDescription(String packDescription) {
        this.packDescription = packDescription;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
