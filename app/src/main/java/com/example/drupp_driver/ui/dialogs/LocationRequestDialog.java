package com.example.drupp_driver.ui.dialogs;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.IDialogResponseObserver;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.ui.base.DialogBaseFragment;


public class LocationRequestDialog extends DialogBaseFragment {

    private Button mBtnAllow;
    private IDialogResponseObserver iDialogResponseObserver;

    public static LocationRequestDialog newInstance() {
        return new LocationRequestDialog();
    }


    public void setiDialogResponseObserver(IDialogResponseObserver iDialogResponseObserver) {
        this.iDialogResponseObserver = iDialogResponseObserver;
    }

    @Override
    protected void initViewsForFragment(View view) {

    }


    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.dialog_location_request, container, false);
    }

    @Override
    public void showDialogProgressBar() {

    }

    @Override
    public void hideDialogProgressBar() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStyle(DialogFragment.STYLE_NO_FRAME,
                    R.style.AppTheme);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBtnAllow = view.findViewById(R.id.btn_allow);

        //Setting Listener
        mBtnAllow.setOnClickListener(v -> {
            // Permission is not granted
            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.ACCESS_FINE_LOCATION) && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                requestPermissions(
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        AppConstants.REQUEST_ACCESS_LOCATION);
            } else {
                // No explanation needed; request the permission
                requestPermissions(
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        AppConstants.REQUEST_ACCESS_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case AppConstants.REQUEST_ACCESS_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    dismiss();
                    iDialogResponseObserver.onAllowAction();
                } else {
                    iDialogResponseObserver.onNoAction();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }

    }
}
