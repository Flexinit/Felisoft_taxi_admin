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
import com.example.drupp_driver.Models.ScheduledRideUserReponse;
import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.MyCustomPoolDialogFragment;
import com.example.drupp_driver.Utils.MyCustomPostEditDailogFragment;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetworkList;
import com.example.drupp_driver.connectivity.http.QualStandardResponseList;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.ui.PostRideActivity;
import com.example.drupp_driver.ui.base.MainBaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;
import retrofit2.Response;

public class UserRideFragment extends MainBaseFragment {

    // shows list of rides scheduled by rider

    // functionalites are same as Your Ride Fragment
    // for descriptions look Your Ride Fragment

    private static final String TAG = "TripHistory";

    private SectionedRecyclerViewAdapter sectionAdapter;



    ArrayList<ScheduledRideUserReponse> scheduleList=new ArrayList<>();


    HashMap<String, List<RideModel>> whole_list = new HashMap<>();

    String auth = "Bearer " + SessionManager.getInstance().getUserModel().getToken();

    private ImageView ivNoUserScheduledRides;
    private TextView tvNoUserScheduledRides;
    private UserScheduledRidesAdapter userScheduledRidesAdapter;
    private RecyclerView recyclerView;
    private Button btnScheduleRides;


    @Override
    protected void initViewsForFragment(View view) {

    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_schedule_rides_your_new, container, false);


        sectionAdapter = new SectionedRecyclerViewAdapter();

//        getTripData();

        ivNoUserScheduledRides = view.findViewById(R.id.iv_no_scheduled_rides);
        tvNoUserScheduledRides = view.findViewById(R.id.tv_no_scheduled_rides);
        btnScheduleRides = view.findViewById(R.id.btn_schedule_a_ride);
        userScheduledRidesAdapter =new UserScheduledRidesAdapter(getActivity(),scheduleList);
        recyclerView = view.findViewById(R.id.rv_your_ride);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        btnScheduleRides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PostRideActivity.class));
            }
        });
        getTripData();

        return view;
    }


    // showcasing preferences of rider for ride posted by driver


    public void getTripData() {
        showDialogProgressBar();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<ScheduledRideUserReponse>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<ScheduledRideUserReponse>> response) {
                Log.d("DriverScheduledRides","request made");
                //allTrips.clear();
                scheduleList.clear();
                //sectionAdapter.removeAllSections();

                if (response.isSuccessful() && response.body() != null) {
                    for(ScheduledRideUserReponse scheduledRideUserReponse : response.body().getResponse()){
                        Log.d("DriverScheduledRides","successful");
                        String timeStamp=handleDateString(scheduledRideUserReponse.getDayTimestamp());
                        scheduleList.add(scheduledRideUserReponse);

                        //getAllTimeStamps(scheduledRideUserReponse.getScheduledRides());

                    }

//                    allTrips.addAll(tripList);

                    //sectionAdapter.notifyDataSetChanged();
                    hideDialogProgressBar();
                    //driverScheduledRidesAdapter.setItems(scheduleList,timeStamps,vehicleTypeList);
                    //driverScheduledRidesAdapter.notifyDataSetChanged();
                    if(scheduleList.size()>0){
                        userScheduledRidesAdapter =new UserScheduledRidesAdapter(getActivity(),scheduleList);
                        recyclerView.setAdapter(userScheduledRidesAdapter);
                        ivNoUserScheduledRides.setVisibility(View.GONE);
                        tvNoUserScheduledRides.setVisibility(View.GONE);
                        btnScheduleRides.setVisibility(View.GONE);
                    }
                    else {
                        ivNoUserScheduledRides.setVisibility(View.VISIBLE);
                        tvNoUserScheduledRides.setVisibility(View.VISIBLE);
                        btnScheduleRides.setVisibility(View.VISIBLE);
                    }


                }
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<ScheduledRideUserReponse>> response) {
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
                        getTripData();
                    });
                }
            }
        }, SessionManager.getAccessToken()).getScheduledRidesUser();
    }


    //Sort List Based on date
    private void displaySortedData() {
//        for (Trip trip : allTrips) {
//            String currentDateKey = handleDateString(trip.getRideDate());
//            if (whole_list.containsKey(currentDateKey)) {
//                List<Trip> currentDateList = whole_list.get(currentDateKey);
//                currentDateList.add(trip);
//            } else {
//                List<Trip> newDateList = new ArrayList<>();
//                newDateList.add(trip);
//                whole_list.put(currentDateKey, newDateList);
//            }
//        }

        for (Map.Entry<String, List<RideModel>> entry : whole_list.entrySet()) {
            sectionAdapter.addSection(new UserRideFragment.RideSection(entry.getKey(), entry.getValue()));
        }

    }

    private String handleDateString(String timeStamp) {
        if (timeStamp != null) {
            java.util.Date given_timestamp = new java.util.Date((long) (Long.valueOf(timeStamp)) * 1000);

            String my_timestamp = String.valueOf(given_timestamp);

            String[] arr = my_timestamp.split(" ");
            String week_day = arr[0];
            String month = arr[1];
            String day = arr[2];
            String time = arr[3];
            String utc = arr[4];
            String year = arr[5];

            return (day + "/" + month + "/" + year);
        } else {
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
                        Intent intent = new Intent(getActivity(), SingleScheduledRide.class);
                        intent.putExtra(AppConstants.K_S_CITY, tvSourceCity.getText().toString());
                        intent.putExtra(AppConstants.K_D_CITY, tvDestinationCity.getText().toString());
                        intent.putExtra(AppConstants.K_DATE, list.get(sectionAdapter.getPositionInSection(getAdapterPosition())).getRideDate());
                        intent.putExtra(AppConstants.K_RIDE_ID, list.get(sectionAdapter.getPositionInSection(getAdapterPosition())).getRideId());

                        startActivity(intent);

                    }
                });
            }

        }
    }

    public void method() {

    }

    public void showDialogPostEditRide(String type, String co_rider_pref, String pick_up, String pref, String driver_type) {
        MyCustomPostEditDailogFragment dailogFragment3 = new MyCustomPostEditDailogFragment();
        Bundle args = new Bundle();

        args.putString("type", type);
        args.putString("co-riders_preference", co_rider_pref);
        args.putString("pick_up_location", pick_up);
        args.putString("preference", pref);
        args.putString("pick_up_location", pick_up);
        args.putString("type_of_driver", driver_type);

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
