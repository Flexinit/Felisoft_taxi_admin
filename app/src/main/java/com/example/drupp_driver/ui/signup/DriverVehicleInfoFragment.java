package com.example.drupp_driver.ui.signup;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.drupp_driver.Models.MyResponse;
import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.Helper;
import com.example.drupp_driver.Utils.ImagePickerActivity;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetwork;
import com.example.drupp_driver.connectivity.http.QualStandardResponse;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.ISignUpObserver;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.helpers.UIHelper;
import com.example.drupp_driver.ui.base.MainBaseFragment;
import com.isapanah.awesomespinner.AwesomeSpinner;
import com.jakewharton.rxbinding3.widget.RxRadioGroup;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

public class DriverVehicleInfoFragment extends MainBaseFragment {
    @BindView(R.id.rg_have_ac)
    RadioGroup mHaveAC;
    @BindView(R.id.et_reason_no_ac)
    EditText mNoAcReason;
    @BindView(R.id.et_veh_color)
    EditText mVehicleColor;
    @BindView(R.id.et_car_name)
    EditText etCarName;
    @BindView(R.id.et_car_model)
    EditText etCarModel;
    @BindView(R.id.et_chasis_number)
    EditText mVehicleChasis;
    @BindView(R.id.rg_driver_type)
    RadioGroup rgDriverType;
    @BindView(R.id.rg_car_condition)
    RadioGroup rgCarCondition;
    @BindView(R.id.spinner_car_type)
    AwesomeSpinner spinnerCarType;
    @BindView(R.id.tv_front_image)
    TextView mTextFrontImage;
    @BindView(R.id.tv_back_image)
    TextView mTextBackImage;
    @BindView(R.id.tv_left_image)
    TextView mTextLeftImage;
    @BindView(R.id.tv_right_image)
    TextView mTextRightImage;
    @BindView(R.id.tv_engine_image)
    TextView mTextEngine;
    @BindView(R.id.tv_boot_image)
    TextView mTextBoot;
    @BindView(R.id.iv_front_image)
    ImageView mFrontImage;
    @BindView(R.id.iv_back_image)
    ImageView mBackImage;
    @BindView(R.id.iv_side_left_image)
    ImageView mLeftImage;
    @BindView(R.id.iv_side_right_image)
    ImageView mRightImage;
    @BindView(R.id.iv_engine_image)
    ImageView mEngineImage;
    @BindView(R.id.iv_side_boot_image)
    ImageView mBootImage;
    @BindView(R.id.et_vehicle_nu)
    EditText mVehicleNumber;
    @BindView(R.id.container_vehicle_type)
    LinearLayout containerVehicleType;
    @BindView(R.id.et_car_year)
    EditText etCarYear;
    @BindView(R.id.tv_interior_front)
    TextView mTextInteriorFront;
    @BindView(R.id.tv_interior_back)
    TextView mTextInteriorBack;
    @BindView(R.id.iv_interior_front)
    ImageView mInteriorFontImage;
    @BindView(R.id.iv_interior_back)
    ImageView mInteriorBackImage;


    private HashMap<String, String> map = new HashMap<>();
    private ISignUpObserver iSignUpObserver;
    private String currentImageType = "";
    private String imageUri;
    private int driverType = AppConstants.CAR_DRIVER;
    private int vehicleType = AppConstants.WITHOUT_AC;
    private int conditionType = 1;
    private String textInteriorFront="interior \nfront";
    private String textInteriorBack="interior \nfront";

    public void setiSignUpObserver(ISignUpObserver iSignUpObserver) {
        this.iSignUpObserver = iSignUpObserver;
    }

    @Override
    protected void initViewsForFragment(View view) {
        Observable<Integer> haveAcObservable = RxRadioGroup.checkedChanges(mHaveAC).map(integer -> integer);
        Disposable subscribe = haveAcObservable.subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {
                    switch (integer) {
                        case R.id.rb_yes:
                            vehicleType = AppConstants.WITH_AC;
                            mNoAcReason.setVisibility(View.GONE);
                            break;
                        case R.id.rb_no:
                            vehicleType = AppConstants.WITHOUT_AC;
                            mNoAcReason.setVisibility(View.VISIBLE);
                            break;
                    }
                });

