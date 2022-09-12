package com.example.drupp_driver.Models;

import com.example.drupp_driver.helpers.AppConstants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RiderInfoModel {
    @SerializedName(AppConstants.K_NAME)
    @Expose
    private String riderName;
    @SerializedName(AppConstants.K_RIDER_ID)
    @Expose
    private String riderId;
    @SerializedName("phone")
    @Expose
    private String phone;

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setRiderId(String riderId) {
        this.riderId = riderId;
    }

    public String getRiderId() {
        return riderId;
    }

    public void setRiderName(String riderName) {
        this.riderName = riderName;
    }

    public String getRiderName() {
        return riderName;
    }
}
