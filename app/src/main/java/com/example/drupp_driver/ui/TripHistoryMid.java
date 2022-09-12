package com.example.drupp_driver.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.drupp_driver.Models.SingleRideModel;
import com.example.drupp_driver.R;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetwork;
import com.example.drupp_driver.connectivity.http.QualStandardResponse;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.ui.base.MainBaseActivity;

import retrofit2.Response;

public class TripHistoryMid extends MainBaseActivity {

    // Screen for Showing Single Trip of Scheduled Ride(by driver)
    ImageView edit;
    Boolean editable = false;
    String timestamp;
    private TextView mDate, mTime, mSource, mDestination, mCoRiders, mDriverType, mMessage;
    private SingleRideModel singleRideModel;

    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_history_mid);
        mDate = findViewById(R.id.tv_date_to);
        mTime = findViewById(R.id.tv_time);
        mSource = findViewById(R.id.tv_rider_source);
        mDestination = findViewById(R.id.tvDestination);
        mCoRiders = findViewById(R.id.tv_co_riders);
        mDriverType = findViewById(R.id.tv_driver_type);
        mMessage = findViewById(R.id.tv_message);

        edit = findViewById(R.id.image_edit);

        findViewById(R.id.image_back).setOnClickListener(v -> onBackPressed());

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(TripHistoryMid.this, EditRides.class);

                intent1.putExtra(AppConstants.K_DATE, getIntent().getStringExtra(AppConstants.K_DATE));
                intent1.putExtra(AppConstants.K_S_CITY, getIntent().getStringExtra(AppConstants.K_S_CITY));
                intent1.putExtra(AppConstants.K_D_CITY, getIntent().getStringExtra(AppConstants.K_D_CITY));
                intent1.putExtra(AppConstants.K_PASSENGERS_PREFERENCE, getIntent().getIntExtra(AppConstants.K_PASSENGERS_PREFERENCE, 1));
                intent1.putExtra(AppConstants.K_BASE_FARE, getIntent().getDoubleExtra(AppConstants.K_BASE_FARE, 1.00));
                intent1.putExtra(AppConstants.K_RIDE_TYPE, getIntent().getIntExtra(AppConstants.K_RIDE_TYPE, 0));
                intent1.putExtra(AppConstants.K_RIDE_ID, getIntent().getIntExtra(AppConstants.K_RIDE_ID, 0));
                startActivityForResult(intent1,AppConstants.REQUEST_EDIT_RIDE);
            }
        });
        try {
            getSingleRide(getIntent().getIntExtra(AppConstants.K_RIDE_ID, 0));
        } catch (Exception ignored) {

        }


    }


    private void getSingleRide(int rideId) {
        showDialogProgressBar();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<SingleRideModel>() {
            @Override
            public void onResponse(Response<QualStandardResponse<SingleRideModel>> response) {
                hideDialogProgressBar();
                if (response.isSuccessful() && response.body() != null) {
                    singleRideModel = response.body().getResponse();
                    try {
                        mDate.setText(handleDateString(singleRideModel.getRideDate()));
                        mTime.setText(handleTimeString(singleRideModel.getRideDate()));
                        mSource.setText(singleRideModel.getSource());
                        mDestination.setText(singleRideModel.getDestination());
                        mCoRiders.setText(singleRideModel.getPassengersPreference().toString());
                        String vehicleType = "";
                        switch (singleRideModel.getVehicleType()) {
                            case AppConstants.WITHOUT_AC:
                                vehicleType = getString(R.string.without_ac);
                                break;
                            case AppConstants.WITH_AC:
                                vehicleType = getString(R.string.with_ac);
                                break;
                        }
                        mDriverType.setText(vehicleType);

                        mMessage.setText(singleRideModel.getPassengersPreference().toString());
                    } catch (Exception e) {


                    }
                }
            }

            @Override
            public void onError(Response<QualStandardResponse<SingleRideModel>> response) {
                hideDialogProgressBar();
                showErrorPrompt(response);
            }

            @Override
            public void onNullResponse() {
                hideDialogProgressBar();
            }

            @Override
            public void onFailure(Throwable t) {
                hideDialogProgressBar();
                showErrorPrompt(getString(R.string.something_went_wrong_try_again));
            }
        }, SessionManager.getAccessToken()).getSingleScheduledDriverRide(rideId);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private String handleDateString(String timeStamp) {
        java.util.Date given_timestamp = new java.util.Date((long) (Long.valueOf(timeStamp)) * 1000);

        String my_timestamp = String.valueOf(given_timestamp);

        String[] arr = my_timestamp.split(" ");
        String week_day = arr[0];
        String month = arr[1];
        String day = arr[2];
        String time = arr[3];
        String utc = arr[4];
        String year = arr[5];


        switch (month) {
            case "Jan":
                month = "1";
                break;
            case "Feb":
                month = "2";
                break;
            case "Mar":
                month = "3";
                break;
            case "Apr":
                month = "4";
                break;
            case "May":
                month = "5";
                break;
            case "Jun":
                month = "6";
                break;
            case "Jul":
                month = "7";
                break;
            case "Aug":
                month = "8";
                break;
            case "Sep":
                month = "9";
                break;
            case "Oct":
                month = "10";
                break;
            case "Nov":
                month = "11";
                break;
            case "Dec":
                month = "12";
                break;

        }

        String xdate = (day + "/" + month + "/" + year);
        return xdate;
    }

    private String handleTimeString(String timeStamp) {
        java.util.Date given_timestamp = new java.util.Date((long) (Long.valueOf(timeStamp)) * 1000);

        String my_timestamp = String.valueOf(given_timestamp);

        String[] arr = my_timestamp.split(" ");
        String week_day = arr[0];
        String month = arr[1];
        String day = arr[2];
        String time = arr[3];
        String utc = arr[4];
        String year = arr[5];

        String xdate = (day + "/" + month + "/" + year);
        return time;
    }

}
