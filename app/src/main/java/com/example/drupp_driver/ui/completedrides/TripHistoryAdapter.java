package com.example.drupp_driver.ui.completedrides;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drupp_driver.Models.RiderInfo;
import com.example.drupp_driver.Models.ScheduleRideDriverResponse;
import com.example.drupp_driver.Models.Trip;
import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.RxBus;
import com.example.drupp_driver.Utils.Timing;
import com.example.drupp_driver.adapters.NotificationAdapter;
import com.example.drupp_driver.adapters.TripRideSection;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.ui.TripHistoryFinal;
import com.example.drupp_driver.ui.TripHistoryMid;
import com.example.drupp_driver.ui.bookride.DriverScheduledRidesAdapter;
import com.example.drupp_driver.ui.dialogs.RideRequestDialog;

import java.util.ArrayList;
import java.util.List;

public class TripHistoryAdapter extends RecyclerView.Adapter<TripHistoryAdapter.TripHistoryHolder>  {

    private Context context;
        List<RiderInfo> tripHistory;

    public static final long SECONDS_SINCE = 1L;
    public static final long MINUTES_SINCE = SECONDS_SINCE * 60;
    public static final long HOURS_SINCE = MINUTES_SINCE * 60;
    public static final long DAYS_SINCE = HOURS_SINCE * 24;
    public static final long WEEKS_SINCE = DAYS_SINCE * 7;
    public static final long MONTHS_SINCE = DAYS_SINCE * 30;
    public static final long YEARS_SINCE = MONTHS_SINCE * 12;

    private TripHistoryDialogFinal tripHistoryFinalDialog;


    public TripHistoryAdapter(Context context, List<RiderInfo> tripHistory){
            this.tripHistory=tripHistory;
            this.context=context;
        }


        public void setItems(List<RiderInfo> tripHistory){
            this.tripHistory=tripHistory;
        }

