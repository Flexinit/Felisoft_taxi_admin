package com.example.drupp_driver.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.drupp_driver.R;
import com.example.drupp_driver.helpers.AppConstants;

public class BillPaidActivity extends AppCompatActivity {

    private int id, user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_paid);

        if (getIntent() != null) {
            id = getIntent().getIntExtra(AppConstants.K_ID, 0);
            user_id = getIntent().getIntExtra(AppConstants.K_USER_ID, 1);
        }


        findViewById(R.id.btDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
}
