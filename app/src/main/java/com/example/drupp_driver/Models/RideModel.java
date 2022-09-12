package com.example.drupp_driver.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RideModel {

    @SerializedName("id")
    @Expose
    private Integer Id;
    @SerializedName("ride_id")
    @Expose
    private Integer rideId;
    @SerializedName("driver_id")
    @Expose
    private Integer driverId;
    @SerializedName("driver_fare")
    @Expose
    private String driverFare;
    @SerializedName("ride_type")
    @Expose
    private Integer rideType;
    @SerializedName("passengers_preference")
    @Expose
    private Integer passengersPreference;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("source_latitude")
    @Expose
    private String sourceLatitude;
    @SerializedName("source_longitude")
    @Expose
    private String sourceLongitude;
    @SerializedName("destination")
    @Expose
    private String destination;
    @SerializedName("destination_latitude")
    @Expose
    private String destinationLatitude;
    @SerializedName("destination_longitude")
    @Expose
    private String destinationLongitude;
    @SerializedName("ride_date")
    @Expose
    private String rideDate;
    @SerializedName("user_id")
    @Expose
    private Object userId;
    @SerializedName("type_of_driver")
    @Expose
    private Object typeOfDriver;
    @SerializedName("co_riders_preference")
    @Expose
    private Object coRidersPreference;
    @SerializedName("pick_up_location")
    @Expose
    private Object pickUpLocation;
    @SerializedName("preference")
    @Expose
    private Object preference;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Integer getRideId() {
        return rideId;
    }

    public void setRideId(Integer rideId) {
        this.rideId = rideId;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public String getDriverFare() {
        return driverFare;
    }

    public void setDriverFare(String driverFare) {
        this.driverFare = driverFare;
    }

    public Integer getRideType() {
        return rideType;
    }

    public void setRideType(Integer rideType) {
        this.rideType = rideType;
    }

    public Integer getPassengersPreference() {
        return passengersPreference;
    }

    public void setPassengersPreference(Integer passengersPreference) {
        this.passengersPreference = passengersPreference;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceLatitude() {
        return sourceLatitude;
    }

    public void setSourceLatitude(String sourceLatitude) {
        this.sourceLatitude = sourceLatitude;
    }

    public String getSourceLongitude() {
        return sourceLongitude;
    }

    public void setSourceLongitude(String sourceLongitude) {
        this.sourceLongitude = sourceLongitude;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestinationLatitude() {
        return destinationLatitude;
    }

    public void setDestinationLatitude(String destinationLatitude) {
        this.destinationLatitude = destinationLatitude;
    }

    public String getDestinationLongitude() {
        return destinationLongitude;
    }

    public void setDestinationLongitude(String destinationLongitude) {
        this.destinationLongitude = destinationLongitude;
    }

    public String getRideDate() {
        return rideDate;
    }

    public void setRideDate(String rideDate) {
        this.rideDate = rideDate;
    }

    public Object getUserId() {
        return userId;
    }

    public void setUserId(Object userId) {
        this.userId = userId;
    }

    public Object getTypeOfDriver() {
        return typeOfDriver;
    }

    public void setTypeOfDriver(Object typeOfDriver) {
        this.typeOfDriver = typeOfDriver;
    }

    public Object getCoRidersPreference() {
        return coRidersPreference;
    }

    public void setCoRidersPreference(Object coRidersPreference) {
        this.coRidersPreference = coRidersPreference;
    }

    public Object getPickUpLocation() {
        return pickUpLocation;
    }

    public void setPickUpLocation(Object pickUpLocation) {
        this.pickUpLocation = pickUpLocation;
    }

    public Object getPreference() {
        return preference;
    }

    public void setPreference(Object preference) {
        this.preference = preference;
    }
}
