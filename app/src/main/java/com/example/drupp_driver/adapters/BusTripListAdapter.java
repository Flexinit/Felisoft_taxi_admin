package com.example.drupp_driver.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drupp_driver.Models.BusInfoModel;
import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.IAdapterItemClickListener;
import com.example.drupp_driver.Utils.Timing;

import java.util.ArrayList;

public class BusTripListAdapter extends RecyclerView.Adapter<BusTripListAdapter.NewHolder> {
    private ArrayList<BusInfoModel> mData;
    private Context mContext;
    private int mResId;
    private IAdapterItemClickListener iAdapterItemClickListener;

    public BusTripListAdapter(Context context, int resId, ArrayList<BusInfoModel> busInfoModels) {
        mContext = context;
        mData = busInfoModels;
        mResId = resId;
    }

    public void setiAdapterItemClickListener(IAdapterItemClickListener iAdapterItemClickListener) {
        this.iAdapterItemClickListener = iAdapterItemClickListener;
    }

    @NonNull
    @Override
    public BusTripListAdapter.NewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewHolder(LayoutInflater.from(mContext).inflate(mResId, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BusTripListAdapter.NewHolder holder, int position) {
        try {
            if (mData.get(position).getRemainingSeats() == 0 || Timing.getCurrentTimeEpoch() > Long.valueOf(mData.get(position).getStartTime())) {
                holder.next.setVisibility(View.GONE);
                holder.btnStartRide.setVisibility(View.VISIBLE);
                switch (mData.get(position).getStatus()) {
                    case 1:
                        holder.btnStartRide.setText(mContext.getString(R.string.start_ride));
                        break;
                    case 2:
                        holder.btnStartRide.setText(mContext.getString(R.string.on_the_way));
                        break;
                    case 3:
                        holder.btnStartRide.setText(mContext.getString(R.string.finished));
                        break;
                }
            } else {
                holder.next.setVisibility(View.VISIBLE);
                holder.btnStartRide.setVisibility(View.GONE);
            }
        } catch (Exception e) {

        }

        holder.itemView.setClickable(mData.get(position).getStatus() == 1);
        holder.mSource.setText(mData.get(position).getSource());
        holder.mDestination.setText(mData.get(position).getDestination());
        holder.mStartTime.setText(Timing.getTimeInString(Long.valueOf(mData.get(position).getStartTime()), Timing.TimeFormats.HH_12));
        holder.mFinishTime.setText(Timing.getTimeInString(Long.valueOf(mData.get(position).getFinishTime()), Timing.TimeFormats.HH_12));
        holder.mRemainingSeats.setText(String.valueOf(mData.get(position).getRemainingSeats()));

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class NewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mSource, mDestination, mStartTime, mFinishTime;
        private TextView mRemainingSeats;
        private Button btnStartRide;
        private ImageView next;

        public NewHolder(@NonNull View itemView) {
            super(itemView);
            mSource = itemView.findViewById(R.id.tv_rider_source);
            mDestination = itemView.findViewById(R.id.tvDestination);
            mStartTime = itemView.findViewById(R.id.tv_start_time);
            mFinishTime = itemView.findViewById(R.id.tv_finish_time);
            mRemainingSeats = itemView.findViewById(R.id.tv_remaining_seats);
            next = itemView.findViewById(R.id.iv_next);
            btnStartRide = itemView.findViewById(R.id.btn_start_ride);
            btnStartRide.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (iAdapterItemClickListener == null) return;
            iAdapterItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}
