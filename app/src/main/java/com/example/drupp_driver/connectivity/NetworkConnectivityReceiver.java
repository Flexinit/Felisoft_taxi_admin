package com.example.drupp_driver.connectivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.net.ConnectivityManagerCompat;

import com.example.drupp_driver.DruppDriverApp;

import java.util.concurrent.TimeUnit;

public class NetworkConnectivityReceiver extends BroadcastReceiver {
    private static long ONLINE_CHECK_THRESHOLD_MILLIS = TimeUnit.MINUTES.toMillis(1);
    private boolean online = true;
    private long lastCheckedMillis;

    public boolean isOnline() {
        if (System.currentTimeMillis() - lastCheckedMillis > ONLINE_CHECK_THRESHOLD_MILLIS) {
            updateOnlineState();
            lastCheckedMillis = System.currentTimeMillis();
        }
        return online;
    }

    @Override public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            NetworkInfo networkInfo = getNetworkInfoFromBroadcast(context, intent);
            if (networkInfo != null && networkInfo.isConnected()) {
                Log.e("manage:", "SometimeGetHere");
            }
            updateOnlineState();
        }
    }

    private void updateOnlineState() {
        NetworkInfo info = getConnectivityManager(DruppDriverApp.getInstance()).getActiveNetworkInfo();
        online = info != null && info.isConnected();
    }

    @Nullable
    private NetworkInfo getNetworkInfoFromBroadcast(Context context, Intent intent) {
        return ConnectivityManagerCompat.getNetworkInfoFromBroadcast(getConnectivityManager(context), intent);
    }

    @Nullable private ConnectivityManager getConnectivityManager(Context context) {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }
}
