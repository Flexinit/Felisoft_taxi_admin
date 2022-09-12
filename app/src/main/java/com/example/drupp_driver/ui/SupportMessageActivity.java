package com.example.drupp_driver.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.drupp_driver.Models.Token;
import com.example.drupp_driver.R;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetworkList;
import com.example.drupp_driver.connectivity.http.QualStandardResponseList;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.ui.base.MainBaseActivity;

import java.util.HashMap;

import retrofit2.Response;

public class SupportMessageActivity extends MainBaseActivity {

    String ride_id;
    String source;
    String destination;
    String issue;
    String ride_date;
    EditText query;
    TextView issueTitle;

    // Driver drops a message for getting support
    private static final String TAG = "SupportMessageActivity";
    String auth = "Bearer " + SessionManager.getInstance().getUserModel().getToken();

    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_message);


        Intent intent = getIntent();

        ride_id = intent.getStringExtra("ride_id");
        source = intent.getStringExtra("source");
        destination = intent.getStringExtra("destination");
        issue = intent.getStringExtra("issue");
        ride_date = intent.getStringExtra("ride_date");

        query = findViewById(R.id.etQuery);
        issueTitle = findViewById(R.id.tvIssueTitle);

        issueTitle.setText(issue);


        findViewById(R.id.image_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.btSendQuery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (!query.getText().toString().trim().isEmpty()) {
                        getIn();
                    } else {
                        showMessage(R.string.please_enter_details);
                    }

                } catch (Exception e) {
                    Log.d(TAG, "onClick: API EXCEPTION");
                }
            }
        });

    }

    private void getIn() {
        HashMap<String, String> parse = new HashMap<>();
        parse.put("ride_id", ride_id);
        parse.put("ride_source", source);
        parse.put("ride_destination", destination);
        parse.put("date", ride_date);
        parse.put("issue", issue + query.getText().toString());

//        DruppRequestHandler.getInstance(new INetworkList<Token>() {
//            @Override
//            public void onResponse(Response<QualStandardResponseList<Token>> response) {
//
//
//                try {
//                    //if (response.isSuccessful() && response.body()!=null)
//                    if (response.isSuccessful())
//                    {
//
//                        Intent intent = new Intent(SupportMessageActivity.this, RideActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
//
//                }
//                catch (Exception e)
//                {
//                    Log.d(TAG, "onResponse: Exception: "+e.getMessage());
//                }
//
//
//            }
//
//            @Override
//            public void onError(Response<QualStandardResponseList<Token>> response) {
//
//                Log.d(TAG, "onError: Support message");
//            }
//
//            @Override
//            public void onNullResponse() {
//
//                Log.d(TAG, "onNullResponse: Support message");
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                Log.d(TAG, "onFailure: Support message "+t.getMessage());
//
//            }
//        }).driverSupport(auth,parse);


        DruppRequestHandler.getInstance(new INetworkList<Token>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<Token>> response) {
                if (response.isSuccessful()) {

//                        Intent intent = new Intent(SupportMessageActivity.this, RideActivity.class);
//                        startActivity(intent);
//                        finish();
                    showCancelledDailog();
                }
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<Token>> response) {
                Log.d(TAG, "onError: Support message");

            }

            @Override
            public void onNullListResponse() {
                Log.d(TAG, "onNullResponse: Support message");

            }

            @Override
            public void onFailureList(Throwable t) {
//                Log.d(TAG, "onFailure: Support message "+t.getMessage());

            }
        }).driverSupport(auth, parse);
    }

    private void showCancelledDailog() {

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_alert_dailog_ride_canceled, null);
        AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = alertDialogbuilder.create();
        alertDialog.setView(alertLayout);
        // alertDialog.setContentView(R.layout.custom_alert_dailog);
        alertDialog.show();
        //alertDialog.getWindow().setLayout(1000,900);
        // alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);

        TextView cancel_ride_big = alertDialog.findViewById(R.id.tvYourRide);
        cancel_ride_big.setText("Your query has been sent to the \n admin and one of our representative will contact you soon!!");

        TextView cancel_ride = alertDialog.findViewById(R.id.tvRide_Cancel);
        cancel_ride.setText("Thank You!");
        alertDialog.findViewById(R.id.btOk_clicked).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ok clicked");

                Intent intent = new Intent(SupportMessageActivity.this, RideActivity.class);
                startActivity(intent);
                finish();
                alertDialog.dismiss();
            }
        });
    }



}
