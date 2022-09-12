package com.example.drupp_driver.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.UiUtils;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.UIHelper;
import com.example.drupp_driver.ui.base.MainBaseActivity;
import com.example.drupp_driver.ui.signup.SignUpActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import java.util.concurrent.TimeUnit;

import static com.google.firebase.auth.PhoneAuthProvider.getInstance;

public class VerificationActivity extends MainBaseActivity {

    private String mPhoneNumber, mCountryCode;
    private OtpView otpView;
    private TextView tvResend;
    private String verificationId;
    private FirebaseAuth mAuth;

    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_new);
        mAuth = FirebaseAuth.getInstance();
        TextView tvPhoneNumber = findViewById(R.id.tv_phone_number);
        tvResend = findViewById(R.id.tv_resend_code);
        otpView = findViewById(R.id.otp_verification_view);
        if (getIntent() != null) {
            mCountryCode = getIntent().getStringExtra(AppConstants.K_COUNTRY_CODE);
            mPhoneNumber = getIntent().getStringExtra(AppConstants.K_PHONE_NUMBER);
            tvPhoneNumber.setText(getString(R.string.full_number, mCountryCode, mPhoneNumber));
        }
        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String code1) {
                VerificationActivity.this.verifyOtp(code1);
            }
        });
        setUpResendTextView();
        tvResend.setOnClickListener(view -> sendOtp());
        findViewById(R.id.bt_submit).setOnClickListener(v -> {
            Editable temp = otpView.getText();
            if (temp == null) UiUtils.showToast(getString(R.string.enter_verification_code_first));
            else {
                String code = temp.toString();
                if (code.isEmpty())
                    UiUtils.showToast(getString(R.string.enter_verification_code_first));
                else verifyOtp(code);
                //else switchToNextVerificationFailed(false);
            }
        });

        sendOtp();
    }

    private void setUpResendTextView() {
        SpannableString text=new SpannableString("Resend Code");
        text.setSpan(new UnderlineSpan(),0,11,0);
        tvResend.setText(text);
    }

    private void sendOtp() {
        showLoading();
        String number = "+" + mCountryCode + mPhoneNumber;
        getInstance().verifyPhoneNumber(
                number,        // Phone number to verify
                120,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                getCallBacks());        // OnVerificationStateChangedCallbacks
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks getCallBacks() {
        return new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        PhoneAuthCredential credential;
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                showLoading();
                proceedWithCredentials(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                hideLoading();
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    UiUtils.showLongToast(e.getLocalizedMessage());
                }
                else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                    UiUtils.showLongToast( "onVerificationFailed: FirebaseTooManyRequestsException");
                } else {
                    Log.d("VerificationActivity","Error:"+e.getMessage());
                    UiUtils.showLongToast(getString(R.string.msg_sms_exceeeded));
//                    String code = otpView.getText().toString();

//                    proceedWithCredentials(PhoneAuthProvider.getCredential(verificationId,code )); // i added on purposse
                }
//                else {
//                    UiUtils.showLongToast(e.getLocalizedMessage());
//                    System.out.println("firebase error is: ");
//                    System.out.println(e);
////                    UiUtils.showLongToast(getString(R.string.msg_sms_exceeeded));
//                }

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                hideLoading();
                verificationId = s;
                UiUtils.showToast(getString(R.string.code_has_been_sent_successfully));
                startResendTime();
            }
        };
    }

    private void verifyOtp(String code) {
        try {
            showLoading();
            proceedWithCredentials(PhoneAuthProvider.getCredential(verificationId, code));
        } catch (Exception ex) {
            hideLoading();
            ex.printStackTrace();
            UiUtils.showToast(ex.getLocalizedMessage());
        }

    }

    private void proceedWithCredentials(PhoneAuthCredential credential) {
        UiUtils.showToast("proceding with credentials " + credential);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    hideLoading();
                    if (task.isSuccessful()) {
                        Snackbar.make(otpView, getString(R.string.number_verification_successful), Snackbar.LENGTH_LONG).show();
                        new Handler().postDelayed(this::switchToNext, 800);
                    } else {
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            UiUtils.showLongToast(getString(R.string.invalid_verification_code));
                        } else {
                            UiUtils.showLongToast(
                                    getString(R.string.failed_to_verify_code)
                            );
                        }
                    }
                });
        UiUtils.showToast("can not proceed " );
    }

    private void startResendTime() {
        tvResend.setText(null);
        tvResend.setEnabled(false);
        new CountDownTimer(TimeUnit.MINUTES.toMillis(1), TimeUnit.SECONDS.toMillis(1)) {
            @Override
            public void onTick(long millisLeft) {
                long tempMin = TimeUnit.MILLISECONDS.toMinutes(millisLeft);
                long tempSec = TimeUnit.MILLISECONDS.toSeconds(millisLeft);
                String minutes = tempMin < 10 ? "0" + tempMin : String.valueOf(tempMin);
                tvResend.setText(
                        new StringBuffer()
                                .append(minutes)
                                .append(":")
                                .append(tempSec)
                );
            }

            @Override
            public void onFinish() {
                tvResend.setEnabled(true);
                tvResend.setText(getString(R.string.resend_code));
            }
        }.start();
    }

    private void switchToNext() {
        if (userInfo.getToken() != null && !userInfo.getToken().isEmpty()) {
            UIHelper.getInstance().switchActivity(VerificationActivity.this, RideActivity.class, null, null, null, true);
        } else {
            Intent intent = new Intent(VerificationActivity.this, SignUpActivity.class);
            intent.putExtra(AppConstants.K_COUNTRY_CODE, mCountryCode);
            intent.putExtra(AppConstants.K_PHONE_NUMBER, mPhoneNumber);
            startActivity(intent);
            finish();
        }
    }
    private void switchToNextVerificationFailed(boolean signedIn){
        if(signedIn){
            UIHelper.getInstance().switchActivity(VerificationActivity.this, RideActivity.class, null, null, null, true);
        }
        else{
            Intent intent = new Intent(VerificationActivity.this, SignUpActivity.class);
            intent.putExtra(AppConstants.K_COUNTRY_CODE, mCountryCode);
            intent.putExtra(AppConstants.K_PHONE_NUMBER, mPhoneNumber);
            startActivity(intent);
            finish();

        }

    }
}
