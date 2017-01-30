package com.touristadev.tourista.models;

public final class CurrentUser {
    public static String name;
    public static String email;
    public static String userFacebookId;
    public static String userFirebaseId;
    public static String photoUrl;
    public static boolean toggle;

    public CurrentUser() {
    }

    public CurrentUser(String name, String email, String userFacebookId, String userFirebaseId, String photoUrl) {
        this.name = name;
        this.email = email;
        this.userFacebookId = userFacebookId;
        this.userFirebaseId = userFirebaseId;
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUserFacebookId() {
        return userFacebookId;
    }

    public String getUserFirebaseId() {
        return userFirebaseId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserFacebookId(String userFacebookId) {
        this.userFacebookId = userFacebookId;
    }

    public void setUserFirebaseId(String userFirebaseId) {
        this.userFirebaseId = userFirebaseId;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
