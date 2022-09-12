package com.example.drupp_driver.ui.signup;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.drupp_driver.Models.MyResponse;
import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.Helper;
import com.example.drupp_driver.Utils.ImagePickerActivity;
import com.example.drupp_driver.Utils.UiUtils;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetwork;
import com.example.drupp_driver.connectivity.http.QualStandardResponse;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.ISignUpObserver;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.helpers.UIHelper;
import com.example.drupp_driver.ui.base.MainBaseFragment;
import com.jakewharton.rxbinding3.widget.RxRadioGroup;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import retrofit2.Response;

public class DriverExperienceFragment extends MainBaseFragment {
    @BindView(R.id.rg_driver_lis)
    RadioGroup mHaveLiscence;
    @BindView(R.id.rg_experience)
    RadioGroup mHaveExp;
    @BindView(R.id.container_licence)
    LinearLayout containerLicence;
    @BindView(R.id.container_experience)
    LinearLayout containerExperience;
    @BindView(R.id.iv_front_license)
    CircleImageView frontImage;
    @BindView(R.id.iv_back_license)
    CircleImageView backImage;
    @BindView(R.id.btStartRide)
    Button mBtnNext;
    @BindView(R.id.et_license_num)
    EditText mLicenseNumber;
    @BindView(R.id.spinner_experience)
    MaterialSpinner spinnerExperience;
    @BindView(R.id.tvExpiry)
    TextView tvExpiry;


    private ISignUpObserver iSignUpObserver;
    private boolean isValidForm;
    private String frontLicenceImagePath;
    private String imageUri;
    private String profileImageUrl;
    private String currentImageType;
    private String frontLicenseUri = "", backLicenseUri = "";
    private String experience = "1";
    private String isExperienced = "0";

    public void setiSignUpObserver(ISignUpObserver iSignUpObserver) {
        this.iSignUpObserver = iSignUpObserver;
    }

    @Override
    protected void initViewsForFragment(View view) {
        Observable<Integer> liscenceObservable = RxRadioGroup.checkedChanges(mHaveLiscence)
                .map(integer -> integer)
                .share();

        Observable<Integer> expObservable = RxRadioGroup.checkedChanges(mHaveExp)
                .map(integer -> integer).share();

        Disposable subscribe1 = liscenceObservable.subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {
                    switch (integer) {
                        case R.id.rb_yes:
                            containerLicence.setVisibility(View.VISIBLE);
                            break;
                        case R.id.rb_no:
                            containerLicence.setVisibility(View.GONE);
                            break;
                    }
                });

