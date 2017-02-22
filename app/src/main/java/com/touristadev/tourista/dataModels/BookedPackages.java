package com.touristadev.tourista.dataModels;

import java.util.ArrayList;

/**
 * Created by Shanyl Jimenez on 11/26/2016.
 */

public class BookedPackages {
    private String packageId;
    private String packageName;
    private ArrayList<Itinerary> packageItinerary;
    private String packageTourGuideClassification;
    private int rating;
    private int packageNoOfSpots;
    private int packageTotalNoOfHours;
    private int packageImage;
    private ArrayList<Spots > spotItinerary= new ArrayList<>();
    private String packDescription;
    private String packPrice;
    private String date;
    private String CompanyName;
    private String tourTransactionId;

    public BookedPackages() {
    }

    public BookedPackages(String packageId, String packageName, ArrayList<Itinerary> packageItinerary, String packageTourGuideClassification, int rating, int packageNoOfSpots, int packageTotalNoOfHours, int packageImage, ArrayList<Spots> spotItinerary, String packDescription, String packPrice, String date, String companyName, String tourTransactionId) {
        this.packageId = packageId;
        this.packageName = packageName;
        this.packageItinerary = packageItinerary;
        this.packageTourGuideClassification = packageTourGuideClassification;
        this.rating = rating;
        this.packageNoOfSpots = packageNoOfSpots;
        this.packageTotalNoOfHours = packageTotalNoOfHours;
        this.packageImage = packageImage;
        this.spotItinerary = spotItinerary;
        this.packDescription = packDescription;
        this.packPrice = packPrice;
        this.date = date;
        CompanyName = companyName;
        this.tourTransactionId = tourTransactionId;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public ArrayList<Itinerary> getPackageItinerary() {
        return packageItinerary;
    }

    public void setPackageItinerary(ArrayList<Itinerary> packageItinerary) {
        this.packageItinerary = packageItinerary;
    }

    public String getPackageTourGuideClassification() {
        return packageTourGuideClassification;
    }

    public void setPackageTourGuideClassification(String packageTourGuideClassification) {
        this.packageTourGuideClassification = packageTourGuideClassification;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getPackageNoOfSpots() {
        return packageNoOfSpots;
    }

    public void setPackageNoOfSpots(int packageNoOfSpots) {
        this.packageNoOfSpots = packageNoOfSpots;
    }

    public int getPackageTotalNoOfHours() {
        return packageTotalNoOfHours;
    }

    public void setPackageTotalNoOfHours(int packageTotalNoOfHours) {
        this.packageTotalNoOfHours = packageTotalNoOfHours;
    }

    public int getPackageImage() {
        return packageImage;
    }

    public void setPackageImage(int packageImage) {
        this.packageImage = packageImage;
    }

    public ArrayList<Spots> getSpotItinerary() {
        return spotItinerary;
    }

    public void setSpotItinerary(ArrayList<Spots> spotItinerary) {
        this.spotItinerary = spotItinerary;
    }

    public String getPackDescription() {
        return packDescription;
    }

    public void setPackDescription(String packDescription) {
        this.packDescription = packDescription;
    }

    public String getPackPrice() {
        return packPrice;
    }

    public void setPackPrice(String packPrice) {
        this.packPrice = packPrice;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getTourTransactionId() {
        return tourTransactionId;
    }

    public void setTourTransactionId(String tourTransactionId) {
        this.tourTransactionId = tourTransactionId;
    }
}