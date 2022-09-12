package com.example.drupp_driver.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardModel {
    @SerializedName("driver_details")
    @Expose
    private DriverDashboardModel driverDetails;
    @SerializedName("total_rides")
    @Expose
    private Integer totalRides;
    @SerializedName("total_completed_rides")
    @Expose
    private Integer totalAcceptedRides;
    @SerializedName("total_canceled_rides")
    @Expose
    private Integer totalCanceledRides;
    @SerializedName("total_km")
    @Expose
    private String totalKm;
    @SerializedName("total_earnings")
    @Expose
    private Float totalEarnings;

    public DriverDashboardModel getDriverDetails() {
        return driverDetails;
    }

    public void setDriverDetails(DriverDashboardModel driverDetails) {
        this.driverDetails = driverDetails;
    }

    public Integer getTotalRides() {
        return totalRides;
    }

    public void setTotalRides(Integer totalRides) {
        this.totalRides = totalRides;
    }

    public Integer getTotalAcceptedRides() {
        return totalAcceptedRides;
    }

    public void setTotalAcceptedRides(Integer totalAcceptedRides) {
        this.totalAcceptedRides = totalAcceptedRides;
    }

    public Integer getTotalCanceledRides() {
        return totalCanceledRides;
    }

    public void setTotalCanceledRides(Integer totalCanceledRides) {
        this.totalCanceledRides = totalCanceledRides;
    }

    public String getTotalKm() {
        return totalKm;
    }

    public void setTotalKm(String totalKm) {
        this.totalKm = totalKm;
    }

    public Float getTotalEarnings() {
        return totalEarnings;
    }

    public void setTotalEarnings(Float totalEarnings) {
        this.totalEarnings = totalEarnings;
    }
}
