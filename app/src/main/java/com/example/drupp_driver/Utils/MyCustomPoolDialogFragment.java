package com.example.drupp_driver.Utils;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.drupp_driver.Models.PostRideInfo;
import com.example.drupp_driver.R;
import com.example.drupp_driver.helpers.SessionManager;

public class MyCustomPoolDialogFragment extends DialogFragment {

    private onActionPostRide onActionPostRide;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setOnActionPostRide(MyCustomPoolDialogFragment.onActionPostRide onActionPostRide) {
        this.onActionPostRide = onActionPostRide;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.custom_alert_dailog_post_ride_accept, container, false);
        TextView mPicUP = v.findViewById(R.id.tvPickup);
        TextView message = v.findViewById(R.id.tvMessageValue);
        TextView mCoRiderPref = v.findViewById(R.id.tvNumCoRidersValue);
        TextView driver_type = v.findViewById(R.id.tvDriverTypeValue);

        try {
            PostRideInfo postRideInfo = SessionManager.getInstance().getPostRideInfo();
            mCoRiderPref.setText(postRideInfo.getCoRidersPreference());
            mPicUP.setText(postRideInfo.getPickUpLocation());
            message.setText(postRideInfo.getMessage());
            switch (postRideInfo.getTypeOfDriver()) {
                case "1":
                    driver_type.setText(R.string.chatty);
                    break;
                case "2":
                    driver_type.setText(R.string.silent);
                    break;
                case "3":
                    driver_type.setText(R.string.indifferent);
                    break;
            }
        } catch (Exception e) {
            Log.e("Exception: ", e.getMessage());
        }

        v.findViewById(R.id.btAccept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onActionPostRide != null) onActionPostRide.onAccept();
            }
        });

        v.findViewById(R.id.btReject).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onActionPostRide != null) onActionPostRide.onReject();
            }
        });
        return v;
    }

    public interface onActionPostRide {
        void onAccept();

        void onReject();
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
    }
}
