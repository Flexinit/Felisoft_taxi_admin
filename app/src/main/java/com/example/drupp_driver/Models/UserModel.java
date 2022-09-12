package com.example.drupp_driver.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserModel {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("country_code")

    @Expose
    private int countryCode;

    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("driver_status")
    @Expose
    private int driverStatus = 1;
    @SerializedName("fb_token")
    @Expose
    private String fbToken;
    @SerializedName("driver_id")
    @Expose
    private Integer driverId;
    @SerializedName("vehicle_id")
    @Expose
    private Integer vehicleId;


    @SerializedName("ride_preference")
    @Expose
    private String ridePreference;

    private boolean isOTPVerified;

    private String vehichle_number;
    private String vehichle_name;

    private String color;

    private String veh_brand;
    private String veh_model;
    private String veh_chassis_num;


    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public String getVeh_brand() {
        return veh_brand;
    }

    public void setVeh_brand(String veh_brand) {
        this.veh_brand = veh_brand;
    }

    public String getVeh_model() {
        return veh_model;
    }

    public void setVeh_model(String veh_model) {
        this.veh_model = veh_model;
    }

    public String getVeh_chassis_num() {
        return veh_chassis_num;
    }

    public void setVeh_chassis_num(String veh_chassis_num) {
        this.veh_chassis_num = veh_chassis_num;
    }

    public void setFbToken(String fbToken) {
        this.fbToken = fbToken;
    }

    public String getFbToken() {
        return fbToken;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVehichle_number() {
        return vehichle_number;
    }

    public void setVehichle_number(String vehichle_number) {
        this.vehichle_number = vehichle_number;
    }

    public String getVehichle_name() {
        return vehichle_name;
    }

    public void setVehichle_name(String vehichle_name) {
        this.vehichle_name = vehichle_name;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(int countryCode) {
        this.countryCode = countryCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(int driverStatus) {
        this.driverStatus = driverStatus;
    }

    public void setOTPVerified(boolean OTPVerified) {
        isOTPVerified = OTPVerified;
    }
    public String getRidePreference() {
        return ridePreference;
    }

    public void setRidePreference(String ridePreference) {
        this.ridePreference = ridePreference;
    }
}

