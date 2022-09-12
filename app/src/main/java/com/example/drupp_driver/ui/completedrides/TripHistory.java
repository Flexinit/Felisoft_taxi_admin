package com.example.drupp_driver.ui.completedrides;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.drupp_driver.Models.Trip;
import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.RxBus;
import com.example.drupp_driver.adapters.TripHistoryPagerAdapter;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetworkList;
import com.example.drupp_driver.connectivity.http.QualStandardResponseList;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.ui.TripHistoryFinal;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;
import retrofit2.Response;

public class TripHistory extends AppCompatActivity {

    // For showing Ride History


    @BindView(R.id.pager_trip_history)
    ViewPager pagerTripHistory;
    @BindView(R.id.trip_tab_layout)
    TabLayout tabLayout;
    private TripHistoryPagerAdapter tripHistoryPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_history);
        ButterKnife.bind(this);

        findViewById(R.id.image_back).setOnClickListener(v -> onBackPressed());

        //Initialising Pager adapter
        tripHistoryPagerAdapter = new TripHistoryPagerAdapter(this, getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        tabLayout.setupWithViewPager(pagerTripHistory);
        pagerTripHistory.setAdapter(tripHistoryPagerAdapter);
        pagerTripHistory.setCurrentItem(0);


        if (pagerTripHistory.getCurrentItem() == 0) {
            tripHistoryPagerAdapter.completedRideFragment.getCompletedRides();
        }
        pagerTripHistory.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    tripHistoryPagerAdapter.completedRideFragment.getCompletedRides();
                } else if (position == 1) {
                    tripHistoryPagerAdapter.canceledRideFragment.getCanceledRides();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }


}
