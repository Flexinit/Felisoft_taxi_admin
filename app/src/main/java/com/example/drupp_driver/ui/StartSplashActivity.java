package com.example.drupp_driver.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.drupp_driver.R;

public class StartSplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_new);
        Handler handler=new Handler();
        Intent intent=new Intent(this, StartUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

             startActivity(intent);
            }
        },3000);
    }

}
