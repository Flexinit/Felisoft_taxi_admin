package com.example.drupp_driver.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.SectionPageAdapter;
import com.example.drupp_driver.ui.bookride.UserRideFragment;
import com.example.drupp_driver.ui.bookride.YourRideFragment;
import com.google.android.material.tabs.TabLayout;

public class TestActivity extends AppCompatActivity {

    // This activity handles work which was previously done by Schedule Ride Activity
    // It contains two fragment screens : YourRide(For driver booked rides) and UserRide(For rider booked rides)
    private ViewPager mViewPager;
    SectionPageAdapter adapter;
    private YourRideFragment yourRideFragment;
    private UserRideFragment userRideFragment;
    private static final String TAG = "TestActivity";
    // recieve notifications
    private BroadcastReceiver listener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra("type");
            try {
                switch (type) {
                    case "1": {
                    /*Log.d(TAG, "onReceive: user  request ride");
                    // show dialog for ride posted by user
                    if (intent.hasExtra("ride_option") && intent.getStringExtra("ride_option").equals("2")) {
                        yourRideFragment.method();
                        userRideFragment.method();
                        showDailogFragmentRideLater(
                                intent.getStringExtra("id"),
                                intent.getStringExtra("scity"),
                                intent.getStringExtra("dcity"),
                                intent.getStringExtra("ride_date"),
                                intent.getStringExtra("name"));} else
                        showDailogFragment(intent.getStringExtra("id"),
                                intent.getStringExtra("name"),
                                intent.getStringExtra("scity"),
                                intent.getStringExtra("dcity"),
                                intent.getStringExtra("ride_type"),
                                intent.getStringExtra("source_latitude"),
                                intent.getStringExtra("source_longitude"),
                                intent.getStringExtra("destination_latitude"),
                                intent.getStringExtra("destination_longitude"),
                                intent.getStringExtra("phone"), intent.getStringExtra("user-id"));*/
                    }
                    break;

                    case "8": {
                        Log.d(TAG, "onReceive: driver request ride");
                        // show dialog for post ride(by driver)
                        userRideFragment.showDailogFragmentPostRide(intent.getStringExtra("type"),
                                intent.getStringExtra("co-riders_preference"),
                                intent.getStringExtra("pick_up_location"),
                                intent.getStringExtra("preference"),
                                intent.getStringExtra("type_of_driver"),
                                intent.getStringExtra("request_ride_id"));

                        yourRideFragment.showDailogFragmentPostRide(intent.getStringExtra("type"),
                                intent.getStringExtra("co-riders_preference"),
                                intent.getStringExtra("pick_up_location"),
                                intent.getStringExtra("preference"),
                                intent.getStringExtra("type_of_driver"),
                                intent.getStringExtra("request_ride_id"));
                    }
                    break;

                    case "12": {
                        Log.d(TAG, "onReceive: driver request ride edit");
                        // show dialog for post ride(by driver)

                        userRideFragment.showDialogPostEditRide(intent.getStringExtra("type"),
                                intent.getStringExtra("co-riders_preference"),
                                intent.getStringExtra("pick_up_location"),
                                intent.getStringExtra("preference"),
                                intent.getStringExtra("type_of_driver")
                        );

                        yourRideFragment.showDialogPostEditRide(intent.getStringExtra("type"),
                                intent.getStringExtra("co-riders_preference"),
                                intent.getStringExtra("pick_up_location"),
                                intent.getStringExtra("preference"),
                                intent.getStringExtra("type_of_driver")
                        );


                    }
                    break;

                    case "13": {

                    }
                    case "15": {
                        //showCancelledDailog();
                    }
                    break;
                }
            } catch (Exception e) {
                Log.e("Exception, ", e.getMessage());
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(listener, new IntentFilter("update_notification"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mViewPager = (ViewPager) findViewById(R.id.viewpager_container);

        setUpViewPager();


        findViewById(R.id.image_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(listener);
        } catch (IllegalArgumentException e) {

            Log.d(TAG, "onDestroy: unable to uregister");
        }

    }

    private void setUpViewPager() {
        yourRideFragment = new YourRideFragment();
        userRideFragment = new UserRideFragment();
        adapter = new SectionPageAdapter(getSupportFragmentManager());
        adapter.addFragment(yourRideFragment);
        adapter.addFragment(userRideFragment);


        mViewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setText(getString(R.string.your_rides));
        tabLayout.getTabAt(1).setText(getString(R.string.user_posted));
    }
}
