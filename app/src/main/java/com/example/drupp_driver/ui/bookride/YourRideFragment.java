package com.example.drupp_driver.ui.bookride;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drupp_driver.Models.RideModel;
import com.example.drupp_driver.Models.ScheduleRideDriverResponse;
import com.example.drupp_driver.Models.SingleRideModel;
import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.MyCustomPoolDialogFragment;
import com.example.drupp_driver.Utils.MyCustomPostEditDailogFragment;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetwork;
import com.example.drupp_driver.connectivity.http.INetworkList;
import com.example.drupp_driver.connectivity.http.QualStandardResponse;
import com.example.drupp_driver.connectivity.http.QualStandardResponseList;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.ui.PostRideActivity;
import com.example.drupp_driver.ui.TripHistoryMid;
import com.example.drupp_driver.ui.base.MainBaseFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;
import retrofit2.Response;

public class YourRideFragment extends MainBaseFragment {

    // shows list of rides scheduled by driver
    private static final String TAG = "TripHistory";

    // uses SectionedRecyclerViewAdapter library to display data
    private SectionedRecyclerViewAdapter sectionAdapter;

    //List<DriverRideModel> allTrips = new ArrayList<>();


    // hashmap contains all the data
    // the key(String) is a ride date:- for each ride date there is list of trips in that given day
    //HashMap<String, List<DriverRideModel>> whole_list = new HashMap<>();
    ArrayList<ScheduleRideDriverResponse> scheduleList=new ArrayList<>();

    String auth = "Bearer " + SessionManager.getInstance().getUserModel().getToken();
    private DriverScheduledRidesAdapter driverScheduledRidesAdapter;
    private List<String> timeStamps=new ArrayList<>();
    private List<String> vehicleTypeList=new ArrayList<>();
    private RecyclerView recyclerView;
    private ImageView noScheduledRides;
    private TextView tvNoScheduledRides;
    private Button btnScheduleRides;
    private FloatingActionButton btnScheduleNewRide;
    //private String driverType=getArguments().getString(ScheduledRidesActivity.DRIVER_TYPE);

    @Override
    protected void initViewsForFragment(View view) {

    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_schedule_rides_your_new, container, false);


        //sectionAdapter = new SectionedRecyclerViewAdapter();
        noScheduledRides = view.findViewById(R.id.iv_no_scheduled_rides);
        tvNoScheduledRides = view.findViewById(R.id.tv_no_scheduled_rides);
        btnScheduleRides = view.findViewById(R.id.btn_schedule_a_ride);
        driverScheduledRidesAdapter=new DriverScheduledRidesAdapter(getActivity(),scheduleList);
        recyclerView = view.findViewById(R.id.rv_your_ride);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        btnScheduleNewRide = view.findViewById(R.id.btnScheduleRide);
        //recyclerView.setAdapter(driverScheduledRidesAdapter);

