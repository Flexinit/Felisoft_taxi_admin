package com.example.drupp_driver.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.drupp_driver.Models.Trip;
import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.IAdapterItemClickListener;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class TripRideSection extends StatelessSection {

    String title;
    List<Trip> list;
    private Context mContext;

    private IAdapterItemClickListener iAdapterItemClickListener;

    public TripRideSection(Context context, String title, List<Trip> list) {
        super(SectionParameters.builder()
                .itemResourceId(R.layout.layout_scheduled_rides_item)
                .headerResourceId(R.layout.layout_trip_history_header)
                .build());
        mContext = context;

        this.title = title;
        this.list = list;
    }

    public void setiAdapterItemClickListener(IAdapterItemClickListener iAdapterItemClickListener) {
        this.iAdapterItemClickListener = iAdapterItemClickListener;
    }

    @Override
    public int getContentItemsTotal() {
        return list.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }


    public List<Trip> getList() {
        return list;
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {

        final ItemViewHolder itemHolder = (ItemViewHolder) holder;

        String scity = list.get(position).getSource();
        String dcity = list.get(position).getDestination();


        itemHolder.tvSourceCity.setText(scity);
        itemHolder.tvDestinationCity.setText(dcity);


    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }


    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

        headerHolder.tvTitle.setText(title);
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitle;

        HeaderViewHolder(View view) {
            super(view);

            tvTitle = view.findViewById(R.id.tvHeaderTripHis);
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final View rootView;
        public final TextView tvSourceCity, tvDestinationCity;

        ItemViewHolder(View view) {
            super(view);

            rootView = view;
            tvSourceCity = view.findViewById(R.id.tvSourceCity);
            tvDestinationCity = view.findViewById(R.id.tv_rider_destination);

//
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(TripHistory.this, TripHistoryFinal.class);
//                    RxBus.getInstance().setIntentEvent(list.get(sectionAdapter.getPositionInSection(getAdapterPosition())));
//                    startActivity(intent);
//
//
//                }
//            });
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (iAdapterItemClickListener == null) return;
            iAdapterItemClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}
