package com.touristadev.tourista.models;

/**
 * Created by Christian on 12/26/2016.
 */

public class TourGuideModel {
    private String tgName;
    private int tgImage;
    private String tgMotto;
    private int tgStars;
    private String tgAge;

    public TourGuideModel(String tgName, int tgImage, String tgMotto, int tgStars, String tgAge) {
        this.tgName = tgName;
        this.tgImage = tgImage;
        this.tgMotto = tgMotto;
        this.tgStars = tgStars;
        this.tgAge = tgAge;
    }

    public TourGuideModel(TourGuideModel tourGuideModel) {
    }

    public String getTgName() {
        return tgName;
    }

    public void setTgName(String tgName) {
        this.tgName = tgName;
    }

    public int getTgImage() {
        return tgImage;
    }

    public void setTgImage(int tgImage) {
        this.tgImage = tgImage;
    }

    public String getTgMotto() {
        return tgMotto;
    }

    public void setTgMotto(String tgMotto) {
        this.tgMotto = tgMotto;
    }

    public int getTgStars() {
        return tgStars;
    }

    public void setTgStars(int tgStars) {
        this.tgStars = tgStars;
    }

    public String getTgAge() {
        return tgAge;
    }

    public void setTgAge(String tgAge) {
        this.tgAge = tgAge;
    }
}
