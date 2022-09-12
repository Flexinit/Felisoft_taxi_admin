package com.example.drupp_driver.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

public class RiderInfo implements Comparator {
    @SerializedName("destination_latitude")
    @Expose
    private String destinationLatitude;
    @SerializedName("destination")
    @Expose
    private String destination;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("ride_option")
    @Expose
    private String rideOption;
    @SerializedName("total_fare")
    @Expose
    private String totalFare;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("ride_date")
    @Expose
    private String rideDate;
    @SerializedName("ride_type")
    @Expose
    private String rideType;
    @SerializedName("destination_longitude")
    @Expose
    private String destinationLongitude;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("source_longitude")
    @Expose
    private String sourceLongitude;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("ride_id")
    @Expose
    private String rideId;
    @SerializedName("source_latitude")
    @Expose
    private String sourceLatitude;

    @SerializedName("average_rating")
    @Expose
    private Double rating;

    private String succeededOrCancelled;


    public RiderInfo() {

    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Double getRating() {
        return rating;
    }

    public String getDestinationLatitude() {
        return destinationLatitude;
    }

    public void setDestinationLatitude(String destinationLatitude) {
        this.destinationLatitude = destinationLatitude;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getRideOption() {
        return rideOption;
    }

    public void setRideOption(String rideOption) {
        this.rideOption = rideOption;
    }

    public String getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(String totalFare) {
        this.totalFare = totalFare;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRideDate() {
        return rideDate;
    }

    public void setRideDate(String rideDate) {
        this.rideDate = rideDate;
    }

    public String getRideType() {
        return rideType;
    }

    public void setRideType(String rideType) {
        this.rideType = rideType;
    }

    public String getDestinationLongitude() {
        return destinationLongitude;
    }

    public void setDestinationLongitude(String destinationLongitude) {
        this.destinationLongitude = destinationLongitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSourceLongitude() {
        return sourceLongitude;
    }

    public void setSourceLongitude(String sourceLongitude) {
        this.sourceLongitude = sourceLongitude;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRideId() {
        return rideId;
    }

    public void setRideId(String rideId) {
        this.rideId = rideId;
    }

    public String getSourceLatitude() {
        return sourceLatitude;
    }

    public void setSourceLatitude(String sourceLatitude) {
        this.sourceLatitude = sourceLatitude;
    }

    public String getSucceededOrCancelled() {
        return succeededOrCancelled;
    }

    public void setSucceededOrCancelled(String succeededOrCancelled) {
        this.succeededOrCancelled = succeededOrCancelled;
    }
    @Override
    public int compare(Object o1, Object o2) {

        return Long.compare(Long.parseLong(((RiderInfo)o2).getRideDate()),Long.parseLong(((RiderInfo)o1).getRideDate()));
    }

}
