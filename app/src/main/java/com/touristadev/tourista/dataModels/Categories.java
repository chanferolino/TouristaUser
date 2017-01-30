package com.touristadev.tourista.dataModels;

/**
 * Created by Shanyl Jimenez on 11/26/2016.
 */

public class Categories {

    String categoryName;

    public Categories() {
    }

    public Categories(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
