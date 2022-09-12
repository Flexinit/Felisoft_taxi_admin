package com.example.drupp_driver.ui.dashboard;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.drupp_driver.Models.DashboardCountModel;
import com.example.drupp_driver.R;
import com.example.drupp_driver.ui.EarningGraphActivty;
import com.example.drupp_driver.ui.PostRideActivity;
import com.example.drupp_driver.ui.base.MainBaseFragment;
import com.example.drupp_driver.ui.bookride.DriverScheduledRidesAdapter;

import java.text.DecimalFormat;

public class SummaryFragment  extends MainBaseFragment {

    private TextView tv_card_payments_no,tv_cash_payments_no,tv_total_rides_no,tv_completed_rides_no,tv_cancelled_rides_no,tv_earnings_no;
    private ConstraintLayout layout_earnings;
    DashboardCountModel dashboardCountModel;

    int typeOfSummary;
    SummaryFragment(int typeOfSummary){
        this.typeOfSummary=typeOfSummary;
    }

    SummaryFragment(int typeOfSummary, DashboardCountModel dashboardCountModel) {
        this.typeOfSummary= typeOfSummary;
        this.dashboardCountModel= dashboardCountModel;
    }

    @Override
    protected void initViewsForFragment(View view) {

    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_summary, container, false);


        layout_earnings = view.findViewById(R.id.layout_earnings);
        tv_card_payments_no = view.findViewById(R.id.tv_card_payments_no);
        tv_cash_payments_no = view.findViewById(R.id.tv_cash_payments_no);
        tv_total_rides_no = view.findViewById(R.id.tv_total_rides_no);
        tv_completed_rides_no = view.findViewById(R.id.tv_completed_rides_no);
        tv_cancelled_rides_no = view.findViewById(R.id.tv_cancelled_rides_no);
        tv_earnings_no = view.findViewById(R.id.tv_earnings_no);



        if(typeOfSummary == 1){
            tv_card_payments_no.setText(String.valueOf(dashboardCountModel.getTotalCardPayment()));
            tv_cash_payments_no.setText(String.valueOf(dashboardCountModel.getTotalCashPayment()));
            tv_total_rides_no.setText(String.valueOf(dashboardCountModel.getTotalRides()));
            tv_completed_rides_no.setText(String.valueOf(dashboardCountModel.getTotalCompletedRides()));
            tv_cancelled_rides_no.setText(String.valueOf(dashboardCountModel.getTotalCanceledRides()));
            tv_earnings_no.setText(String.valueOf(Double.parseDouble(new DecimalFormat("##.##").format(dashboardCountModel.getTotalEarnings()))));
        }else{
            tv_card_payments_no.setText(String.valueOf(dashboardCountModel.getWeekTotalCardPayment()));
            tv_cash_payments_no.setText(String.valueOf(dashboardCountModel.getWeekTotalCashPayment()));
            tv_total_rides_no.setText(String.valueOf(dashboardCountModel.getWeekTotalRides()));
            tv_completed_rides_no.setText(String.valueOf(dashboardCountModel.getWeekTotalCompletedRides()));
            tv_cancelled_rides_no.setText(String.valueOf(dashboardCountModel.getWeekTotalCanceledRides()));
            tv_earnings_no.setText(String.valueOf(Double.parseDouble(new DecimalFormat("##.##").format(dashboardCountModel.getWeekTotalEarnings()))));
        }


        layout_earnings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EarningGraphActivty.class));
            }
        });


        return view;
    }
}
