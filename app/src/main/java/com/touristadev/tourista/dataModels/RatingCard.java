package com.touristadev.tourista.dataModels;

/**
 * Created by Christian on 1/3/2017.
 */

public class RatingCard {
    private String name;
    private int rating;
    private int image;

    public RatingCard(String name, int rating, int image) {
        this.name = name;
        this.rating = rating;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
