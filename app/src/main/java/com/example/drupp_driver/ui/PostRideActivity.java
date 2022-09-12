package com.example.drupp_driver.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.drupp_driver.Models.PostRideDetails;
import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.Helper;
import com.example.drupp_driver.Utils.IDateDialogObserver;
import com.example.drupp_driver.Utils.IDialogButtonClickListener;
import com.example.drupp_driver.Utils.ITimeDialogObserver;
import com.example.drupp_driver.Utils.RxBus;
import com.example.drupp_driver.Utils.Timing;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetwork;
import com.example.drupp_driver.connectivity.http.QualStandardResponse;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.IFragmentEventObserver;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.helpers.UIHelper;
import com.example.drupp_driver.ui.base.MainBaseActivity;
import com.example.drupp_driver.ui.dialogs.CompletedDialog;
import com.example.drupp_driver.ui.dialogs.DateDialog;
import com.example.drupp_driver.ui.dialogs.PlaceSearchDialog;
import com.example.drupp_driver.ui.dialogs.TimeDialog;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.card.MaterialCardView;
import com.victor.loading.rotate.RotateLoading;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Response;

public class PostRideActivity extends MainBaseActivity implements IDateDialogObserver,
        ITimeDialogObserver, IFragmentEventObserver, IDialogButtonClickListener {

    // Driver can post aride in this activity
    private static final String TAG = "PostRideActivity";
    private int num_riders = 3;

    private TextView total_riders;
    private MaterialCardView cardDateSelector, cardTimeSelector;
    private TextView tvDate, eReminderTime;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private DateDialog dateDialog;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;

    @BindView(R.id.container_single_ride)
    LinearLayout containerSingleRide;
    @BindView(R.id.container_pool_ride)
    LinearLayout containerPoolRide;
    @BindView(R.id.et_source)
    EditText etSource;
    @BindView(R.id.et_destination)
    EditText etDestination;
    private Place fromPlace, toPlace;

    private EditText fare, oneFare, twoFare, threeFare;
    private TextView tvNumRiders;


    // private EditText mSource,mDestonation;
    String scity = "", dcity = "";

    public static String auth = "";

    private String xtime, xDate;
    private TextView tvWith, tvWithValue;

    private int ride_type = 1, passenger_pref = 1;

    PlacesClient placesClient;

    Place src_place = null, des_place = null;
    private RotateLoading rotateLoading;
    private int currentPoolFare = 3;
    private CompletedDialog completedDialog;


    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_ride_new);
        ButterKnife.bind(this);

        total_riders = findViewById(R.id.tvTotalRiders);


        fare = findViewById(R.id.tvFare);
        rotateLoading = findViewById(R.id.pb_rl);

        auth = "Bearer " + SessionManager.getInstance().getUserModel().getToken();
        // tvNumRiders=findViewById(R.id.tvNumRiders);

//        mSource=findViewById(R.id.tvSource);
//        mDestonation=findViewById(R.id.tvDestination);

        tvWith = findViewById(R.id.tvWith);
        tvWithValue = findViewById(R.id.tvWithValue);

        cardDateSelector = findViewById(R.id.cardDateSelector);
        cardTimeSelector = findViewById(R.id.cardTimeSelector);


        initPlaces();

        // Specify the types of place data to return.


        // Specify thle types of place data to return.


        findViewById(R.id.image_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.ivAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (num_riders < 3) {
                    num_riders++;
                    total_riders.setText(String.valueOf(num_riders));
                    passenger_pref = num_riders;
                } else
                    Toast.makeText(PostRideActivity.this, "Maximum three riders allowed", Toast.LENGTH_SHORT).show();
                switch (num_riders) {
                    case 1:
                        currentPoolFare = 10;
                        tvWith.setText(getString(R.string.with_one_person));
                        tvWithValue.setText(getString(R.string.with_one_person_10));
                        break;
                    case 2:
                        currentPoolFare = 5;
                        tvWith.setText(getString(R.string.with_two_person));
                        tvWithValue.setText(getString(R.string.with_two_person_5));
                        break;
                    case 3:
                        currentPoolFare = 3;
                        tvWith.setText(getString(R.string.with_three_person));
                        tvWithValue.setText(getString(R.string.with_three_person_3));
                        break;
                }


            }
        });

        findViewById(R.id.ivSubtract).setOnClickListener(v -> {

            if (num_riders > 1) {
                num_riders--;
                total_riders.setText(String.valueOf(num_riders));
                passenger_pref = num_riders;
            } else
                Toast.makeText(PostRideActivity.this, getString(R.string.minimum_riders_required), Toast.LENGTH_SHORT).show();
            switch (num_riders) {
                case 1:
                    tvWith.setText(getString(R.string.with_one_person));
                    tvWithValue.setText(getString(R.string.with_one_person_10));
                    currentPoolFare = 10;
                    break;
                case 2:
                    currentPoolFare = 5;
                    tvWith.setText(getString(R.string.with_two_person));
                    tvWithValue.setText(getString(R.string.with_two_person_5));
                    break;
                case 3:
                    currentPoolFare = 3;
                    tvWith.setText(getString(R.string.with_three_person));
                    tvWithValue.setText(getString(R.string.with_three_person_3));
                    break;
            }


        });
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_alert_dailog_post_ride, null);
        AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(PostRideActivity.this);
        final AlertDialog alertDialog = alertDialogbuilder.create();
        alertDialog.setView(alertLayout);

        final Calendar cal = Calendar.getInstance();

        tvDate = findViewById(R.id.tvDate);


        cardDateSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int year = cal.get(Calendar.YEAR);
