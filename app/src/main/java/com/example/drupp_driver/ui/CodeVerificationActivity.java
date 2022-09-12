package com.example.drupp_driver.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.example.drupp_driver.R;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetworkList;
import com.example.drupp_driver.connectivity.http.QualStandardResponseList;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.ui.base.BaseActivity;

import java.util.HashMap;

import retrofit2.Response;

public class CodeVerificationActivity extends BaseActivity {

    private EditText mOtpView;

    private String rideID;

    @Override
    protected void setUp() {

    }

    @Override
    protected boolean isRequireHideKeyboard() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification_for_ride_code);

        mOtpView = findViewById(R.id.otp_verification_view);

        if (getIntent() != null && getIntent().hasExtra(AppConstants.K_RIDE_ID)) {
            rideID = getIntent().getStringExtra(AppConstants.K_RIDE_ID);
        }

        findViewById(R.id.bt_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeVerification(mOtpView.getText().toString().trim());
            }
        });

        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() == null) return;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return true;
    }

    private void codeVerification(String code) {
        HashMap<String, String> parse = new HashMap<>();
        parse.put(AppConstants.K_RIDE_ID, rideID);
        parse.put(AppConstants.K_ONE_TIME_PASSWORD, code);
        showLoading();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<String>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<String>> response) {
                hideLoading();
                if (response.isSuccessful()) {
                    Intent intent = new Intent();
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<String>> response) {
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
        }, SessionManager.getAccessToken()).codeVerification(parse);
    }
}
