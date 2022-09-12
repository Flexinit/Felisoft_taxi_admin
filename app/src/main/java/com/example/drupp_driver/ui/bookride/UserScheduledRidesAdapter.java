package com.example.drupp_driver.ui.bookride;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drupp_driver.Models.RideModel;
import com.example.drupp_driver.Models.ScheduleRideDriverResponse;
import com.example.drupp_driver.Models.ScheduledRideUserReponse;
import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.RxBus;
import com.example.drupp_driver.ui.ScheduledRidesActivity;

import java.util.ArrayList;
import java.util.List;

public class UserScheduledRidesAdapter extends RecyclerView.Adapter<UserScheduledRidesAdapter.UserScheduledRidesHolder> {

    private Context context;

    ArrayList<ScheduledRideUserReponse> scheduleList;
    private List<RideModel> rideModelList =new ArrayList<>();

    public UserScheduledRidesAdapter(Context context, ArrayList<ScheduledRideUserReponse> scheduleList){
        this.scheduleList=scheduleList;
        this.context=context;
        getAllDriverModels();


    }

    private void getAllDriverModels() {
        rideModelList.clear();
        for(int i=0;i<scheduleList.size();i++){
            rideModelList.addAll(scheduleList.get(i).getScheduledRides());
        }
    }

    public void setItems(ArrayList<ScheduledRideUserReponse> scheduleList){
        scheduleList.clear();
        this.scheduleList=scheduleList;
        getAllDriverModels();
        notifyDataSetChanged();
    }
    public void setItemOnClickListener(){

    }

    @NonNull
    @Override
    public UserScheduledRidesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserScheduledRidesHolder(LayoutInflater.from(context).inflate(R.layout.layout_scheduled_rides_item_new, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserScheduledRidesHolder holder, int position) {

        RideModel rideModel= rideModelList.get(position);
        holder.sourceCity.setText(rideModel.getSource());
        holder.rideDate.setText(handleDateString(rideModel.getRideDate()));
        holder.destination.setText(rideModel.getDestination());
        holder.rideTime.setText(handleTimeString(rideModel.getRideDate()));
        String rideTypePreference="";
        if(rideModel.getRideType()==1){
            rideTypePreference="Single";
        }
        else{
            rideTypePreference="Pool";
        }
        holder.rideTypeAndPreference.setText(rideTypePreference);


    }

    @Override
    public int getItemCount() {
        return rideModelList.size();
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

    public class UserScheduledRidesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView sourceCity;
        public TextView destination;
        public TextView rideDate;
        public TextView rideTime;
        public TextView rideTypeAndPreference;
        public int position;



        public UserScheduledRidesHolder(@NonNull View itemView) {
            super(itemView);
            sourceCity=itemView.findViewById(R.id.tvSourceCity);
            destination=itemView.findViewById(R.id.tv_rider_destination);
            rideDate=itemView.findViewById(R.id.tv_ride_date);
            rideTime=itemView.findViewById(R.id.tv_ride_time);
            rideTypeAndPreference=itemView.findViewById(R.id.tv_ridetype_and_preference);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
           /* Intent intent = new Intent(context, TripHistoryMid.class);
            intent.putExtra(AppConstants.K_S_CITY,sourceCity.getText().toString());
            intent.putExtra(AppConstants.K_D_CITY, destination.getText().toString());
            intent.putExtra(AppConstants.K_DATE, driverRideModelList.get(getAdapterPosition()).getRideDate());
            intent.putExtra(AppConstants.K_RIDE_ID, driverRideModelList.get(getAdapterPosition()).getRideId());
            intent.putExtra(AppConstants.K_RIDE_TYPE, driverRideModelList.get(getAdapterPosition()).getRideType());
            intent.putExtra(AppConstants.K_PASSENGERS_PREFERENCE, driverRideModelList.get(getAdapterPosition()).getPassengersPreference());
            intent.putExtra(AppConstants.K_BASE_FARE, driverRideModelList.get(getAdapterPosition()).getDriverFare());
            context.startActivity(intent);*/

            ScheduledRideDetailDialogUser scheduledRideDetailDialog = new ScheduledRideDetailDialogUser();
            scheduledRideDetailDialog.show(((ScheduledRidesActivity)context).getSupportFragmentManager(),ScheduledRideDetailDialogUser.class.getSimpleName());
            RxBus.getInstance().setIntentEvent(rideModelList.get(getAdapterPosition()));
        }
    }

}
