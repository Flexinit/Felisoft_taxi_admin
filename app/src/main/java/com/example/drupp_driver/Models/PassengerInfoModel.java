package com.example.drupp_driver.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PassengerInfoModel implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("bus_ride_detail_id")
    @Expose
    private Integer busRideDetailId;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("seat_no")
    @Expose
    private Object seatNo;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("laravel_through_key")
    @Expose
    private Integer laravelThroughKey;
    @SerializedName("status")
    @Expose
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBusRideDetailId() {
        return busRideDetailId;
    }

    public void setBusRideDetailId(Integer busRideDetailId) {
        this.busRideDetailId = busRideDetailId;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Object getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(Object seatNo) {
        this.seatNo = seatNo;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getLaravelThroughKey() {
        return laravelThroughKey;
    }

    public void setLaravelThroughKey(Integer laravelThroughKey) {
        this.laravelThroughKey = laravelThroughKey;
    }
}
