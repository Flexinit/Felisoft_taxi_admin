package com.example.drupp_driver.adapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drupp_driver.Models.NotificationModel;
import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.IAdapterItemClickListener;
import com.example.drupp_driver.Utils.Timing;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NewHolder> {
    private Context mContext;
    private int mResId;
    private ArrayList<NotificationModel> mData;
    private IAdapterItemClickListener iAdapterItemClickListener;

    public NotificationAdapter(Context context, int resId, ArrayList<NotificationModel> data) {
        mContext = context;
        mResId = resId;
        mData = data;
    }

    @NonNull
    @Override
    public NotificationAdapter.NewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewHolder(LayoutInflater.from(mContext).inflate(mResId, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.NewHolder holder, int position) {
        holder.mMessage.setText(mData.get(position).getNotificationMessage());
        //  long date = Timing.getTimeInUnixStamp(mData.get(position).getUpdatedAt(), Timing.TimeFormats.DD_MM_YYYY_HH_MM_A);
        // String currentDate = Timing.getTimeInString(date, Timing.TimeFormats.DD_MM_YYYY_HH_MM_A);
        //   holder.mRecent.setText(mContext.getString(R.string._1_hour_ago, time));
        setTimeDiff(holder, position);
    }

    private void setTimeDiff(NewHolder holder, int position) {
        try {

            CharSequence ago = DateUtils.getRelativeTimeSpanString(mData.get(position).getNotificationTime() * 1000L, Timing.getCurrentTimeMillis(), DateUtils.MINUTE_IN_MILLIS);
            holder.time.setText(ago);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    public void setItems(ArrayList<NotificationModel> mData){
        this.mData=mData;
    }

    public void setiAdapterItemClickListener(IAdapterItemClickListener iAdapterItemClickListener) {
        this.iAdapterItemClickListener = iAdapterItemClickListener;
    }

    public class NewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mMessage;
        public TextView time;

        public NewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.tv_notification_time);
            mMessage = itemView.findViewById(R.id.tv_notification_content);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (iAdapterItemClickListener == null) return;
            iAdapterItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}
