package com.example.drupp_driver.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.drupp_driver.ui.poolride.DriverProfileFragment;
import com.example.drupp_driver.ui.poolride.RideActionFragment;
import com.example.drupp_driver.ui.poolride.SingleRideNowFragment;

public class PagerHomeAdapter extends FragmentStatePagerAdapter {


    public PagerHomeAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                return new SingleRideNowFragment();
            default:
                return new DriverProfileFragment();
        }

    }

    @Override
    public int getCount() {
        return 4;
    }


}
