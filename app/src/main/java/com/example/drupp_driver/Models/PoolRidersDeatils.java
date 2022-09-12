package com.example.drupp_driver.Models;

public class PoolRidersDeatils {

    private String id;
    private String rname;
    private String scity;
    private String dcity;
    private String status;

    private String source_longitude;
    private String source_latitude;

    private String destination_latitude;
    private String destination_longitude;
    private String phone;
    private int user_id;


    public PoolRidersDeatils() {
    }

    public PoolRidersDeatils(String id, String rname, String scity, String dcity, String status, String source_longitude, String source_latitude, String destination_latitude, String destination_longitude, String phone, int user_id) {
        this.id = id;
        this.rname = rname;
        this.scity = scity;
        this.dcity = dcity;
        this.status = status;
        this.source_longitude = source_longitude;
        this.source_latitude = source_latitude;
        this.destination_latitude = destination_latitude;
        this.destination_longitude = destination_longitude;
        this.phone = phone;
        this.user_id = user_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSource_longitude() {
        return source_longitude;
    }

    public void setSource_longitude(String source_longitude) {
        this.source_longitude = source_longitude;
    }

    public String getSource_latitude() {
        return source_latitude;
    }

    public void setSource_latitude(String source_latitude) {
        this.source_latitude = source_latitude;
    }

    public String getDestination_latitude() {
        return destination_latitude;
    }

    public void setDestination_latitude(String destination_latitude) {
        this.destination_latitude = destination_latitude;
    }

    public String getDestination_longitude() {
        return destination_longitude;
    }

    public void setDestination_longitude(String destination_longitude) {
        this.destination_longitude = destination_longitude;
    }

    public String getid() {
        return id;
    }

    public void setid(String id) {
        this.id = id;
    }

    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }

    public String getScity() {
        return scity;
    }

    public void setScity(String scity) {
        this.scity = scity;
    }

    public String getDcity() {
        return dcity;
    }

    public void setDcity(String dcity) {
        this.dcity = dcity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
