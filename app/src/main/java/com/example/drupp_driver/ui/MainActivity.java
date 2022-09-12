package com.example.drupp_driver.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.drupp_driver.Models.PopState;
import com.example.drupp_driver.Models.RideInfoModel;
import com.example.drupp_driver.Models.RiderInfo;
import com.example.drupp_driver.Models.UserModel;
import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.CountryCodes;
import com.example.drupp_driver.Utils.Helper;
import com.example.drupp_driver.Utils.IDialogResponseObserver;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetwork;
import com.example.drupp_driver.connectivity.http.QualStandardResponse;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.ui.base.MainBaseActivity;
import com.example.drupp_driver.ui.dialogs.LocationAccessDialog;
import com.example.drupp_driver.ui.dialogs.LocationRequestDialog;
import com.example.drupp_driver.ui.signup.SignUpActivity;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.List;
import java.util.Objects;

import retrofit2.Response;

public class MainActivity extends MainBaseActivity implements IDialogResponseObserver {

    private static final String TAG = "MainActivity";

    public static String fb_token;
    private LocationAccessDialog locationAccessDialog;
    private Class clazz;

    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_new);


        checkPermission();
        CountryCodes.getCountryCodes();
    }


    private void checkOverLayPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED) {//Can add more as per requirement

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW},
                    AppConstants.REQUEST_SYSTE_ALERT);

        }
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            askFloatingPermission();
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            LocationRequestDialog locationRequestDialog = LocationRequestDialog.newInstance();
            locationRequestDialog.setiDialogResponseObserver(this);
            locationRequestDialog.setCancelable(false);
            locationRequestDialog.show(getSupportFragmentManager(), LocationRequestDialog.class.getSimpleName());

        } else {
            checkLocationAccess();
        }
    }

    private void checkLocationAccess() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                    , AppConstants.REQUEST_ACCESS_LOCATION);
        } else {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationAccessDialog = LocationAccessDialog.newInstance();
                locationAccessDialog.show(getSupportFragmentManager(), LocationAccessDialog.class.getSimpleName());
            } else {
                checkDesireActivity();
                //      checkOverLayPermission();

            }
        }
    }

    private void askFloatingPermission() {
        //If the draw over permission is not available open the settings screen
        //to grant the permission.
        Intent chatHeadIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(chatHeadIntent, AppConstants.CODE_DRAW_OVER_OTHER_APP_PERMISSION);
    }


    private void checkDesireActivity() {
        Class clazz = null;
        UserModel userModel = SessionManager.getInstance().loadUser(MainActivity.this);
        SessionManager.getInstance().loadPopState(MainActivity.this);
        SessionManager.getInstance().loadRiderInformation(MainActivity.this);
        SessionManager.getInstance().loadUserLocation(MainActivity.this);
        SessionManager.getInstance().loadPostRideInfo(MainActivity.this);

        if (Helper.getSignUpState(this) != -1) {
            clazz = SignUpActivity.class;
            Intent intent = new Intent(MainActivity.this, clazz);
            switch (Helper.getSignUpState(this)) {
                case AppConstants.SIGN_UP_STATUS.PERSONAL_INFO:
                    intent.putExtra(AppConstants.K_SIGN_UP_STATE, AppConstants.SIGN_UP_STATUS.PERSONAL_INFO);
                    break;
                case AppConstants.SIGN_UP_STATUS.NEXT_OF_KIN:
                    intent.putExtra(AppConstants.K_SIGN_UP_STATE, AppConstants.SIGN_UP_STATUS.NEXT_OF_KIN);
                    break;
                case AppConstants.SIGN_UP_STATUS.DRIVER_EXPERIENCE:
                    intent.putExtra(AppConstants.K_SIGN_UP_STATE, AppConstants.SIGN_UP_STATUS.DRIVER_EXPERIENCE);
                    break;
                case AppConstants.SIGN_UP_STATUS.VEHICLE_INFO:
                    intent.putExtra(AppConstants.K_SIGN_UP_STATE, AppConstants.SIGN_UP_STATUS.VEHICLE_INFO);
                    break;
                case AppConstants.SIGN_UP_STATUS.ACCOUNT_INFO:
                    intent.putExtra(AppConstants.K_SIGN_UP_STATE, AppConstants.SIGN_UP_STATUS.ACCOUNT_INFO);
                    break;
            }
            switchToNext(intent);

        } else {
            if (userModel == null) {
                switchToSplash();
            } else {
                if (userModel.getToken() == null) {
                    switchToSplash();
                } else {
                    getRideInfo();
                }

            }


        }


    }

    private void switchToSplash() {
        if (getToken() != null) {
            fb_token = getToken();
        }

        Handler handler=new Handler();
        Intent intent=new Intent(this, StartUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {



                startActivity(intent);
            }
        },3000);

    }

    private void switchToNext(Intent intent) {
        try {
            if (getToken() != null) {
                fb_token = getToken();
            }
            intent.putExtra(AppConstants.K_FIREBASE_TOKEN, fb_token);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }


    }

    private String getToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "getInstanceId failed", task.getException());
                        return;
                    }
                    String token = Objects.requireNonNull(task.getResult()).getToken();
                    fb_token = token;
                    Log.d(TAG, "onComplete: TOKEN \n" + token);
                });
        return fb_token;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.REQUEST_ACCESS_LOCATION) {
            if (resultCode == Activity.RESULT_OK) {
                if (locationAccessDialog != null) {
                    checkDesireActivity();
                    locationAccessDialog.dismiss();
                }
            }
        } else if (requestCode == AppConstants.REQUEST_SYSTE_ALERT) {
            checkDesireActivity();

        }
    }

    public void getRideInfo() {
        int rideId = 0;
        int isDriverPosted = -1;
        try {
            isDriverPosted = Helper.getRideType(this);
            rideId = Integer.valueOf(Objects.requireNonNull(Helper.getRideNotification(this).get(AppConstants.K_RIDE_ID)));
        } catch (Exception e) {

        }

        PopState popState = new PopState();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<RideInfoModel>() {
            @Override
            public void onResponse(Response<QualStandardResponse<RideInfoModel>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    RideInfoModel rideInfoModel = response.body().getResponse();
                    Intent intent=new Intent();
                    switch (rideInfoModel.getStatus()) {
                        case AppConstants.RIDE_STATUS.RIDE_ACCEPTED:
                            popState.setStateType(3);
                            SessionManager.getInstance().savePopState(MainActivity.this, popState);
//                            clazz = PoolRideActivity.class;
                            clazz = RideActivity.class;
                            break;
                        case AppConstants.RIDE_STATUS.RIDE_STARTED:
                            popState.setStateType(4);
                            SessionManager.getInstance().savePopState(MainActivity.this, popState);
//                            clazz = PoolRideActivity.class;
                            clazz = RideActivity.class;
                            break;
                        case AppConstants.RIDE_STATUS.RIDE_COMPLETED:

                            final RiderInfo riderInfo;
                            List<RiderInfo> ridersInfo = SessionManager.getInstance().getRiderInfos();
                            if(SessionManager.getInstance().loadPopState(MainActivity.this).getStateType()==4){
                                if(ridersInfo.size()>0){
                                    riderInfo = ridersInfo.get(ridersInfo.size() - 1);
                                    clazz = BillActivity.class;
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.putExtra(AppConstants.K_ID, Integer.valueOf(riderInfo.getRideId()));
                                    intent.putExtra(AppConstants.K_USER_ID, riderInfo.getUserId());
                                }

                                break;
                            }
                        case AppConstants.RIDE_STATUS.RIDE_CANCELLED:
                        case AppConstants.RIDE_STATUS.RIDE_PAID:
                        default:
                            popState.setStateType(0);
//                            clazz = RideActivity.class;
                            clazz = RideActivity.class;
                            break;
                    }

                    intent.setClass(MainActivity.this,clazz);
                    intent.putExtra(AppConstants.K_RIDE_INFO_MODEL, rideInfoModel);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onError(Response<QualStandardResponse<RideInfoModel>> response) {
                hideLoading();
                Log.d("MainActivity","popState Removed error:"+response.errorBody());
                SessionManager.getInstance().removePopState(MainActivity.this);
                Intent intent = new Intent(MainActivity.this, RideActivity.class);
                startActivity(intent);
                finish();

            }

            @Override
            public void onNullResponse() {
                Log.d("MainActivity","null response");
                hideLoading();
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoading();
                showAlertDialog(R.layout.dialog_network_error);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
                        hideAlertDialog();
                        getRideInfo();
                    });
                }
            }
        }, SessionManager.getAccessToken()).getRideInfo(isDriverPosted, rideId);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onAllowAction() {
        checkLocationAccess();
    }

    @Override
    public void onNoAction() {

    }
}
