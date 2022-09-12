package com.example.drupp_driver.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.drupp_driver.R;

public class VehicleRegistrationActivity extends AppCompatActivity {

    // Not in use
    // Vehicle registration is done in driver registration activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_registration);

        findViewById(R.id.btStartRide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VehicleRegistrationActivity.this, RideActivity.class);
                startActivity(intent);

            }
        });
    }
}
