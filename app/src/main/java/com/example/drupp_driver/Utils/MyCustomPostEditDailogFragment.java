package com.example.drupp_driver.Utils;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.drupp_driver.Models.Token;
import com.example.drupp_driver.R;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetworkList;
import com.example.drupp_driver.connectivity.http.QualStandardResponseList;
import com.example.drupp_driver.helpers.SessionManager;

import java.util.HashMap;

import retrofit2.Response;

public class MyCustomPostEditDailogFragment extends DialogFragment {

    private static final String TAG = "MyCustomDialogFragment";
    public static String auth="";

    private TextView pick_up,message,co_riders,driver_type;

    String request_id;

  //  Context context=MyFirebaseMessagingService.this;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        auth = "Bearer "+ SessionManager.getInstance().getUserModel().getToken();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.custom_alert_dailog_post_ride_edit, container, false);


        Log.d(TAG, "onCreateView: showing dailog");

        pick_up=v.findViewById(R.id.tvPickup);
        message=v.findViewById(R.id.tvMessageValue);
        co_riders=v.findViewById(R.id.tvNumCoRidersValue);
        driver_type=v.findViewById(R.id.tvDriverTypeValue);


        co_riders.setText(getArguments().getString("co-riders_preference"));
        pick_up.setText(getArguments().getString("pick_up_location"));
        message.setText(getArguments().getString("preference"));

        switch (getArguments().getString("type_of_driver"))
        {
            case "1":  driver_type.setText("Chatty");
                             break;
            case "2":  driver_type.setText("Silent");
                             break;
            case "3":  driver_type.setText("Conversational,Quiet and indifferent");
                             break;
        }

//        request_id=getArguments().getString("request_id");
//
//        v.findViewById(R.id.btAccept).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//
//                acceptPostedRide(request_id);
//            }
//        });
//
//        v.findViewById(R.id.btReject).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//                rejectPostedRide(request_id);
//            }
//        });

        v.findViewById(R.id.btOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        
        return v;
    }

    /*private void acceptPostedRide(String id)
    {
        DruppRequestHandler.clearInstance();

        HashMap<String, Integer> parse = new HashMap<>();
        parse.put("status",2);
        parse.put("id",Integer.valueOf(id));

        //retrieveValuesFromListMethod(parse);

        DruppRequestHandler.getInstance(new INetworkList<Token>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<Token>> response) {


                if (response.isSuccessful() && response.body()!=null)
                {

                    Log.d(TAG, "onResponse: successssssssssssss...............");

                }

            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<Token>> response) {


                // Toast.makeText(DriverRegistrationActivity.this, "on error", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onError: error");
            }

            @Override
            public void onNullListResponse() {
                //Toast.makeText(DriverRegistrationActivity.this, "on null", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onNullResponse: error");
            }

            @Override
            public void onFailureList(Throwable t) {
                //  Toast.makeText(DriverRegistrationActivity.this, "on failure", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: error");
            }
        }).driverAcceptRidePost(auth,parse);
    }*/

    /*private void rejectPostedRide(String id)
    {
        DruppRequestHandler.clearInstance();

        HashMap<String, Integer> parse = new HashMap<>();
        parse.put("status",3);
        parse.put("id",Integer.valueOf(id));

        //retrieveValuesFromListMethod(parse);

        DruppRequestHandler.getInstance(new INetworkList<Token>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<Token>> response) {


                if (response.isSuccessful() && response.body()!=null)
                {

                    Log.d(TAG, "onResponse: successssssssssssss...............");

                }

            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<Token>> response) {


                // Toast.makeText(DriverRegistrationActivity.this, "on error", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onError: error");
            }

            @Override
            public void onNullListResponse() {
                //Toast.makeText(DriverRegistrationActivity.this, "on null", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onNullResponse: error");
            }

            @Override
            public void onFailureList(Throwable t) {
                //  Toast.makeText(DriverRegistrationActivity.this, "on failure", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: error");
            }
        }).driverAcceptRidePost(auth,parse);
    }*/
}
