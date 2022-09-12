package com.example.drupp_driver.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.drupp_driver.Models.DashboardModel;
import com.example.drupp_driver.Models.UserDetails;
import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.Timing;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetwork;
import com.example.drupp_driver.connectivity.http.QualStandardResponse;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.helpers.UIHelper;
import com.example.drupp_driver.ui.completedrides.TripHistory;
import com.example.drupp_driver.ui.base.MainBaseActivity;
import com.example.drupp_driver.ui.completedrides.TripHistoryNew;
import com.example.drupp_driver.ui.payment.PaymentActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

public class DashBoardActivity extends MainBaseActivity {

    @BindView(R.id.tv_welcome_user)
    TextView mWelcomeUser;
    @BindView(R.id.tv_vehicle_name)
    TextView mVehicleName;
    @BindView(R.id.tv_last_login)
    TextView mLastLogin;

    @BindView(R.id.tv_balance)
    TextView mBalance;
    @BindView(R.id.tv_rides)
    TextView mTotalRides;
    @BindView(R.id.tv_earnings)
    TextView mEarnings;
    @BindView(R.id.tv_rides_accepted)
    TextView mRidesAccepted;
    @BindView(R.id.tv_rides_cancelled)
    TextView mRidesCancelled;
    @BindView(R.id.tv_km_travelled)
    TextView mKmTravelled;
    @BindView(R.id.iv_dashboard_profile)
    ImageView dashboardProfile;

    private UserDetails userDetails;

    @Override
    protected void setUp() {
        TextView toolbarTitle = findViewById(R.id.tv_toolbar_title);
        toolbarTitle.setText(getString(R.string.dashboard));

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_layout);
        ButterKnife.bind(this);
        userDetails = SessionManager.getInstance().loadUserDetails(this);
        if (userDetails != null) {
            Glide.with(this).load(AppConstants.IMAGE_URL + userDetails.getProfilePicture()).apply(new RequestOptions()
                    .error(R.drawable.ic_user_silhouette)
                    .centerCrop().placeholder(R.drawable.ic_user_silhouette)).into(dashboardProfile);
        }
        getDashboard();
    }

    @OnClick(R.id.card_btn_rides)
    public void onShowRides() {
        UIHelper.getInstance().switchActivity(this, TripHistory.class, null, null, null, false);
    }

    @OnClick(R.id.card_accepted_rides)
    public void onShowCompletedRides() {
        UIHelper.getInstance().switchActivity(this, TripHistory.class, null, null, null, false);
    }

    @OnClick(R.id.card_cancelled_rides)
    public void onCancelRides() {
        UIHelper.getInstance().switchActivity(this, TripHistory.class, null, null, null, false);
    }

    @OnClick(R.id.card_btn_payment)
    public void onShowPayment() {
        UIHelper.getInstance().switchActivity(this, PaymentActivity.class, null, null, null, false);
    }

    @OnClick(R.id.image_back)
    public void onBack() {
        onBackPressed();
    }

    @OnClick(R.id.iv_refresh)
    public void refreshDashboard() {
        getDashboard();
    }

    private void getDashboard() {
        showDialogProgressBar();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<DashboardModel>() {
            @Override
            public void onResponse(Response<QualStandardResponse<DashboardModel>> response) {
                hideDialogProgressBar();
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        DashboardModel dashboardModel = response.body().getResponse();
                        mTotalRides.setText(dashboardModel.getTotalRides().toString());
                        mEarnings.setText(getString(R.string.in_naira, dashboardModel.getTotalEarnings().toString()));
                        mRidesAccepted.setText(dashboardModel.getTotalAcceptedRides().toString());
                        mRidesCancelled.setText(dashboardModel.getTotalCanceledRides().toString());
                        mKmTravelled.setText(dashboardModel.getTotalKm().toString());
                        mVehicleName.setText(dashboardModel.getDriverDetails().getVehicleName());
                        mLastLogin.setText(getString(R.string.last_login, Timing.getTimeInString(dashboardModel.getDriverDetails().getLastLogin(), Timing.TimeFormats.DD_MM_YYYY_HH_MM_A)));
                        mWelcomeUser.setText(getString(R.string.welcome_back_user, dashboardModel.getDriverDetails().getName()));
                        mBalance.setText(getString(R.string.available_balance_, dashboardModel.getDriverDetails().getWalletBalance()));
                    } catch (Exception e) {
                        Log.d(getClass().getSimpleName(), "error");
                    }

                }
            }

            @Override
            public void onError(Response<QualStandardResponse<DashboardModel>> response) {
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
                        hideDialogProgressBar();
                        getDashboard();
                    });
                }
            }
        }, SessionManager.getAccessToken()).getDashboard();
    }
}
