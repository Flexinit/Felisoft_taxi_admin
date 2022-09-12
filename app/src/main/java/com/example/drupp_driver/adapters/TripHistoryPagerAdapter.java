package com.example.drupp_driver.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.drupp_driver.R;
import com.example.drupp_driver.ui.completedrides.CanceledRideFragment;
import com.example.drupp_driver.ui.completedrides.CompletedRideFragment;

public class TripHistoryPagerAdapter extends FragmentPagerAdapter {

    public CanceledRideFragment canceledRideFragment;
    public CompletedRideFragment completedRideFragment;
    private Context mContext;

    public TripHistoryPagerAdapter(Context context, @NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        mContext = context;
        canceledRideFragment = new CanceledRideFragment();
        completedRideFragment = new CompletedRideFragment();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return completedRideFragment;
        }
        return canceledRideFragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title;
        if (position == 0) {
            title = mContext.getString(R.string.completed_rides);
        } else {
            title = mContext.getString(R.string.canceled_rides);
        }
        return title;

    }
}
