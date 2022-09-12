package com.example.drupp_driver.Models;

public class UserRatingModel {

    private String rate_from;
    private int id,ride_id;
    private int from_user,to_user;
    private int rate;
    private String review,created_at,updated_at;

    public UserRatingModel(String rate_from, int id, int ride_id, int from_user, int to_user, int rate, String review, String created_at, String updated_at) {
        this.rate_from = rate_from;
        this.id = id;
        this.ride_id = ride_id;
        this.from_user = from_user;
        this.to_user = to_user;
        this.rate = rate;
        this.review = review;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getRate_from() {
        return rate_from;
    }

    public void setRate_from(String rate_from) {
        this.rate_from = rate_from;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRide_id() {
        return ride_id;
    }

    public void setRide_id(int ride_id) {
        this.ride_id = ride_id;
    }

    public int getFrom_user() {
        return from_user;
    }

    public void setFrom_user(int from_user) {
        this.from_user = from_user;
    }

    public int getTo_user() {
        return to_user;
    }

    public void setTo_user(int to_user) {
        this.to_user = to_user;
    }

    public int getRate() {
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
