package com.example.drupp_driver.ui.completedrides;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drupp_driver.Models.RideInfo;
import com.example.drupp_driver.Models.RiderInfo;
import com.example.drupp_driver.Models.Token;
import com.example.drupp_driver.Models.Trip;
import com.example.drupp_driver.Models.UserModel;
import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.CommonUtils;
import com.example.drupp_driver.Utils.IDateDialogObserver;
import com.example.drupp_driver.Utils.Timing;
import com.example.drupp_driver.connectivity.BaseModelHandler;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetworkList;
import com.example.drupp_driver.connectivity.http.QualStandardResponse;
import com.example.drupp_driver.connectivity.http.QualStandardResponseList;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.ui.RideActivity;
import com.example.drupp_driver.ui.dialogs.DateDialog;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.MarkerManager;
import com.txusballesteros.bubbles.BubbleLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Response;

public class TripHistoryNew extends AppCompatActivity implements IDateDialogObserver {

    List<RiderInfo> allTrips = new ArrayList<>();
    List<RiderInfo> succeededRides = new ArrayList<>();
    List<RiderInfo> cancelledRides = new ArrayList<>();
    List<RiderInfo> filteredList= new ArrayList<>();


    private RecyclerView tripsList;
    protected AlertDialog mAlertDialog;
    private TextView tvNoRideHistory;
    private ImageView ivNoRideHistory;
    private ConstraintLayout containerTop;
    private TextView textDateFrom,textDateTo;
    private DateDialog dateDialog;
    private long timeFromInMilli;
    private long timeToInMilli;
    private TripHistoryAdapter tripHistoryAdapter;
    private boolean settingDateFrom=false;
    private String dateToSet;
    public static final long CONVERT_TO_DAYS_MILLI = 24 * 3600 * 1000L;
    public static final long CONVERT_TO_MONTHS_MILLI = 30*24 * 3600 * 1000L;
    public static final long CONVERT_TO_HOURS_MILLI = 3600*1000L;
    public static final long CONVERT_TO_SECONDS_MILLI = 1000L;

