package com.example.drupp_driver.ui.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.drupp_driver.Models.PopState;
import com.example.drupp_driver.Models.RideInfoModel;
import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.Helper;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetwork;
import com.example.drupp_driver.connectivity.http.QualStandardResponse;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.ui.RideActivity;
import com.example.drupp_driver.ui.base.DialogBaseFragment;
import com.uzairiqbal.circulartimerview.CircularTimerListener;
import com.uzairiqbal.circulartimerview.CircularTimerView;
import com.uzairiqbal.circulartimerview.TimeFormatEnum;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;

public class RideRequestDialog extends DialogBaseFragment {
    CircularTimerView circularTimerView;
    CircleImageView ivRiderImage;
    TextView tvRiderName,tvPaymentMethod,tvRideType;
    ScaleRatingBar riderRating;
    ImageView ivClose;
    Button btnDecline,btnAccept;
    private int rideId;
    private String profileImage;
    Context context;
    public RideRequestDialog(Context context){
      this.context=context;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.AppTheme_FullScreenDialog);
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        //dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return dialog;
    }

    @Override
    protected void initViewsForFragment(View view) {

    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.custom_alert_dailog_recieved_ride_2, container, false);

        return view;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_new_ride_request, container, false);
        tvRiderName=view.findViewById(R.id.tv_rider_name);
        tvRideType=view.findViewById(R.id.tv_ride_type);
        tvPaymentMethod=view.findViewById(R.id.tv_payment_method);
        riderRating=view.findViewById(R.id.rating_bar_rider);
        ivClose=view.findViewById(R.id.iv_close);
        btnAccept=view.findViewById(R.id.btStartRide);
        btnDecline=view.findViewById(R.id.btCancelRide);
        circularTimerView=view.findViewById(R.id.timer);
        ivRiderImage=view.findViewById(R.id.iv_rider_image);
        ivClose.setOnClickListener(v -> {
            PopState popState=new PopState();
            popState.setStateType(0);
            SessionManager.getInstance().setPopState(popState);
            ((RideActivity)getActivity()).handleDriverAvailableState();
            dismiss();
        });
        btnAccept.setOnClickListener(v -> {
            dismiss();
            ((RideActivity)getActivity()).acceptRide(String.valueOf(rideId));
        });
        btnDecline.setOnClickListener(v -> {
            dismiss();
            ((RideActivity)getActivity()).declineRide();
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getRideInfo();
    }

    private void setUpTimer() {

        circularTimerView.setBackgroundColor(Color.TRANSPARENT);
        circularTimerView.setProgressColor(Color.TRANSPARENT);
        circularTimerView.setStrokeWidthDimension(6);
        circularTimerView.setCircularTimerListener(new CircularTimerListener() {
            @Override
            public String updateDataOnTick(long remainingTimeInMs) {
                return String.valueOf((int)Math.ceil((remainingTimeInMs/1000.f)));
            }

            @Override
            public void onTimerFinished() {
                PopState popState=new PopState();
                popState.setStateType(0);
                SessionManager.getInstance().setPopState(popState);
                ((RideActivity)getActivity()).handleDriverAvailableState();
                Toast.makeText(getActivity(),"Time to accept ride elapsed",Toast.LENGTH_SHORT).show();
                dismiss();
            }
        },30, TimeFormatEnum.SECONDS);

        circularTimerView.startTimer();
    }

    private void getRideInfo() {
        showLoading();

        rideId = 0;
        int isDriverPosted = -1;
        try {
            isDriverPosted = Helper.getRideType(getActivity());
            rideId = Integer.valueOf(Objects.requireNonNull(Helper.getRideNotification(getActivity()).get(AppConstants.K_RIDE_ID)));
        } catch (Exception e) {

        }

        /*setUpTimer();
        HashMap<String,String> map=Helper.getRideNotification(getActivity());
        int paymentOption=Helper.getPaymentOption(getActivity());
        int rideType = Helper.getTypeOfRide(getActivity());
        rideId=map.get(AppConstants.K_RIDE_ID);

        tvRiderName.setText(map.get(AppConstants.K_NAME));
        String method = "";
        switch (paymentOption) {
            case AppConstants.PAYMENT_METHOD.CARD:
                method = "Debit Card";
                break;
            case AppConstants.PAYMENT_METHOD.WALLET:
                method = "Wallet";
                break;
            case AppConstants.PAYMENT_METHOD.NET_BANKING:
                method = "Banking";
                break;
            case AppConstants.PAYMENT_METHOD.CASH:
                method = "Cash";
        }
        tvPaymentMethod.setText(method);
        String sRideType="";
        if(rideType==1){
            sRideType="Single";
        }
        else if(rideType==2) {
            sRideType="Pool";
        }
        tvRideType.setText(sRideType);
        String rating=map.get(AppConstants.K_AVERAGE_RATING)+"f";
        riderRating.setRating(Float.parseFloat(rating));
        Glide.with(getActivity()).load(AppConstants.IMAGE_URL + map.get(AppConstants.K_PROFILE_PICTURE)).apply(new RequestOptions()
                .error(R.drawable.ic_user_silhouette)
                .centerCrop()
                .placeholder(R.drawable.ic_user_silhouette))
                .into(ivRiderImage);

        hideLoading();*/
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<RideInfoModel>() {
            @Override
            public void onResponse(Response<QualStandardResponse<RideInfoModel>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    hideLoading();
                    circularTimerView.setProgress(0);
                    setUpTimer();


                    RideInfoModel rideInfo = response.body().getResponse();
                    tvRiderName.setText(rideInfo.getRiderName());
                    Log.d("RideRequest",rideInfo.getRiderName());
                    Log.d("RideRequest",String.valueOf(rideInfo.getRideType()));
                    Log.d("RideRequest",String.valueOf(rideInfo.getPaymentOption()));
                    Log.d("RideRequest",String.valueOf(rideInfo.getAverageRating()));

                    String method = "";
                    switch (rideInfo.getPaymentOption()) {
                        case AppConstants.PAYMENT_METHOD.CARD:
                            method = "Debit Card";
                            break;
                        case AppConstants.PAYMENT_METHOD.WALLET:
                            method = "Wallet";
                            break;
                        case AppConstants.PAYMENT_METHOD.NET_BANKING:
                            method = "Banking";
                            break;
                        case AppConstants.PAYMENT_METHOD.CASH:
                            method = "Cash";
                    }
                    tvPaymentMethod.setText(method);

                    String rideType="";
                    if(rideInfo.getRideType()==1){
                        rideType="Single";
                    }
                    else {
                        rideType="Pool";
                    }

                    tvRideType.setText(rideType);
                    String rating=rideInfo.getAverageRating()+"f";
                    riderRating.setRating(Float.parseFloat(rating));
                    Glide.with(getActivity()).load(AppConstants.IMAGE_URL + rideInfo.getProfilePicture()).apply(new RequestOptions()
                            .error(R.drawable.ic_user_silhouette)
                            .centerCrop()
                            .placeholder(R.drawable.ic_user_silhouette))
                            .into(ivRiderImage);



                }

            }

            @Override
            public void onError(Response<QualStandardResponse<RideInfoModel>> response) {
                hideLoading();
                showErrorPrompt(response);
                Log.d(getClass().getSimpleName(), "ERROR");

            }

            @Override
            public void onNullResponse() {

                Log.d(getClass().getSimpleName(), "ERROR");
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d(getClass().getSimpleName(), "ERROR");
//                hideLoading();
//                showAlertDialog(R.layout.dialog_network_error, AppConstants.NotificationType.NETWORK_ERROR);
//                if (mAlertDialog != null) {
//                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(view -> {
//                        hideAlertDialog();
//                        getRideInfo();
//                    });
//                }
            }
        }, SessionManager.getAccessToken()).getRideInfo(isDriverPosted, rideId);
    }

}
