package com.example.drupp_driver.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

public class Trip implements Comparator {


    @SerializedName("ride_id")
    @Expose
    private Integer rideId;
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
    private String passengersPreference;
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
    private String distance;
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
    private Object driverRideId;
    @SerializedName("user_fare")
    @Expose
    private String userFare;
    @SerializedName("driver_fare")
    @Expose
    private float driverFare;
    @SerializedName("unique_key")
    @Expose
    private String uniqueKey;
    @SerializedName("drupp_earnings")
    @Expose
    private Object druppEarnings;

    private String succeededOrCancelled;

    public Integer getRideId() {
        return rideId;
    }

    public void setRideId(Integer rideId) {
        this.rideId = rideId;
    }

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

    public String getPassengersPreference() {
        return passengersPreference;
    }

    public void setPassengersPreference(String passengersPreference) {
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

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
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

    public Object getDriverRideId() {
        return driverRideId;
    }

    public void setDriverRideId(Object driverRideId) {
        this.driverRideId = driverRideId;
    }

    public String getUserFare() {
        return userFare;
    }

    public void setUserFare(String userFare) {
        this.userFare = userFare;
    }

    public float getDriverFare() {
        return driverFare;
    }

    public void setDriverFare(float driverFare) {
        this.driverFare = driverFare;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public Object getDruppEarnings() {
        return druppEarnings;
    }

    public void setDruppEarnings(Object druppEarnings) {
        this.druppEarnings = druppEarnings;
    }

   /* @Override
    public int compareTo(Object trip) {
        int compareDate=Integer.parseInt(((Trip)trip).getRideDate());
        return Integer.parseInt(this.getRideDate())-compareDate;
    }*/

    public String getSucceededOrCancelled() {
        return succeededOrCancelled;
    }

    public void setSucceededOrCancelled(String succeededOrCancelled) {
        this.succeededOrCancelled = succeededOrCancelled;
    }

    @Override
    public int compare(Object o1, Object o2) {

        return Long.compare(Long.parseLong(((Trip)o2).getRideDate()),Long.parseLong(((Trip)o1).getRideDate()));
    }
}
