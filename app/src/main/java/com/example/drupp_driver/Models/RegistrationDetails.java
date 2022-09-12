package com.example.drupp_driver.Models;

public class RegistrationDetails {


    private String first_name;
    private String last_name ;
    private String email;
    private String password;
    private String phone;
    private String city;
    private String license_number;
    private String vehicle_number;
    private String vehicle_name;
    private String images;
    private String vehicle_type_id;
    private String longitude;
    private String latitude;



    public RegistrationDetails(String first_name, String last_name, String email, String password, String phone, String city, String license_number, String vehicle_number, String vehicle_name, String images, String vehicle_type_id, String longitude, String latitude) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.city = city;
        this.license_number = license_number;
        this.vehicle_number = vehicle_number;
        this.vehicle_name = vehicle_name;
        this.images = images;
        this.vehicle_type_id = vehicle_type_id;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getVehicle_name() {
        return vehicle_name;
    }

    public void setVehicle_name(String vehicle_name) {
        this.vehicle_name = vehicle_name;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getVehicle_type_id() {
        return vehicle_type_id;
    }

    public void setVehicle_type_id(String vehicle_type_id) {
        this.vehicle_type_id = vehicle_type_id;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "RegistrationDetails{" +
                "first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", city='" + city + '\'' +
                ", license_number='" + license_number + '\'' +
                ", vehicle_number='" + vehicle_number + '\'' +
                ", vehicle_name='" + vehicle_name + '\'' +
                ", images='" + images + '\'' +
                '}';
    }
}
