package com.example.drupp_driver.Utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SectionPageAdapter extends FragmentPagerAdapter {

    private static final String TAG = "SectionPageAdapter";

    private final List<Fragment> mFragment=new ArrayList<>();

    public SectionPageAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment)
    {
        mFragment.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragment.get(position);
    }

    @Override
    public int getCount() {
        return mFragment.size();
    }
}
