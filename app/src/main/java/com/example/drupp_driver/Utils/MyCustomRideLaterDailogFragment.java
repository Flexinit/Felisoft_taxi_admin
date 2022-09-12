package com.example.drupp_driver.Utils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.drupp_driver.Models.Token;
import com.example.drupp_driver.R;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetworkList;
import com.example.drupp_driver.connectivity.http.QualStandardResponseList;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.ui.base.DialogBaseFragment;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import retrofit2.Response;

public class MyCustomRideLaterDailogFragment extends DialogBaseFragment {

    private static final String TAG = "MyCustomDialogFragment";
    public static String auth = "";

    private TextView mSource, mDestination, mDate, mRiderName;
    //--------------------Globals--------------------L
    private String request_id;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        auth = "Bearer " + SessionManager.getInstance().getUserModel().getToken();


    }

    @Override
    protected void initViewsForFragment(View view) {

        mSource = view.findViewById(R.id.tv_rider_source);
        mDestination = view.findViewById(R.id.tvDestination);
        mDate = view.findViewById(R.id.tv_date_to);
        mRiderName = view.findViewById(R.id.tv_rider_name);
    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.custom_alert_dailog_ride_later, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        HashMap<String, String> map = Helper.getRideNotification(Objects.requireNonNull(getActivity()));
        if (map != null) {

            mSource.setText(map.get(AppConstants.K_SOURCE));
            mDestination.setText(map.get(AppConstants.K_DESTINATION));
            mDate.setText(handleDateString(map.get(AppConstants.K_RIDE_DATE)) + " " + handleTimeString(map.get(AppConstants.K_RIDE_DATE)));
            mRiderName.setText(map.get(AppConstants.K_NAME));
            request_id = map.get(AppConstants.K_RIDE_ID);
        } else {
            Toast.makeText(getContext(), getString(R.string.some_thing_went_wrong), Toast.LENGTH_SHORT).show();
        }
        view.findViewById(R.id.btStartRide).setOnClickListener(v -> {
            dismiss();
            if (request_id != null) {
                acceptRide(request_id);
            } else {
                Toast.makeText(getContext(), getString(R.string.some_thing_went_wrong), Toast.LENGTH_SHORT).show();
                return;
            }


        });

        view.findViewById(R.id.btn_reject).setOnClickListener(v -> dismiss());


    }

    private String handleDateString(String timeStamp) {
        Date given_timestamp = new Date(Long.valueOf(timeStamp) * 1000);

        String my_timestamp = String.valueOf(given_timestamp);

        String[] arr = my_timestamp.split(" ");
        String month = arr[1];
        String day = arr[2];
        String year = arr[5];

        return (day + ":" + month + ":" + year);
    }


    private String handleTimeString(String timeStamp) {
        Date given_timestamp = new Date(Long.valueOf(timeStamp) * 1000);

        String my_timestamp = String.valueOf(given_timestamp);

        String[] arr = my_timestamp.split(" ");
        String month = arr[1];
        String day = arr[2];
        String time = arr[3];
        String year = arr[5];

        String xdate = (day + ":" + month + ":" + year);
        return time;
    }


    private void acceptRide(String id) {
        DruppRequestHandler.clearInstance();

        HashMap<String, Integer> parse = new HashMap<>();
        parse.put(AppConstants.K_STATUS, 2);
        parse.put(AppConstants.K_RIDE_ID, Integer.valueOf(id));

        //retrieveValuesFromListMethod(parse);

        DruppRequestHandler.getInstance(new INetworkList<Token>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<Token>> response) {
                if (response.isSuccessful() && response.body() != null) {

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
                showMessage(R.string.some_thing_went_wrong);
            }
        },SessionManager.getAccessToken()).driverAcceptRide(parse);
    }
}
