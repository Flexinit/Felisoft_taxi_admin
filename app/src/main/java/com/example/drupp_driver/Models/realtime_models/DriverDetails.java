package com.example.drupp_driver.Models.realtime_models;

import com.example.drupp_driver.Models.RiderInfo;

public class DriverDetails {

    private int type;
    private int id, availability;
    private String name;
    private double latitude, longitude;

    public DriverDetails() {

    }


    public DriverDetails(int id, int availability, int type, String name, double latitude, double longitude) {
        this.id = id;
        this.availability = availability;
        this.name = name;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }
}
