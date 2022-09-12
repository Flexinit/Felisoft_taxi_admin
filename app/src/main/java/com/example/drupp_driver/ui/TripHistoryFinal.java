package com.example.drupp_driver.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.drupp_driver.Models.RiderInfo;
import com.example.drupp_driver.Models.SingleRideModel;
import com.example.drupp_driver.Models.Trip;
import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.RxBus;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetwork;
import com.example.drupp_driver.connectivity.http.QualStandardResponse;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.ui.base.MainBaseActivity;
import com.example.drupp_driver.ui.support.SupportActivity;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import retrofit2.Response;

public class TripHistoryFinal extends MainBaseActivity {

    private TextView scity, dcity, date, time, totalFare;
    int id;
    Button query;
    private Disposable rideInfoDisposable;

    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_details_final);


        findViewById(R.id.image_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        scity = findViewById(R.id.tvSourceCity);
        dcity = findViewById(R.id.tv_rider_destination);
        date = findViewById(R.id.tvDateValue);
        time = findViewById(R.id.tvTimeValue);
        query = findViewById(R.id.btQuery);
        totalFare = findViewById(R.id.tv_total_fare);

        if (getIntent().hasExtra(AppConstants.K_ORIGIN))
            query.setVisibility(View.VISIBLE);


        query.setOnClickListener(v -> {
            Intent intent1 = new Intent(TripHistoryFinal.this, SupportActivity.class);
            intent1.putExtra(AppConstants.K_S_CITY, getIntent().getStringExtra(AppConstants.K_S_CITY));
            intent1.putExtra(AppConstants.K_D_CITY, getIntent().getStringExtra(AppConstants.K_D_CITY));
            intent1.putExtra(AppConstants.K_DATE, getIntent().getStringExtra(AppConstants.K_DATE));
            intent1.putExtra(AppConstants.K_RIDE_ID, id);
            intent1.putExtra(AppConstants.K_ORIGIN, AppConstants.K_TRIP_HIS_FINAL);
            startActivity(intent1);
        });

        rideInfoDisposable = RxBus.getInstance().getIntentEvent()
                .subscribe(o -> {
                    if (o instanceof RiderInfo) {
                        RiderInfo riderInfo = (RiderInfo) o;
                        id = Integer.parseInt(riderInfo.getRideId());
                        scity.setText(riderInfo.getSource());
                        dcity.setText(riderInfo.getDestination());
                        date.setText(handleDateString(riderInfo.getRideDate()));
                        time.setText(handleTimeString(riderInfo.getRideDate()));
                        getTripHistory(id);
                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rideInfoDisposable.dispose();
    }

    private String handleDateString(String timeStamp) {
        java.util.Date given_timestamp = new java.util.Date(Long.valueOf(timeStamp) * 1000);

        String my_timestamp = String.valueOf(given_timestamp);

        String[] arr = my_timestamp.split(" ");
        String week_day = arr[0];
        String month = arr[1];
        String day = arr[2];
        String time = arr[3];
        String utc = arr[4];
        String year = arr[5];

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


        return time;
    }

    private void getTripHistory(int rideId) {
        showDialogProgressBar();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<Trip>() {
            @Override
            public void onResponse(Response<QualStandardResponse<Trip>> response) {
                hideDialogProgressBar();
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        scity.setText(response.body().getResponse().getSource());
                        dcity.setText(response.body().getResponse().getDestination());
                        date.setText(handleDateString(response.body().getResponse().getRideDate()));
                        time.setText(handleTimeString(response.body().getResponse().getRideDate()));
                        totalFare.setText(getString(R.string.in_naira, response.body().getResponse().getTotalFare()));
                    } catch (Exception e) {

                    }


                }
            }

            @Override
            public void onError(Response<QualStandardResponse<Trip>> response) {
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
                showAlertDialog(R.layout.dialog_network_error);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
                        hideAlertDialog();
                        getTripHistory(rideId);
                    });
                }
            }
        }, SessionManager.getAccessToken()).getSingleTripHistory(rideId);
    }


}
