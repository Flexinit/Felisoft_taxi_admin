package com.example.drupp_driver.ui.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.drupp_driver.R;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetworkList;
import com.example.drupp_driver.connectivity.http.QualStandardResponseList;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.IMainDialogResponseObserver;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.ui.base.DialogBaseFragment;
import com.mukesh.OtpView;

import java.util.HashMap;

import retrofit2.Response;

public class CodeVerificationDialog extends DialogBaseFragment {
    private OtpView mOtpView;
    private Button mStartRide;

    private String rideID;
    private IMainDialogResponseObserver iMainDialogResponseObserver;

    public static CodeVerificationDialog newInstance(Bundle bundle) {
        CodeVerificationDialog codeVerificationDialog = new CodeVerificationDialog();
        codeVerificationDialog.setArguments(bundle);
        return codeVerificationDialog;
    }

    public void setiMainDialogResponseObserver(IMainDialogResponseObserver iMainDialogResponseObserver) {
        this.iMainDialogResponseObserver = iMainDialogResponseObserver;
    }

    @Override
    protected void initViewsForFragment(View view) {

    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.dialog_code_verification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mOtpView = view.findViewById(R.id.otp_verification_view);
        mOtpView.setFocusable(true);
        mOtpView.setFocusableInTouchMode(true);

        mStartRide = view.findViewById(R.id.btn_start_ride);
        mStartRide.setEnabled(true);

        if (getArguments() != null) {
            rideID = getArguments().getString(AppConstants.K_RIDE_ID);
        }

        /*mOtpView.setOtpCompletionListener(s -> {
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            codeVerification(s);
        });*/

        mStartRide.setOnClickListener(v -> {
            if(mOtpView.getText().toString().length()!=mOtpView.getItemCount()){
                showErrorPrompt("Enter OTP in full");
            }
            else{
                getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                codeVerification(mOtpView.getText().toString());
            }
            //dismiss();
            //iMainDialogResponseObserver.onDialogResult(IMainDialogResponseObserver.RESULTOK);
        });
    }

    private void codeVerification(String code) {
        HashMap<String, String> parse = new HashMap<>();
        parse.put(AppConstants.K_RIDE_ID, rideID);
        parse.put(AppConstants.K_ONE_TIME_PASSWORD, code);
        showDialogProgressBar();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<String>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<String>> response) {
                hideDialogProgressBar();
                if (response.isSuccessful()) {
                    dismiss();
                    iMainDialogResponseObserver.onDialogResult(IMainDialogResponseObserver.RESULTOK);
                    //mStartRide.setEnabled(true);
                }
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<String>> response) {

                hideDialogProgressBar();
                Toast.makeText(getActivity(),"Wrong OTP",Toast.LENGTH_SHORT).show();
                showErrorPrompt(response);
            }

            @Override
            public void onNullListResponse() {
                hideDialogProgressBar();

            }

            @Override
            public void onFailureList(Throwable t) {
                hideDialogProgressBar();
                showAlertDialog(R.layout.dialog_network_error);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
                        hideAlertDialog();
                        codeVerification(code);
                    });
                }
            }
        }, SessionManager.getAccessToken()).codeVerification(parse);
    }
}
