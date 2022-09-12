package com.example.drupp_driver.ui.signup;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.drupp_driver.Models.MyResponse;
import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.Helper;
import com.example.drupp_driver.Utils.IDateDialogObserver;
import com.example.drupp_driver.Utils.ImagePickerActivity;
import com.example.drupp_driver.Utils.Timing;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetwork;
import com.example.drupp_driver.connectivity.http.INetworkList;
import com.example.drupp_driver.connectivity.http.QualStandardResponse;
import com.example.drupp_driver.connectivity.http.QualStandardResponseList;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.ISignUpObserver;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.helpers.UIHelper;
import com.example.drupp_driver.ui.MainActivity;
import com.example.drupp_driver.ui.StartUpActivity;
import com.example.drupp_driver.ui.base.MainBaseFragment;
import com.example.drupp_driver.ui.dialogs.DateDialog;
import com.theartofdev.edmodo.cropper.CropImage;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

public class DriverPersonalInfoFragment extends MainBaseFragment implements IDateDialogObserver {
    @BindView(R.id.editTextFirstName)
    EditText mFirstName;
    @BindView(R.id.editTextLastName)
    EditText mLastName;
    /*@BindView(R.id.et_age)
    TextView mAge;*/
    @BindView(R.id.editTextDateOfBirth)
    EditText mDob;
    @BindView(R.id.editTextPhoneNumber)
    EditText mPhone;
    /*@BindView(R.id.et_address)
    EditText mAddress;*/
    @BindView(R.id.et_State)
    EditText state;
    @BindView(R.id.et_LGA)
    EditText LGA;

    @BindView(R.id.editTextEmail)
    EditText mEmail;
    @BindView(R.id.editTextTextPassword)
    EditText mPassword;
    @BindView(R.id.editTextConfirmPassword)
    EditText mConfirmPassword;

    @BindView(R.id.spinnerMaritalStatus)
    Spinner spinnnerMarital;
    @BindView(R.id.iv_profile)
    ImageView imageProfile;
    @BindView(R.id.have_an_account)
    TextView haveAnAccount;


    private ISignUpObserver iSignUpObserver;
    private HashMap<String, String> hashMap = new HashMap<>();
    private int maritalType = -1;
    private String countryCode, phone,mAge=" ";
    private String currentImageType;
    private String imageUri;

    public void setiSignUpObserver(ISignUpObserver iSignUpObserver) {
        this.iSignUpObserver = iSignUpObserver;
    }

