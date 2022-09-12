package com.example.drupp_driver.ui.signup;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.drupp_driver.Models.MyResponse;
import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.Helper;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetwork;
import com.example.drupp_driver.connectivity.http.QualStandardResponse;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.ISignUpObserver;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.helpers.UIHelper;
import com.example.drupp_driver.ui.base.MainBaseFragment;
import com.jakewharton.rxbinding3.widget.RxRadioGroup;
import com.theartofdev.edmodo.cropper.CropImage;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import retrofit2.Response;

public class DriverKinInfoFragment extends MainBaseFragment {
    @BindView(R.id.et_kin_first_name)
    EditText mKinFirstName;
    @BindView(R.id.et_kin_last_name)
    EditText mKinLastName;
    @BindView(R.id.et_kin_email)
    EditText mKinEmail;
    @BindView(R.id.et_kin_phone_number)
    EditText mKinPhone;

    @BindView(R.id.rg_driver_lis)
    RadioGroup mHaveLiscence;
    @BindView(R.id.rg_experience)
    RadioGroup mHaveExp;
    @BindView(R.id.container_licence)
    LinearLayout containerLicence;
    @BindView(R.id.iv_front_license)
    CircleImageView frontImage;
    @BindView(R.id.iv_back_license)
    CircleImageView backImage;
    @BindView(R.id.btStartRide)
    Button mBtnNext;
    @BindView(R.id.et_license_number)
    EditText mLicenseNumber;



    private HashMap<String, String> hashMap = new HashMap<>();

    private ISignUpObserver iSignUpObserver;
    private boolean isValidForm;
    private String frontLicenceImagePath;
    private String imageUri;
    private String profileImageUrl;
    private String experience = "1";
    private String isExperienced = "0";
    private String frontLicenseUri = "", backLicenseUri = "";
    private String currentImageType;

    public void setiSignUpObserver(ISignUpObserver iSignUpObserver) {
        this.iSignUpObserver = iSignUpObserver;
    }