//                int month = cal.get(Calendar.MONTH);
//                int day = cal.get(Calendar.DAY_OF_MONTH);
//
//                Calendar c = Calendar.getInstance();
//                c.add(Calendar.DATE, 2);
//
//                DatePickerDialog dialog = new DatePickerDialog(
//                        PostRideActivity.this,
//                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
//                        mDateSetListener,
//                        year, month, day);
//                dialog.getDatePicker().setMinDate(cal.getTimeInMillis());
//                dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                dialog.show();
                showDateDialog();

            }
        });


        eReminderTime = findViewById(R.id.tvTime);

        cardTimeSelector.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // Calendar mcurrentTime = Calendar.getInstance();
//                int hour = cal.get(Calendar.HOUR_OF_DAY);
//                int minute = cal.get(Calendar.MINUTE);
//                TimePickerDialog mTimePicker;
//                mTimePicker = new TimePickerDialog(PostRideActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//
//
//                    }
//                }, hour, minute, true);//Yes 24 hour time

                // mTimePicker=new TimePickerDialog(PostRideActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,mTimeSetListener,hour,minute);
//                mTimePicker.setTitle("Select Time");
//                mTimePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                mTimePicker.show();
                showTimeDialog();

            }
        });



        //Set Date
        Calendar calendar = Calendar.getInstance();
        xDate = dateFormat.format(calendar.getTime());
        tvDate.setText(xDate);
        xtime = timeFormat1.format(calendar.getTime());
        eReminderTime.setText(xtime);


        findViewById(R.id.post_ride).setOnClickListener(v -> {

            if (checkDetails()) {
                rotateLoading.setVisibility(View.VISIBLE);
                rotateLoading.start();
                //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm", Locale.ENGLISH);
                // Date date=new Date();
                try {

                    // Date date=simpleDateFormat.parse(tvDate.getText().toString()+" "+eReminderTime.getText().toString());
                    // Date date=simpleDateFormat.parse(xDate+" "+xtime);
                    String alldt = xDate + " " + xtime;

                    long date = Timing.getTimeInUnixStamp(alldt, Timing.TimeFormats.CUSTOM_DATE_TIME);
                    Log.d(TAG, "saveDetails: Date " + date);
                    PostRideDetails postRideDetails;

                    if (fare.getVisibility() == View.VISIBLE) {
                        if (fare.getText().toString().trim().isEmpty()) {
                            showMessage(R.string.please_enter_fare);
                            return;
                        }
                        postRideDetails = new PostRideDetails(fromPlace.getAddress(), toPlace.getAddress(),
                                fromPlace.getLatLng().latitude, fromPlace.getLatLng().longitude, toPlace.getLatLng().latitude, toPlace.getLatLng().longitude
                                , ride_type, date, fare.getText().toString().trim(), passenger_pref);
                    } else {
                        postRideDetails = new PostRideDetails(fromPlace.getAddress(), toPlace.getAddress(),
                                fromPlace.getLatLng().latitude, fromPlace.getLatLng().longitude, toPlace.getLatLng().latitude, toPlace.getLatLng().longitude
                                , ride_type, date, String.valueOf(currentPoolFare), passenger_pref);
                    }

                    Log.e("Ride Posted","Rides");
                    saveDetails(postRideDetails);


                } catch (Exception e) {

                    Log.e(TAG, "saveDetails: exception" + e.getMessage());
                    Toast.makeText(PostRideActivity.this, "Some error occured", Toast.LENGTH_SHORT).show();

                }

                Toast.makeText(PostRideActivity.this, "Ride Posted \n Please wait...", Toast.LENGTH_SHORT).show();


            }
        });


        containerSingleRide.setSelected(true);

        setupPlaces();
    }

    private void setupPlaces() {
        etSource.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt(AppConstants.K_LOCATION_TYPE, AppConstants.HOME_LOCATION);
            bundle.putString(AppConstants.SEARCH_DIALOG_TITLE, getString(R.string.enter_pickup_location));

            PlaceSearchDialog placeSearchDialog = PlaceSearchDialog.newInstance(bundle);
            placeSearchDialog.show(getSupportFragmentManager(), PlaceSearchDialog.class.getSimpleName());

        });
        etSource.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                Bundle bundle = new Bundle();
                bundle.putInt(AppConstants.K_LOCATION_TYPE, AppConstants.HOME_LOCATION);
                bundle.putString(AppConstants.SEARCH_DIALOG_TITLE, getString(R.string.enter_pickup_location));

                PlaceSearchDialog placeSearchDialog = PlaceSearchDialog.newInstance(bundle);
                placeSearchDialog.show(getSupportFragmentManager(), PlaceSearchDialog.class.getSimpleName());

            } else {
                hideKeyboard();
            }
        });
        etDestination.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                Bundle b = new Bundle();
                b.putInt(AppConstants.K_LOCATION_TYPE, AppConstants.WORK_LOCATION);
                b.putString(AppConstants.SEARCH_DIALOG_TITLE, getString(R.string.enter_drop_location));

                PlaceSearchDialog placeSearchDialog = PlaceSearchDialog.newInstance(b);
                placeSearchDialog.show(getSupportFragmentManager(), PlaceSearchDialog.class.getSimpleName());

            } else {
                hideKeyboard();
            }
        });
        etDestination.setOnClickListener(v -> {
            Bundle b = new Bundle();
            b.putInt(AppConstants.K_LOCATION_TYPE, AppConstants.WORK_LOCATION);
            b.putString(AppConstants.SEARCH_DIALOG_TITLE, getString(R.string.enter_drop_location));

            PlaceSearchDialog placeSearchDialog = PlaceSearchDialog.newInstance(b);
            placeSearchDialog.show(getSupportFragmentManager(), PlaceSearchDialog.class.getSimpleName());


        });

    }


    @OnClick(R.id.container_single_ride)
    public void onSingleRide() {
        containerSingleRide.setSelected(true);
        containerPoolRide.setSelected(false);

        ride_type = 1;
        passenger_pref = 1;

        findViewById(R.id.lin_c).setVisibility(View.GONE);
        findViewById(R.id.lin_num).setVisibility(View.GONE);
        findViewById(R.id.lin_rider_counter).setVisibility(View.GONE);
        fare.setVisibility(View.VISIBLE);
    }


    @OnClick(R.id.container_pool_ride)
    public void onPoolRide() {
        containerPoolRide.setSelected(true);
        containerSingleRide.setSelected(false);


        ride_type = 2;
        passenger_pref = num_riders;

        fare.setVisibility(View.GONE);

        findViewById(R.id.lin_c).setVisibility(View.VISIBLE);
        findViewById(R.id.lin_num).setVisibility(View.VISIBLE);
        findViewById(R.id.lin_rider_counter).setVisibility(View.VISIBLE);
    }

    private void showDateDialog() {
        Bundle bundle = new Bundle();
        bundle.putLong(AppConstants.K_MIN_DATE, System.currentTimeMillis() + 1000);
        bundle.putLong(AppConstants.K_MAX_DATE, System.currentTimeMillis() + 24 * 7 * 60 * 60 * 1000);
        dateDialog = DateDialog.newInstance(bundle);
        dateDialog.setiDateDialogObserver(this);
        dateDialog.show(getSupportFragmentManager(), DateDialog.class.getSimpleName());
    }

    private void showTimeDialog() {
        TimeDialog timeDialog = TimeDialog.newInstance();
        timeDialog.setiTimeDialogObserver(this);
        timeDialog.show(getSupportFragmentManager(), TimeDialog.class.getSimpleName());


    }

    private boolean checkDetails() {
        if (tvDate.getText().toString().isEmpty()) {
            showMessage(R.string.please_enter_date);
            return false;
        } else if (eReminderTime.getText().toString().trim().isEmpty()) {
            showMessage(R.string.please_enter_time);
            return false;
        } else if (etSource.getText().toString().trim().isEmpty()) {
            showMessage(R.string.please_enter_source);
            return false;

        } else if (etDestination.getText().toString().trim().isEmpty()) {
            showMessage(R.string.please_enter_destination);
            return false;
        }
        else if(!checkDifferenceBtwDates())
        {
            showMessage("You can only schedule 24 hours later from now");
            return false;
        }

        return true;

    }

    private boolean checkDifferenceBtwDates() {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        String date = xDate+" "+xtime;
        Date selectedDate = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        try {
            selectedDate = simpleDateFormat.parse(date);
            long diff = selectedDate.getTime() - currentDate.getTime();

            long diffDays =(long)( diff/(24*60*60*1000));

            return diffDays >= 1;

        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

    }

    private void saveDetails(PostRideDetails pd) {
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<HashMap<String, String>>() {
            @Override
            public void onResponse(Response<QualStandardResponse<HashMap<String, String>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    rotateLoading.setVisibility(View.GONE);
                    rotateLoading.stop();

                    Helper.savePostRideId(Integer.valueOf(response.body().getResponse().get(AppConstants.K_RIDE_ID)), PostRideActivity.this);
                    completedDialog = new CompletedDialog(PostRideActivity.this,R.layout.dialog_ride_posted);
                    completedDialog.show(getSupportFragmentManager(),CompletedDialog.class.getSimpleName());

                }

            }

            @Override
            public void onError(Response<QualStandardResponse<HashMap<String, String>>> response) {
                rotateLoading.setVisibility(View.GONE);
                rotateLoading.stop();
                showErrorPrompt(response);
            }

            @Override
            public void onNullResponse() {
                rotateLoading.setVisibility(View.GONE);
                rotateLoading.stop();
            }

            @Override
            public void onFailure(Throwable t) {
                rotateLoading.setVisibility(View.GONE);
                rotateLoading.stop();
                showAlertDialog(R.layout.dialog_network_error);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(view -> {
                        hideAlertDialog();
                        saveDetails(pd);
                    });
                }
            }
        }, SessionManager.getAccessToken()).postRide(pd);
    }

    private void showCustomDailog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_alert_dailog_ride_posted, null);
        AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = alertDialogbuilder.create();
        alertDialog.setView(alertLayout);

        alertDialog.setCanceledOnTouchOutside(false);


        alertDialog.show();


        alertDialog.findViewById(R.id.btOk).setOnClickListener(v -> {


            alertDialog.dismiss();

            Intent intent = new Intent(PostRideActivity.this, RideActivity.class);
            startActivity(intent);
            finish();

        });


    }

    private void initPlaces() {
        Places.initialize(this, getString(R.string.google_maps_key));
        //var place =  PlaceAPI.Builder().apikey(getString(R.string.google_maps_key)).build(this);
        placesClient = Places.createClient(this);
    }

    SimpleDateFormat dateFormat = new SimpleDateFormat("d/MM/yyyy", Locale.US);
    SimpleDateFormat timeFormat1 = new SimpleDateFormat("HH:mm:ss",Locale.US);
    @Override
    public void onDateSelected(int year, int month, int dayOfMonth) {
        String sday = String.valueOf(dayOfMonth);
        String smonth = String.valueOf(month);
        if (dayOfMonth < 10) {
            sday = "0" + sday;
        }
        if (month < 10) {
            smonth = "0" + smonth;
        }
        String date = sday + "/" + smonth + "/" + year;
        tvDate.setText(date);
        xDate = date;
    }

    @Override
    public void onTimeSelected(int hour, int minute) {
        String sHour = String.valueOf(hour);
        String sMinute = String.valueOf(minute);

        if (hour < 10) {
            sHour = "0" + sHour;
        }
        if (minute < 10) {
            sMinute = "0" + sMinute;
        }

        String time = sHour + ":" + sMinute + ":" + "00";
        eReminderTime.setText(time);
        xtime = sHour + ":" + sMinute+":"+"00";
    }

    @Override
    public void onPlaceSelected(Place place, int locationType) {
        if (locationType == AppConstants.HOME_LOCATION) {
            fromPlace = place;
            etSource.setText(place.getAddress());
        } else if (locationType == AppConstants.WORK_LOCATION) {
            toPlace = place;
            etDestination.setText(place.getAddress());
        }
    }

    @Override
    public void onButtonClick() {
        Intent intent = new Intent(PostRideActivity.this, ScheduledRidesActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void setUpDialogViews(View view) {
        TextView tvSource, tvDestination,tvDate,tvTime,tvRideTpe;
        tvSource=view.findViewById(R.id.tv_source);
        tvDestination=view.findViewById(R.id.tv_destination);
        tvDate=view.findViewById(R.id.date);
        tvTime=view.findViewById(R.id.time);
        tvRideTpe=view.findViewById(R.id.ride_type);
        tvSource.setText( etSource.getText().toString());
        tvDestination.setText(etDestination.getText().toString());
        tvDate.setText(this.tvDate.getText().toString());
        tvTime.setText(eReminderTime.getText().toString());
        String rideType=ride_type==1?"Single":"Pool";
        StringBuilder rideTypeAndVehicle=new StringBuilder(rideType);


        tvRideTpe.setText(rideTypeAndVehicle);

    }
}
