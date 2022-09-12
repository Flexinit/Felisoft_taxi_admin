package com.example.drupp_driver.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.drupp_driver.R;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.ui.bookride.UserRideFragment;
import com.example.drupp_driver.ui.bookride.YourRideFragment;
import com.google.android.material.tabs.TabLayout;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScheduledRidesActivity extends AppCompatActivity {
    public static final String DRIVER_TYPE ="driver Type" ;
    public static final String VEHICLE_TYPEID ="vehicleTypeId" ;
    private String bundleDriverType;
    private Bundle bundle=new Bundle();

    // IMPORTANT-----
    //>>>All work of this activity is now done in activity named TEST ACTIVITY which contains two fragments YOUR RIDE & USERS RIDE
    // This activity is for NAMESAKE and is nowhere displayed in app

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduled_rides);
        ButterKnife.bind(this);

        setVehicleType();
        ViewPager viewPager = findViewById(R.id.pager_rides);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        RidesFragmentPagerAdapter fragmentAdapter = new RidesFragmentPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);


        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setCurrentItem(0);

        try {
            tabLayout.getTabAt(0).setText(getString(R.string.your_rides));
            tabLayout.getTabAt(1).setText(getString(R.string.rider_posted));
        } catch (Exception e) {

        }

        /*viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0) {
                    fragmentAdapter.driverPostedRides.getTripData();
                } else if (position == 1) {
                    fragmentAdapter.userPostedRides.getTripData();
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
*/

    }

    private void setVehicleType() {
        if(getIntent().hasExtra(DRIVER_TYPE)&&getIntent().hasExtra(VEHICLE_TYPEID)){
            int driverType=getIntent().getIntExtra(DRIVER_TYPE,0);
            int vehicleTypeId=getIntent().getIntExtra(VEHICLE_TYPEID,0);
            switch (driverType) {
                case AppConstants.CAR_DRIVER:
                    if (vehicleTypeId == AppConstants.WITHOUT_AC) {
                        bundleDriverType=(getString(R.string.driver_type_regular));
                    } else if (vehicleTypeId == AppConstants.WITH_AC) {
                        bundleDriverType=getString(R.string.driver_type_luxury);
                    }
                    break;
                case AppConstants.BUS_DRIVER:
                    bundleDriverType=getString(R.string.bus_driver);
                    break;
                case AppConstants.KEKE_DRIVER:
                    bundleDriverType=getString(R.string.keke_driver);
                    break;
            }
            bundle.putString(DRIVER_TYPE,bundleDriverType);

        }


    }

    @OnClick(R.id.image_back)
    public void onBack() {
        onBackPressed();
    }

    private class RidesFragmentPagerAdapter extends FragmentPagerAdapter {
        // UserRideFragment userPostedRideLater;
        // DriverPostedRide driverPostedRides;
        YourRideFragment driverPostedRides;
        UserRideFragment userPostedRides;

        public RidesFragmentPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
//            userPostedRideLater = new UserPostedRide();
            //          driverPostedRides = new DriverPostedRide();
            userPostedRides = new UserRideFragment();
            driverPostedRides = new YourRideFragment();
        }

        @Override
        public Fragment getItem(int i) {
            driverPostedRides.setArguments(bundle);
            if (i == 1) {

                return userPostedRides;
            }
            return driverPostedRides;

        }
        

        @Override
        public int getCount() {
            return 2;
        }
    }

}
