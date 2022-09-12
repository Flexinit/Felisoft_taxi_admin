package com.example.drupp_driver.Models.realtime_models;

public class RunningRideInfo {

    private int rideID;
    private String riderName, driverName;
    private double sLatitude, sLongitude, dLatitude, dLongitude, cLatitude, cLongitude;

    public RunningRideInfo() {

    }

    public RunningRideInfo(int rideID, String riderName, String driverName, double sLatitude, double sLongitude, double dLatitude, double dLongitude, double cLatitude, double cLongittude) {
        this.rideID = rideID;
        this.riderName = riderName;
        this.driverName = driverName;
        this.sLatitude = sLatitude;
        this.sLongitude = sLongitude;
        this.dLatitude = dLatitude;
        this.dLongitude = dLongitude;
        this.cLatitude = cLatitude;
        this.cLongitude = cLongittude;
    }

    public int getRideID() {
        return rideID;
    }

    public void setRideID(int rideID) {
        this.rideID = rideID;
    }

    public String getRiderName() {
        return riderName;
    }

    public void setRiderName(String riderName) {
        this.riderName = riderName;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public double getsLatitude() {
        return sLatitude;
    }

    public void setsLatitude(double sLatitude) {
        this.sLatitude = sLatitude;
    }

    public double getsLongitude() {
        return sLongitude;
    }

    public void setsLongitude(double sLongitude) {
        this.sLongitude = sLongitude;
    }

    public double getdLatitude() {
        return dLatitude;
    }

    public void setdLatitude(double dLatitude) {
        this.dLatitude = dLatitude;
    }

    public double getdLongitude() {
        return dLongitude;
    }

    public void setdLongitude(double dLongitude) {
        this.dLongitude = dLongitude;
    }

    public double getcLatitude() {
        return cLatitude;
    }

    public void setcLatitude(double cLatitude) {
        this.cLatitude = cLatitude;
    }

    public double getcLongitude() {
        return cLongitude;
    }

    public void setcLongitude(double cLongitude) {
        this.cLongitude = cLongitude;
    }
}