        btnScheduleRides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PostRideActivity.class));
            }
        });

        btnScheduleNewRide.hide();

        btnScheduleNewRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PostRideActivity.class));
            }
        });

        getTripData();
        return view;
    }

    public void getTripData() {
        showDialogProgressBar();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<ScheduleRideDriverResponse>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<ScheduleRideDriverResponse>> response) {
                Log.d("DriverScheduledRides","request made");
                //allTrips.clear();
                scheduleList.clear();
                //sectionAdapter.removeAllSections();

                if (response.isSuccessful() && response.body() != null) {
                    for(ScheduleRideDriverResponse scheduleRideDriverResponse : response.body().getResponse()){
                        Log.d("DriverScheduledRides","successful");
                        //String currentDateKey = handleDateString(scheduleRideDriverResponse.getDayTimestamp());
                        String timeStamp=handleDateString(scheduleRideDriverResponse.getDayTimestamp());
                        //whole_list.put(timeStamp,scheduleRideDriverResponse.getScheduledRides());
                        scheduleList.add(scheduleRideDriverResponse);

                        //getAllTimeStamps(scheduleRideDriverResponse.getScheduledRides());

                    }

//                    allTrips.addAll(tripList);

                    //sectionAdapter.notifyDataSetChanged();
                    hideDialogProgressBar();
                    //driverScheduledRidesAdapter.setItems(scheduleList,timeStamps,vehicleTypeList);
                    //driverScheduledRidesAdapter.notifyDataSetChanged();
                    if(scheduleList.size()>0){
                        driverScheduledRidesAdapter=new DriverScheduledRidesAdapter(getActivity(),scheduleList);
                        recyclerView.setAdapter(driverScheduledRidesAdapter);
                        btnScheduleNewRide.show();
                        noScheduledRides.setVisibility(View.GONE);
                        tvNoScheduledRides.setVisibility(View.GONE);
                        btnScheduleRides.setVisibility(View.GONE);
                    }
                    else {
                        noScheduledRides.setVisibility(View.VISIBLE);
                        tvNoScheduledRides.setVisibility(View.VISIBLE);
                        btnScheduleRides.setVisibility(View.VISIBLE);
                        btnScheduleNewRide.hide();
                    }


                }
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<ScheduleRideDriverResponse>> response) {
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
                showErrorPrompt(R.layout.dialog_network_error);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
                        hideAlertDialog();
                        getTripData();
                    });
                }
            }
        },SessionManager.getAccessToken()).getScheduledRidesDriver();

    }

    private void getAllTimeStamps(List<RideModel> scheduledRides) {

        for(RideModel rideModel :scheduledRides){
            getSingleRide(rideModel.getRideId());
        }
    }


    //Sort List Based on date
    /*private void displaySortedData() {
//        for (DriverRideModel trip : allTrips) {
//            String currentDateKey = handleDateString(trip.getRideDate());
//            if (whole_list.containsKey(currentDateKey)) {
//                List<DriverRideModel> currentDateList = whole_list.get(currentDateKey);
//                currentDateList.add(trip);
//            } else {
//                List<DriverRideModel> newDateList = new ArrayList<>();
//                newDateList.add(trip);
//                whole_list.put(currentDateKey, newDateList);
//            }
//        }

        *//*for (Map.Entry<String, List<DriverRideModel>> entry : whole_list.entrySet()) {
            sectionAdapter.addSection(new YourRideFragment.RideSection(entry.getKey(), entry.getValue()));
        }*//*
        driverScheduledRidesAdapter.setItems(whole_list);
        sectionAdapter.notifyDataSetChanged();

    }*/

    private void getSingleRide(int rideId) {
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<SingleRideModel>() {
            @Override
            public void onResponse(Response<QualStandardResponse<SingleRideModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("SingleRide","request successful");
                    SingleRideModel singleRideModel = response.body().getResponse();
                    try {
                        timeStamps.add(singleRideModel.getRideDate());
                        switch (singleRideModel.getVehicleType()) {
                            case AppConstants.WITHOUT_AC:
                                vehicleTypeList.add(getString(R.string.without_ac));
                                break;
                            case AppConstants.WITH_AC:
                                vehicleTypeList.add(getString(R.string.with_ac));
                                break;
                        }

                    } catch (Exception e) {


                    }
                }
            }

            @Override
            public void onError(Response<QualStandardResponse<SingleRideModel>> response) {
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
                showErrorPrompt(getString(R.string.something_went_wrong_try_again));
            }
        }, SessionManager.getAccessToken()).getSingleScheduledDriverRide(rideId);

    }

    // converts timestamp to date
    private String handleDateString(String timeStamp) {
        if(timeStamp !=null){
            java.util.Date given_timestamp = new java.util.Date((long) (Long.valueOf(timeStamp)) * 1000);

            String my_timestamp = String.valueOf(given_timestamp);

            String[] arr = my_timestamp.split(" ");
            String week_day = arr[0];
            String month = arr[1];
            String day = arr[2];
            String time = arr[3];
            String utc = arr[4];
            String year = arr[5];

            String xdate = (day + "/" + month + "/" + year);
            return xdate;
        }else {
            return "";
        }

    }


    private class RideSection extends StatelessSection {

        String title;
        List<RideModel> list;

        RideSection(String title, List<RideModel> list) {
            super(SectionParameters.builder()
                    .itemResourceId(R.layout.layout_scheduled_rides_item)
                    .headerResourceId(R.layout.layout_trip_history_header)
                    .build());

            this.title = title;
            this.list = list;
        }

        @Override
        public int getContentItemsTotal() {
            return list.size();
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {

            final ItemViewHolder itemHolder = (ItemViewHolder) holder;

            String scity = list.get(position).getSource();
            String dcity = list.get(position).getDestination();

            itemHolder.tvSourceCity.setText(scity);
            itemHolder.tvDestinationCity.setText(dcity);
        }

        @Override
        public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
            return new HeaderViewHolder(view);
        }


        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

            headerHolder.tvTitle.setText(title);
        }

        private class HeaderViewHolder extends RecyclerView.ViewHolder {

            private final TextView tvTitle;

            HeaderViewHolder(View view) {
                super(view);

                tvTitle = view.findViewById(R.id.tvHeaderTripHis);
            }
        }

        private class ItemViewHolder extends RecyclerView.ViewHolder {

            public final View rootView;
            public final TextView tvSourceCity, tvDestinationCity;

            ItemViewHolder(View view) {
                super(view);

                rootView = view;
                tvSourceCity = view.findViewById(R.id.tvSourceCity);
                tvDestinationCity = view.findViewById(R.id.tv_rider_destination);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), TripHistoryMid.class);
                        intent.putExtra(AppConstants.K_S_CITY, tvSourceCity.getText().toString());
                        intent.putExtra(AppConstants.K_D_CITY, tvDestinationCity.getText().toString());
                        intent.putExtra(AppConstants.K_DATE, list.get(sectionAdapter.getPositionInSection(getAdapterPosition())).getRideDate());
                        intent.putExtra(AppConstants.K_RIDE_ID, list.get(sectionAdapter.getPositionInSection(getAdapterPosition())).getRideId());
                        intent.putExtra(AppConstants.K_RIDE_TYPE, list.get(sectionAdapter.getPositionInSection(getAdapterPosition())).getRideType());
                        intent.putExtra(AppConstants.K_PASSENGERS_PREFERENCE, list.get(sectionAdapter.getPositionInSection(getAdapterPosition())).getPassengersPreference());
                        intent.putExtra(AppConstants.K_BASE_FARE, list.get(sectionAdapter.getPositionInSection(getAdapterPosition())).getDriverFare());
                        startActivity(intent);

                    }
                });
            }

        }
    }

    public void method() {

    }

    // dialogs for responding to broadcast receivers in Test Activity
    public void showDialogPostEditRide(String type, String co_rider_pref, String pick_up, String pref, String driver_type) {
        MyCustomPostEditDailogFragment dailogFragment3 = new MyCustomPostEditDailogFragment();
        Bundle args = new Bundle();

        args.putString(AppConstants.K_TYPE, type);
        args.putString(AppConstants.K_CO_RIDERS_PREFERENCE, co_rider_pref);
        args.putString(AppConstants.K_PICK_UP_LOCATION, pick_up);
        args.putString(AppConstants.K_PREFERENCE, pref);
        args.putString(AppConstants.K_PICK_UP_LOCATION, pick_up);
        args.putString(AppConstants.K_TYPE_OF_DRIVER, driver_type);

        dailogFragment3.setArguments(args);
        dailogFragment3.setCancelable(false);
        dailogFragment3.show(getActivity().getSupportFragmentManager(), "post edit");

    }

    public void showDailogFragmentPostRide(String type, String co_rider_pref, String pick_up, String pref, String driver_type, String request_id) {
        MyCustomPoolDialogFragment dialogFragment2 = new MyCustomPoolDialogFragment();
        Bundle args = new Bundle();

        args.putString("type", type);
        args.putString("co-riders_preference", co_rider_pref);
        args.putString("pick_up_location", pick_up);
        args.putString("preference", pref);
        args.putString("pick_up_location", pick_up);
        args.putString("type_of_driver", driver_type);
        args.putString("request_ride_id", request_id);

        dialogFragment2.setArguments(args);
        dialogFragment2.setCancelable(false);
        dialogFragment2.show(getActivity().getSupportFragmentManager(), "fcvyu");
    }
}
