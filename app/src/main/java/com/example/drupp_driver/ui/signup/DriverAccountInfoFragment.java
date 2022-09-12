package com.example.drupp_driver.ui.signup;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.example.drupp_driver.Models.BankDetailsModel;
import com.example.drupp_driver.Models.LoginResponse;
import com.example.drupp_driver.Models.UserDetails;
import com.example.drupp_driver.Models.UserModel;
import com.example.drupp_driver.Models.VehicleDetails;
import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.Helper;
import com.example.drupp_driver.Utils.IDialogButtonClickListener;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetwork;
import com.example.drupp_driver.connectivity.http.INetworkList;
import com.example.drupp_driver.connectivity.http.QualStandardResponse;
import com.example.drupp_driver.connectivity.http.QualStandardResponseList;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.CcHelper;
import com.example.drupp_driver.helpers.ISignUpObserver;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.helpers.UIHelper;
import com.example.drupp_driver.ui.LoginActivity;
import com.example.drupp_driver.ui.MainActivity;
import com.example.drupp_driver.ui.RideActivity;
import com.example.drupp_driver.ui.VerificationActivity;
import com.example.drupp_driver.ui.base.MainBaseFragment;
import com.example.drupp_driver.ui.dialogs.CompletedDialog;
import com.isapanah.awesomespinner.AwesomeSpinner;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

public class DriverAccountInfoFragment extends MainBaseFragment implements IDialogButtonClickListener {
    private ArrayList<BankDetailsModel> bankDetailsModels = new ArrayList<>();
    private ArrayList<String> bankNames = new ArrayList<>();
    private ArrayAdapter<String> bankNamesAdapter;
    @BindView(R.id.spinner_bank)
    AwesomeSpinner spinnerBank;
    @BindView(R.id.et_bank_account_number)
    EditText mBankAccountNumber;
    @BindView(R.id.et_bvn)
    EditText mBankBVN;
    private int bankSelection = -1;
    private String bankName = "";
    private ISignUpObserver iSignUpObserver;
    public void setiSignUpObserver(ISignUpObserver iSignUpObserver) {
        this.iSignUpObserver = iSignUpObserver;
    }

    @Override
    protected void initViewsForFragment(View view) {
        bankNamesAdapter = new ArrayAdapter<>(getmActivity(), android.R.layout.simple_spinner_item, bankNames);
        spinnerBank.setAdapter(bankNamesAdapter);
        spinnerBank.setSelectedItemHintColor(getResources().getColor(R.color.colorBlack));
        spinnerBank.setHintTextColor(getResources().getColor(R.color.colorBlack));
        spinnerBank.setDownArrowTintColor(getResources().getColor(R.color.colorBlack));
        spinnerBank.setOnSpinnerItemClickListener(new AwesomeSpinner.onSpinnerItemClickListener<String>() {
            @Override
            public void onItemSelected(int position, String itemAtPosition) {
                bankName = bankNames.get(position);
                bankSelection = position;
            }
        });
        getBanksList();

    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_bank_details_layout_new, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btn_sign_up)
    public void signup() {
        if (isValidate()) {
            signUpDriver();
        }


    }

    @OnClick(R.id.btCancelRide)
    public void onPrevious() {
        getmActivity().onBackPressed();
    }

