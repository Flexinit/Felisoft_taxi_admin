package com.example.drupp_driver.ui.bookride;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drupp_driver.Models.Trip;
import com.example.drupp_driver.R;
import com.example.drupp_driver.ui.TripHistoryMid;
import com.example.drupp_driver.ui.base.MainBaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class DriverPostedRide extends MainBaseFragment {

    private static final String TAG = DriverPostedRide.class.getSimpleName();
    private List<Object> tripList;
    private RecyclerView tripRv;
    private ListView tripLv;
    private SectionedRecyclerViewAdapter sectionAdapter;
    List<Trip> allTrips = new ArrayList<>();
    HashMap<String, List<Trip>> whole_list = new HashMap<>();

    @BindView(R.id.rv_driver_posted_rides)
    RecyclerView driverPostedRides;

    @Override
    protected void initViewsForFragment(View view) {
        driverPostedRides.setLayoutManager(new LinearLayoutManager(getmContext()));
        // scheduledRidesAdaper = new ScheduledRidesAdaper(getmContext(), R.layout.layout_trip_history_item,)
        sectionAdapter = new SectionedRecyclerViewAdapter();
        getTripData();
    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.driver_posted_ride_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public void getTripData() {
//        DruppRequestHandler.clearInstance();
//
//        DruppRequestHandler.getInstance(new INetworkList<Trip>() {
//            @Override
//            public void onResponseList(Response<QualStandardResponseList<Trip>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    List<Trip> tripList = response.body().getResponse();
//                    allTrips.addAll(tripList);
//                    for (Trip sh : tripList) {
//                        Log.d(TAG, "onResponse: destination : " + sh.getDestination());
//                        Log.d(TAG, "onResponse: source : " + sh.getSource());
//                        Log.d(TAG, "onResponse: ID : " + sh.getId());
//                        Log.d(TAG, "onResponse: TimeStamp : " + sh.getRideDate());
//                        Log.d(TAG, "\n \n");
//                    }
//                    sectionAdapter.notifyDataSetChanged();
//                    displayData();
//                }
//            }
//
//            @Override
//            public void onErrorList(Response<QualStandardResponseList<Trip>> response) {
//                // Toast.makeText(DriverRegistrationActivity.this, "on error", Toast.LENGTH_SHORT).show();
//                Log.d(TAG, "onError Finish RIDE : error");
//            }
//
//            @Override
//            public void onNullListResponse() {
//                //Toast.makeText(DriverRegistrationActivity.this, "on null", Toast.LENGTH_SHORT).show();
//                Log.d(TAG, "onNullResponse Finish RIDE : error");
//            }
//
//            @Override
//            public void onFailureList(Throwable t) {
//                //  Toast.makeText(DriverRegistrationActivity.this, "on failure", Toast.LENGTH_SHORT).show();
//                Log.d(TAG, "onFailure Finish RIDE : error");
//            }
//        }, SessionManager.getAccessToken()).getScheduledRidesDriver();
    }


    private void displayData() {
        try {
            if (allTrips.size() != 0) {
                for (Trip t : allTrips) {
                    String converted_date = handleDateString(t.getRideDate());
                    if (whole_list.containsKey(converted_date)) {
                        List<Trip> new_trip_list = whole_list.get(converted_date);
                        new_trip_list.add(t);
                        whole_list.put(converted_date, new_trip_list);
                    } else {
                        List<Trip> new_trip_list = new ArrayList<>();
                        new_trip_list.add(t);

                        whole_list.put(converted_date, new_trip_list);

                    }
                }

                for (Map.Entry<String, List<Trip>> entry : whole_list.entrySet()) {
                    sectionAdapter.addSection(new RideSection(entry.getKey(), entry.getValue()));
                }

            }


        } catch (Exception e) {
            Log.d(TAG, "onCreate: " + e.getMessage());
        }


        //sectionAdapter.addSection(new RideSection("Today",list1));


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

        String xdate = (day + ":" + month + ":" + year);
        return xdate;
    }

    private class RideSection extends StatelessSection {

        String title;
        List<Trip> list;

        RideSection(String title, List<Trip> list) {
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
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitle;

        HeaderViewHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.tvHeaderTripHis);
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final View rootView;
        public final TextView tvSourceCity, tvDestinationCity;

        ItemViewHolder(View view) {
            super(view);

            rootView = view;
            tvSourceCity = view.findViewById(R.id.tvSourceCity);
            tvDestinationCity = view.findViewById(R.id.tv_rider_destination);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getmContext(), TripHistoryMid.class);
            startActivity(intent);
        }
    }


}
