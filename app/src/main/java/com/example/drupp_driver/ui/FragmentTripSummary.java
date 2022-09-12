package com.example.drupp_driver.ui;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.drupp_driver.Models.PopState;
import com.example.drupp_driver.Models.RideInfoModel;
import com.example.drupp_driver.Models.RiderInfo;
import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.Helper;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetwork;
import com.example.drupp_driver.connectivity.http.QualStandardResponse;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.ui.base.MainBaseFragment;

import org.json.JSONException;

import java.util.List;
import java.util.Objects;

import retrofit2.Response;

public class FragmentTripSummary extends MainBaseFragment {
    private int paymentMethod;
    private String fareAmount;
    private String destination;
    private String source;
    TextView tvSource, tvDestination, tvFareAmount, tvPaymentMethod;
    FragmentTripSummary(String source,String destination,String fareAmount,int paymentMethod){
        this.source=source;
        this.destination=destination;
        this.fareAmount=fareAmount;
        this.paymentMethod=paymentMethod;

    }

    @Override
    protected void initViewsForFragment(View view) {
        tvSource =view.findViewById(R.id.tv_source);
        tvDestination =view.findViewById(R.id.tv_destination);
        tvFareAmount =view.findViewById(R.id.tvFareAmount);
        tvPaymentMethod =view.findViewById(R.id.tvPaymentMethod);
        getRideInfo();
    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_trip_summary, container, false);
        return view;
    }
    public void getRideInfo() {
        int rideId = 0;
        int isDriverPosted = -1;
        showLoading();
        try {
            isDriverPosted = Helper.getRideType(getActivity());
            rideId = Integer.parseInt(Objects.requireNonNull(Helper.getRideNotification(getActivity()).get(AppConstants.K_RIDE_ID)));
        } catch (Exception e) {

        }

        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<RideInfoModel>() {
            @Override
            public void onResponse(Response<QualStandardResponse<RideInfoModel>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    RideInfoModel rideInfoModel = response.body().getResponse();

                    tvSource.setText(rideInfoModel.getSource());
                    tvDestination.setText(rideInfoModel.getDestination());
                    String fullAmount = getString(R.string.fare_in_naira, rideInfoModel.getTotalFare());
                    tvFareAmount.setText(fullAmount);



                    String method = "";
                    switch (rideInfoModel.getPaymentOption()) {
                        case AppConstants.PAYMENT_METHOD.CARD:
                            method = getString(R.string.debit_card);
                            break;
                        case AppConstants.PAYMENT_METHOD.WALLET:
                            method = getString(R.string.wallet);
                            break;
                        case AppConstants.PAYMENT_METHOD.NET_BANKING:
                            method = getString(R.string.net_banking);
                            break;
                        case AppConstants.PAYMENT_METHOD.CASH:
                            method = getString(R.string.cash);
                            break;
                    }

                    tvPaymentMethod.setText(method);
                }
            }

            @Override
            public void onError(Response<QualStandardResponse<RideInfoModel>> response) {
                hideLoading();

                showErrorPrompt(response);

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
}