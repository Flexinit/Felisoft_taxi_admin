package com.example.drupp_driver.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.drupp_driver.Models.DriverProfileModel;
import com.example.drupp_driver.Models.MyResponse;
import com.example.drupp_driver.Models.RxMessageEvent;
import com.example.drupp_driver.Models.Token;
import com.example.drupp_driver.Models.UserDetails;
import com.example.drupp_driver.Models.UserModel;
import com.example.drupp_driver.Models.VehicleDetails;
import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.CommonUtils;
import com.example.drupp_driver.Utils.Helper;
import com.example.drupp_driver.Utils.RxBus;
import com.example.drupp_driver.connectivity.BaseModelHandler;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetwork;
import com.example.drupp_driver.connectivity.http.INetworkList;
import com.example.drupp_driver.connectivity.http.QualStandardResponse;
import com.example.drupp_driver.connectivity.http.QualStandardResponseList;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.ui.base.MainBaseActivity;
import com.example.drupp_driver.ui.dashboard.DashboardActivityNew;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.theartofdev.edmodo.cropper.CropImage;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;

public class ProfileActivity extends MainBaseActivity {
    @BindView(R.id.btn_profile)
    Button mEditProfile;
    private TextView mDriverName, mLicenseNumber;
    @BindView(R.id.tv_interior_front)
    TextView tvInteriorFront;
    @BindView(R.id.tv_interior_back)
    TextView tvInteriorBack;
    @BindView(R.id.tv_car_side_left)
    TextView tvCarSideLeft;
    @BindView(R.id.tv_car_side_right)
    TextView tvCarSideRight;

    @BindView(R.id.et_vehicle_name)
    EditText mVehicleName;
    @BindView(R.id.et_first_name)
    EditText mFirstName;
    @BindView(R.id.et_last_name)
    EditText mLastName;
    @BindView(R.id.et_mobile)
    TextView mPhone;
    @BindView(R.id.et_email)
    EditText mEmail;
    @BindView(R.id.et_city)
    EditText mCity;
    @BindView(R.id.iv_profile_image)
    CircleImageView profileImage;
    @BindView(R.id.iv_front_image)
    ImageView frontImage;
    @BindView(R.id.iv_back_image)
    ImageView backImage;
    @BindView(R.id.iv_interior_front)
    ImageView interiorFront;
    @BindView(R.id.iv_interior_back)
    ImageView interiorBack;
    @BindView(R.id.iv_engine_image)
    ImageView engineImage;
    @BindView(R.id.iv_boot_image)
    ImageView bootImage;
    private UserDetails userDetails;
    private VehicleDetails vehicleDetails;
    private boolean isEdit;
    private String imageUri;
    private SwitchCompat mDriverStatus;
    private DatabaseReference mDatabase;
    protected AlertDialog mAlertDialog;
    private ProgressDialog mProgressDialog;

    @Override
    protected void setUp() {


    }

