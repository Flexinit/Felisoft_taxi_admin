package com.example.drupp_driver.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RideInfoModel implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("destination")
    @Expose
    private String destination;
    @SerializedName("source_latitude")
    @Expose
    private String sourceLatitude;
    @SerializedName("source_longitude")
    @Expose
    private String sourceLongitude;
    @SerializedName("destination_latitude")
    @Expose
    private String destinationLatitude;
    @SerializedName("destination_longitude")
    @Expose
    private String destinationLongitude;
    @SerializedName("ride_type")
    @Expose
    private Integer rideType;
    @SerializedName("passengers_preference")
    @Expose
    private Object passengersPreference;
    @SerializedName("ride_option")
    @Expose
    private Integer rideOption;
    @SerializedName("ride_date")
    @Expose
    private String rideDate;
    @SerializedName("ride_time")
    @Expose
    private Object rideTime;
    @SerializedName("requested_at")
    @Expose
    private Object requestedAt;
    @SerializedName("start_time")
    @Expose
    private Object startTime;
    @SerializedName("complete_time")
    @Expose
    private Object completeTime;
    @SerializedName("duration")
    @Expose
    private Object duration;
    @SerializedName("distance")
    @Expose
    private Object distance;
    @SerializedName("driver_id")
    @Expose
    private Integer driverId;
    @SerializedName("vehicle_type")
    @Expose
    private Integer vehicleType;
    @SerializedName("base_fare")
    @Expose
    private String baseFare;
    @SerializedName("per_km")
    @Expose
    private String perKm;
    @SerializedName("per_minute_wait_charge")
    @Expose
    private String perMinuteWaitCharge;
    @SerializedName("vehicle_name")
    @Expose
    private String vehicleName;
    @SerializedName("vehicle_number")
    @Expose
    private String vehicleNumber;
    @SerializedName("cancel_reason")
    @Expose
    private Object cancelReason;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("total_fare")
    @Expose
    private String totalFare;
    @SerializedName("posted_by_driver")
    @Expose
    private Integer postedByDriver;
    @SerializedName("driver_ride_id")
    @Expose
    private Integer driverRideId;
    @SerializedName("user_fare")
    @Expose
    private String userFare;
    @SerializedName("driver_fare")
    @Expose
    private Object driverFare;
    @SerializedName("unique_key")
    @Expose
    private Object uniqueKey;
    @SerializedName("drupp_earnings")
    @Expose
    private Object druppEarnings;
    @SerializedName("rider_name")
    @Expose
    private String riderName;
    @SerializedName("profile_picture")
    @Expose
    private String profilePicture;
    @SerializedName("country_code")
    @Expose
    private Integer countryCode;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("average_rating")
    @Expose
    private Object averageRating;
    @SerializedName("payment_option")
    @Expose
    private Integer paymentOption;
    @SerializedName("name")
    @Expose
    private String riderNameOption2;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public String getSourceLatitude() {
        return sourceLatitude;
    }

    public void setSourceLatitude(String sourceLatitude) {
        this.sourceLatitude = sourceLatitude;
    }

    public String getSourceLongitude() {
        return sourceLongitude;
    }

    public void setSourceLongitude(String sourceLongitude) {
        this.sourceLongitude = sourceLongitude;
    }

    public String getDestinationLatitude() {
        return destinationLatitude;
    }

    public void setDestinationLatitude(String destinationLatitude) {
        this.destinationLatitude = destinationLatitude;
    }

    public String getDestinationLongitude() {
        return destinationLongitude;
    }

    public void setDestinationLongitude(String destinationLongitude) {
        this.destinationLongitude = destinationLongitude;
    }

    public Integer getRideType() {
        return rideType;
    }

    public void setRideType(Integer rideType) {
        this.rideType = rideType;
    }

    public Object getPassengersPreference() {
        return passengersPreference;
    }

    public void setPassengersPreference(Object passengersPreference) {
        this.passengersPreference = passengersPreference;
    }

    public Integer getRideOption() {
        return rideOption;
    }

    public void setRideOption(Integer rideOption) {
        this.rideOption = rideOption;
    }

    public String getRideDate() {
        return rideDate;
    }

    public void setRideDate(String rideDate) {
        this.rideDate = rideDate;
    }

    public Object getRideTime() {
        return rideTime;
    }

    public void setRideTime(Object rideTime) {
        this.rideTime = rideTime;
    }

    public Object getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(Object requestedAt) {
        this.requestedAt = requestedAt;
    }

    public Object getStartTime() {
        return startTime;
    }

    public void setStartTime(Object startTime) {
        this.startTime = startTime;
    }

    public Object getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Object completeTime) {
        this.completeTime = completeTime;
    }

    public Object getDuration() {
        return duration;
    }

    public void setDuration(Object duration) {
        this.duration = duration;
    }

    public Object getDistance() {
        return distance;
    }

    public void setDistance(Object distance) {
        this.distance = distance;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public Integer getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(Integer vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(String baseFare) {
        this.baseFare = baseFare;
    }

    public String getPerKm() {
        return perKm;
    }

    public void setPerKm(String perKm) {
        this.perKm = perKm;
    }

    public String getPerMinuteWaitCharge() {
        return perMinuteWaitCharge;
    }

    public void setPerMinuteWaitCharge(String perMinuteWaitCharge) {
        this.perMinuteWaitCharge = perMinuteWaitCharge;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public Object getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(Object cancelReason) {
        this.cancelReason = cancelReason;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(String totalFare) {
        this.totalFare = totalFare;
    }

    public Integer getPostedByDriver() {
        return postedByDriver;
    }

    public void setPostedByDriver(Integer postedByDriver) {
        this.postedByDriver = postedByDriver;
    }

    public Integer getDriverRideId() {
        return driverRideId;
    }

    public void setDriverRideId(Integer driverRideId) {
        this.driverRideId = driverRideId;
    }

    public String getUserFare() {
        return userFare;
    }

    public void setUserFare(String userFare) {
        this.userFare = userFare;
    }

    public Object getDriverFare() {
        return driverFare;
    }

    public void setDriverFare(Object driverFare) {
        this.driverFare = driverFare;
    }

    public Object getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(Object uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public Object getDruppEarnings() {
        return druppEarnings;
    }

    public void setDruppEarnings(Object druppEarnings) {
        this.druppEarnings = druppEarnings;
    }

    public String getRiderName() {
        return riderName;
    }

    public void setRiderName(String riderName) {
        this.riderName = riderName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Integer getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(Integer countryCode) {
        this.countryCode = countryCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Object getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Object averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getPaymentOption() {
        return paymentOption;
    }

    public void setPaymentOption(Integer paymentOption) {
        this.paymentOption = paymentOption;
    }

    public String getRiderNameOption2() {
        return riderNameOption2;
    }

    public void setRiderNameOption2(String riderNameOption2) {
        this.riderNameOption2 = riderNameOption2;
    }
}
