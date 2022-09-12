package com.example.drupp_driver.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScheduledRideInfo {
    @SerializedName("ride_id")
    @Expose
    public String id;
    String source;
    String destination;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getRide_date() {
        return ride_date;
    }

    public void setRide_date(String ride_date) {
        this.ride_date = ride_date;
    }

    String ride_date;
}
