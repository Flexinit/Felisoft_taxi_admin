package com.example.drupp_driver.ui.bookride;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.drupp_driver.R;
import com.example.drupp_driver.ui.base.MainBaseActivity;

public class BusTripActivity extends MainBaseActivity {

    private void setupToolbar() {
        TextView toolbarTitle = findViewById(R.id.tv_toolbar_title);
        toolbarTitle.setText(getString(R.string.bus_ride));

        findViewById(R.id.image_back).setOnClickListener(v -> onBackPressed());
    }

    @Override
    protected void setUp() {
        setupToolbar();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_bus_list, new BusTripFragment(), BusTripFragment.class.getSimpleName())
                .commit();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_trip);

    }
}
