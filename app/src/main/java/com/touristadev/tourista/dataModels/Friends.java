package com.touristadev.tourista.dataModels;

/**
 * Created by Christian on 1/18/2017.
 */

public class Friends {
    private String userId;
    private String facebookId;

    public Friends(String userId, String facebookId) {
        this.userId = userId;
        this.facebookId = facebookId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }
}