    @Override
    protected void initViewsForFragment(View view) {
        ArrayList<String> maritalStatusList = Helper.getMaritalStatus(getmActivity());
        ArrayAdapter<String> maritalAdapter = new ArrayAdapter<>(getmActivity(), android.R.layout.simple_spinner_item, maritalStatusList);
        spinnnerMarital.setSelection(0);
        spinnnerMarital.setAdapter(maritalAdapter);
       /* spinnnerMarital.setSelectedItemHintColor(getResources().getColor(R.color.colorBlack));
        spinnnerMarital.setHintTextColor(getResources().getColor(R.color.colorBlack));
        spinnnerMarital.setDownArrowTintColor(getResources().getColor(R.color.colorBlack));
        spinnnerMarital.setSpinnerHint(getString(R.string.marital_status));*/
        spinnnerMarital.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    maritalType = AppConstants.MARRIED;
                } else {
                    maritalType = AppConstants.UNMARRIED;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
       /* spinnnerMarital.setOnSpinnerItemClickListener((position, itemAtPosition) -> {
            if (position == 0) {
                maritalType = AppConstants.MARRIED;
            } else {
                maritalType = AppConstants.UNMARRIED;
            }
        });*/
        haveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent=new Intent(getActivity(),StartUpActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
            }
        });

        mDob.setOnClickListener(v -> showDateDialog());
        mDob.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                showDateDialog();
            }

        });
        if (getArguments() != null) {
            countryCode = getArguments().getString(AppConstants.K_COUNTRY_CODE);
            phone = getArguments().getString(AppConstants.K_PHONE_NUMBER);
            mPhone.setText(getString(R.string.phone_with_country_code, getArguments().getString(AppConstants.K_COUNTRY_CODE), getArguments().getString(AppConstants.K_PHONE_NUMBER)));
            mPhone.setEnabled(false);
        }

    }

    private void showDateDialog() {
        Bundle bundle = new Bundle();
        bundle.putLong(AppConstants.K_MIN_DATE, 0);
        bundle.putLong(AppConstants.K_MAX_DATE, (System.currentTimeMillis() + 1000) - 18 * 365 * 24 * 60 * 60 * 1000L);
        DateDialog dateDialog = DateDialog.newInstance(bundle);
        dateDialog.setiDateDialogObserver(this);
        dateDialog.show(getFragmentManager(), DateDialog.class.getSimpleName());
    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_driver_registration_new, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.container_upload_image)
    public void uploadProfile() {
        PickImageDialog.build(new PickSetup())
                .setOnPickResult(new IPickResult() {
                    @Override
                    public void onPickResult(PickResult pickResult) {
                        if (pickResult.getError() == null) {
                            setImageForCrop(pickResult);
                        }
                    }
                }).setOnPickCancel(new IPickCancel() {
            @Override
            public void onCancelClick() {

            }
        }).show(getFragmentManager());
//        Dexter.withActivity(getmActivity())
//                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .withListener(new MultiplePermissionsListener() {
//                    @Override
//                    public void onPermissionsChecked(MultiplePermissionsReport report) {
//                        if (report.areAllPermissionsGranted()) {
//                            showImagePickerOptions();
//                        }
//
//                        if (report.isAnyPermissionPermanentlyDenied()) {
//                            showSettingsDialog();
//                        }
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//                        token.continuePermissionRequest();
//                    }
//                }).check();


    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getmActivity());
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();
    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getmActivity().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(getmContext(), new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(getmActivity(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, AppConstants.REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(getmActivity(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, AppConstants.REQUEST_IMAGE);
    }

    @OnClick(R.id.btStartRide)
    public void onNext() {


        if (isValidate()) {
            hashMap.put(AppConstants.K_FIRST_NAME, mFirstName.getText().toString().trim());
            hashMap.put(AppConstants.K_LAST_NAME, mLastName.getText().toString().trim());
            hashMap.put(AppConstants.K_EMAIL, mEmail.getText().toString().trim());
            hashMap.put(AppConstants.K_PASSWORD, mPassword.getText().toString().trim());
            hashMap.put(AppConstants.K_ADDRESS, (state.getText().toString()).trim());
            hashMap.put(AppConstants.K_LGA, (LGA.getText().toString()).trim());
            hashMap.put(AppConstants.K_AGE, mAge.trim());
            hashMap.put(AppConstants.K_DATE_OF_BIRTH, String.valueOf(Timing.getTimeInUnixStamp(mDob.getText().toString().trim(), Timing.TimeFormats.DD_MM_YYYY)));
            hashMap.put(AppConstants.K_PHONE, phone);
            hashMap.put(AppConstants.K_COUNTRY_CODE, countryCode);
            hashMap.put(AppConstants.K_MARITAL_STATUS, String.valueOf(maritalType));
            hashMap.put(AppConstants.K_PLATFORM, "1");
            hashMap.put(AppConstants.K_FIREBASE_TOKEN, MainActivity.fb_token);
            hashMap.put(AppConstants.K_LATITUDE, "22.68610");
            hashMap.put(AppConstants.K_LONGITUDE, "75.86016");

            signUpPersonalInfo(hashMap);

            //          validateEmail(mEmail.getText().toString().trim());

        }



    }

    private boolean isValidate() {
        String emailPattern = "[a-zA-Z0-9._-]{2,20}+@[a-z]{2,10}+(\\.[a-z]{2,5}+)*(\\.[a-z]{2,5}+)";
        String fullNamePattern = "[a-z._ A-Z]{2,20}+";
        if (mFirstName.getText().toString().trim().isEmpty()) {
            showMessage(R.string.enter_first_name);
            return false;
        } else if (mLastName.getText().toString().trim().isEmpty()) {
            showMessage(R.string.enter_last_name);
            return false;
        } else if (mAge.trim().isEmpty()) {
            showMessage(R.string.enter_age);
            return false;
        } else if (mDob.getText().toString().trim().isEmpty()) {
            showMessage(R.string.enter_dob);
            return false;
        } else if (mPassword.getText().toString().trim().isEmpty()) {
            showMessage(R.string.please_enter_new_password);
            return false;
        }
        else if (mConfirmPassword.getText().toString().trim().isEmpty()) {
            showMessage(R.string.please_enter_confirm_password);
            return false;

        }
        else if (!mPassword.getText().toString().equals(mConfirmPassword.getText().toString())) {
            showMessage(R.string.passwords_must_match);
            return false;

        }



        else if (state.getText().toString().trim().isEmpty()) {
            showMessage(R.string.enter_address);
            return false;
        }
        else if (LGA.getText().toString().trim().isEmpty()) {
            showMessage(R.string.enter_lga);
            return false;
        }
        else if (mEmail.getText().toString().trim().isEmpty() || !mEmail.getText().toString().trim().matches(emailPattern)) {
            showMessage(R.string.enter_valid_email);
            return false;
        } else if (maritalType == -1) {
            showMessage(R.string.please_select_marital_status);
            return false;
        } /*else if (imageProfile.getVisibility() == View.GONE) {
            showMessage(R.string.please_upload_profile_image);
            return false;
        }*/

        return true;
    }

    @Override
    public void onDateSelected(int year, int month, int dayOfMonth) {
        hideKeyboard();
        String sday = String.valueOf(dayOfMonth);
        String smonth = String.valueOf(month);
        if (dayOfMonth < 10) {
            sday = "0" + sday;
        }
        if (month < 10) {
            smonth = "0" + smonth;
        }
        String date = sday + "/" + smonth + "/" + year;
        mDob.setText(date);
        mAge=(Helper.getInstance(getActivity()).getAge(date).toString());

    }

    private void setImageForCrop(PickResult r) {
//        Intent intent = CropImage.activity(r.getUri()).getIntent(getmActivity());
//        startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
        CropImage.activity(r.getUri()).start(getContext(), this);

    }

    private void validateEmail(String email) {
        showDialogProgressBar();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<String>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<String>> response) {
                hideDialogProgressBar();
                if (response.isSuccessful() && response.body() != null) {
                    //  UIHelper.getInstance().switchFragment((AppCompatActivity) getmActivity(), R.id.fl_sign_up, driverKinInfoFragment, DriverKinInfoFragment.class.getSimpleName(), true);
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
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
                        hideAlertDialog();
                        validateEmail(email);
                    });
                }
            }
        }, SessionManager.getAccessToken()).validateEmail(email);
    }

    public void uploadImage(String licenceImagePath) {
        showDialogProgressBar();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<MyResponse>() {
            @Override
            public void onResponse(Response<QualStandardResponse<MyResponse>> response) {
                hideDialogProgressBar();
                if (response.isSuccessful() && response.body() != null) {
                    imageProfile.setVisibility(View.VISIBLE);
                    hashMap.put(AppConstants.K_PROFILE_IMAGE, response.body().getResponse().getFile_path());
                }
            }

            @Override
            public void onError(Response<QualStandardResponse<MyResponse>> response) {
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
                        uploadImage(licenceImagePath);
                    });
                }

            }
        }, SessionManager.getAccessToken()).updateUserImage(licenceImagePath);

    }

    public void signUpPersonalInfo(HashMap<String, String> param) {
        showDialogProgressBar();
        param.put(AppConstants.K_TYPE, String.valueOf(AppConstants.SIGN_UP_STATUS.PERSONAL_INFO));
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<HashMap<String, String>>() {
            @Override
            public void onResponse(Response<QualStandardResponse<HashMap<String, String>>> response) {
                hideDialogProgressBar();
                if (response.isSuccessful() && response.body() != null) {
                    Helper.saveSignUpState(AppConstants.SIGN_UP_STATUS.PERSONAL_INFO, getmContext());
                    iSignUpObserver.onNextSelected(AppConstants.SIGNUP_STEP.PERSONAL_INFO, response.body().getResponse());
                    DriverKinInfoFragment driverKinInfoFragment = new DriverKinInfoFragment();
                    driverKinInfoFragment.setiSignUpObserver(iSignUpObserver);
                    UIHelper.getInstance().switchFragment((AppCompatActivity) getmActivity(), R.id.fragment_signup_container, driverKinInfoFragment, DriverKinInfoFragment.class.getSimpleName(), true);
                }
            }

            @Override
            public void onError(Response<QualStandardResponse<HashMap<String, String>>> response) {
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
                mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
                    if (mAlertDialog != null) {
                        hideAlertDialog();
                        signUpPersonalInfo(param);
                    }
                });
            }
        }).userSignin(param);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    imageUri = result.getUri().getPath();
                    uploadImage(result.getUri().getPath());
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                showMessage(error.getMessage());
            }

        } else if (requestCode == AppConstants.REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getmActivity().getContentResolver(), uri);

                    // loading profile image from local cache
//                    loadProfile(uri.toString());
                    //uploadImage(uri.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