        Disposable subscribe2 = expObservable.subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {
                    switch (integer) {
                        case R.id.yes_exp:
                            isExperienced = "1";
                            containerExperience.setVisibility(View.VISIBLE);
                            break;
                        case R.id.no_exp:
                            isExperienced = "0";
                            containerExperience.setVisibility(View.GONE);
                            break;
                    }
                });

        Observable.combineLatest(liscenceObservable, expObservable, (integer, integer2) -> {
            if (integer == R.id.rb_yes) {
                return true;
            }
            return false;

        }).subscribe(new DisposableObserver<Boolean>() {
            @Override
            public void onNext(Boolean aBoolean) {
                mBtnNext.setEnabled(aBoolean);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        spinnerExperience.setItems("0-1", "2-5", "5+");
        spinnerExperience.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view12, position, id, item) -> experience = String.valueOf(position + 1));

        tvExpiry.setOnClickListener(view1 -> UiUtils.selectExpiry(tvExpiry));
    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_driver_experience_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btCancelRide)
    public void onPrevious() {
        getmActivity().onBackPressed();
    }

    @OnClick(R.id.container_back_image)
    public void uploadBackLicense() {
        PickImageDialog.build(new PickSetup())
                .setOnPickResult(new IPickResult() {
                    @Override
                    public void onPickResult(PickResult pickResult) {
                        if (pickResult.getError() == null) {
                            currentImageType = AppConstants.BACK_IMAGE_LICENSE;
                            setImageForCrop(pickResult);
                        }
                    }
                }).setOnPickCancel(new IPickCancel() {
            @Override
            public void onCancelClick() {

            }
        }).show(getFragmentManager());
//        startPicker();

    }

    @OnClick(R.id.container_front_image)
    public void uploadFrontLicense() {
        PickImageDialog.build(new PickSetup())
                .setOnPickResult(new IPickResult() {
                    @Override
                    public void onPickResult(PickResult pickResult) {
                        if (pickResult.getError() == null) {
                            currentImageType = AppConstants.FRONT_IMAGE_LICENSE;
                            setImageForCrop(pickResult);
                        }
                    }
                }).setOnPickCancel(new IPickCancel() {
            @Override
            public void onCancelClick() {

            }
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

    @OnClick(R.id.btStartRide)
    public void onNext() {
        if (isValidate()) {
            HashMap<String, String> map = new HashMap<>();
            map.put(AppConstants.K_LICENSE_FRONT_IMAGE, frontLicenseUri);
            map.put(AppConstants.K_LICENSE_REAR_IMAGE, backLicenseUri);
            map.put(AppConstants.K_EXPERIENCE, isExperienced);
            map.put(AppConstants.K_EXPERIENCE_YR, experience);
            map.put(AppConstants.K_LICENSE_NUMBER, mLicenseNumber.getText().toString().trim());
            map.put(AppConstants.K_AUTHENTICATED_LICENSE, String.valueOf(1));
            signUpDriverExperience(map);
        }


    }

    private boolean isValidate() {
        if (mLicenseNumber.getText().toString().trim().isEmpty()) {
            showMessage(R.string.enter_license_number);
            return false;
        } else if (frontImage.getVisibility() == View.GONE) {
            showMessage(R.string.pleas_upload_front_image);
            return false;
        } else if (backImage.getVisibility() == View.GONE) {
            showMessage(R.string.upload_back_image);
            return false;
        } else if (frontImage.getVisibility() == View.GONE && backImage.getVisibility() == View.GONE) {
            showMessage(R.string.license_image_mandatory);
            return false;
        } else if (tvExpiry.getText().toString().isEmpty()) {
            showMessage(getString(R.string.select_expiry_date));
            return false;
        }

        return true;
    }

    private void setImageForCrop(PickResult r) {
        Intent intent = CropImage.activity(r.getUri()).getIntent(getmActivity());
        startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    public void signUpDriverExperience(HashMap<String, String> param) {
        showDialogProgressBar();
        param.put(AppConstants.K_TYPE, String.valueOf(AppConstants.SIGN_UP_STATUS.DRIVER_EXPERIENCE));
        param.put(AppConstants.K_DRIVER_ID, iSignUpObserver.getUser().getDriverId().toString());
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<HashMap<String, String>>() {
            @Override
            public void onResponse(Response<QualStandardResponse<HashMap<String, String>>> response) {
                hideDialogProgressBar();
                if (response.isSuccessful() && response.body() != null) {
                    Helper.saveSignUpState(AppConstants.SIGN_UP_STATUS.DRIVER_EXPERIENCE, getmContext());
                    iSignUpObserver.onNextSelected(AppConstants.SIGNUP_STEP.EXPERIENCE, response.body().getResponse());
                    DriverVehicleInfoFragment driverVehicleInfoFragment = new DriverVehicleInfoFragment();
                    driverVehicleInfoFragment.setiSignUpObserver(iSignUpObserver);
                    UIHelper.getInstance().switchFragment((AppCompatActivity) getmActivity(), R.id.fl_sign_up, driverVehicleInfoFragment, DriverVehicleInfoFragment.class.getSimpleName(), true);
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
                        signUpDriverExperience(param);
                    }
                });
            }
        }).userSignin(param);
    }

    public void uploadImage(String licenceImagePath, String imageType) {
        showDialogProgressBar();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<MyResponse>() {
            @Override
            public void onResponse(Response<QualStandardResponse<MyResponse>> response) {
                hideDialogProgressBar();
                if (response.isSuccessful() && response.body() != null) {


                    if (imageType.equals(AppConstants.FRONT_IMAGE_LICENSE)) {
                        frontLicenseUri = response.body().getResponse().getFile_path();
                        //
//                        Glide.with(getmActivity()).load(licenceImagePath).apply(new RequestOptions()
//                                .error(R.drawable.ic_user_silhouette)
//                                .centerCrop().placeholder(R.drawable.ic_user_silhouette)).into(frontImage);
                        frontImage.setVisibility(View.VISIBLE);
                    } else {
                        backLicenseUri = response.body().getResponse().getFile_path();
//                        Glide.with(getmActivity()).load(licenceImagePath).apply(new RequestOptions()
//                                .error(R.drawable.ic_user_silhouette)
//                                .centerCrop().placeholder(R.drawable.ic_user_silhouette)).into(backImage);
                        backImage.setVisibility(View.VISIBLE);
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
                        uploadImage(frontLicenceImagePath, currentImageType);
                    });
                }

            }
        }, SessionManager.getAccessToken()).uploadLicenseImage(licenceImagePath, imageType);

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
