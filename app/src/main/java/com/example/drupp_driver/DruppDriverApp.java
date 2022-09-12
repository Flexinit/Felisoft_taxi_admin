package com.example.drupp_driver;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.example.drupp_driver.connectivity.NetworkConnectivityReceiver;
import com.google.android.libraries.places.api.Places;

public class DruppDriverApp extends Application {

    private static DruppDriverApp INSTANCE;
    private static Application sApplication;

    private NetworkConnectivityReceiver connectivityReceiver = new NetworkConnectivityReceiver();

    public DruppDriverApp() {
        INSTANCE = this;
    }

    public static DruppDriverApp getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        Places.initialize(this, getString(R.string.google_maps_key));
        Places.createClient(this);
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }


    public static Application getApplication() {
        return sApplication;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }

}
