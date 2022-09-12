package com.example.drupp_driver.ui.notification;

import android.app.Notification;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.drupp_driver.Models.NotificationModel;
import com.example.drupp_driver.Models.PopState;
import com.example.drupp_driver.Models.SingleNotification;
import com.example.drupp_driver.Models.Token;
import com.example.drupp_driver.R;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetwork;
import com.example.drupp_driver.connectivity.http.INetworkList;
import com.example.drupp_driver.connectivity.http.QualStandardResponse;
import com.example.drupp_driver.connectivity.http.QualStandardResponseList;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.ui.RideActivity;
import com.example.drupp_driver.ui.base.MainBaseActivity;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

public class RidePreferenceActivity extends MainBaseActivity {

    @BindView(R.id.tv_pick_up)
    TextView mPickUp;
    @BindView(R.id.tv_num_of_riders)
    TextView mCoriders;
    @BindView(R.id.tv_trip_type)
    TextView mTripType;
    @BindView(R.id.tv_message)
    TextView mMessage;
    @BindView(R.id.tv_toolbar_title)
    TextView title;
    private int rideId;

    @Override
    protected void setUp() {
        title.setText(R.string.rider_pickup_details);
        if (getIntent() != null) {
            getNotification(Integer.valueOf(getIntent().getStringExtra(AppConstants.K_ID)));
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_ride_preference_layout);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.image_back)
    public void onBack() {
        onBackPressed();
    }

    @OnClick(R.id.btn_confirm)
    public void onConfirm() {
        actionNotification(2, rideId);
    }

    @OnClick(R.id.btn_reject)
    public void onCancel() {
        actionNotification(3, rideId);
    }

    private void getNotification(int id) {
        showLoading();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<SingleNotification>() {
            @Override
            public void onResponse(Response<QualStandardResponse<SingleNotification>> response) {
                hideLoading();

                if (response.isSuccessful() && response.body() != null) {
                    try {
                        rideId = response.body().getResponse().getDriverRideId();
                        mPickUp.setText(response.body().getResponse().getPickUpLocation());
                        mCoriders.setText(getString(R.string.no_of_co_rider));
                        switch (response.body().getResponse().getTypeOfDriver()) {
                            case "1":
                                mTripType.setText(R.string.chatty);
                                break;
                            case "2":
                                mTripType.setText(R.string.silent);
                                break;
                            case "3":
                                mTripType.setText(R.string.indifferent);
                                break;
                        }
                        mMessage.setText(response.body().getResponse().getPreference());
                    } catch (Exception e) {

                    }


                }
            }

            @Override
            public void onError(Response<QualStandardResponse<SingleNotification>> response) {
                hideLoading();
                showErrorPrompt(response);
            }

            @Override
            public void onNullResponse() {
                hideLoading();
            }

            @Override
            public void onFailure(Throwable t) {
                hideLoading();
                showAlertDialog(R.layout.dialog_network_error);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(view -> {
                        hideAlertDialog();
                        getNotification(id);
                    });
                }
            }
        }, SessionManager.getAccessToken()).getSingleNotification(id);
    }


    private void actionNotification(int status, int rideId) {
        try {
            showLoading();
            HashMap<String, Integer> parse = new HashMap<>();
            parse.put(AppConstants.K_STATUS, status);
            parse.put(AppConstants.K_ID, rideId);
            DruppRequestHandler.clearInstance();
            DruppRequestHandler.getInstance(new INetworkList<Token>() {
                @Override
                public void onResponseList(Response<QualStandardResponseList<Token>> response) {
                    hideLoading();
                    if (response.isSuccessful() && response.body() != null) {
                        showErrorPrompt(status == 2 ? R.string.accept_rider_pref_with_post_ride : R.string.reject_rider_pref_with_post_ride);
                        if (status == 2) {
                            setResult(RESULT_OK);
                        } else {
                            setResult(RESULT_CANCELED);
                        }
                        finish();
                    }
                }

                @Override
                public void onErrorList(Response<QualStandardResponseList<Token>> response) {
                    hideLoading();
                    showErrorPrompt(response);

                }

                @Override
                public void onNullListResponse() {
                    hideLoading();
                }

                @Override
                public void onFailureList(Throwable t) {
                    hideLoading();
                    showErrorPrompt(R.string.some_thing_went_wrong);
                }
            }, SessionManager.getAccessToken()).driverAcceptRidePost(parse);
        } catch (Exception e) {
            Log.e("Exception: ", e.getMessage());
        }
    }


}
