package com.example.drupp_driver.Models;

public class RideCancelDetails {

    private int ride_id;

    private String cancel_reason;

    public RideCancelDetails(int id, String reason) {
        ride_id = id;
        cancel_reason = reason;
    }

    public int getid() {
        return ride_id;
    }

    public void setid(int id) {
        this.ride_id = id;
    }

    public String getCancel_reason() {
        return cancel_reason;
    }

    public void setCancel_reason(String cancel_reason) {
        this.cancel_reason = cancel_reason;
    }
}
