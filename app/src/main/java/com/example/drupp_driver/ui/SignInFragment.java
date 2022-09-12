package com.example.drupp_driver.ui;

import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.drupp_driver.Models.FireBaseToken;
import com.example.drupp_driver.Models.LoginResponse;
import com.example.drupp_driver.Models.UserDetails;
import com.example.drupp_driver.Models.UserModel;
import com.example.drupp_driver.Models.VehicleDetails;
import com.example.drupp_driver.R;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetwork;
import com.example.drupp_driver.connectivity.http.QualStandardResponse;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.CcHelper;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.ui.base.MainBaseFragment;
import com.google.firebase.iid.FirebaseInstanceId;
import com.rilixtech.CountryCodePicker;
import com.sachinvarma.easylocation.EasyLocationInit;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Objects;

import butterknife.ButterKnife;
import retrofit2.Response;

public class SignInFragment extends MainBaseFragment {
    private Button btnSignIn;
    private CountryCodePicker ccp;
    private EditText edtPhoneNumber;
    private final int TIME_INTERVAL = 3000;
    private final int FASTED_TIME_INTERVAL = 3000;
    @Override
    protected void initViewsForFragment(View view) {
        btnSignIn.setOnClickListener(v -> {
            checkToken();
        });
    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        ButterKnife.bind(this, view);
        btnSignIn = view.findViewById(R.id.button_sign_in);

        ccp = view.findViewById(R.id.country_code_picker);

        edtPhoneNumber = view.findViewById(R.id.et_input_email);

        edtPhoneNumber.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                if (isValidate()) {
                    CcHelper.cc = ccp.getSelectedCountryCode();
                    CcHelper.number = edtPhoneNumber.getText().toString();
                    checkToken();
                }
            }
            return false;
        });

        new EasyLocationInit(getActivity(), TIME_INTERVAL, FASTED_TIME_INTERVAL, true);


        return view;
    }
    private boolean isValidate() {
        if (edtPhoneNumber.getText().toString().trim().isEmpty() || edtPhoneNumber.getText().toString().trim().length() < 10) {
            showMessage(R.string.please_add_a_valid_number);
            return false;
        }
        return true;
    }

    private void checkToken() {
        String token = SessionManager.getInstance().loadFireBaseToken(getActivity()).getFirebaseToken();
        if (token == null || token.isEmpty()) {
            generateToken(true);
        } else {
            generateToken(false);
            getIn(token, false);
        }

    }

    private void generateToken(boolean doLogin) {
        if (doLogin) showLoading();
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(getClass().getSimpleName(), "getInstanceId failed", task.getException());
                        showErrorPrompt(task.getException().getLocalizedMessage());
                        if (doLogin) hideLoading();
                        return;
                    }
                    String token = Objects.requireNonNull(task.getResult()).getToken();
                    SessionManager.getInstance().saveFireBaseToken(getActivity(), new FireBaseToken(token, "1"));
                    if (doLogin) getIn(token, true);
                });
    }

    private void getIn(String fbToken, boolean doLogin) {
        if (!doLogin) showLoading();
        try {
            HashMap<String, String> parse = new HashMap<>();
            parse.put(AppConstants.K_COUNTRY_CODE, ccp.getSelectedCountryCode());
            parse.put(AppConstants.K_PHONE, edtPhoneNumber.getText().toString());
            parse.put(AppConstants.K_PLATFORM, "1");
            parse.put(AppConstants.K_FIREBASE_TOKEN, fbToken);
            DruppRequestHandler.getInstance(new INetwork<LoginResponse>() {
                @Override
                public void onResponse(Response<QualStandardResponse<LoginResponse>> response) {
                    hideLoading();
                    if (response.isSuccessful() && response.body() != null) {
                        UserModel userModel = new UserModel();
                        if (response.code() == 200) {

                            try {

                                showMessage("email is: "+response.body().getResponse().getUserDetails().getEmail());

                                String token = response.body().getResponse().getToken();
                                VehicleDetails vehicle_details = response.body().getResponse().getVehicleDetails();


                                userModel.setFbToken(parse.get(AppConstants.K_FIREBASE_TOKEN));
                                userModel.setId(response.body().getResponse().getUserDetails().getId());
                                userModel.setCountryCode(Integer.parseInt(ccp.getSelectedCountryCode()));
                                userModel.setName(vehicle_details.getName());
                                userModel.setVehichle_name(vehicle_details.getVehicleName());
                                userModel.setVehichle_number(vehicle_details.getVehicleNumber());
                                userModel.setColor(vehicle_details.getVehicleColor());
                                userModel.setEmail("");
                                userModel.setVeh_brand(vehicle_details.getVehicleBrand());
                                userModel.setVeh_chassis_num(vehicle_details.getChassisNumber());
                                userModel.setVeh_model(vehicle_details.getVehicleModel());
                                userModel.setToken(token);

                                UserDetails userDetails = response.body().getResponse().getUserDetails();
                                userDetails.setToken(token);

                                SessionManager.getInstance().saveUserDetails(getActivity(), userDetails);
                                SessionManager.getInstance().saveUser(getActivity(), userModel);

                                Intent intent = new Intent(getActivity(), RideActivity.class);
                                intent.putExtra(AppConstants.K_LAUNCH_TYPE, AppConstants.EXISTING_USER);
                                intent.putExtra(AppConstants.K_PHONE_NUMBER, edtPhoneNumber.getText().toString());
                                intent.putExtra(AppConstants.K_COUNTRY_CODE, ccp.getSelectedCountryCode());
                                startActivity(intent);
                                getActivity().finish();
                            } catch (Exception e) {

                            }
                        } else if (response.code() == 201) {


                           /* Intent intent = new Intent(StartUpActivity.this, VerificationActivity.class);
                            intent.putExtra(AppConstants.K_PHONE_NUMBER, edtPhoneNumber.getText().toString());
                            intent.putExtra(AppConstants.K_COUNTRY_CODE, ccp.getSelectedCountryCode());
                            userModel.setCountryCode(Integer.parseInt(ccp.getSelectedCountryCode()));
                            userModel.setNumber(edtPhoneNumber.getText().toString());
                            SessionManager.getInstance().saveUser(StartUpActivity.this, userModel);
                            startActivity(intent);
                            finish();*/
                        }


                    }
                }

                @Override
                public void onError(Response<QualStandardResponse<LoginResponse>> response) {
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
                    showErrorPrompt(R.string.some_error);
                }
            }).numberVerification(parse);
        } catch (Exception e) {
            Log.e("Exception: ", e.getMessage());
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
    }


    private void loginWithEmail(String email) {
    }



}