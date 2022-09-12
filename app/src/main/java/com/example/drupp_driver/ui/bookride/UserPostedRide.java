package com.example.drupp_driver.ui.bookride;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drupp_driver.R;
import com.example.drupp_driver.ui.base.MainBaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserPostedRide extends MainBaseFragment {
    @BindView(R.id.rv_user_posted_rides)
    RecyclerView userPostedRecyclerView;


    @Override
    protected void initViewsForFragment(View view) {
        userPostedRecyclerView.setLayoutManager(new LinearLayoutManager(getmContext()));

    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.user_posted_rides_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public void getTripData() {

    }
}
