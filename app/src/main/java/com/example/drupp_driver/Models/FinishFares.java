package com.example.drupp_driver.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FinishFares {
    /*{"response":{"fare":"1159.00"},"message":"success","status":200}*/
    @SerializedName("id")
    @Expose
    private int rideID;
    @SerializedName("fare")
    @Expose
    private String fare;

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }
}
