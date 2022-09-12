package com.example.drupp_driver.ui.bookride;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.drupp_driver.Models.RideModel;
import com.example.drupp_driver.Models.RiderInfo;
import com.example.drupp_driver.Models.SingleRideModel;
import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.RxBus;
import com.example.drupp_driver.Utils.Timing;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetwork;
import com.example.drupp_driver.connectivity.http.QualStandardResponse;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.helpers.UIHelper;
import com.example.drupp_driver.ui.StartUpActivity;
import com.example.drupp_driver.ui.base.DialogBaseFragment;
import com.willy.ratingbar.ScaleRatingBar;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ScheduledRideDetailDialogDriver extends DialogBaseFragment {
    private String date, source, destination, fare;
    private TextView mDate, mSource, mDestination,mTime,tvReviewsNumber,rideNotYetAccepted,textDriverDetails,paymentOption;
    private TextView mDriverName;
    private TextView mFare;
    private TextView mVehicleDetails,tvCarPlate;
    private CircleImageView driverProfile;
    private Button query;
    private ScaleRatingBar ratingBar;
    private TableRow containerAmount;
    private RideModel rideModel;
    private Disposable tripDisposable;

    @Override
    protected void initViewsForFragment(View view) {

    }

    @Override
    public void onResume() {
        super.onResume();
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes(params);
    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_dialog_ride_details, container, false);
//        getToolbarBack().setOnClickListener(v -> onBackPressed());
//        setToolbarTitle(R.string.ride_details);
        mVehicleDetails = view.findViewById(R.id.tv_car_details);
        tvCarPlate= view.findViewById(R.id.tv_car_plate);
        driverProfile = view.findViewById(R.id.iv_driver_image);
        mDriverName = view.findViewById(R.id.tv_driver_name);
        mDate = view.findViewById(R.id.tv_Trip_His_Time);
        mTime=view.findViewById(R.id.tvTimeValue);
        tvReviewsNumber=view.findViewById(R.id.tv_reviews_num);
        rideNotYetAccepted=view.findViewById(R.id.tv_ride_not_yet_accepted);
        textDriverDetails=view.findViewById(R.id.tv_driver_details);
        paymentOption=view.findViewById(R.id.tv_payment);
        mSource = view.findViewById(R.id.tvSourceCity);
        mDestination = view.findViewById(R.id.tvDestinationCity);
        mFare = view.findViewById(R.id.tv_fare);
        ratingBar = view.findViewById(R.id.rate_bar);


//        query.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                RxBus.getInstance().setIntentEvent(tripHistoryModel);
//                Intent in = new Intent(TripHistoryFinalDialog.this, TripHistoryQuery.class);
//                startActivity(in);
//            }
//        });
        tripDisposable = RxBus.getInstance().getIntentEvent()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof RideModel) {
                        rideModel = (RideModel) o;
                        getData( ((RideModel) o).getRideId());


                    }
                });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tripDisposable.dispose();
    }

    private void getData(Integer rideId) {
        showDialogProgressBar();


        DruppRequestHandler.clearInstance();

        DruppRequestHandler.getInstance(new INetwork<SingleRideModel>() {
            @Override
            public void onResponse(Response<QualStandardResponse<SingleRideModel>> response) {
                hideDialogProgressBar();
                if (response.isSuccessful() && response.body() != null) {

                    try {
                        source = response.body().getResponse().getSource();
                        date = response.body().getResponse().getRideDate();
                        destination = response.body().getResponse().getDestination();
                        //fare = response.body().getResponse().getTotal_fare();
                        String datefinal = Timing.getTimeInString(Long.parseLong(date), Timing.TimeFormats.DD_MMM_YYYY);
                        String timeFinal=Timing.getTimeInString(Long.parseLong(date), Timing.TimeFormats.HH_12);
                        mDate.setText(datefinal);
                        mTime.setText(timeFinal);
                        mSource.setText(source);
                        mDestination.setText(destination);


                        String method = "";
                        if(response.body().getResponse().getPaymentOption()!=null){
                            switch (response.body().getResponse().getPaymentOption()) {
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

                            paymentOption.setText(method);
                        }


                        mFare.setText(getString(R.string.fare_in_naira,response.body().getResponse().getDriverFare()));


                        tvCarPlate.setVisibility(View.GONE);




                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("ScheduledRideDetail","exception:"+e.getMessage());
                    }

                }
            }

            @Override
            public void onError(Response<QualStandardResponse<SingleRideModel>> response) {
                hideDialogProgressBar();
                if (response.code() == 401) {
                    showAlertDialog(R.layout.dialog_app_error);
                    if (mAlertDialog != null) {
                        mAlertDialog.setCancelable(false);
                        TextView title = mAlertDialog.findViewById(R.id.tv_title);
                        title.setText(getString(R.string.your_session_has_expired));
                        Button btnOk = mAlertDialog.findViewById(R.id.btn_cancel);
                        btnOk.setText(getString(R.string.login));
                        btnOk.setOnClickListener(v -> {
                            SessionManager.getInstance().removeUserData(getActivity());
                            UIHelper.getInstance().switchActivity(getActivity(), StartUpActivity.class, null, null, null, true);
                        });
                    }
                    return;

                }
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
                        getData(rideId);
                    });
                }

            }
        }, SessionManager.getAccessToken()).getSingleScheduledDriverRide(rideId);


    }

    @Override
    public void showAlertDialog(int resId) {

    }

}

