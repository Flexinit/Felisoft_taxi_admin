package com.example.drupp_driver.ui.completedrides;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drupp_driver.Models.Trip;
import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.AppUtil;
import com.example.drupp_driver.Utils.IAdapterItemClickListener;
import com.example.drupp_driver.Utils.RxBus;
import com.example.drupp_driver.adapters.TripRideSection;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetworkList;
import com.example.drupp_driver.connectivity.http.QualStandardResponseList;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.ui.TripHistoryFinal;
import com.example.drupp_driver.ui.base.MainBaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import retrofit2.Response;

public class CompletedRideFragment extends MainBaseFragment implements IAdapterItemClickListener {
    private List<Object> tripList;
    private ListView tripLv;
    private SectionedRecyclerViewAdapter sectionAdapter;
    List<Trip> allTrips = new ArrayList<>();
    HashMap<String, List<Trip>> whole_list = new HashMap<>();


    @BindView(R.id.rv_completed_rides)
    RecyclerView completedRidesRecyclerView;

    @Override
    protected void initViewsForFragment(View view) {
        sectionAdapter = new SectionedRecyclerViewAdapter();
        completedRidesRecyclerView.setLayoutManager(new LinearLayoutManager(getmContext()));
        completedRidesRecyclerView.setAdapter(sectionAdapter);
    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_completed_ride, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public void getCompletedRides() {

        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<Trip>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<Trip>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    sectionAdapter.removeAllSections();
                    allTrips.clear();
                    whole_list.clear();
                    List<Trip> tripList = response.body().getResponse();
                    allTrips.addAll(tripList);

                    displayData();

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
        }, SessionManager.getAccessToken()).getCompletedRides();
    }

    private void displayData() {
        try {

            if (allTrips.size() != 0) {
                for (Trip t : allTrips) {
                    String converted_date = AppUtil.handleDateString(t.getRideDate());
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
                    TripRideSection tripRideSection = new TripRideSection(getmActivity(), entry.getKey(), entry.getValue());
                    tripRideSection.setiAdapterItemClickListener(this);
                    sectionAdapter.addSection(tripRideSection);
                }

            }

        } catch (Exception e) {
            Log.d(getClass().getSimpleName(), e.getMessage());
        }
        sectionAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View v, int position) {
        Intent intent = new Intent(getmActivity(), TripHistoryFinal.class);

        TripRideSection currentSection = (TripRideSection) sectionAdapter.getSectionForPosition(position);
        RxBus.getInstance().setIntentEvent(currentSection.getList().get(sectionAdapter.getPositionInSection(position)));
        startActivity(intent);
    }
}
