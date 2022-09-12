package com.example.drupp_driver.Utils;

import com.example.drupp_driver.Models.PoolRidersDeatils;

import java.util.ArrayList;

public class PoolHelper {

    private ArrayList<PoolRidersDeatils> poolRidersDeatilsList=new ArrayList<>();



    public ArrayList<PoolRidersDeatils> getPoolRidersDeatilsList() {
        return poolRidersDeatilsList;
    }

    public void setPoolRidersDeatilsList(ArrayList<PoolRidersDeatils> poolRidersDeatilsList) {
        this.poolRidersDeatilsList = poolRidersDeatilsList;
    }
}
