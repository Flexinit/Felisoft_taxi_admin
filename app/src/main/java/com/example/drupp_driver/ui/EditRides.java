package com.example.drupp_driver.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.drupp_driver.Models.EditRideDetails;
import com.example.drupp_driver.Models.Token;
import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.Timing;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetworkList;
import com.example.drupp_driver.connectivity.http.QualStandardResponseList;
import com.example.drupp_driver.helpers.SessionManager;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.victor.loading.rotate.RotateLoading;

import java.util.Arrays;
import java.util.Calendar;

import retrofit2.Response;

public class EditRides extends AppCompatActivity {

    private static final String TAG = "EditRides";
    private int num_riders = 3;

    private TextView total_riders;

    private EditText tvDate, eReminderTime;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;

    private RadioButton rbSingle, rbPool;

    private EditText fare, oneFare, twoFare, threeFare;
    private TextView tvNumRiders;

    private RadioGroup radioGroup;
    AutocompleteSupportFragment mSource, mDestonation;

    // private EditText mSource,mDestonation;
    String scity = "", dcity = "";

    public static String auth = "";

    private String xtime, xDate;
    private TextView tvWith, tvWithValue;

    private int ride_type = 0, passenger_pref = 1;

    PlacesClient placesClient;

    Place src_place = null, des_place = null;
    private RotateLoading rotateLoading;

    int id;
    public AlertDialog alertDialog;

    private TextView tvtoolbar;

    // for editing the scheduled rides
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_rides);

//        total_riders=findViewById(R.id.tvTotalRiders);
//        rbSingle=findViewById(R.id.rbSingle);
//        rbPool=findViewById(R.id.rbPool);

        fare = findViewById(R.id.tvFare);
        //rotateLoading=findViewById(R.id.pb_rl);

        auth = "Bearer " + SessionManager.getInstance().getUserModel().getToken();
        Log.d(TAG, "onCreate: " + auth);
        // tvNumRiders=findViewById(R.id.tvNumRiders);

//        mSource=findViewById(R.id.tvSource);
//        mDestonation=findViewById(R.id.tvDestination);

//        tvWith=findViewById(R.id.tvWith);
//        tvWithValue=findViewById(R.id.tvWithValue);


        //radioGroup=findViewById(R.id.rg);


        tvtoolbar = findViewById(R.id.tvToolText);
        tvtoolbar.setText("Edit Ride");


        initPlaces();
        mSource = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        mSource.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));


        mSource.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                scity = place.getName();
                src_place = place;
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        mDestonation = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment2);

        // Specify the types of place data to return.
        mDestonation.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        mDestonation.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                dcity = place.getName();
                des_place = place;
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });


