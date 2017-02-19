package com.touristadev.tourista.dataModels;

/**
 * Created by Christian on 2/6/2017.
 */

public class FBNotif {


    private String nameOfTourist;
    private String numberOfPerson;
    private String packageId;
    private String reserveDate;
    private String paymentforTG;
    private String notifType;
    private String userId;

    public FBNotif(String nameOfTourist, String numberOfPerson, String packageId, String reserveDate, String paymentforTG, String notifType, String userId) {
        this.nameOfTourist = nameOfTourist;
        this.numberOfPerson = numberOfPerson;
        this.packageId = packageId;
        this.reserveDate = reserveDate;
        this.paymentforTG = paymentforTG;
        this.notifType = notifType;
        this.userId = userId;
    }

    public String getNameOfTourist() {
        return nameOfTourist;
    }

    public void setNameOfTourist(String nameOfTourist) {
        this.nameOfTourist = nameOfTourist;
    }

    public String getNumberOfPerson() {
        return numberOfPerson;
    }

    public void setNumberOfPerson(String numberOfPerson) {
        this.numberOfPerson = numberOfPerson;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getReserveDate() {
        return reserveDate;
    }

    public void setReserveDate(String reserveDate) {
        this.reserveDate = reserveDate;
    }

    public String getPaymentforTG() {
        return paymentforTG;
    }

    public void setPaymentforTG(String paymentforTG) {
        this.paymentforTG = paymentforTG;
    }

    public String getNotifType() {
        return notifType;
    }

    public void setNotifType(String notifType) {
        this.notifType = notifType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
