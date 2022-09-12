package com.example.drupp_driver.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListPopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.drupp_driver.Models.Driverregistration;
import com.example.drupp_driver.Models.ImageDetail;
import com.example.drupp_driver.Models.MyResponse;
import com.example.drupp_driver.Models.UserModel;
import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.Helper;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetwork;
import com.example.drupp_driver.connectivity.http.QualStandardResponse;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.ui.base.MainBaseActivity;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Response;

public class DriverRegistrationActivity extends MainBaseActivity {

    // SignUp/Registration of driver
    // inputs driver's personel details and vehicles details
    private static final String TAG = "DriverRegistrationActiv";


    private Spinner sp_car_type;
    //---------------------Views---------------------
    private EditText fName, lName, email, password, phone_num, city, et_car_type;
    private CheckBox mTerms;
    private FrameLayout registrationContainer;
    private View driverRegistrationView, vehicleRegistrationView;
    private Button btnVehicleSpinner;
    //---------------------Globals-------------------
    private List<String> categories = new ArrayList<String>();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private ArrayAdapter<String> categoriesAdapter;

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String MyLoginStatus = "MyLoginStatus";

    private String country_code;

    int index = 0;
    private EditText veh_num, veh_name, lisc_num, veh_color, veh_model, veh_chassis_num, veh_brand;

