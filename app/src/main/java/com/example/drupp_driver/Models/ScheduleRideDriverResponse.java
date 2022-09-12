package com.example.drupp_driver.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ScheduleRideDriverResponse {

    @SerializedName("day_timestamp")
    @Expose
    private String dayTimestamp;
    @SerializedName("scheduled_rides")
    @Expose
    private List<RideModel> scheduledRides;

    public String getDayTimestamp() {
        return dayTimestamp;
    }

    public void setDayTimestamp(String dayTimestamp) {
        this.dayTimestamp = dayTimestamp;
    }

    public List<RideModel> getScheduledRides() {
        return scheduledRides;
    }

    public void setScheduledRides(List<RideModel> scheduledRides) {
        this.scheduledRides = scheduledRides;
    }
}
