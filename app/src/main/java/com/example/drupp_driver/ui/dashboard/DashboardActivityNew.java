package com.example.drupp_driver.ui.dashboard;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.drupp_driver.Models.DashboardCountModel;
import com.example.drupp_driver.Models.Token;
import com.example.drupp_driver.Models.UserModel;
import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.CommonUtils;
import com.example.drupp_driver.connectivity.BaseModelHandler;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetwork;
import com.example.drupp_driver.connectivity.http.INetworkList;
import com.example.drupp_driver.connectivity.http.QualStandardResponse;
import com.example.drupp_driver.connectivity.http.QualStandardResponseList;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.ui.ScheduledRidesActivity;
import com.example.drupp_driver.ui.bookride.UserRideFragment;
import com.example.drupp_driver.ui.bookride.YourRideFragment;
import com.example.drupp_driver.ui.completedrides.TripHistoryNew;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

public class DashboardActivityNew extends AppCompatActivity {
    private SwitchCompat mDriverStatus;
    private DatabaseReference mDatabase;
    protected AlertDialog mAlertDialog;
    private ProgressDialog mProgressDialog;
    private TextView tv_name_of_driver,tv_name_of_vehicle,tv_balance;
    private  ViewPager viewPager;
    private  TabLayout tabLayout;
    DashboardCountModel dashboardModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_layout_new);
        ButterKnife.bind(this);

        mDriverStatus =findViewById(R.id.driver_status);
        setOnlineStatus();

        viewPager = findViewById(R.id.pager_summary);
        tabLayout = findViewById(R.id.tabLayout_summary);
        tv_name_of_driver = findViewById(R.id.tv_name_of_driver);
        tv_name_of_vehicle = findViewById(R.id.tv_name_of_vehicle);
        tv_balance = findViewById(R.id.tv_balance);

        getDashboard();



        /*viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0) {
                    fragmentAdapter.driverPostedRides.getTripData();
                } else if (position == 1) {
                    fragmentAdapter.userPostedRides.getTripData();
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
*/

    }


    private void setOnlineStatus() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(AppConstants.FIREBASE_DATABASE.USERS);
        UserModel userModel = SessionManager.getInstance().getUserModel();
        mDriverStatus.setChecked(userModel.getDriverStatus() == 1);


        //TODO : CHANGE DRIVER STATUS
        mDriverStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mDatabase.child(AppConstants.FIREBASE_DATABASE.DRIVERS)
                    .child(String.valueOf(userModel.getId()))
                    .child(AppConstants.FIREBASE_DATABASE.AVAILABILITY).setValue(isChecked ? 1 : 0);
            changeDriverStatus(isChecked ? 1 : 0);
        });
    }

    @OnClick(R.id.image_back)
    public void onBack() {
        onBackPressed();
    }

    private void changeDriverStatus(final int status) {
        try {
            showLoading();
            DruppRequestHandler.clearInstance();
            HashMap<String, Integer> parse = new HashMap<>();
            parse.put(AppConstants.K_DRIVER_STATUS, status);
            DruppRequestHandler.getInstance(new INetworkList<Token>() {
                @Override
                public void onResponseList(Response<QualStandardResponseList<Token>> response) {
                    hideLoading();
                    if (response.isSuccessful() && response.body() != null) {
                        UserModel userModel = SessionManager.getInstance().getUserModel();
                        userModel.setDriverStatus(status);
                        SessionManager.getInstance().saveUser(DashboardActivityNew.this, userModel);
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
            }, SessionManager.getAccessToken()).managerDriverStatus(parse);
        } catch (Exception e) {
            Log.e("Exception: ", e.getMessage());
        }
    }

    public void showLoading() {
        hideKeyboard();
        mProgressDialog = CommonUtils.showLoadingDialog(this);
    }

    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }
    public void showErrorPrompt(int error) {
        showErrorPrompt(getString(error));
    }
    public boolean showErrorPrompt(Response response) {
        try {
            String error = getErrorFromResponsse(response);
            this.showErrorPrompt(error);
            return false;
        } catch (Exception ex) {
            return false;
        }
    }
    private String getErrorFromResponsse(Response response) {
        QualStandardResponse stdResponse = getStandardResponse(response);
        String error = stdResponse.getErrorResponse();
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        return error;
    }
    public QualStandardResponse getStandardResponse(Response response) {
        return BaseModelHandler.parseError(response);
    }
    public void showAlertDialog(int resId) {
        hideKeyboard();
        mAlertDialog = CommonUtils.showCustomAlertDialog(this, resId);
        mAlertDialog.show();
    }
    public void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public void showErrorPrompt(String error) {
        Toast toast = Toast.makeText(this, error, Toast.LENGTH_LONG);
        if (!toast.getView().isShown()) {
            toast.show();
        }
    }



    private class SummaryFragmentPagerAdapter extends FragmentPagerAdapter {
        // UserRideFragment userPostedRideLater;
        // DriverPostedRide driverPostedRides;
        SummaryFragment todaySummaryFragment,thisWeekSummaryFragment;

        public SummaryFragmentPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
//            userPostedRideLater = new UserPostedRide();
            //          driverPostedRides = new DriverPostedRide();

            if(dashboardModel!= null)
                todaySummaryFragment = new SummaryFragment(1,dashboardModel);
            thisWeekSummaryFragment=new SummaryFragment(2,dashboardModel);
        }

        @Override
        public Fragment getItem(int i) {

            if (i == 1) {

                return todaySummaryFragment;
            }
            return thisWeekSummaryFragment;

        }


        @Override
        public int getCount() {
            return 2;
        }
    }

    private void getDashboard() {
        showLoading();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<DashboardCountModel>() {
            @Override
            public void onResponse(Response<QualStandardResponse<DashboardCountModel>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        dashboardModel = response.body().getResponse();

                        tv_name_of_vehicle.setText(dashboardModel.getVehicleName());
                        tv_name_of_driver.setText(dashboardModel.getName());
                        tv_balance.setText(String.valueOf(dashboardModel.getWalletBalance()));
                        DashboardActivityNew.SummaryFragmentPagerAdapter fragmentAdapter = new
                                DashboardActivityNew.SummaryFragmentPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

                        tabLayout.setupWithViewPager(viewPager);
                        viewPager.setAdapter(fragmentAdapter);
                        viewPager.setCurrentItem(0);

                        try {
                            tabLayout.getTabAt(0).setText(getString(R.string.text_today));
                            tabLayout.getTabAt(1).setText(getString(R.string.text_this_week));
                        } catch (Exception e) {

                        }

                    } catch (Exception e) {
                        Log.d(getClass().getSimpleName(), "error");
                    }

                }
            }

            @Override
            public void onError(Response<QualStandardResponse<DashboardCountModel>> response) {
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
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
                        hideLoading();
                        getDashboard();
                    });
                }
            }
        }, SessionManager.getAccessToken()).getDashboardCount("today");
    }

}
