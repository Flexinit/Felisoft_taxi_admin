package com.example.drupp_driver.Models;

public class RatingModel {

    private float rate;
    private String review;
    private int ride_id, receiver;
    private int ride_type;

    public RatingModel(float rate, int receiver, String review, int id, int rideType) {
        this.rate = rate;
        this.ride_type = rideType;
        this.receiver = receiver;
        this.review = review;
        this.ride_id = id;
    }

    public int getRide_type() {
        return ride_type;
    }

    public void setRide_type(int ride_type) {
        this.ride_type = ride_type;
    }

    public int getRide_id() {
        return ride_id;
    }

    public void setRide_id(int ride_id) {
        this.ride_id = ride_id;
    }

    public int getReceiver() {
        return receiver;
    }

    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
