package com.example.drupp_driver.Utils;

import com.example.drupp_driver.Models.LoginDetails;
import com.example.drupp_driver.Models.RiderInfo;
import com.example.drupp_driver.Models.RiderInfoModel;
import com.google.firebase.auth.UserInfo;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;

public class PoolRideManager {

    private static volatile PoolRideManager fragmentSingleton;

    private static final Queue<RiderInfoModel> poolRiderQueue = new ArrayDeque<>();

    private PoolRideManager() {

    }

    //Using Synchronized thread safe singleton
    public static PoolRideManager getInstance() {
        if (fragmentSingleton == null) {
            fragmentSingleton = new PoolRideManager();
        }

        synchronized (fragmentSingleton) {
            return fragmentSingleton;
        }
    }

    public Queue<RiderInfoModel> getRiders() {
        return poolRiderQueue;
    }

    public void addToQueue(RiderInfoModel rider) {
        poolRiderQueue.add(rider);

    }

    public void removeFromQueue() {
        poolRiderQueue.remove();
    }


}
