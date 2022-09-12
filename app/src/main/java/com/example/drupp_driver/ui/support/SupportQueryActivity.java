package com.example.drupp_driver.ui.support;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.drupp_driver.R;
import com.example.drupp_driver.ui.base.MainBaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SupportQueryActivity extends MainBaseActivity {
    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.support_query_layout);
        ButterKnife.bind(this);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_support, new SupportQueryFragment(), SupportQueryFragment.class.getSimpleName())
                .commit();
    }

    @OnClick(R.id.image_back)
    public void onBack() {
        onBackPressed();
    }

}
