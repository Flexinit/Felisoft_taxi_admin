package com.example.drupp_driver.ui.bookride;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.drupp_driver.R;
import com.example.drupp_driver.ui.base.MainBaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RideDetails extends MainBaseActivity {

    @BindView(R.id.tv_Trip_His_Time)
    TextView mTripDate;


    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_details);
        ButterKnife.bind(this);
        findViewById(R.id.image_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


}
