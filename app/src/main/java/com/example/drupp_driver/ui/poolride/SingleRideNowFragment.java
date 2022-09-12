package com.example.drupp_driver.ui.poolride;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.drupp_driver.R;
import com.example.drupp_driver.ui.base.MainBaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SingleRideNowFragment extends MainBaseFragment {

    @BindView(R.id.tv_source_city)
    TextView mSource;
    @BindView(R.id.tv_destination_city)
    TextView mDestination;
    @BindView(R.id.tv_rider_name)
    TextView mRiderName;
    @BindView(R.id.tv_rider_rating)
    TextView mRiderRating;

    @Override
    protected void initViewsForFragment(View view) {

    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.single_ride_now_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
