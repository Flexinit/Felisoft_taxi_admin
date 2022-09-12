package com.example.drupp_driver.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardCountModel {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("wallet_balance")
    @Expose
    private String walletBalance;
    @SerializedName("vehicle_name")
    @Expose
    private String vehicleName;
    @SerializedName("last_login")
    @Expose
    private String lastLogin;
    @SerializedName("total_rides")
    @Expose
    private Integer totalRides;
    @SerializedName("total_completed_rides")
    @Expose
    private Integer totalCompletedRides;
    @SerializedName("total_canceled_rides")
    @Expose
    private Integer totalCanceledRides;
    @SerializedName("total_cash_payment")
    @Expose
    private Double totalCashPayment;
    @SerializedName("total_card_payment")
    @Expose
    private Double totalCardPayment;
    @SerializedName("total_earnings")
    @Expose
    private Double totalEarnings;
    @SerializedName("week_total_rides")
    @Expose
    private Integer weekTotalRides;
    @SerializedName("week_total_completed_rides")
    @Expose
    private Integer weekTotalCompletedRides;
    @SerializedName("week_total_canceled_rides")
    @Expose
    private Integer weekTotalCanceledRides;
    @SerializedName("week_total_cash_payment")
    @Expose
    private Double weekTotalCashPayment;
    @SerializedName("week_total_card_payment")
    @Expose
    private Double weekTotalCardPayment;
    @SerializedName("week_total_earnings")
    @Expose
    private Double weekTotalEarnings;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(String walletBalance) {
        this.walletBalance = walletBalance;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Integer getTotalRides() {
        return totalRides;
    }

    public void setTotalRides(Integer totalRides) {
        this.totalRides = totalRides;
    }

    public Integer getTotalCompletedRides() {
        return totalCompletedRides;
    }

    public void setTotalCompletedRides(Integer totalCompletedRides) {
        this.totalCompletedRides = totalCompletedRides;
    }

    public Integer getTotalCanceledRides() {
        return totalCanceledRides;
    }

    public void setTotalCanceledRides(Integer totalCanceledRides) {
        this.totalCanceledRides = totalCanceledRides;
    }

    public Double getTotalCashPayment() {
        return totalCashPayment;
    }

    public void setTotalCashPayment(Double totalCashPayment) {
        this.totalCashPayment = totalCashPayment;
    }

    public Double getTotalCardPayment() {
        return totalCardPayment;
    }

    public void setTotalCardPayment(Double totalCardPayment) {
        this.totalCardPayment = totalCardPayment;
    }

    public Double getTotalEarnings() {
        return totalEarnings;
    }

    public void setTotalEarnings(Double totalEarnings) {
        this.totalEarnings = totalEarnings;
    }

    public Integer getWeekTotalRides() {
        return weekTotalRides;
    }

    public void setWeekTotalRides(Integer weekTotalRides) {
        this.weekTotalRides = weekTotalRides;
    }

    public Integer getWeekTotalCompletedRides() {
        return weekTotalCompletedRides;
    }

    public void setWeekTotalCompletedRides(Integer weekTotalCompletedRides) {
        this.weekTotalCompletedRides = weekTotalCompletedRides;
    }

    public Integer getWeekTotalCanceledRides() {
        return weekTotalCanceledRides;
    }

    public void setWeekTotalCanceledRides(Integer weekTotalCanceledRides) {
        this.weekTotalCanceledRides = weekTotalCanceledRides;
    }

    public Double getWeekTotalCashPayment() {
        return weekTotalCashPayment;
    }

    public void setWeekTotalCashPayment(Double weekTotalCashPayment) {
        this.weekTotalCashPayment = weekTotalCashPayment;
    }

    public Double getWeekTotalCardPayment() {
        return weekTotalCardPayment;
    }

    public void setWeekTotalCardPayment(Double weekTotalCardPayment) {
        this.weekTotalCardPayment = weekTotalCardPayment;
    }

    public Double getWeekTotalEarnings() {
        return weekTotalEarnings;
    }

    public void setWeekTotalEarnings(Double weekTotalEarnings) {
        this.weekTotalEarnings = weekTotalEarnings;
    }

}
