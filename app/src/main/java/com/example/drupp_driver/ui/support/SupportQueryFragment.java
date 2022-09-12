package com.example.drupp_driver.ui.support;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.drupp_driver.Models.Trip;
import com.example.drupp_driver.Models.UserModel;
import com.example.drupp_driver.R;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetwork;
import com.example.drupp_driver.connectivity.http.QualStandardResponse;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.ui.SupportMessageActivity;
import com.example.drupp_driver.ui.base.MainBaseFragment;
import com.victor.loading.rotate.RotateLoading;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

public class SupportQueryFragment extends MainBaseFragment {
    @BindView(R.id.tvdate)
    TextView date;
    @BindView(R.id.tvTime)
    TextView time;
    @BindView(R.id.tvAddress)
    TextView address;
    @BindView(R.id.tvModel)
    TextView model;
    @BindView(R.id.tvCRN_num)
    TextView veh_num;
    @BindView(R.id.rl_support)
    RotateLoading rotateLoading;

    String ride_id, ride_date, source, destination, issue;

    @Override
    protected void initViewsForFragment(View view) {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_alert_dailog_emergancy, null);
        AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(getmContext());
        final AlertDialog alertDialog = alertDialogbuilder.create();
        alertDialog.setView(alertLayout);

        getRecentRide();
    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_support_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.lin_op4)
    public void onRideCancel() {

        passIntent(getString(R.string.ride_cancellation_fee));
    }

    @OnClick(R.id.lin_op3)
    public void onLostAnItem() {
        passIntent(getString(R.string.lost_an_item));
    }

    @OnClick(R.id.lin_op2)
    public void onUserMisconduct() {
        passIntent(getString(R.string.user_misconduct));

    }

    @OnClick(R.id.lin_op1)
    public void onResendReceipt() {
        passIntent(getString(R.string.resend_reciept));
    }


    private void passIntent(String issue) {
        Intent intent = new Intent(getmActivity(), SupportMessageActivity.class);
        intent.putExtra(AppConstants.K_RIDE_DATE, ride_date);
        intent.putExtra(AppConstants.K_RIDE_ID, ride_id);
        intent.putExtra(AppConstants.K_SOURCE, source);
        intent.putExtra(AppConstants.K_DESTINATION, destination);
        intent.putExtra(AppConstants.K_ISSUE, issue);
        startActivity(intent);
    }

    private void getRecentRide() {

        rotateLoading.start();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<Trip>() {
            @Override
            public void onResponse(Response<QualStandardResponse<Trip>> response) {
                rotateLoading.stop();
                if (response.isSuccessful() && response.body() != null) {
                    Trip t = response.body().getResponse();

                    date.setText(handleDateString(t.getRideDate()));
                    time.setText(handleTimeString(t.getRideDate()));

                    ride_id = String.valueOf(t.getRideId());
                    source = t.getSource();
                    destination = t.getDestination();
                    ride_date = t.getRideDate();

                    UserModel um = SessionManager.getInstance().getUserModel();

                    model.setText(um.getVeh_model());
                    veh_num.setText(um.getVehichle_number());

                    address.setText(t.getSource());

                    rotateLoading.stop();
                }
            }

            @Override
            public void onError(Response<QualStandardResponse<Trip>> response) {
                rotateLoading.stop();
                showErrorPrompt(response);
            }

            @Override
            public void onNullResponse() {
                rotateLoading.stop();
            }

            @Override
            public void onFailure(Throwable t) {
                rotateLoading.stop();
                showAlertDialog(R.layout.dialog_network_error);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
                        hideAlertDialog();
                        getRecentRide();
                    });
                }
            }
        }, SessionManager.getAccessToken()).getRecentRide();


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
}
