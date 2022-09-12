package com.example.drupp_driver.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriverProfileModel {
    @SerializedName("country_code")
    private String countryCode;
    @SerializedName("average_rating")
    @Expose
    private Double averageRating;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("profile_picture")
    @Expose
    private Object profilePicture;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("address")
    @Expose
    private String city;
    @SerializedName("license_number")
    @Expose
    private String licenseNumber;
    @SerializedName("vehicle_number")
    @Expose
    private String vehicleNumber;
    @SerializedName("vehicle_name")
    @Expose
    private String vehicleName;
    @SerializedName("exterior_front")
    @Expose
    private String exteriorFront;
    @SerializedName("exterior_back")
    @Expose
    private String exteriorBack;
    @SerializedName("exterior_right")
    @Expose
    private String exteriorRight;
    @SerializedName("exterior_left")
    @Expose
    private String exteriorLeft;
    @SerializedName("interior_front")
    @Expose
    private String interiorFront;
    @SerializedName("interior_back")
    @Expose
    private String interiorBack;
    @SerializedName("engine")
    @Expose
    private String engine;
    @SerializedName("boot")
    @Expose
    private String boot;
    @SerializedName("driver_type")
    @Expose
    private Integer driverType;
    @SerializedName("vehicle_type_id")
    @Expose
    private Integer vehicleTypeId;




    public Integer getDriverType() {
        return driverType;
    }

    public Integer getVehicleTypeId() {
        return vehicleTypeId;
    }

    public String getExteriorFront() {
        return exteriorFront;
    }

    public void setExteriorFront(String exteriorFront) {
        this.exteriorFront = exteriorFront;
    }

    public String getExteriorBack() {
        return exteriorBack;
    }

    public void setExteriorBack(String exteriorBack) {
        this.exteriorBack = exteriorBack;
    }

    public String getExteriorRight() {
        return exteriorRight;
    }

    public void setExteriorRight(String exteriorRight) {
        this.exteriorRight = exteriorRight;
    }

    public String getExteriorLeft() {
        return exteriorLeft;
    }

    public void setExteriorLeft(String exteriorLeft) {
        this.exteriorLeft = exteriorLeft;
    }

    public String getInteriorFront() {
        return interiorFront;
    }

    public void setInteriorFront(String interiorFront) {
        this.interiorFront = interiorFront;
    }

    public String getInteriorBack() {
        return interiorBack;
    }

    public void setInteriorBack(String interiorBack) {
        this.interiorBack = interiorBack;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getBoot() {
        return boot;
    }

    public void setBoot(String boot) {
        this.boot = boot;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Object getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Object profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }
}
