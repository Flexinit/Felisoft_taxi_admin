package com.example.drupp_driver.ui.poolride;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.drupp_driver.R;
import com.example.drupp_driver.ui.base.MainBaseFragment;

import butterknife.ButterKnife;

public class RideActionFragment extends MainBaseFragment {

    @Override
    protected void initViewsForFragment(View view) {

    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_ride_action, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
