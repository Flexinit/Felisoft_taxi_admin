package com.example.drupp_driver.helpers;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.example.drupp_driver.Utils.UiUtils;

public class MapsHelper {

    public static void startNavigation(Activity activity, String destLat, String desLng) {
        try {
            Uri uri = Uri.parse("google.navigation:q=" + destLat + "," + desLng + "&mode=d");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage("com.google.android.apps.maps");
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            UiUtils.showLongToast(e.getLocalizedMessage());
        }

    }
}
