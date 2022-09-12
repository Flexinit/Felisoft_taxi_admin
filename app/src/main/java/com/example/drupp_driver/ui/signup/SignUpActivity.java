package com.example.drupp_driver.ui.signup;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.drupp_driver.Models.UserModel;
import com.example.drupp_driver.R;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.ISignUpObserver;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.helpers.UIHelper;
import com.example.drupp_driver.ui.StartUpActivity;
import com.example.drupp_driver.ui.base.MainBaseActivity;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends MainBaseActivity implements FragmentManager.OnBackStackChangedListener, ISignUpObserver {
   /* @BindView(R.id.tv_sign_up_step_title)
    TextView signUpTitle;*/
    private HashMap<String, String> mainData = new HashMap<>();

    private UserModel userModel;
    public static String fbToken;
    private TextView tvHeader;
    private TextView tvStep;
    private TextView tvJoinUs;

    @Override
    protected void setUp() {
        getToken();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_new);
        ButterKnife.bind(this);
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        //signUpTitle.setText(getString(R.string.personal_info));
        Bundle bundle = new Bundle();
        UserModel loadUser = SessionManager.getInstance().loadUser(this);
        tvHeader = findViewById(R.id.tvHeader);
        tvStep = findViewById(R.id.tvStep);
        tvJoinUs = findViewById(R.id.tvJoinUsHere);
        if (loadUser == null) {
            userModel = new UserModel();
        } else {
            userModel = loadUser;
        }
        if (getIntent() != null) {
            try {
                userModel.setCountryCode(Integer.parseInt(getIntent().getStringExtra(AppConstants.K_COUNTRY_CODE)));
                userModel.setNumber(getIntent().getStringExtra(AppConstants.K_PHONE_NUMBER));
                bundle.putString(AppConstants.K_COUNTRY_CODE, getIntent().getStringExtra(AppConstants.K_COUNTRY_CODE));
                bundle.putString(AppConstants.K_PHONE_NUMBER, getIntent().getStringExtra(AppConstants.K_PHONE_NUMBER));
            } catch (Exception e) {
                Log.d(getClass().getSimpleName(), "error");
            }

        }


        if (getIntent() != null) {
            if (getIntent().hasExtra(AppConstants.K_FIREBASE_TOKEN)) {
                fbToken = getIntent().getStringExtra(AppConstants.K_FIREBASE_TOKEN);
            }
            switch (getIntent().getIntExtra(AppConstants.K_SIGN_UP_STATE, -1)) {
                case AppConstants.SIGN_UP_STATUS.NEXT_OF_KIN:
                    tvHeader.setText("Next of Kin");
                    tvStep.setText("Step 2 of 4");

                    DriverKinInfoFragment driverKinInfoFragment = new DriverKinInfoFragment();
                    driverKinInfoFragment.setiSignUpObserver(this);
                    driverKinInfoFragment.setArguments(bundle);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.fragment_signup_container, driverKinInfoFragment, DriverKinInfoFragment.class.getSimpleName())
                            .commit();
                    break;
                case AppConstants.SIGN_UP_STATUS.DRIVER_EXPERIENCE:
                    //signUpTitle.setText(getString(R.string.driver_experience));
                    tvHeader.setText("Next of Kin");
                    tvStep.setText("Step 2 of 4");
                    DriverKinInfoFragment driverKinInfo = new DriverKinInfoFragment();
                    driverKinInfo.setiSignUpObserver(this);
                    driverKinInfo.setArguments(bundle);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.fragment_signup_container, driverKinInfo, DriverKinInfoFragment.class.getSimpleName())
                            .commit();
                    break;
                case AppConstants.SIGN_UP_STATUS.VEHICLE_INFO:
                    //signUpTitle.setText(getString(R.string.vehicle_to_register));
                    tvHeader.setText("Vehicle Registration");
                    tvStep.setText("Step 3 of 4");
                    DriverVehicleInfoFragment driverVehicleInfoFragment = new DriverVehicleInfoFragment();
                    driverVehicleInfoFragment.setiSignUpObserver(this);
                    driverVehicleInfoFragment.setArguments(bundle);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.fragment_signup_container, driverVehicleInfoFragment, DriverVehicleInfoFragment.class.getSimpleName())
                            .commit();
                    break;
                case AppConstants.SIGN_UP_STATUS.ACCOUNT_INFO:
                    //signUpTitle.setText(getString(R.string.bank_details));
                    tvHeader.setText("Bank Information");
                    tvStep.setText("Step 4 of 4");
                    DriverAccountInfoFragment driverAccountInfoFragment = new DriverAccountInfoFragment();
                    driverAccountInfoFragment.setiSignUpObserver(this);
                    driverAccountInfoFragment.setArguments(bundle);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.fragment_signup_container, driverAccountInfoFragment, DriverAccountInfoFragment.class.getSimpleName())
                            .commit();
                    break;
                default:
                    switchToPersonalInfo(bundle);
                    break;

            }

        }

    }

    private void switchToPersonalInfo(Bundle bundle) {
        DriverPersonalInfoFragment driverPersonalInfoFragment = new DriverPersonalInfoFragment();
        driverPersonalInfoFragment.setiSignUpObserver(this);
        if (bundle.isEmpty() && userModel != null) {
            bundle.putString(AppConstants.K_COUNTRY_CODE, String.valueOf(userModel.getCountryCode()));
            bundle.putString(AppConstants.K_PHONE_NUMBER, String.valueOf(userModel.getNumber()));
        }
        driverPersonalInfoFragment.setArguments(bundle);
        findViewById(R.id.tvJoinUsHere).setVisibility(View.VISIBLE);
        tvHeader.setText("Personal Information");
        tvStep.setText("Step 1 of 4");
        //signUpTitle.setText(getString(R.string.personal_info));
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_signup_container, driverPersonalInfoFragment, DriverPersonalInfoFragment.class.getSimpleName())
                .commit();

    }


    @Override
    public void onBackStackChanged() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_signup_container);
        if (fragment instanceof DriverPersonalInfoFragment) {
            tvJoinUs.setVisibility(View.VISIBLE);
            tvHeader.setText("Personal Information");
            tvStep.setText("Step 1 of 4");
            //signUpTitle.setText(getString(R.string.personal_info));
        } else if (fragment instanceof DriverKinInfoFragment) {
            tvJoinUs.setVisibility(View.GONE);
            tvHeader.setText("Next of Kin");
            tvStep.setText("Step 2 of 4");
            //signUpTitle.setText(getString(R.string.next_of_kin));
        } else if (fragment instanceof DriverExperienceFragment) {
            tvHeader.setText("Vehicle Registration");
            tvStep.setText("Step 3 of 4");
            //signUpTitle.setText(getString(R.string.driver_experience));
        } else if (fragment instanceof DriverVehicleInfoFragment) {
            tvHeader.setText("Vehicle Registration");
            tvStep.setText("Step 3 of 4");
            //signUpTitle.setText(getString(R.string.vehicle_to_register));
        } else if (fragment instanceof DriverAccountInfoFragment) {
            tvHeader.setText("Bank Information");
            tvStep.setText("Step 4 of 4");
            //signUpTitle.setText(getString(R.string.bank_details));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        SessionManager.getInstance().saveUser(this, userModel);
    }


    @Override
    public void onNextSelected(AppConstants.SIGNUP_STEP step, HashMap<String, String> dataMap) {

        switch (step) {
            case PERSONAL_INFO:
            case NEXT_OF_KIN:
                try {
                    userModel.setDriverId(Integer.valueOf(dataMap.get(AppConstants.K_DRIVER_ID)));
                    userModel.setVehicleId(Integer.valueOf(dataMap.get(AppConstants.K_VEHICLE_ID)));
                } catch (Exception e) {

                    Log.d(getClass().getSimpleName(), "error");
                }
                mainData.putAll(dataMap);
                break;
            case EXPERIENCE:
                try {
                    userModel.setVehicleId(Integer.valueOf(dataMap.get(AppConstants.K_VEHICLE_ID)));

                } catch (Exception e) {
                    Log.d(getClass().getSimpleName(), "error");
                }
                mainData.putAll(dataMap);
                break;
            case VEHICLE_TO_REGISTER:
                mainData.putAll(dataMap);
                break;
            case BANK_DETAILS:
                mainData.putAll(dataMap);
                break;
        }
        SessionManager.getInstance().saveUser(this, userModel);
    }


    @Override
    public HashMap<String, String> getParams() {
        return mainData;
    }

    @Override
    public UserModel getUser() {
        return userModel;
    }

    @Override
    public String getToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(getClass().getSimpleName(), "getInstanceId failed", task.getException());
                        return;
                    }
                    String token = Objects.requireNonNull(task.getResult()).getToken();
                    fbToken = token;
                    Log.d(getClass().getSimpleName(), "onComplete: TOKEN \n" + token);
                });
        return fbToken;
    }

    @Override
    public void onBackPressed() {
        UIHelper.getInstance().switchActivity(this, StartUpActivity.class, null, null, false);
    }
}
