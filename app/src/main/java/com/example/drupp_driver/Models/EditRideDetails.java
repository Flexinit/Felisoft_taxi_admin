package com.example.drupp_driver.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EditRideDetails {

    private int id;

    private String source;
    private Double source_latitude,source_longitude;
    private String destination;
    private Double destination_latitude,destination_longitude;

    private String ride_date;

    @SerializedName("driver_fare")
    @Expose
    private double base_fare;
    int passengers_preference,ride_type;

    public EditRideDetails(int id, String source, Double source_latitude, Double source_longitude, String destination, Double destination_latitude, Double destination_longitude, String ride_date, double base_fare, int passengers_preference, int ride_type) {
        this.id = id;
        this.source = source;
        this.source_latitude = source_latitude;
        this.source_longitude = source_longitude;
        this.destination = destination;
        this.destination_latitude = destination_latitude;
        this.destination_longitude = destination_longitude;
        this.ride_date = ride_date;
        this.base_fare = base_fare;
        this.passengers_preference = passengers_preference;
        this.ride_type = ride_type;
    }

    public int getRide_type() {
        return ride_type;
    }

    public void setRide_type(int ride_type) {
        this.ride_type = ride_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Double getSource_latitude() {
        return source_latitude;
    }

    public void setSource_latitude(Double source_latitude) {
        this.source_latitude = source_latitude;
    }

    public Double getSource_longitude() {
        return source_longitude;
    }

    public void setSource_longitude(Double source_longitude) {
        this.source_longitude = source_longitude;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }


    public Double getDestination_latitude() {
        return destination_latitude;
    }

    public void setDestination_latitude(Double destination_latitude) {
        this.destination_latitude = destination_latitude;
    }

    public Double getDestination_longitude() {
        return destination_longitude;
    }

    public void setDestination_longitude(Double destination_longitude) {
        this.destination_longitude = destination_longitude;
    }

    public String getRide_date() {
        return ride_date;
    }

    public void setRide_date(String ride_date) {
        this.ride_date = ride_date;
    }


    public double getBase_fare() {
        return base_fare;
    }

    public void setBase_fare(double base_fare) {
        this.base_fare = base_fare;
    }

    public int getPassengers_preference() {
        return passengers_preference;
    }

    public void setPassengers_preference(int passengers_preference) {
        this.passengers_preference = passengers_preference;
    }
}
