package com.example.drupp_driver.ui.dialogs;

import android.content.IntentSender;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.LocationUtil;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.ui.base.DialogBaseFragment;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.Task;

public class LocationAccessDialog extends DialogBaseFragment {

    private Button mBtnAllow;

    public static LocationAccessDialog newInstance() {
        return new LocationAccessDialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStyle(DialogFragment.STYLE_NORMAL,
                    android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        }
    }

    @Override
    protected void initViewsForFragment(View view) {

    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.dialog_location_access, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBtnAllow = view.findViewById(R.id.btn_allow);

        //Setting Listener
        mBtnAllow.setOnClickListener(v -> fetchCurrentLocation());
    }


    public Location fetchCurrentLocation() {
        LocationSettingsRequest.Builder builder = LocationUtil.showLocationRequestPopUp(getmActivity());

        //Ask user to enable GPS
        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getmActivity()).checkLocationSettings(builder.build());
        result.addOnCompleteListener(task -> {
            try {
                LocationSettingsResponse response = task.getResult(ApiException.class);
                // All location settings are satisfied. The client can initialize location
                // requests here.
            } catch (ApiException e) {
                switch (e.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the
                        // user a dialog.
                        try {
                            // Cast to a resolvable exception.
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            resolvable.startResolutionForResult(
                                    getmActivity(),
                                    AppConstants.REQUEST_ACCESS_LOCATION);
                        } catch (IntentSender.SendIntentException e1) {
                            // Ignore the error.
                        } catch (ClassCastException e2) {
                            // Ignore, should be an impossible error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.

                        break;
                }

            }
        });

        return null;


    }


}