    String file_path, file_name;
    MyResponse x_response;
    private List<ImageDetail> images = new ArrayList<>();
    private ListPopupWindow vehicleListPopUp;
    String car_type;
    private String profile_file_path = "";

    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_registration);
        registrationContainer = findViewById(R.id.fl_registration);
        setUpRegistrationActivity();

        categories.addAll(Helper.getVehicleTypes(this));
        vehicleListPopUp = new ListPopupWindow(this);
        categoriesAdapter = new ArrayAdapter<>(this, R.layout.item_spinner_list, categories);
        categoriesAdapter.notifyDataSetChanged();


        sharedPreferences = getSharedPreferences("contact", MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    private void setUpRegistrationActivity() {
        registrationContainer.removeAllViews();
        driverRegistrationView = getLayoutInflater().inflate(R.layout.fragment_driver_registration, registrationContainer);
        if (driverRegistrationView != null) {

            fName = driverRegistrationView.findViewById(R.id.et_first_name);
            lName = driverRegistrationView.findViewById(R.id.et_last_name);
            email = driverRegistrationView.findViewById(R.id.et_email);
            password = driverRegistrationView.findViewById(R.id.etEmail);
            phone_num = driverRegistrationView.findViewById(R.id.et_phone);
//            city = driverRegistrationView.findViewById(R.id.et_city);
//            mTerms = driverRegistrationView.findViewById(R.id.cb_BySingning);
//            driverRegistrationView.findViewById(R.id.image_profile).setOnClickListener(v -> setImages());
            driverRegistrationView.findViewById(R.id.btStartRide).setOnClickListener(v -> {
                if (checkDriverData()) {
                    index = 1;
                    registrationContainer.removeAllViews();
                    vehicleRegistrationView = getLayoutInflater().inflate(R.layout.fragment_vehicle_registration, registrationContainer);
                    if (vehicleRegistrationView != null) {
                        btnVehicleSpinner = vehicleRegistrationView.findViewById(R.id.spinner_car_type);
//                        veh_name = vehicleRegistrationView.findViewById(R.id.et_veh_name);
//                        veh_num = vehicleRegistrationView.findViewById(R.id.et_veh_num);
//                        lisc_num = vehicleRegistrationView.findViewById(R.id.et_lic_num);
                        veh_color = vehicleRegistrationView.findViewById(R.id.et_veh_color);
                        registrationContainer = vehicleRegistrationView.findViewById(R.id.fl_registration);
                        veh_brand = vehicleRegistrationView.findViewById(R.id.et_veh_brand);
                        veh_model = vehicleRegistrationView.findViewById(R.id.et_veh_model);
                        veh_chassis_num = vehicleRegistrationView.findViewById(R.id.et_veh_chasis);

                        vehicleListPopUp.setAnchorView(btnVehicleSpinner);
                        vehicleListPopUp.setAdapter(categoriesAdapter);
                        btnVehicleSpinner.setText(categories.get(0));
                        vehicleListPopUp.setOnItemClickListener((parent, view, position, id) -> {
                            car_type = String.valueOf(position);
                            btnVehicleSpinner.setText(categories.get(position));
                            vehicleListPopUp.dismiss();
                        });

                        btnVehicleSpinner.setOnClickListener(v1 -> vehicleListPopUp.show());

                        vehicleRegistrationView.findViewById(R.id.btStartRide).setOnClickListener(v12 -> {
                            if (chechVehicleData()) {
                                Toast.makeText(DriverRegistrationActivity.this, "Please wait \n Signing up", Toast.LENGTH_SHORT).show();
                                saveDetails();
                                findViewById(R.id.btStartRide).setEnabled(false);
                            }
                        });
                        vehicleRegistrationView.findViewById(R.id.tv_add_front_image).setOnClickListener(v13 -> setImagesCar());


                    }

                }
            });
            Intent intent = getIntent();
            phone_num.setText(intent.getStringExtra(AppConstants.K_PHONE_NUMBER));
            country_code = intent.getStringExtra(AppConstants.K_COUNTRY_CODE);
        }


    }


    private void setImages() {
        PickSetup setup = new PickSetup();
        PickImageDialog.build(setup, pickResult -> {

            if (pickResult.getError() == null) {

                try {
//                    CircleImageView image_profile = findViewById(R.id.image_profile);
                    //                  image_profile.setImageBitmap(pickResult.getBitmap());

                    DruppRequestHandler.clearInstance();
                    DruppRequestHandler.getInstance(new INetwork<MyResponse>() {
                        @Override
                        public void onResponse(Response<QualStandardResponse<MyResponse>> response) {
                            if (response.isSuccessful()) {
                                MyResponse myResponse = response.body().getResponse();

                                profile_file_path = myResponse.getFile_path();
                                file_name = myResponse.getFile_name();
                                x_response = myResponse;

                                ImageDetail imageDetail = new ImageDetail(file_name, file_path);
                                images.add(imageDetail);

                                Toast.makeText(DriverRegistrationActivity.this, "Image upload succsess", Toast.LENGTH_LONG).show();

                            } else Log.d(TAG, "onResponse: something happeend");
                        }

                        @Override
                        public void onError(Response<QualStandardResponse<MyResponse>> response) {
                            Log.e("response: Error", response.message());
                        }

                        @Override
                        public void onNullResponse() {
                            Log.e("response: ", "null");
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            Log.e("response: ", "onFail");
                        }
                    }, SessionManager.getAccessToken()).updateUserImage(pickResult.getPath());


                } catch (Exception e) {
                    Log.d(TAG, "onPickResult: " + e.getMessage());

                    Toast.makeText(DriverRegistrationActivity.this, "Erorr uploading file", Toast.LENGTH_SHORT).show();
                }

            }

        }).setOnPickCancel(new IPickCancel() {
            @Override
            public void onCancelClick() {

            }
        }).show(DriverRegistrationActivity.this);
    }

    private void setImagesCar() {
        PickSetup setup = new PickSetup();
        //PickImageDialog.build(setup).show(DriverRegistrationActivity.this);


        PickImageDialog pickImageDialog = PickImageDialog.newInstance(setup);
        //pickImageDialog.show(DriverRegistrationActivity.this);

        PickImageDialog.build(setup, new IPickResult() {
            @Override
            public void onPickResult(PickResult pickResult) {
                if (pickResult.getError() == null) {
                    try {
                        CircleImageView image_car = findViewById(R.id.image_car);
                        image_car.setVisibility(View.VISIBLE);
                        image_car.setImageBitmap(pickResult.getBitmap());
                        showDialogProgressBar();
                        DruppRequestHandler.clearInstance();
                        DruppRequestHandler.getInstance(new INetwork<MyResponse>() {
                            @Override
                            public void onResponse(Response<QualStandardResponse<MyResponse>> response) {
                                hideDialogProgressBar();
                                if (response.isSuccessful()) {
                                    MyResponse myResponse = response.body().getResponse();

                                    file_path = myResponse.getFile_path();
                                    file_name = myResponse.getFile_name();
                                    x_response = myResponse;
                                    ImageDetail imageDetail = new ImageDetail(file_name, file_path);
                                    images.add(imageDetail);
                                }
                            }

                            @Override
                            public void onError(Response<QualStandardResponse<MyResponse>> response) {
                                showDialogProgressBar();
                                showErrorPrompt(response);
                            }

                            @Override
                            public void onNullResponse() {
                                Log.e("response: ", "null");
                                showDialogProgressBar();
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                showDialogProgressBar();
                                Log.e("response: ", "onFail");
                            }
                        }, SessionManager.getAccessToken()).updateUserImage(pickResult.getPath());
                    } catch (Exception e) {
                        Log.e("Exception: ", e.getMessage());
                    }
                }
            }
        }).setOnPickCancel(new IPickCancel() {
            @Override
            public void onCancelClick() {
            }
        }).show(DriverRegistrationActivity.this);


    }


    private void saveDetails() {
        //TODO : CHANGE STATIC COORDINATES
        DruppRequestHandler.clearInstance();

        HashMap<String, String> parse = new HashMap<>();
        parse.put(AppConstants.K_FIRST_NAME, fName.getText().toString());
        parse.put(AppConstants.K_LAST_NAME, lName.getText().toString());
        parse.put(AppConstants.K_EMAIL, email.getText().toString());
        parse.put(AppConstants.K_PASSWORD, password.getText().toString());
        parse.put(AppConstants.K_PHONE, phone_num.getText().toString());
        parse.put(AppConstants.K_CITY, city.getText().toString());
        parse.put(AppConstants.K_LICENSE_NUMBER, lisc_num.getText().toString());
        parse.put(AppConstants.K_VEHICLE_NUMBER, veh_num.getText().toString());
        parse.put(AppConstants.K_VEHICLE_NAME, veh_name.getText().toString());
        parse.put(AppConstants.K_IMAGES, " ");
        parse.put(AppConstants.K_PROFILE_IMAGE, profile_file_path);
        parse.put(AppConstants.K_VEHICLE_TYPE_ID, "1");
        parse.put(AppConstants.K_LATITUDE, "48.68610");
        parse.put(AppConstants.K_LONGITUDE, "95.86016");
        parse.put(AppConstants.K_PLATFORM, "1");
        parse.put(AppConstants.K_FIREBASE_TOKEN, MainActivity.fb_token);
        parse.put(AppConstants.K_COUNTRY_CODE, "234");
        parse.put(AppConstants.K_VEHICLE_COLOR, veh_color.getText().toString());
        parse.put(AppConstants.K_VEHICLE_BRAND, veh_brand.getText().toString());
        parse.put(AppConstants.K_VEHICLE_MODEL, veh_model.getText().toString());
        parse.put(AppConstants.K_CHASSIS_NUMBER, veh_chassis_num.getText().toString());

        Driverregistration dr = new Driverregistration();

        dr.setFirst_name(fName.getText().toString());
        dr.setLast_name(lName.getText().toString());
        dr.setEmail(email.getText().toString());
        dr.setPassword(password.getText().toString());
        dr.setPhone(phone_num.getText().toString());
        dr.setCity(city.getText().toString());
        dr.setLicense_number(lisc_num.getText().toString());
        dr.setVehicle_number(veh_num.getText().toString());
        dr.setVehicle_name(veh_name.getText().toString());
        dr.setImages(images);
        dr.setVehicle_type_id(car_type);
        dr.setLatitude("22.68610");
        dr.setLongitude("75.86016");
        dr.setPlatform("1");
        dr.setFirebase_token(MainActivity.fb_token);
        dr.setCountry_code("234");
        dr.setVehicle_color(veh_color.getText().toString());
        dr.setVehicle_brand(veh_brand.getText().toString());
        dr.setVehicle_model(veh_model.getText().toString());
        dr.setChassis_number(veh_chassis_num.getText().toString());

        //retrieveValuesFromListMethod(parse);

        DruppRequestHandler.getInstance(new INetwork<HashMap<String, String>>() {
            @Override
            public void onResponse(Response<QualStandardResponse<HashMap<String, String>>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getResponse().get(AppConstants.K_TOKEN);
                    String driverId = response.body().getResponse().get(AppConstants.K_DRIVER_ID);
                    editor.putString("token", token).apply();

                    UserModel userModel = new UserModel();
                    userModel.setId(Integer.valueOf(driverId));
                    userModel.setCountryCode(234);
                    userModel.setName(fName.getText().toString() + " " + lName.getText().toString());
                    userModel.setVehichle_name(veh_name.getText().toString());
                    userModel.setVehichle_number(veh_num.getText().toString());
                    userModel.setColor(veh_color.getText().toString());
                    userModel.setEmail(email.getText().toString());
                    userModel.setVeh_brand(veh_brand.getText().toString());
                    userModel.setVeh_chassis_num(veh_chassis_num.getText().toString());
                    userModel.setVeh_model(veh_model.getText().toString());
                    userModel.setToken(token);
                    SessionManager.getInstance().saveUser(DriverRegistrationActivity.this, userModel);
                    Intent intent = new Intent(DriverRegistrationActivity.this, RideActivity.class);
                    intent.putExtra(AppConstants.K_LAUNCH_TYPE, AppConstants.NEW_USER);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onError(Response<QualStandardResponse<HashMap<String, String>>> response) {
                findViewById(R.id.btStartRide).setEnabled(true);
                showErrorPrompt(response);
            }

            @Override
            public void onNullResponse() {
                findViewById(R.id.btStartRide).setEnabled(true);
                Toast.makeText(DriverRegistrationActivity.this, "on null", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable t) {
                findViewById(R.id.btStartRide).setEnabled(true);
                Toast.makeText(DriverRegistrationActivity.this, "on failure", Toast.LENGTH_SHORT).show();
            }
        }, SessionManager.getAccessToken()).userSignin(new HashMap<>());


    }


    private boolean checkDriverData() {
        if (fName.getText().toString().equals("")
                || lName.getText().toString().equals("")
                || city.getText().toString().equals("")
                || email.getText().toString().equals("")
                || password.getText().toString().equals("")
                || phone_num.getText().toString().equals("")) {
            Toast.makeText(this, getString(R.string.please_fill_all), Toast.LENGTH_SHORT).show();

            return false;

        } else if (!isEmailValid(email.getText().toString())) {
            Toast.makeText(this, getString(R.string.enter_valid_email), Toast.LENGTH_SHORT).show();
            return false;
        } else if (!mTerms.isChecked()) {
            Toast.makeText(this, getString(R.string.agree_to_tnc), Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }

    private boolean chechVehicleData() {
        if (veh_num.getText().toString().equals("")
                || veh_name.getText().toString().equals("")
                || lisc_num.getText().toString().equals("")
                || veh_color.getText().toString().equals("")
                || veh_model.getText().toString().equals("")
                || veh_chassis_num.getText().toString().equals("")
                || veh_brand.getText().toString().equals("")) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();

            return false;
        } else if (x_response == null) {
            Toast.makeText(this, "Please Upload vehichle image", Toast.LENGTH_SHORT).show();
            return false;
        } else if (btnVehicleSpinner.getText().toString().equalsIgnoreCase(getString(R.string.select_car_type))) {
            Toast.makeText(this, "Please Select Car Type", Toast.LENGTH_SHORT).show();
            return false;

        }


        return true;
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();

        if (index == 1) {
            index = 0;
            vehicleRegistrationView = null;
            driverRegistrationView = null;
            setUpRegistrationActivity();
        } else {
            finish();
        }
    }

    public static void retrieveValuesFromListMethod(HashMap<String, String> map) {
        Set keys = map.keySet();
        Iterator itr = keys.iterator();

        String key;
        String value;
        while (itr.hasNext()) {
            key = (String) itr.next();
            value = map.get(key);
            System.out.println(key + "-" + value);
        }
    }

}