    private void getBanksList() {
        showDialogProgressBar();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<BankDetailsModel>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<BankDetailsModel>> response) {
                hideDialogProgressBar();
                bankNames.clear();
                if (response.isSuccessful() && response.body() != null) {
                    for (BankDetailsModel bankDetailsModel : response.body().getResponse()) {
                        bankNames.add(bankDetailsModel.getName());
                    }
                }
                bankNamesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<BankDetailsModel>> response) {
                hideDialogProgressBar();
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
                        getBanksList();
                    });
                }
            }
        }, SessionManager.getAccessToken()).getBanks();
    }

    private boolean isValidate() {
        if (mBankAccountNumber.getText().toString().trim().isEmpty()) {
            showMessage(R.string.enter_account_number);
            return false;
        } else if (mBankBVN.getText().toString().trim().isEmpty()) {
            showMessage(R.string.enter_bank_bvn);
            return false;
        } else if (bankSelection == -1) {
            showMessage(R.string.please_select_bank);
            return false;
        }
        return true;
    }

    private void signUpDriver() {
        if (isAdded()) {
            String token = SessionManager.getInstance().loadFireBaseToken(getmContext()).getFirebaseToken();
            if (token == null) {
                token = SignUpActivity.fbToken;
            }
            showDialogProgressBar();
            Helper.saveSignUpState(AppConstants.SIGN_UP_STATUS.ACCOUNT_INFO, getmContext());
            HashMap<String, String> param = new HashMap<>();
            param.put(AppConstants.K_BANK, bankName);
            param.put(AppConstants.K_ACCOUNT_NUMBER, mBankAccountNumber.getText().toString().trim());
            param.put(AppConstants.K_BVN, mBankBVN.getText().toString().trim());
            param.put(AppConstants.K_TYPE, String.valueOf(AppConstants.SIGN_UP_STATUS.ACCOUNT_INFO));
            param.put(AppConstants.K_DRIVER_ID, ((SignUpActivity) getmActivity()).getUser().getDriverId().toString());
            param.put(AppConstants.K_FIREBASE_TOKEN, token);
            param.put(AppConstants.K_PLATFORM, "1");
            //           param.put(AppConstants.K_DRIVER_ID, iSignUpObserver.getUser().getDriverId().toString());

            DruppRequestHandler.clearInstance();
            DruppRequestHandler.getInstance(new INetwork<HashMap<String, String>>() {
                @Override
                public void onResponse(Response<QualStandardResponse<HashMap<String, String>>> response) {
                    hideDialogProgressBar();
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            Helper.saveSignUpState(AppConstants.SIGN_UP_STATUS.FINISHED, getmContext());
                            String token = response.body().getResponse().get(AppConstants.K_TOKEN);
                            String driverId = response.body().getResponse().get(AppConstants.K_DRIVER_ID);

                            UserModel userModel = new UserModel();
                            userModel.setId(Integer.valueOf(driverId));
//                            userModel.setCountryCode(Integer.valueOf(param.get(AppConstants.K_COUNTRY_CODE)));
//                            userModel.setName(param.get(AppConstants.K_FIRST_NAME) + " " + param.get(AppConstants.K_LAST_NAME).toString());
//                            userModel.setVehichle_name(param.get(AppConstants.K_VEHICLE_NAME));
//                            userModel.setVehichle_number(param.get(AppConstants.K_CHASSIS_NUMBER));
//                            userModel.setColor(param.get(AppConstants.K_VEHICLE_COLOR));
//                            userModel.setEmail(param.get(AppConstants.K_EMAIL));
//                            userModel.setVeh_brand(param.get(AppConstants.K_VEHICLE_BRAND));
//                            userModel.setVeh_chassis_num(param.get(AppConstants.K_CHASSIS_NUMBER));
//                            userModel.setVeh_model(param.get(AppConstants.K_VEHICLE_MODEL));
                            Log.d("Driver Registration","Success");
                            userModel.setToken(token);
                            userModel.setFbToken(MainActivity.fb_token);
                            CompletedDialog signInCompletedDialog=new CompletedDialog(DriverAccountInfoFragment.this,R.layout.custom_dialog_account_created);
                            signInCompletedDialog.setCancelable(false);
                            signInCompletedDialog.show(getActivity().getSupportFragmentManager(), CompletedDialog.class.getSimpleName());
                            //showMessage(R.string.registration_successfull);

                            //UIHelper.getInstance().switchActivity(getmActivity(), SplashActivity.class, UIHelper.ActivityAnimations.LEFT_TO_RIGHT, null, null, true);

                        } catch (Exception e) {
                            Log.d(getClass().getSimpleName(), "error");
                        }
                    }
                }

                @Override
                public void onError(Response<QualStandardResponse<HashMap<String, String>>> response) {
                    hideDialogProgressBar();
                    Log.d("Driver Registration","Error registering driver");
                    showErrorPrompt(response);
                }

                @Override
                public void onNullResponse() {
                    Log.d("Driver Registration","null response");
                    hideDialogProgressBar();
                }

                @Override
                public void onFailure(Throwable t) {
                    hideDialogProgressBar();
                    Log.d("Driver Registration","failure");
                    showAlertDialog(R.layout.dialog_network_error);
                    if (mAlertDialog != null) {
                        mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
                            hideAlertDialog();
                            signUpDriver();
                        });
                    }
                }
            }).userSignin(param);
        }

    }

    private void getIn(String cc, String number) {
        if (cc == null || cc.isEmpty()) {
            moveNext();
            return;
        }
        showLoading();
        try {
            HashMap<String, String> parse = new HashMap<>();
            parse.put(AppConstants.K_COUNTRY_CODE, cc);
            parse.put(AppConstants.K_PHONE, number);
            parse.put(AppConstants.K_PLATFORM, "1");
            parse.put(AppConstants.K_FIREBASE_TOKEN, SessionManager.getInstance().loadFireBaseToken(getContext()).getFirebaseToken());
            DruppRequestHandler.getInstance(new INetwork<LoginResponse>() {
                @Override
                public void onResponse(Response<QualStandardResponse<LoginResponse>> response) {
                    hideLoading();
                    if (response.isSuccessful() && response.body() != null) {
                        UserModel userModel = new UserModel();
                        if (response.code() == 200) {
                            try {

                                String token = response.body().getResponse().getToken();
                                VehicleDetails vehicle_details = response.body().getResponse().getVehicleDetails();

                                userModel.setFbToken(parse.get(AppConstants.K_FIREBASE_TOKEN));
                                userModel.setId(response.body().getResponse().getUserDetails().getId());
                                userModel.setCountryCode(Integer.parseInt(cc));
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

                                SessionManager.getInstance().saveUserDetails(getmContext(), userDetails);
                                SessionManager.getInstance().saveUser(getmContext(), userModel);

                                Intent intent = new Intent(getmContext(), RideActivity.class);
                                intent.putExtra(AppConstants.K_LAUNCH_TYPE, AppConstants.EXISTING_USER);
                                intent.putExtra(AppConstants.K_PHONE_NUMBER, number);
                                intent.putExtra(AppConstants.K_COUNTRY_CODE, cc);
                                startActivity(intent);
                                getmActivity().finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (response.code() == 201) {
                            Intent intent = new Intent(getmContext(), VerificationActivity.class);
                            intent.putExtra(AppConstants.K_PHONE_NUMBER, number);
                            intent.putExtra(AppConstants.K_COUNTRY_CODE, cc);
                            userModel.setCountryCode(Integer.parseInt(cc));
                            userModel.setNumber(number);
                            SessionManager.getInstance().saveUser(getmContext(), userModel);
                            startActivity(intent);
                            getmActivity().finish();
                        } else moveNext();
                    } else moveNext();
                }

                @Override
                public void onError(Response<QualStandardResponse<LoginResponse>> response) {
                    hideLoading();
                    showErrorPrompt(response);
                    moveNext();
                }

                @Override
                public void onNullResponse() {
                    hideLoading();
                }

                @Override
                public void onFailure(Throwable t) {
                    hideLoading();
                    showErrorPrompt(R.string.some_error);
                    moveNext();

                }
            }).numberVerification(parse);
        } catch (Exception e) {
            Log.e("Exception: ", e.getMessage());
            moveNext();
        }

    }

    private void moveNext() {
        UIHelper.getInstance().switchActivity(
                getmActivity(),
                LoginActivity.class,
                UIHelper.ActivityAnimations.LEFT_TO_RIGHT,
                null,
                null,
                true
        );
    }


    @Override
    public void onButtonClick() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getIn(
                        CcHelper.cc,
                        CcHelper.number
                );
            }
        }, 800);
    }

    @Override
    public void setUpDialogViews(View view) {

    }
}