        formatInterorImageTextviews();
        ArrayAdapter<String> carConditionAdapter = new ArrayAdapter<>(getmActivity(), android.R.layout.simple_spinner_item, Helper.getCarCondition(getmActivity()));

        /*rgCarCondition.setAdapter(carConditionAdapter);
        setSpinnerView(rgCarCondition);*/
        rgCarCondition.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.condition_neat:
                        conditionType = AppConstants.NEAT;
                        break;
                    case R.id.condition_old:
                        conditionType = AppConstants.OLD;
                        break;
                    case R.id.condition_new:
                        conditionType = AppConstants.NEW;
                        break;

                }
            }
        });
        ArrayAdapter<String> carTypeAdapter = new ArrayAdapter<>(getmActivity(), android.R.layout.simple_spinner_item, Helper.getVehicleTypes(getmActivity()));
//        spinnerCarType.setAdapter(carTypeAdapter);
        //setSpinnerView(spinnerCarType);
        ArrayAdapter<String> carNames = new ArrayAdapter<>(getmActivity(), android.R.layout.simple_spinner_item, Helper.getCarNames(getmActivity()));
        ArrayAdapter<String> carModels = new ArrayAdapter<>(getmActivity(), android.R.layout.simple_spinner_item, Helper.getCarModels(getmActivity()));
        ArrayAdapter<String> carYear = new ArrayAdapter<>(getmActivity(), android.R.layout.simple_spinner_item, Helper.getCarYear(getmActivity()));
        /*mCarNameSpinner.setAdapter(carNames);
        mCarModelSpinner.setAdapter(carModels);
        spinnerYear.setAdapter(carYear);
         mCarNameSpinner.setOnSpinnerItemClickListener((position, itemAtPosition) -> {
            if (position == 0) {
                //maritalType = AppConstants.MARRIED;
            } else {
                //maritalType = AppConstants.UNMARRIED;
            }
        });*/
        //mCarNameSpinner.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        ArrayAdapter<String> driverTypeAdapter = new ArrayAdapter<>(getmActivity(), R.layout.spinner_item, Helper.getDriverType(getmActivity()));
        //rgDriverType.setAdapter(driverTypeAdapter);
        //setSpinnerView(rgDriverType);

        /*setSpinnerView(mCarNameSpinner);
        setSpinnerView(mCarModelSpinner);
        setSpinnerView(spinnerYear);*/
        rgDriverType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.car_driver:
                        containerVehicleType.setVisibility(View.VISIBLE);
                        driverType = AppConstants.CAR_DRIVER;
                        break;
                    case R.id.bus_driver:
                        containerVehicleType.setVisibility(View.GONE);
                        driverType = AppConstants.BUS_DRIVER;
                        break;
                    case R.id.keke_driver:
                        containerVehicleType.setVisibility(View.GONE);
                        driverType = AppConstants.KEKE_DRIVER;
                        break;

                }
            }
        });

        //tvExpiry.setOnClickListener(view1 -> UiUtils.selectExpiry(tvExpiry));

    }

    private void formatInterorImageTextviews() {
        Spannable spannableTextInteriorFront=new SpannableString("interior \nfront");
        Spannable spannableTextInteriorBack=new SpannableString("interior \nback");
    }

    private void setSpinnerView(AwesomeSpinner spinner) {
        spinner.setHintTextColor(getResources().getColor(R.color.colorBlack));
        spinner.setSelectedItemHintColor(getResources().getColor(R.color.colorBlack));
        spinner.setDownArrowTintColor(getResources().getColor(R.color.colorBlack));
        //spinner.setDropDownViewResource(R.drawable.ic_down);
        spinner.setOnSpinnerItemClickListener((position, itemAtPosition) -> {
            if (position == 0) {
                //maritalType = AppConstants.MARRIED;
            } else {
                //maritalType = AppConstants.UNMARRIED;
            }
        });
    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_vehicle_registration_new, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.container_front_image)
    public void addFrontImage() {
        PickImageDialog.build(new PickSetup())
                .setOnPickResult(pickResult -> {
                    if (pickResult.getError() == null) {
                        currentImageType = AppConstants.FRONT_IMAGE_VEHICLE;
                        setImageForCrop(pickResult);
                    }
                }).setOnPickCancel(() -> {

        }).show(getFragmentManager());
//        startPicker();
    }

    @OnClick(R.id.container_back_image)
    public void addBackImage() {
        PickImageDialog.build(new PickSetup())
                .setOnPickResult(pickResult -> {
                    if (pickResult.getError() == null) {
                        currentImageType = AppConstants.BACK_IMAGE_VEHICLE;
                        setImageForCrop(pickResult);
                    }
                }).setOnPickCancel(() -> {

        }).show(getFragmentManager());
//        startPicker();

    }

    @OnClick(R.id.container_left_image)
    public void addLeftImage() {
        PickImageDialog.build(new PickSetup())
                .setOnPickResult(pickResult -> {
                    if (pickResult.getError() == null) {
                        currentImageType = AppConstants.LEFT_IMAGE_VEHICLE;
                        setImageForCrop(pickResult);
                    }
                }).setOnPickCancel(() -> {

        }).show(getFragmentManager());
//        startPicker();
    }

    @OnClick(R.id.container_right_image)
    public void addRightImage() {
        PickImageDialog.build(new PickSetup())
                .setOnPickResult(pickResult -> {
                    if (pickResult.getError() == null) {
                        currentImageType = AppConstants.RIGHT_IMAGE_VEHICLE;
                        setImageForCrop(pickResult);
                    }
                }).setOnPickCancel(() -> {

        }).show(getFragmentManager());
//        startPicker();
    }
    @OnClick(R.id.container_interior_front_image)
    public void addInteriorFrontImage() {
        PickImageDialog.build(new PickSetup())
                .setOnPickResult(pickResult -> {
                    if (pickResult.getError() == null) {
                        currentImageType = AppConstants.INTERIOR_FRONT_IMAGE_VEHICLE;
                        setImageForCrop(pickResult);
                    }
                }).setOnPickCancel(() -> {

        }).show(getFragmentManager());
//        startPicker();
    }

    @OnClick(R.id.container_interior_back_image)
    public void addInterior() {
        PickImageDialog.build(new PickSetup())
                .setOnPickResult(pickResult -> {
                    if (pickResult.getError() == null) {
                        currentImageType = AppConstants.INTERIOR_BACK_IMAGE_VEHICLE;
                        setImageForCrop(pickResult);
                    }
                }).setOnPickCancel(() -> {

        }).show(getFragmentManager());
//        startPicker();
    }

    @OnClick(R.id.container_engine_image)
    public void addEngineImage() {
        PickImageDialog.build(new PickSetup())
                .setOnPickResult(pickResult -> {
                    if (pickResult.getError() == null) {
                        currentImageType = AppConstants.ENGINE_IMAGE_VEHICLE;
                        setImageForCrop(pickResult);
                    }
                }).setOnPickCancel(() -> {

        }).show(getFragmentManager());
//        startPicker();
    }

    @OnClick(R.id.container_boot_image)
    public void addBootImage() {
        PickImageDialog.build(new PickSetup())
                .setOnPickResult(pickResult -> {
                    if (pickResult.getError() == null) {
                        currentImageType = AppConstants.BOOT_IMAGE_VEHICLE;
                        setImageForCrop(pickResult);
                    }
                }).setOnPickCancel(() -> {

        }).show(getFragmentManager());
//        startPicker();
    }

    private void startPicker() {
        Dexter.withActivity(getmActivity())
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
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

    @OnClick(R.id.btCancelRide)
    public void onPrevious() {
        getmActivity().onBackPressed();
    }

    @OnClick(R.id.btStartRide)
    public void onNext() {
        //DriverAccountInfoFragment driverAccountInfoFragment = new DriverAccountInfoFragment();
        //UIHelper.getInstance().switchFragment((AppCompatActivity) getmActivity(), R.id.fragment_signup_container, new DriverAccountInfoFragment(), DriverAccountInfoFragment.class.getSimpleName(), true);
        if (isValidate()) {
            map.put(AppConstants.K_VEHICLE_MODEL, etCarModel.getText().toString().trim());
            map.put(AppConstants.K_VEHICLE_YEAR, etCarYear.getText().toString());
            map.put(AppConstants.K_VEHICLE_BRAND, etCarName.getText().toString().trim());
            map.put(AppConstants.K_VEHICLE_COLOR, mVehicleColor.getText().toString().trim());
            map.put(AppConstants.K_FUNCTIONAL_AC, String.valueOf(vehicleType));
            map.put(AppConstants.K_CHASSIS_NUMBER, mVehicleChasis.getText().toString().trim());
            map.put(AppConstants.K_VEHICLE_CONDITION, String.valueOf(conditionType));
            map.put(AppConstants.K_DRIVER_TYPE, String.valueOf(driverType));
            map.put(AppConstants.K_VEHICLE_NUMBER, mVehicleNumber.getText().toString().trim());
            map.put(AppConstants.K_VEHICLE_NAME, etCarName.getText().toString().trim());
            map.put(AppConstants.K_VEHICLE_TYPE_ID, String.valueOf(vehicleType));
            map.put(AppConstants.K_REASON_FOR_NO_AC, mNoAcReason.getText().toString().trim());
            signUpVehicleInfo(map);
        }
    }

    private boolean isValidate() {
        if (mVehicleNumber.getText().toString().trim().isEmpty()) {
            showMessage(R.string.please_enter_vehicle);
            return false;
        } else if (etCarName.getText().toString().trim().isEmpty()) {
            showMessage(R.string.enter_vehicle_brand);
            return false;
        } else if (mVehicleColor.getText().toString().trim().isEmpty()) {
            showMessage(R.string.enter_vehicle_color);
            return false;
        } else if (etCarModel.getText().toString().isEmpty()) {
            showMessage(R.string.enter_vehicle_model);
            return false;
        } else if (mVehicleChasis.getText().toString().trim().isEmpty()) {
            showMessage(R.string.enter_chasis_number);
            return false;
        } else if (conditionType == -1) {
            showMessage(R.string.select_vehicle_condition);
            return false;
        } else if (mFrontImage.getVisibility() == View.GONE) {
            showMessage(R.string.select_front_image);
            return false;
        } else if (mBackImage.getVisibility() == View.GONE) {
            showMessage(R.string.select_back_image);
            return false;
        } else if (mLeftImage.getVisibility() == View.GONE) {
            showMessage(R.string.upload_left_image);
            return false;
        } else if (mRightImage.getVisibility() == View.GONE) {
            showMessage(R.string.upload_right_image);
            return false;
        }  else if (mEngineImage.getVisibility() == View.GONE) {
            showMessage(R.string.upload_engine_image);
            return false;
        } else if (mBootImage.getVisibility() == View.GONE) {
            showMessage(R.string.upload_boot_image);
            return false;
        }

        return true;

    }

    private void setImageForCrop(PickResult r) {
        Intent intent = CropImage.activity(r.getUri()).getIntent(getmActivity());
        startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    public void uploadImage(String imagePath, String imageType) {
        showDialogProgressBar();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<MyResponse>() {
            @Override
            public void onResponse(Response<QualStandardResponse<MyResponse>> response) {
                hideDialogProgressBar();
                if (response.isSuccessful() && response.body() != null) {

                    switch (imageType) {
                        case AppConstants.FRONT_IMAGE_VEHICLE:
                            map.put(AppConstants.K_FRONT_IMAGE_VEHICLE, response.body().getResponse().file_path);

                            mFrontImage.setVisibility(View.VISIBLE);
                            break;
                        case AppConstants.BACK_IMAGE_VEHICLE:
                            map.put(AppConstants.K_BACK_IMAGE_VEHICLE, response.body().getResponse().file_path);
                            mBackImage.setVisibility(View.VISIBLE);
                            break;
                        case AppConstants.LEFT_IMAGE_VEHICLE:
                            map.put(AppConstants.K_LEFT_IMAGE_VEHICLE, response.body().getResponse().file_path);
                            mLeftImage.setVisibility(View.VISIBLE);
                            break;
                        case AppConstants.RIGHT_IMAGE_VEHICLE:
                            map.put(AppConstants.K_RIGHT_IMAGE_VEHICLE, response.body().getResponse().file_path);
                            mRightImage.setVisibility(View.VISIBLE);
                            break;
                        case AppConstants.INTERIOR_FRONT_IMAGE_VEHICLE:
                            map.put(AppConstants.K_INTERIOR_FRONT_IMAGE_VEHICLE, response.body().getResponse().file_path);

                            mInteriorFontImage.setVisibility(View.VISIBLE);
                            mTextInteriorFront.setText("interior \nfront");
                            break;
                        case AppConstants.INTERIOR_BACK_IMAGE_VEHICLE:
                            map.put(AppConstants.K_INTERIOR_BACK_IMAGE_VEHICLE, response.body().getResponse().file_path);
                            mInteriorBackImage.setVisibility(View.VISIBLE);
                            mTextInteriorBack.setText("interior \nback");
                            break;
                        case AppConstants.ENGINE_IMAGE_VEHICLE:
                            map.put(AppConstants.K_ENGINE_IMAGE_VEHICLE, response.body().getResponse().file_path);
                            mEngineImage.setVisibility(View.VISIBLE);
                            break;
                        case AppConstants.BOOT_IMAGE_VEHICLE:
                            map.put(AppConstants.K_BOOT_IMAGE_VEHICLE, response.body().getResponse().file_path);
                            mBootImage.setVisibility(View.VISIBLE);
                            break;
                    }

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
                        uploadImage(imagePath, imageType);
                    });
                }

            }
        }, SessionManager.getAccessToken()).uploadVehicleImage(imagePath, imageType);

    }

    public void signUpVehicleInfo(HashMap<String, String> param) {
        showDialogProgressBar();
        param.put(AppConstants.K_TYPE, String.valueOf(AppConstants.SIGN_UP_STATUS.VEHICLE_INFO));
        param.put(AppConstants.K_DRIVER_ID, iSignUpObserver.getUser().getDriverId().toString());
        if(iSignUpObserver.getUser().getVehicleId()!=null){
            param.put(AppConstants.K_VEHICLE_ID, iSignUpObserver.getUser().getVehicleId().toString());
        }
        else{
            param.put(AppConstants.K_VEHICLE_ID, "95985225");
        }

        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<HashMap<String, String>>() {
            @Override
            public void onResponse(Response<QualStandardResponse<HashMap<String, String>>> response) {
                hideDialogProgressBar();
                if (response.isSuccessful() && response.body() != null) {
                    Helper.saveSignUpState(AppConstants.SIGN_UP_STATUS.VEHICLE_INFO, getmContext());

                    iSignUpObserver.onNextSelected(AppConstants.SIGNUP_STEP.VEHICLE_TO_REGISTER, map);
                    DriverAccountInfoFragment driverAccountInfoFragment = new DriverAccountInfoFragment();
                    driverAccountInfoFragment.setiSignUpObserver(iSignUpObserver);
                    UIHelper.getInstance().switchFragment((AppCompatActivity) getmActivity(), R.id.fragment_signup_container, new DriverAccountInfoFragment(), DriverAccountInfoFragment.class.getSimpleName(), true);
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
                        signUpVehicleInfo(param);
                    }
                });
            }
        }).userSignin(param);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (data != null) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                imageUri = result.getUri().getPath();
                uploadImage(result.getUri().getPath(), currentImageType);
            }
        } else if (requestCode == AppConstants.REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getmActivity().getContentResolver(), uri);

                    // loading profile image from local cache
//                    loadProfile(uri.toString());
                    uploadImage(uri.getPath(), currentImageType);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