        @NonNull
        @Override
        public TripHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new TripHistoryHolder(LayoutInflater.from(context).inflate(R.layout.layout_trip_history_new, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull TripHistoryHolder holder, int position) {
                RiderInfo riderInfo=tripHistory.get(position);
                holder.sourceCity.setText(riderInfo.getSource());
                holder.destination.setText(riderInfo.getDestination());

                String fare=context.getString(R.string.naira) + riderInfo.getTotalFare();


                holder.rideFare.setText(fare);
                holder.rideStatus.setText(riderInfo.getSucceededOrCancelled());
                if(riderInfo.getSucceededOrCancelled().equals("Successful")){
                 holder.rideStatus.setTextColor(context.getResources().getColor(R.color.app_theme_color));
                }
                else{
                  holder.rideStatus.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
                }

                setTimeDiff(holder,position);



        }


    private void setTimeDiff(TripHistoryHolder holder, int position) {
        try {

            CharSequence ago = DateUtils.getRelativeTimeSpanString(Long.parseLong(tripHistory.get(position).getRideDate()) * 1000L, Timing.getCurrentTimeMillis(), DateUtils.MINUTE_IN_MILLIS);
            holder.rideDate.setText(ago);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String getTimeSince(String rideDate) {
         long time=System.currentTimeMillis()-Long.parseLong(rideDate);

         long timeInYears=time/YEARS_SINCE;
         if(timeInYears>=1){
            if(timeInYears==1){
                return time/YEARS_SINCE + " year ago";
            }
            else{
                return time/YEARS_SINCE + " years ago";
            }

        }
        long timeInMonths=time/MONTHS_SINCE;
        if(timeInMonths>=1){
            if(timeInMonths==1){
                return timeInMonths + " month ago";
            }
            else{
                return timeInMonths + " months ago";
            }

        }

        long timeInWeeks=time/WEEKS_SINCE;
        if(timeInWeeks>=1){
            if(timeInWeeks==1){
                return timeInWeeks + " week ago";
            }
            else{
                return timeInWeeks + " weeks ago";
            }

        }
        long timeInDays=time/DAYS_SINCE;
        if(timeInDays>=1){
            if(timeInDays==1){
                return timeInDays + " day ago";
            }
            else{
                return timeInDays + " days ago";
            }

        }
        long timeInHours=time/HOURS_SINCE;
        if(timeInHours>=1){
            if(timeInHours==1){
                return timeInHours + " hour ago";
            }
            else{
                return timeInHours + " hours ago";
            }

        }
        long timeInMinutes=time/MINUTES_SINCE;
        if(timeInMinutes>=1){
            if(timeInMinutes==1){
                return timeInMinutes + " minute ago";
            }
            else{
                return timeInMinutes + " minutes ago";
            }

        }
        long timeInSeconds=time/SECONDS_SINCE;
        if(timeInSeconds>1){

            return timeInSeconds + " seconds ago";


        }

        return timeInSeconds + " second ago";


    }

    @Override
        public int getItemCount() {
            return tripHistory.size();
        }
        private String handleDateString(String timeStamp) {
            java.util.Date given_timestamp = new java.util.Date((long) (Long.valueOf(timeStamp)) * 1000);

            String my_timestamp = String.valueOf(given_timestamp);

            String[] arr = my_timestamp.split(" ");
            String week_day = arr[0];
            String month = arr[1];
            String day = arr[2];
            String time = arr[3];
            String utc = arr[4];
            String year = arr[5];


            switch (month) {
                case "Jan":
                    month = "1";
                    break;
                case "Feb":
                    month = "2";
                    break;
                case "Mar":
                    month = "3";
                    break;
                case "Apr":
                    month = "4";
                    break;
                case "May":
                    month = "5";
                    break;
                case "Jun":
                    month = "6";
                    break;
                case "Jul":
                    month = "7";
                    break;
                case "Aug":
                    month = "8";
                    break;
                case "Sep":
                    month = "9";
                    break;
                case "Oct":
                    month = "10";
                    break;
                case "Nov":
                    month = "11";
                    break;
                case "Dec":
                    month = "12";
                    break;

            }

            String xdate = (day + "/" + month + "/" + year);
            return xdate;
        }
        private String handleTimeString(String timeStamp) {
            java.util.Date given_timestamp = new java.util.Date((long) (Long.valueOf(timeStamp)) * 1000);

            String my_timestamp = String.valueOf(given_timestamp);

            String[] arr = my_timestamp.split(" ");
            String week_day = arr[0];
            String month = arr[1];
            String day = arr[2];
            String time = arr[3];
            String utc = arr[4];
            String year = arr[5];

            String xdate = (day + "/" + month + "/" + year);
            return time;
        }

        public class TripHistoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public TextView sourceCity;
            public TextView destination;
            public TextView rideDate;
            public TextView rideFare;
            public TextView rideStatus;

            public int position;



            public TripHistoryHolder(@NonNull View itemView) {
                super(itemView);
                sourceCity=itemView.findViewById(R.id.tvSourceCity);
                destination=itemView.findViewById(R.id.tv_rider_destination);
                rideDate=itemView.findViewById(R.id.tv_time_since);
                rideFare=itemView.findViewById(R.id.tv_ride_fare);
                rideStatus=itemView.findViewById(R.id.tv_status);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {

                Bundle bundle=new Bundle();
                bundle.putString(TripHistoryDialogFinal.RIDE_STATUS,rideStatus.getText().toString());
                tripHistoryFinalDialog = new TripHistoryDialogFinal();
                tripHistoryFinalDialog.setArguments(bundle);

                FragmentTransaction transaction = ((TripHistoryNew)context).getSupportFragmentManager()
                        .beginTransaction()
                        .add(tripHistoryFinalDialog, TripHistoryDialogFinal.class.getSimpleName());
                transaction.commit();
                RxBus.getInstance().setIntentEvent(tripHistory.get(getAdapterPosition()));

            }
        }

    }


