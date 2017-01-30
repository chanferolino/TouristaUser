package com.touristadev.tourista.dataModels;

/**
 * Created by Christian on 1/4/2017.
 */

public class FBfriends {
    private String id;

    private String name;

    public FBfriends(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