    private SwitchCompat mDriverStatus;
    private DatabaseReference mDatabase;
    private ProgressDialog mProgressDialog;
    private ImageView calendarFrom,calendarTo;
    private UserModel userModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_trip_history_new);

        tripsList = findViewById(R.id.rv_trip_list);
        ivNoRideHistory=findViewById(R.id.iv_no_ride_history);
        tvNoRideHistory=findViewById(R.id.tv_ride_history);
        containerTop=findViewById(R.id.container_top);
        textDateFrom=findViewById(R.id.tv_date_from);
        textDateTo=findViewById(R.id.tv_date_to);
        calendarFrom=findViewById(R.id.iv_calendar_from);
        calendarTo=findViewById(R.id.iv_calendar_to);
        containerTop=findViewById(R.id.container_top);
        tripsList.setLayoutManager(new LinearLayoutManager(this));


        mDriverStatus =findViewById(R.id.driver_status);
        userModel = SessionManager.getInstance().getUserModel();
        setOnlineStatus();
        setDates();



        findViewById(R.id.image_back).setOnClickListener(v -> onBackPressed());
        textDateFrom.setOnClickListener(v -> showDateDialog(true));
        calendarFrom.setOnClickListener(v -> showDateDialog(true));
        textDateTo.setOnClickListener(v -> showDateDialog(false));
        calendarTo.setOnClickListener(v -> showDateDialog(false));

        tripHistoryAdapter=new TripHistoryAdapter(this, filteredList);

        //createDummyData();
        getCompletedRides();



    }

    private void setOnlineStatus() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(AppConstants.FIREBASE_DATABASE.USERS);
        userModel = SessionManager.getInstance().getUserModel();
        mDriverStatus.setChecked(userModel.getDriverStatus() == 1);

        //TODO : CHANGE DRIVER STATUS
        mDriverStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mDatabase.child(AppConstants.FIREBASE_DATABASE.DRIVERS)
                    .child(String.valueOf(userModel.getId()))
                    .child(AppConstants.FIREBASE_DATABASE.AVAILABILITY).setValue(isChecked ? 1 : 0);
            changeDriverStatus(isChecked ? 1 : 0);
        });
    }


    private void setDates() {
        timeFromInMilli=(System.currentTimeMillis()-150*CONVERT_TO_DAYS_MILLI)/1000L;
        timeToInMilli=(System.currentTimeMillis()+CONVERT_TO_DAYS_MILLI)/1000L;

        Date date = new Date(System.currentTimeMillis()-150*CONVERT_TO_DAYS_MILLI);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MMMM, yyyy");
        String dateFrom=simpleDateFormat.format(date);
        textDateFrom.setText(dateFrom);
        date=new Date(System.currentTimeMillis());
        String dateTo=simpleDateFormat.format(date);
        textDateTo.setText(dateTo);

    }

    private void createDummyData() {
        showLoading();
        allTrips.clear();
        String[] names={"year","year","day","hour","second"};
        for(int i=0;i<5;i++){
            RiderInfo trip=new RiderInfo();
            trip.setTotalFare("4000");
            trip.setSource(names[i]);
            trip.setDestination("Thika, along Thika Road");
            if(i==0||i==2){
                trip.setSucceededOrCancelled("Cancelled");
            }
            else{
                trip.setSucceededOrCancelled("Succeeded");
            }
            allTrips.add(trip);
        }
        long timeNow=System.currentTimeMillis();
        long[] dates={
                timeNow-6*12*CONVERT_TO_MONTHS_MILLI,
                timeNow-3*12*CONVERT_TO_MONTHS_MILLI,
                timeNow-7*CONVERT_TO_DAYS_MILLI,
                timeNow-3*CONVERT_TO_HOURS_MILLI,
                timeNow-5*CONVERT_TO_SECONDS_MILLI};
        for(int i=0;i<allTrips.size();i++){
            allTrips.get(i).setRideDate(Long.toString(dates[i]));
        }
        ivNoRideHistory.setVisibility(View.GONE);
        tvNoRideHistory.setVisibility(View.GONE);
        Collections.sort(allTrips,new Trip());
        filterList();


    }

    public void getCompletedRides() {
        showLoading();

        FirebaseDatabase.getInstance().getReference()
                .child(AppConstants.FIREBASE_DATABASE.USERS)
                .child(AppConstants.FIREBASE_DATABASE.DRIVERS)
                .child(String.valueOf(userModel.getId()))
                .child(AppConstants.COMPLETED_TRIPS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        allTrips.clear();
                        succeededRides.clear();
                        for(DataSnapshot ds:snapshot.getChildren()){
                            RiderInfo rideInfo=ds.getValue(RiderInfo.class);
                            rideInfo.setSucceededOrCancelled("Successful");
                            succeededRides.add(rideInfo);
                        }
                        getCanceledRides();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
     /*   DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<Trip>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<Trip>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allTrips.clear();
                    succeededRides.clear();

                    List<Trip> tripList = response.body().getResponse();
                    for(Trip trip:tripList){
                        trip.setSucceededOrCancelled("Successful");
                    }


                    succeededRides.addAll(tripList);

                    getCanceledRides();


                }
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<Trip>> response) {
                showErrorPrompt(response);
            }

            @Override
            public void onNullListResponse() {

            }

            @Override
            public void onFailureList(Throwable t) {
                showAlertDialog(R.layout.dialog_network_error);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(view -> {
                        hideAlertDialog();
                        getCompletedRides();
                    });
                }
            }
        }, SessionManager.getAccessToken()).getCompletedRides();*/
    }

    public void getCanceledRides() {
        FirebaseDatabase.getInstance().getReference()
                .child(AppConstants.FIREBASE_DATABASE.USERS)
                .child(AppConstants.FIREBASE_DATABASE.DRIVERS)
                .child(String.valueOf(userModel.getId()))
                .child(AppConstants.CANCELLED_RIDES)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        cancelledRides.clear();
                        for(DataSnapshot ds:snapshot.getChildren()){
                            RiderInfo rideInfo=ds.getValue(RiderInfo.class);
                            rideInfo.setSucceededOrCancelled("Cancelled");
                            cancelledRides.add(rideInfo);
                        }
                        combineLists();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        /*DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<Trip>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<Trip>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cancelledRides.clear();
                    List<Trip> tripList = response.body().getResponse();
                    for(Trip trip:tripList){
                        trip.setSucceededOrCancelled("Cancelled");
                    }
                    cancelledRides.addAll(tripList);
                    combineLists();



                }
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<Trip>> response) {
                showErrorPrompt(response);
            }

            @Override
            public void onNullListResponse() {

            }

            @Override
            public void onFailureList(Throwable t) {
                showAlertDialog(R.layout.dialog_network_error);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(view -> {
                        hideAlertDialog();
                        getCanceledRides();
                    });
                }
            }
        }, SessionManager.getAccessToken()).getCanceledRides();*/
    }
    public boolean showErrorPrompt(Response response) {
        try {
            String error = getErrorFromResponsse(response);
            this.showErrorPrompt(error);
            return false;
        } catch (Exception ex) {
            return false;
        }
    }
    private String getErrorFromResponsse(Response response) {
        QualStandardResponse stdResponse = getStandardResponse(response);
        String error = stdResponse.getErrorResponse();
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        return error;
    }
    public QualStandardResponse getStandardResponse(Response response) {
        return BaseModelHandler.parseError(response);
    }
    public void showAlertDialog(int resId) {
        hideKeyboard();
        mAlertDialog = CommonUtils.showCustomAlertDialog(this, resId);
        mAlertDialog.show();
    }
    public void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public void showErrorPrompt(String error) {
        Toast toast = Toast.makeText(this, error, Toast.LENGTH_LONG);
        if (!toast.getView().isShown()) {
            toast.show();
        }
    }

    private void combineLists() {
        allTrips.addAll(succeededRides);
        allTrips.addAll(cancelledRides);
        if(allTrips.size()>0){
            containerTop.setVisibility(View.VISIBLE);
            ivNoRideHistory.setVisibility(View.GONE);
            tvNoRideHistory.setVisibility(View.GONE);
            Collections.sort(allTrips,new RiderInfo());
            filterList();


        }
        else{
            containerTop.setVisibility(View.GONE);
            tvNoRideHistory.setVisibility(View.VISIBLE);
            ivNoRideHistory.setVisibility(View.VISIBLE);
        }
        hideLoading();


    }
    public void hideAlertDialog() {
        if (mAlertDialog != null && mAlertDialog.isShowing())
            mAlertDialog.dismiss();
    }
    private void showDateDialog(boolean settingDateFrom) {
        this.settingDateFrom=settingDateFrom;
        Bundle bundle = new Bundle();
        bundle.putLong(AppConstants.K_MIN_DATE, System.currentTimeMillis()-72* CONVERT_TO_MONTHS_MILLI);
        bundle.putLong(AppConstants.K_MAX_DATE, System.currentTimeMillis() );
        dateDialog = DateDialog.newInstance(bundle);
        dateDialog.setiDateDialogObserver(this);
        dateDialog.show(getSupportFragmentManager(), DateDialog.class.getSimpleName());
    }


    @Override
    public void onDateSelected(int year, int month, int dayOfMonth) {
        showLoading();
        String sday = String.valueOf(dayOfMonth);
        String smonth = String.valueOf(month);
        if (dayOfMonth < 10) {
            sday = "0" + sday;
        }
        if (month < 10) {
            smonth = "0" + smonth;
        }
        dateToSet = sday + "/" + smonth + "/" + year;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        if(settingDateFrom){
            textDateFrom.setText(dateToSet);
            try {
                Date date = sdf.parse(dateToSet);
                timeFromInMilli=date.getTime()/1000L;
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }
        else{
            textDateTo.setText(dateToSet);
            try {
                Date date = sdf.parse(dateToSet);
                timeToInMilli=(date.getTime()+CONVERT_TO_DAYS_MILLI)/1000L;


            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        filterList();
    }

    private void filterList() {
        filteredList.clear();
        for(int i=0;i<allTrips.size();i++){
           Log.d("Filter",allTrips.get(i).getSource() + "date is " +allTrips.get(i).getRideDate()+"and "+timeToInMilli);
        }
        for(int i=0;i<allTrips.size();i++){
            if(Long.parseLong(allTrips.get(i).getRideDate())>=timeFromInMilli){
                if(Long.parseLong(allTrips.get(i).getRideDate())<=timeToInMilli){

                    filteredList.add(allTrips.get(i));
                }
            }
        }
        hideLoading();
        tripHistoryAdapter=new TripHistoryAdapter(this, filteredList);
        tripsList.setAdapter(tripHistoryAdapter);
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
                        SessionManager.getInstance().saveUser(TripHistoryNew.this, userModel);
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

    public void showLoading() {
        hideKeyboard();
        mProgressDialog = CommonUtils.showLoadingDialog(this);
    }

    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }
    public void showErrorPrompt(int error) {
        showErrorPrompt(getString(error));
    }



}