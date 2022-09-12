package com.example.drupp_driver.ui.bookride;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.drupp_driver.Models.SingleRideModel;
import com.example.drupp_driver.R;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetwork;
import com.example.drupp_driver.connectivity.http.QualStandardResponse;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.helpers.UIHelper;
import com.example.drupp_driver.ui.RideActivity;
import com.example.drupp_driver.ui.base.MainBaseActivity;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

public class SingleScheduledRide extends MainBaseActivity {
    @BindView(R.id.tvDate)
    TextView mDate;
    @BindView(R.id.tvTimeValue)
    TextView mTime;
    @BindView(R.id.tvSourceCity)
    TextView mSource;
    @BindView(R.id.tv_rider_destination)
    TextView mDestination;
    @BindView(R.id.tv_fare)
    TextView mAmountFare;

    private Integer rideId;
    private int rideType;
    private String riderName;

    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_ride_detail);
        ButterKnife.bind(this);
        if (getIntent() != null) {
            rideId = getIntent().getIntExtra(AppConstants.K_RIDE_ID, 0);
            getSingleRide(rideId);
        }

    }

    @OnClick(R.id.image_back)
    public void onBack() {
        onBackPressed();
    }

    @OnClick(R.id.btn_start_ride)
    public void onStartRide() {
        HashMap<String, String> param = new HashMap<>();
        param.put(AppConstants.K_RIDE_ID, rideId.toString());
        param.put(AppConstants.K_RIDE_OPTION, AppConstants.RIDE_OPTION.RIDE_LATER);
        UIHelper.getInstance().switchActivity(this, RideActivity.class, null, param, true);
    }

    private void getSingleRide(int rideId) {
        showDialogProgressBar();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<SingleRideModel>() {
            @Override
            public void onResponse(Response<QualStandardResponse<SingleRideModel>> response) {
                hideDialogProgressBar();
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        rideType = response.body().getResponse().getRideType();
                        mSource.setText(response.body().getResponse().getSource());
                        mDestination.setText(response.body().getResponse().getDestination());
                        mDate.setText(handleDateString(response.body().getResponse().getRideDate()));
                        mTime.setText(handleTimeString(response.body().getResponse().getRideDate()));
                        mAmountFare.setText(response.body().getResponse().getTotalFare());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("ERROR SCHEDULING RIDE ",e.getMessage());

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
                showAlertDialog(R.layout.dialog_network_error);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
                        hideAlertDialog();
                        getSingleRide(rideId);
                    });
                }
            }
        }, SessionManager.getAccessToken()).getSingleScheduledUserRide(rideId);
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

        return (day + "/" + month + "/" + year);
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
}
