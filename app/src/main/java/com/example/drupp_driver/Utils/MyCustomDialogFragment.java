package com.example.drupp_driver.Utils;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.drupp_driver.Models.RiderInfo;
import com.example.drupp_driver.R;
import com.example.drupp_driver.helpers.SessionManager;

import java.util.List;

public class MyCustomDialogFragment extends DialogFragment {

    private TextView mSource, mDestination;
    private onCustomDialogListener onCustomDialogListener;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.custom_alert_dailog_recieved_ride_2, container, false);

        mSource = v.findViewById(R.id.tv_rider_source);
        mDestination = v.findViewById(R.id.tv_rider_destination);

        v.findViewById(R.id.btView).setOnClickListener(v1 -> {
            if (onCustomDialogListener != null)
                onCustomDialogListener.onViewClick();
        });

        v.findViewById(R.id.btCancel).setOnClickListener(v12 -> {
            dismiss();
            if (onCustomDialogListener != null)
                onCustomDialogListener.onCancelClick();
        });
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            List<RiderInfo> ridersInfo = SessionManager.getInstance().getRiderInfos();
            final RiderInfo newAddedRider = ridersInfo.get(ridersInfo.size() - 1);
            mSource.setText(newAddedRider.getSource());
            mDestination.setText(newAddedRider.getDestination());
        } catch (Exception e) {
            Log.e("Exception: ", e.getMessage());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
    }

    public interface onCustomDialogListener {
        void onViewClick();

        void onCancelClick();
    }

    public void setOnCustomDialogListener(MyCustomDialogFragment.onCustomDialogListener onCustomDialogListener) {
        this.onCustomDialogListener = onCustomDialogListener;
    }
}
