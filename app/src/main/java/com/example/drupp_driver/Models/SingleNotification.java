package com.example.drupp_driver.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SingleNotification {


    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("driver_ride_id")
    @Expose
    private Integer driverRideId;
    @SerializedName("co_riders_preference")
    @Expose
    private Integer coRidersPreference;
    @SerializedName("pick_up_location")
    @Expose
    private String pickUpLocation;
    @SerializedName("pick_up_latitude")
    @Expose
    private String pickUpLatitude;
    @SerializedName("pick_up_longitude")
    @Expose
    private String pickUpLongitude;
    @SerializedName("type_of_driver")
    @Expose
    private String typeOfDriver;
    @SerializedName("preference")
    @Expose
    private String preference;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getDriverRideId() {
        return driverRideId;
    }

    public void setDriverRideId(Integer driverRideId) {
        this.driverRideId = driverRideId;
    }

    public Integer getCoRidersPreference() {
        return coRidersPreference;
    }

    public void setCoRidersPreference(Integer coRidersPreference) {
        this.coRidersPreference = coRidersPreference;
    }

    public String getPickUpLocation() {
        return pickUpLocation;
    }

    public void setPickUpLocation(String pickUpLocation) {
        this.pickUpLocation = pickUpLocation;
    }

    public String getPickUpLatitude() {
        return pickUpLatitude;
    }

    public void setPickUpLatitude(String pickUpLatitude) {
        this.pickUpLatitude = pickUpLatitude;
    }

    public String getPickUpLongitude() {
        return pickUpLongitude;
    }

    public void setPickUpLongitude(String pickUpLongitude) {
        this.pickUpLongitude = pickUpLongitude;
    }

    public String getTypeOfDriver() {
        return typeOfDriver;
    }

    public void setTypeOfDriver(String typeOfDriver) {
        this.typeOfDriver = typeOfDriver;
    }

    public String getPreference() {
        return preference;
    }

    public void setPreference(String preference) {
        this.preference = preference;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
