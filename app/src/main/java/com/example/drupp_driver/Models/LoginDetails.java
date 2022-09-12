package com.example.drupp_driver.Models;

public class LoginDetails {

    private String name;
    private int vehicle_type_id;
    private String vehicle_name, vehicle_color, license_number, vehicle_number, vehicle_model, vehicle_brand, chassis_number;

    public LoginDetails(String name, int vehicle_type_id, String vehicle_name, String vehicle_color, String license_number, String vehicle_number, String vehicle_model, String vehicle_brand, String chassis_number) {
        this.name = name;
        this.vehicle_type_id = vehicle_type_id;
        this.vehicle_name = vehicle_name;
        this.vehicle_color = vehicle_color;
        this.license_number = license_number;
        this.vehicle_number = vehicle_number;
        this.vehicle_model = vehicle_model;
        this.vehicle_brand = vehicle_brand;
        this.chassis_number = chassis_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVehicle_type_id() {
        return vehicle_type_id;
    }

    public void setVehicle_type_id(int vehicle_type_id) {
        this.vehicle_type_id = vehicle_type_id;
    }

    public String getVehicle_name() {
        return vehicle_name;
    }

    public void setVehicle_name(String vehicle_name) {
        this.vehicle_name = vehicle_name;
    }

    public String getVehicle_color() {
        return vehicle_color;
    }

    public void setVehicle_color(String vehicle_color) {
        this.vehicle_color = vehicle_color;
    }

    public String getLicense_number() {
        return license_number;
    }

    public void setLicense_number(String license_number) {
        this.license_number = license_number;
    }

    public String getVehicle_number() {
        return vehicle_number;
    }

    public void setVehicle_number(String vehicle_number) {
        this.vehicle_number = vehicle_number;
    }

    public String getVehicle_model() {
        return vehicle_model;
    }

    public void setVehicle_model(String vehicle_model) {
        this.vehicle_model = vehicle_model;
    }

    public String getVehicle_brand() {
        return vehicle_brand;
    }

    public void setVehicle_brand(String vehicle_brand) {
        this.vehicle_brand = vehicle_brand;
    }

    public String getChassis_number() {
        return chassis_number;
    }

    public void setChassis_number(String chassis_number) {
        this.chassis_number = chassis_number;
    }
}
