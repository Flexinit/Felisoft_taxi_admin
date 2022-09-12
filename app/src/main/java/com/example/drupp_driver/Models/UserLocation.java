package com.example.drupp_driver.Models;

import com.google.android.gms.maps.model.LatLng;

public class UserLocation {
    private LatLng currentLatLng;

    public LatLng getCurrentLatLng() {
        return currentLatLng;
    }

    public void setCurrentLatLng(LatLng currentLatLng) {
        this.currentLatLng = currentLatLng;
    }
}
