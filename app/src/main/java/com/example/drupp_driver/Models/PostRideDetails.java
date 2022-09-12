package com.example.drupp_driver.Models;

public class PostRideDetails {

    private String source;
    private String destination;

    private double source_latitude;
    private double source_longitude;
    private double destination_latitude;
    private double destination_longitude;

    private int ride_type;
    private long ride_date;

    private String driver_fare;
    private int passengers_preference;

    public PostRideDetails(String source, String destination, double source_latitude, double source_longitude, double destination_latitude, double destination_longitude, int ride_type, long ride_date, String base_fare, int passengers_preference) {
        this.source = source;
        this.destination = destination;
        this.source_latitude = source_latitude;
        this.source_longitude = source_longitude;
        this.destination_latitude = destination_latitude;
        this.destination_longitude = destination_longitude;
        this.ride_type = ride_type;
        this.ride_date = ride_date;
        this.driver_fare = base_fare;
        this.passengers_preference = passengers_preference;
    }

    public PostRideDetails() {
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

    public double getSource_latitude() {
        return source_latitude;
    }

    public void setSource_latitude(double source_latitude) {
        this.source_latitude = source_latitude;
    }

    public double getSource_longitude() {
        return source_longitude;
    }

    public void setSource_longitude(double source_longitude) {
        this.source_longitude = source_longitude;
    }

    public double getDestination_latitude() {
        return destination_latitude;
    }

    public void setDestination_latitude(double destination_latitude) {
        this.destination_latitude = destination_latitude;
    }

    public double getDestination_longitude() {
        return destination_longitude;
    }

    public void setDestination_longitude(double destination_longitude) {
        this.destination_longitude = destination_longitude;
    }

    public int getRide_type() {
        return ride_type;
    }

    public void setRide_type(int ride_type) {
        this.ride_type = ride_type;
    }

    public long getRide_date() {
        return ride_date;
    }

    public void setRide_date(long ride_date) {
        this.ride_date = ride_date;
    }



    public int getPassengers_preference() {
        return passengers_preference;
    }

    public void setPassengers_preference(int passengers_preference) {
        this.passengers_preference = passengers_preference;
    }
}