//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//
//                if (checkedId==R.id.rbSingle)
//                {
////                   findViewById(R.id.lin_a).setVisibility(View.GONE);
////                   findViewById(R.id.lin_b).setVisibility(View.GONE);
//                    findViewById(R.id.lin_c).setVisibility(View.GONE);
//                    findViewById(R.id.lin_num).setVisibility(View.GONE);
//                    findViewById(R.id.lin_rider_counter).setVisibility(View.GONE);
//                    ride_type=1;
//                    passenger_pref=1;
//                }
//                else if (checkedId==R.id.rbPool)
//                {
////                    findViewById(R.id.lin_a).setVisibility(View.VISIBLE);
////                    findViewById(R.id.lin_b).setVisibility(View.VISIBLE);
//                    findViewById(R.id.lin_c).setVisibility(View.VISIBLE);
//                    findViewById(R.id.lin_num).setVisibility(View.VISIBLE);
//                    findViewById(R.id.lin_rider_counter).setVisibility(View.VISIBLE);
//                    ride_type=2;
//                    passenger_pref=num_riders;
//
//                }
//            }
//        });


        findViewById(R.id.image_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        findViewById(R.id.ivAdd).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (num_riders<3)
//                {
//                    num_riders++;
//                    total_riders.setText(String.valueOf(num_riders));
//                    passenger_pref=num_riders;
//                }
//                else
//                    Toast.makeText(EditRides.this, "Maximum three riders allowed", Toast.LENGTH_SHORT).show();
//                switch (num_riders)
//                {
//                    case 1:
//                        tvWith.setText("With one person: ");
//                        tvWithValue.setText("$10");
//                        break;
//                    case 2:
//                        tvWith.setText("With two person: ");
//                        tvWithValue.setText("$5");
//                        break;
//                    case 3:
//                        tvWith.setText("With three person: ");
//                        tvWithValue.setText("$3.33");
//                        break;
//                }
//
//
//            }
//        });

//        findViewById(R.id.ivSubtract).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (num_riders>1)
//                {
//                    num_riders--;
//                    total_riders.setText(String.valueOf(num_riders));
//                    passenger_pref=num_riders;
//                }
//                else
//                    Toast.makeText(EditRides.this, "Minimum one riders required", Toast.LENGTH_SHORT).show();
//                switch (num_riders)
//                {
//                    case 1:
//                        tvWith.setText("With one person: ");
//                        tvWithValue.setText("$10");
//                        break;
//                    case 2:
//                        tvWith.setText("With two person: ");
//                        tvWithValue.setText("$5");
//                        break;
//                    case 3:
//                        tvWith.setText("With three person: ");
//                        tvWithValue.setText("$3.33");
//                        break;
//                }
//
//
//            }
//        });

//        LayoutInflater inflater = getLayoutInflater();
//        View alertLayout = inflater.inflate(R.layout.custom_alert_dailog_post_ride, null);
//        AlertDialog.Builder alertDialogbuilder=new AlertDialog.Builder(EditRides.this);
//        alertDialog=alertDialogbuilder.create();
//        alertDialog.setView(alertLayout);


        final Calendar cal = Calendar.getInstance();

        tvDate = findViewById(R.id.tvDate);


        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        EditRides.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getDatePicker().setMinDate(cal.getTimeInMillis());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String sday = String.valueOf(day);
                String smonth = String.valueOf(month);
                String syear = String.valueOf(year);
                if (day < 10) {
                    sday = "0" + sday;
                }
                if (month < 10) {
                    smonth = "0" + smonth;
                }
                String date = sday + "-" + smonth + "-" + year;
                tvDate.setText(date);
                xDate = date;
            }
        };

        eReminderTime = findViewById(R.id.tvTime);

        eReminderTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // Calendar mcurrentTime = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EditRides.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        String sHour = String.valueOf(selectedHour);
                        String sMinute = String.valueOf(selectedMinute);

                        if (selectedHour < 10) {
                            sHour = "0" + sHour;
                        }
                        if (selectedMinute < 10) {
                            sMinute = "0" + sMinute;
                        }

                        eReminderTime.setText(sHour + ":" + sMinute + ":" + "00");
                        xtime = sHour + ":" + sMinute;
                    }
                }, hour, minute, true);//Yes 24 hour time

                // mTimePicker=new TimePickerDialog(PostRideActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,mTimeSetListener,hour,minute);
                mTimePicker.setTitle("Select Time");
                mTimePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mTimePicker.show();

            }
        });


        Intent intent = getIntent();
        tvDate.setText(handleDateString(intent.getStringExtra("date")));
        eReminderTime.setText(handleTimeString((intent.getStringExtra("date"))));

        id = intent.getIntExtra("ride_id", 1);
        passenger_pref = intent.getIntExtra("passenger_pref", 1);

        mSource.setText(intent.getStringExtra("scity"));
        scity = intent.getStringExtra("scity");

        mDestonation.setText(intent.getStringExtra("dcity"));
        dcity = intent.getStringExtra("dcity");

        xDate = handleDateString(intent.getStringExtra("date"));
        xtime = filterTimeString(handleTimeString(intent.getStringExtra("date")));

        fare.setText(String.valueOf(intent.getDoubleExtra("base_fare", 1.00)));

        ride_type = intent.getIntExtra("ride_type", 0);

        findViewById(R.id.post_ride).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkDetails()) {
                    try {
                        //TODO : edit lat long to be dynamic
                        String alldt = xDate + " " + xtime;
                        long date = Timing.getTimeInUnixStamp(alldt, Timing.TimeFormats.CUSTOM_DATE_TIME);
                        {
                            Double src_lat = 22.68610;
                            Double src_lon = 75.86016;
                            Double des_lat = 23.71755;
                            Double des_lon = 76.86016;
                            EditRideDetails pd = new EditRideDetails(
                                    id,
                                    EditRides.this.scity,
                                    src_lat,
                                    src_lon,
                                    EditRides.this.dcity,
                                    des_lat,
                                    des_lon,
                                    String.valueOf(date),
                                    Double.valueOf(fare.getText().toString()),
                                    passenger_pref,
                                    ride_type);

                            saveDetails(pd);
                        }
                    } catch (Exception e) {

                        Log.d(TAG, "saveDetails: exception" + e.getMessage());
                        Toast.makeText(EditRides.this, "Some error occured", Toast.LENGTH_SHORT).show();

                    }

                    Toast.makeText(EditRides.this, "Ride Posted \n Please wait...", Toast.LENGTH_SHORT).show();


                }
            }
        });


    }

    private boolean checkDetails() {


        if (tvDate.getText().toString().equals("")
                || eReminderTime.getText().toString().equals("")
                || EditRides.this.dcity.equals("")
                || EditRides.this.scity.equals("")
                || fare.getText().toString().equals("")) {
            Toast.makeText(this, "please fill all the details", Toast.LENGTH_SHORT).show();
            return false;
        } //else if (!rbPool.isChecked() && !rbSingle.isChecked()) {
//
//            Toast.makeText(this, "Please select type of ride", Toast.LENGTH_SHORT).show();
//            return false;
//        }

        return true;

    }

    private void initPlaces() {
        Places.initialize(this, getString(R.string.google_maps_key));
        //var place =  PlaceAPI.Builder().apikey(getString(R.string.google_maps_key)).build(this);
        placesClient = Places.createClient(this);
    }

    private void showCustomDailog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_alert_dailog_ride_posted, null);
        AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = alertDialogbuilder.create();
        alertDialog.setView(alertLayout);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        TextView tvRidePosted = alertDialog.findViewById(R.id.tvRidePosted);
        tvRidePosted.setText(R.string.ride_edited_successfully
        );
        alertDialog.findViewById(R.id.btOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent intent = new Intent(EditRides.this, TestActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private String handleDateString(String timeStamp) {
        java.util.Date given_timestamp = new java.util.Date((long) (Long.valueOf(timeStamp)) * 1000);

        String my_timestamp = String.valueOf(given_timestamp);

        String[] arr = my_timestamp.split(" ");
        String week_day = arr[0];
        String month = arr[1];
        String day = arr[2];
        String time = arr[3];
        String utc = arr[4];
        String year = arr[5];

        switch (month) {
            case "Jan":
                month = "1";
                break;
            case "Feb":
                month = "2";
                break;
            case "Mar":
                month = "3";
                break;
            case "Apr":
                month = "4";
                break;
            case "May":
                month = "5";
                break;
            case "Jun":
                month = "6";
                break;
            case "Jul":
                month = "7";
                break;
            case "Aug":
                month = "8";
                break;
            case "Sep":
                month = "9";
                break;
            case "Oct":
                month = "10";
                break;
            case "Nov":
                month = "11";
                break;
            case "Dec":
                month = "12";
                break;

        }

        String xdate = (day + "-" + month + "-" + year);
        return xdate;
    }

    private String handleTimeString(String timeStamp) {
        java.util.Date given_timestamp = new java.util.Date((long) (Long.valueOf(timeStamp)) * 1000);

        String my_timestamp = String.valueOf(given_timestamp);

        String[] arr = my_timestamp.split(" ");
        String week_day = arr[0];
        String month = arr[1];
        String day = arr[2];
        String time = arr[3];
        String utc = arr[4];
        String year = arr[5];

        String xdate = (day + ":" + month + ":" + year);
        return time;
    }

    private String filterTimeString(String timeStamp) {
        //java.util.Date given_timestamp=new java.util.Date((long)(Long.valueOf(timeStamp))*1000);

        String my_timestamp = String.valueOf(timeStamp);

        String[] arr = my_timestamp.split(":");
        String hour = arr[0];
        String min = arr[1];


        //String xdate=(day+":"+month+":"+year);
        return hour + ":" + min;
    }

    private void saveDetails(EditRideDetails pd) {
        DruppRequestHandler.clearInstance();


        DruppRequestHandler.getInstance(new INetworkList<Token>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<Token>> response) {


                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "onResponseList: Post ride Successsssssssssssssssssssssssssssss");

//                    rotateLoading.setVisibility(View.GONE);
//                    rotateLoading.stop();

                    showCustomDailog();


                }

            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<Token>> response) {

//                rotateLoading.setVisibility(View.GONE);
//                rotateLoading.stop();
                // Toast.makeText(DriverRegistrationActivity.this, "on error", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onError: Accept Post error" + response.message() + response.body());
            }

            @Override
            public void onNullListResponse() {
                //Toast.makeText(DriverRegistrationActivity.this, "on null", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onNullResponse:Post Ride error");
//                rotateLoading.setVisibility(View.GONE);
//                rotateLoading.stop();
            }

            @Override
            public void onFailureList(Throwable t) {
                //  Toast.makeText(DriverRegistrationActivity.this, "on failure", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure:Post Ride error");
                rotateLoading.setVisibility(View.GONE);
                rotateLoading.stop();
            }
        }).editRide(auth, pd);
    }
}