    @Override
    protected void initViewsForFragment(View view) {
        Observable<Integer> liscenceObservable = RxRadioGroup.checkedChanges(mHaveLiscence)
                .map(integer -> integer)
                .share();

        Observable<Integer> expObservable = RxRadioGroup.checkedChanges(mHaveExp)
                .map(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(@NonNull Integer integer) throws Exception {
                        return integer;
                    }
                }).share();

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
                            break;
                        case R.id.no_exp:
                            isExperienced = "0";
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


    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_kin_experience_new, container, false);
        ButterKnife.bind(this, view);
       mHaveLiscence.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(RadioGroup group, int checkedId) {
               if(checkedId==R.id.rb_yes){
                   if(mLicenseNumber.getVisibility()==View.GONE){
                      mLicenseNumber.setVisibility(View.VISIBLE);
                   }
               }
               else if(checkedId==R.id.rb_no){
                   if(mLicenseNumber.getVisibility()==View.VISIBLE){
                       mLicenseNumber.setVisibility(View.GONE);
                   }
               }
           }
       });
        return view;
    }

    @OnClick(R.id.btCancelRide)
    public void onPrevious() {
        getmActivity().onBackPressed();
    }

    @OnClick(R.id.btStartRide)
    public void onNext() {
        /*DriverVehicleInfoFragment driverVehicleInfoFragment=new DriverVehicleInfoFragment();
        UIHelper.getInstance().switchFragment((AppCompatActivity) getmActivity(), R.id.fragment_signup_container, driverVehicleInfoFragment, DriverVehicleInfoFragment.class.getSimpleName(), true);*/
        if (isValidate()) {

            hashMap.put(AppConstants.K_KIN_FIRST_NAME, mKinFirstName.getText().toString().trim());
            hashMap.put(AppConstants.K_KIN_LAST_NAME, mKinLastName.getText().toString().trim());
            hashMap.put(AppConstants.K_KIN_EMAIL, mKinEmail.getText().toString().trim());
            hashMap.put(AppConstants.K_KIN_PHONE, mKinPhone.getText().toString().trim());

            signUpDriverKin(hashMap);


        }


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
    private void setImageForCrop(PickResult r) {
        Intent intent = CropImage.activity(r.getUri()).getIntent(getmActivity());
        startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    private boolean isValidate() {
        String emailPattern = "[a-zA-Z0-9._-]{2,20}+@[a-z]{2,10}+(\\.[a-z]{2,5}+)*(\\.[a-z]{2,5}+)";
        String fullNamePattern = "[a-z._ A-Z]{2,20}+";
        if (mKinFirstName.getText().toString().trim().isEmpty()) {
            showMessage(R.string.enter_first_name);
            return false;
        } else if (mKinLastName.getText().toString().trim().isEmpty()) {
            showMessage(R.string.enter_last_name);
            return false;
        } else if (mKinPhone.getText().toString().trim().isEmpty() || mKinPhone.getText().toString().trim().length() < 7) {
            showMessage(R.string.enter_phone);
            return false;
        } else if (mKinEmail.getText().toString().trim().isEmpty() || !mKinEmail.getText().toString().trim().matches(emailPattern)) {
            showMessage(R.string.enter_valid_email);
            return false;
        }
        else if (mLicenseNumber.getText().toString().trim().isEmpty()) {
            showMessage(R.string.license_number_is_required);
            return false;
        }
        else if (frontImage.getVisibility() == View.GONE) {
            showMessage(R.string.pleas_upload_front_image);
            return false;
        } else if (backImage.getVisibility() == View.GONE) {
            showMessage(R.string.upload_back_image);
            return false;
        }
        else if (frontImage.getVisibility() == View.GONE && backImage.getVisibility() == View.GONE) {
            showMessage(R.string.license_image_mandatory);
            return false;
        }
        return true;
    }

    public void signUpDriverKin(HashMap<String, String> param) {
        showDialogProgressBar();
        param.put(AppConstants.K_TYPE, String.valueOf(AppConstants.SIGN_UP_STATUS.NEXT_OF_KIN));
        param.put(AppConstants.K_DRIVER_ID, iSignUpObserver.getUser().getDriverId().toString());
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<HashMap<String, String>>() {
            @Override
            public void onResponse(Response<QualStandardResponse<HashMap<String, String>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Helper.saveSignUpState(AppConstants.SIGN_UP_STATUS.NEXT_OF_KIN, getmContext());
                    iSignUpObserver.onNextSelected(AppConstants.SIGNUP_STEP.NEXT_OF_KIN, response.body().getResponse());
                    /*DriverVehicleInfoFragment driverVehicleInfoFragment=new DriverVehicleInfoFragment();
                    driverVehicleInfoFragment.setiSignUpObserver(iSignUpObserver);
                    UIHelper.getInstance().switchFragment((AppCompatActivity) getmActivity(), R.id.fragment_signup_container, driverVehicleInfoFragment, DriverVehicleInfoFragment.class.getSimpleName(), true);*/
                    signUpDriverExperience();
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
                        signUpDriverKin(param);
                    }
                });
            }
        }).userSignin(param);


    }
    public boolean validateDriverExperience(){
        if (frontImage.getVisibility() == View.GONE) {
            showMessage(R.string.pleas_upload_front_image);
            return false;
        } else if (backImage.getVisibility() == View.GONE) {
            showMessage(R.string.upload_back_image);
            return false;
        } else if (frontImage.getVisibility() == View.GONE && backImage.getVisibility() == View.GONE) {
            showMessage(R.string.license_image_mandatory);
            return false;
        }
        return true;

    }
    public void signUpDriverExperience() {
        if (validateDriverExperience()) {
            HashMap<String, String> param = new HashMap<>();
            param.put(AppConstants.K_LICENSE_FRONT_IMAGE, frontLicenseUri);
            param.put(AppConstants.K_LICENSE_REAR_IMAGE, backLicenseUri);
            param.put(AppConstants.K_EXPERIENCE, isExperienced);
            param.put(AppConstants.K_EXPERIENCE_YR, experience);
            param.put(AppConstants.K_AUTHENTICATED_LICENSE, String.valueOf(1));
            param.put(AppConstants.K_LICENSE_NUMBER,mLicenseNumber.getText().toString());


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
                    UIHelper.getInstance().switchFragment((AppCompatActivity) getmActivity(), R.id.fragment_signup_container, driverVehicleInfoFragment, DriverVehicleInfoFragment.class.getSimpleName(), true);
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
                        signUpDriverExperience();
                    }
                });
            }
        }).userSignin(param);
    }
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
