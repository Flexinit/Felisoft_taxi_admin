package com.example.drupp_driver.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostRideInfo {
    @SerializedName("co-riders_preference")
    @Expose
    private String coRidersPreference;
    @SerializedName("preference")
    @Expose
    private String preference;
    @SerializedName("pick_up_location")
    @Expose
    private String pickUpLocation;
    @SerializedName("type_of_driver")
    @Expose
    private String typeOfDriver;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("request_ride_id")
    @Expose
    private String requestRideId;

    public String getCoRidersPreference() {
        return coRidersPreference;
    }

    public void setCoRidersPreference(String coRidersPreference) {
        this.coRidersPreference = coRidersPreference;
    }

    public String getPreference() {
        return preference;
    }

    public void setPreference(String preference) {
        this.preference = preference;
    }

    public String getPickUpLocation() {
        return pickUpLocation;
    }

    public void setPickUpLocation(String pickUpLocation) {
        this.pickUpLocation = pickUpLocation;
    }

    public String getTypeOfDriver() {
        return typeOfDriver;
    }

    public void setTypeOfDriver(String typeOfDriver) {
        this.typeOfDriver = typeOfDriver;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequestRideId() {
        return requestRideId;
    }

    public void setRequestRideId(String requestRideId) {
        this.requestRideId = requestRideId;
    }
}