    // Display profile details
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_new);
        ButterKnife.bind(this);
        mDriverStatus =findViewById(R.id.driver_status);
        setOnlineStatus();
        userDetails = SessionManager.getInstance().loadUserDetails(this);
        mDriverName = findViewById(R.id.tv_driver_name);
        String interiorFront="Interior \n front";
        String interiorBack="Interior \n back";
        String sideLeft="Side \n left";
        String sideRight="Side \n right";

        tvInteriorFront.setText(interiorFront);
        tvInteriorBack.setText(interiorBack);
        tvCarSideLeft.setText(sideLeft);
        tvCarSideRight.setText(sideRight);

        //mLicenseNumber = findViewById(R.id.tv_license_number);
        //driverType = findViewById(R.id.tv_driver_type);

        mVehicleName = findViewById(R.id.et_vehicle_name);
        findViewById(R.id.image_back).setOnClickListener(v -> onBackPressed());
        //  vehicleImages.setLayoutManager(new GridLayoutManager(this, 2));

        getDriverProfile();
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
                        SessionManager.getInstance().saveUser(ProfileActivity.this, userModel);
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

    @OnClick(R.id.btn_profile)
    public void onEdit() {
        isEdit = !isEdit;

        profileImage.setClickable(isEdit);

        mFirstName.setClickable(isEdit);
        mFirstName.setCursorVisible(isEdit);
        mFirstName.setFocusableInTouchMode(isEdit);

        mLastName.setClickable(isEdit);
        mLastName.setCursorVisible(isEdit);
        mLastName.setFocusableInTouchMode(isEdit);

        mCity.setCursorVisible(isEdit);
        mCity.setClickable(isEdit);
        mCity.setFocusableInTouchMode(isEdit);

        mPhone.setCursorVisible(isEdit);
        mPhone.setClickable(isEdit);
        mPhone.setFocusableInTouchMode(isEdit);

        mEmail.setCursorVisible(isEdit);
        mEmail.setClickable(isEdit);
        mEditProfile.setText(isEdit ? getString(R.string.done) : getString(R.string.edit_profile));

        if (!isEdit && isValidate()) {
            editProfile();
        }

    }

    private boolean isValidate() {

        if (mFirstName.getText().toString().trim().isEmpty()) {
            showMessage(R.string.please_enter_first_name);
            return false;
        } else if (mLastName.getText().toString().trim().isEmpty()) {
            showMessage(R.string.please_enter_first_name);
            return false;
        } else if (mEmail.getText().toString().trim().isEmpty()) {
            showMessage(R.string.please_enter_valid_email);
            return false;
        }
        return true;
    }

    private void editProfile() {
        showDialogProgressBar();
        HashMap<String, String> params = new HashMap<>();
        params.put(AppConstants.K_FIRST_NAME, mFirstName.getText().toString().trim());
        params.put(AppConstants.K_LAST_NAME, mLastName.getText().toString().trim());
        params.put(AppConstants.K_EMAIL, mEmail.getText().toString().trim());
        params.put(AppConstants.K_COUNTRY_CODE, userDetails.getCountryCode().toString());
        params.put(AppConstants.K_PHONE, userDetails.getPhone());
        params.put(AppConstants.K_ADDRESS, mCity.getText().toString().trim());

        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<String>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<String>> response) {
                hideDialogProgressBar();
                if (response.isSuccessful() && response.body() != null) {
                    RxBus.getInstance().setIntentEvent(new RxMessageEvent(getString(R.string.full_name, mFirstName.getText().toString().trim(),
                            mLastName.getText().toString().trim()), AppConstants.RX_EVENT.NAME_UPDATE));
                    RxBus.getInstance().setIntentEvent(new RxMessageEvent(mCity.getText().toString(), AppConstants.RX_EVENT.CITY_UPDATE));

                    showMessage(response.body().getErrorResponse());
                }
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<String>> response) {
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
                    mAlertDialog.findViewById(R.id.btn_ok)
                            .setOnClickListener(view -> editProfile());
                }
            }
        }).editDriverProfile(params);
    }

    @OnClick(R.id.iv_profile_image)
    public void onProfileChange() {
        PickImageDialog.build(new PickSetup())
                .setOnPickResult(this::setImageForCrop).show(ProfileActivity.this);
    }

    private void setImageForCrop(PickResult r) {
        Intent intent = CropImage.activity(r.getUri()).getIntent(this);
        startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    private void uploadProfilePic(String filePath) {
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<MyResponse>() {
            private String profileImageUrl;

            @Override
            public void onResponse(Response<QualStandardResponse<MyResponse>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {

                    profileImageUrl = response.body().getResponse().getFile_path();
                    RxBus.getInstance().setIntentEvent(new RxMessageEvent(imageUri, AppConstants.RX_EVENT.PROFILE_PIC_UPDATE));
                    //     Helper.saveProfileUrl(profileImageUrl, ProfileActivity.this);
                    Glide.with(ProfileActivity.this).load(imageUri).apply(new RequestOptions()
                            .error(R.drawable.ic_user_silhouette)
                            .centerCrop().placeholder(R.drawable.ic_user_silhouette)).into(profileImage);
                    showMessage(R.string.image_uploaded_successfully);
                }
            }

            @Override
            public void onError(Response<QualStandardResponse<MyResponse>> response) {
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
                        hideAlertDialog();
                        uploadProfilePic(filePath);
                    });
                }
            }
        }, SessionManager.getAccessToken()).updateUserImage(filePath);
    }


    private void showCustomDailog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_alert_dailog_emergancy, null);
        AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = alertDialogbuilder.create();
        alertDialog.setView(alertLayout);

        alertDialog.show();


        alertDialog.findViewById(R.id.btOk).setOnClickListener(v -> alertDialog.dismiss());


    }


    private void getDriverProfile() {
        showDialogProgressBar();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<DriverProfileModel>() {
            @Override
            public void onResponse(Response<QualStandardResponse<DriverProfileModel>> response) {
                hideDialogProgressBar();
                if (response.isSuccessful() && response.body() != null) {
                    DriverProfileModel driverProfileModel = response.body().getResponse();
                    try {
                        mFirstName.setText(driverProfileModel.getFirstName());
                        mLastName.setText(driverProfileModel.getLastName());
                        mDriverName.setText(getString(R.string.full_name, driverProfileModel.getFirstName(), driverProfileModel.getLastName()));
                        Log.d("ProfileActivity","Email is: "+ driverProfileModel.getEmail());
                        mEmail.setText(driverProfileModel.getEmail());
                        //mLicenseNumber.setText(driverProfileModel.getLicenseNumber());
                        mVehicleName.setText(driverProfileModel.getVehicleName());
                        Log.d("ProfileActivity","Vehicle name is: "+ driverProfileModel.getVehicleName());
                        mCity.setText(driverProfileModel.getCity());
                        mPhone.setText(getString(R.string.complete_phone_number, driverProfileModel.getCountryCode(), driverProfileModel.getPhone()));


                        Glide.with(ProfileActivity.this).load(AppConstants.IMAGE_URL + response.body().getResponse().getProfilePicture()).apply(new RequestOptions()
                                .error(R.drawable.no_image_available)
                                .centerCrop().placeholder(R.drawable.no_image_available)).into(profileImage);
                        Log.d("ProfileActivity","Profile picture is: "+ response.body().getResponse().getProfilePicture());

                        Glide.with(ProfileActivity.this).load(AppConstants.IMAGE_URL + response.body().getResponse().getExteriorFront()).apply(new RequestOptions()
                                .error(R.drawable.no_image_available)
                                .centerCrop().placeholder(R.drawable.no_image_available)).into(frontImage);

                        Glide.with(ProfileActivity.this).load(AppConstants.IMAGE_URL + response.body().getResponse().getExteriorBack()).apply(new RequestOptions()
                                .error(R.drawable.no_image_available)
                                .centerCrop().placeholder(R.drawable.no_image_available)).into(backImage);

                        Glide.with(ProfileActivity.this).load(AppConstants.IMAGE_URL + response.body().getResponse().getInteriorFront()).apply(new RequestOptions()
                                .error(R.drawable.no_image_available)
                                .centerCrop().placeholder(R.drawable.no_image_available)).into(interiorFront);

                        Glide.with(ProfileActivity.this).load(AppConstants.IMAGE_URL + response.body().getResponse().getInteriorBack()).apply(new RequestOptions()
                                .error(R.drawable.no_image_available)
                                .centerCrop().placeholder(R.drawable.no_image_available)).into(interiorBack);

                        Glide.with(ProfileActivity.this).load(AppConstants.IMAGE_URL + response.body().getResponse().getEngine()).apply(new RequestOptions()
                                .error(R.drawable.no_image_available)
                                .centerCrop().placeholder(R.drawable.no_image_available)).into(engineImage);

                        Glide.with(ProfileActivity.this).load(AppConstants.IMAGE_URL + response.body().getResponse().getBoot()).apply(new RequestOptions()
                                .error(R.drawable.no_image_available)
                                .centerCrop().placeholder(R.drawable.no_image_available)).into(bootImage);

                    } catch (Exception e) {
                        Log.d(getClass().getSimpleName(), "Error");
                    }

                    /*try {
                        switch (driverProfileModel.getDriverType()) {
                            case AppConstants.CAR_DRIVER:
                                if (driverProfileModel.getVehicleTypeId() == AppConstants.WITHOUT_AC) {
                                    driverType.setText(R.string.regular);
                                } else if (driverProfileModel.getVehicleTypeId() == AppConstants.WITH_AC) {
                                    driverType.setText(R.string.luxury);
                                }
                                break;
                            case AppConstants.BUS_DRIVER:
                                driverType.setText(R.string.bus_driver);
                                break;
                            case AppConstants.KEKE_DRIVER:
                                driverType.setText(R.string.keke_driver);
                                break;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/


                }
            }

            @Override
            public void onError(Response<QualStandardResponse<DriverProfileModel>> response) {
                hideDialogProgressBar();
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
                        getDriverProfile();
                    });
                }
            }
        }, SessionManager.getAccessToken()).getDriverProfile();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (data != null) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                imageUri = result.getUri().getPath();
                uploadProfilePic(result.getUri().getPath());
            }
        }
    }
}
