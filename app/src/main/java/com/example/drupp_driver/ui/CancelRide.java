package com.example.drupp_driver.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.drupp_driver.Models.PopState;
import com.example.drupp_driver.Models.RideCancelDetails;
import com.example.drupp_driver.Models.RiderInfoModel;
import com.example.drupp_driver.Models.Token;
import com.example.drupp_driver.Models.UserModel;
import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.Helper;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetworkList;
import com.example.drupp_driver.connectivity.http.QualStandardResponseList;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.ui.base.BaseActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import retrofit2.Response;

public class CancelRide extends BaseActivity {

    // Activity for Cancelling the Ride
    private static final String TAG = "CancelRide";

    private String id, reason = "";
    private TextView mReasonFirst, mReasonSecond, mReasonThird, mReasonFourth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mMessagesReference, mRideHistoryReference;

    @Override
    protected void setUp() {
        mDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    protected boolean isRequireHideKeyboard() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_ride);

        mReasonFirst = findViewById(R.id.tvCReason1);
        mReasonSecond = findViewById(R.id.tvCReason2);
        mReasonThird = findViewById(R.id.tvCReason3);
        mReasonFourth = findViewById(R.id.tvCReason4);
        TextView toolbarTitle = findViewById(R.id.tv_toolbar_title);
        toolbarTitle.setText(getString(R.string.cancel_ride));

        mReasonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectStateManage(mReasonFirst, mReasonFirst, mReasonSecond, mReasonThird, mReasonFourth);
            }
        });

        mReasonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectStateManage(mReasonSecond, mReasonFirst, mReasonSecond, mReasonThird, mReasonFourth);
            }
        });

        mReasonThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectStateManage(mReasonThird, mReasonFirst, mReasonSecond, mReasonThird, mReasonFourth);
            }
        });

        mReasonFourth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectStateManage(mReasonFourth, mReasonFirst, mReasonSecond, mReasonThird, mReasonFourth);
            }
        });


        if (getIntent() != null && getIntent().hasExtra(AppConstants.K_ID)) {
            id = getIntent().getStringExtra("id");
        }

        findViewById(R.id.image_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.btReasonCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTheRide();
            }
        });

        selectStateManage(mReasonFirst, mReasonFirst, mReasonSecond, mReasonThird, mReasonFourth);
    }

    private void clearChatHistory() {
        RiderInfoModel riderData = Helper.getInstance(this).readFromJson(AppConstants.K_RIDER_DETAILS, RiderInfoModel.class);
        if (SessionManager.getInstance().getUserModel() != null) {
            UserModel userInfo = SessionManager.getInstance().getUserModel();
            if (riderData != null && userInfo != null) {
                mMessagesReference = mDatabase.getReference().child(AppConstants.MESSAGES).child(riderData.getRiderId() + "_" + userInfo.getId());
                mMessagesReference.removeValue((databaseError, databaseReference) -> {
                });
                mRideHistoryReference = mDatabase.getReference().child(AppConstants.K_RIDE_INFO_MODEL).child(id);
                mRideHistoryReference.removeValue((databaseError, databaseReference) -> {

                });
                mDatabase.getReference()
                        .child(AppConstants.FIREBASE_DATABASE.USERS)
                        .child(String.valueOf(userInfo.getId()))
                        .child(AppConstants.CANCELLED_RIDES)
                        .child(id)
                        .setValue(riderData);

            }
        }

    }


    private void selectStateManage(TextView selectedTextView, TextView... textViews) {
        for (TextView t : textViews) {
            t.setTextColor(getResources().getColor(R.color.grey));
        }
        selectedTextView.setTextColor(getResources().getColor(R.color.x_purple));
        selectedTextView.setElevation(20);
        reason = selectedTextView.getText().toString().trim();
    }

    private void cancelTheRide() {
        showLoading();
        DruppRequestHandler.clearInstance();
        RideCancelDetails rideCancelDetails = new RideCancelDetails(Integer.valueOf(id), reason);
        DruppRequestHandler.getInstance(new INetworkList<Token>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<Token>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    clearChatHistory();
                    PopState popState = SessionManager.getInstance().getPopState();
                    popState.setStateType(0);
                    SessionManager.getInstance().savePopState(CancelRide.this, popState);
                    Intent intent = new Intent();
                    setResult(Activity.RESULT_OK, intent);
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
        }, SessionManager.getAccessToken()).driverCancelRide(rideCancelDetails);
    }
}
