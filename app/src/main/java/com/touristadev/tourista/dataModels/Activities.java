package com.touristadev.tourista.dataModels;

/**
 * Created by Christian on 12/2/2016.
 */

public class Activities {
    private String activityName;

    public Activities(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
}
