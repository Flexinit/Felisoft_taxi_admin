package com.example.drupp_driver.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drupp_driver.Models.PassengerInfoModel;
import com.example.drupp_driver.R;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.IAdapterItemCheckListener;

import java.util.ArrayList;
import java.util.List;

public class PassengerListAdapter extends RecyclerView.Adapter<PassengerListAdapter.NewHolder> implements Filterable {
    private List<PassengerInfoModel> mData, mDataFiltered;

    private Context mContext;
    private int mResId;
    private IAdapterItemCheckListener iAdapterItemCheckListener;
    private Filter passengerFilter;

    public PassengerListAdapter(Context context, int resId, ArrayList<PassengerInfoModel> passengerInfoModels) {
        mContext = context;
        mResId = resId;
        mData = passengerInfoModels;
        mDataFiltered = passengerInfoModels;
    }

    public void setiAdapterItemCheckListener(IAdapterItemCheckListener iAdapterItemCheckListener) {
        this.iAdapterItemCheckListener = iAdapterItemCheckListener;
    }

    @NonNull
    @Override
    public PassengerListAdapter.NewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewHolder(LayoutInflater.from(mContext).inflate(mResId, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PassengerListAdapter.NewHolder holder, int position) {
        if (mDataFiltered.get(position).getStatus() == AppConstants.PASSENGER_BOOKED || mDataFiltered.get(position).getStatus() == AppConstants.PASSENGER_NOT_BOARDED) {
            holder.switchStatus.setChecked(false);
        } else {
            holder.switchStatus.setChecked(true);
        }

        holder.passengerNo.setText(mContext.getString(R.string.list_position, position + 1));
        holder.passengerName.setText(mDataFiltered.get(position).getUserName());
    }

    @Override
    public int getItemCount() {
        return mDataFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mDataFiltered = mData;
                } else {
                    List<PassengerInfoModel> filteredList = new ArrayList<>();
                    for (PassengerInfoModel row : mData) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getUserName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    mDataFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mDataFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mDataFiltered = (ArrayList<PassengerInfoModel>) filterResults.values;

                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }


    class PassengerFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();


            //Implement filter logic
            // if edittext is null return the actual list
            if (constraint == null || constraint.length() == 0) {
                //No need for filter
                results.values = mData;
                results.count = mData.size();

            } else {
                //Need Filter
                // it matches the text  entered in the edittext and set the data in adapter list
                ArrayList<PassengerInfoModel> fRecords = new ArrayList<>();

                for (PassengerInfoModel passengerInfoModel : mData) {
                    if (passengerInfoModel.getUserName().toUpperCase().trim().contains(constraint.toString().toUpperCase().trim())) {
                        fRecords.add(passengerInfoModel);
                    }
                }
                results.values = fRecords;
                results.count = fRecords.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //it set the data from filter to adapter list and refresh the recyclerview adapter

            if (results != null && results.count > 0) {
                mData.clear();
                mData.addAll((ArrayList<PassengerInfoModel>) results.values);
                notifyDataSetChanged();

            } else {
                mData.clear();
                notifyDataSetChanged();
            }
        }
    }


    class NewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {
        TextView passengerNo, passengerName;
        Switch switchStatus;

        public NewHolder(@NonNull View itemView) {
            super(itemView);
            switchStatus = itemView.findViewById(R.id.switch_board);
            passengerNo = itemView.findViewById(R.id.tv_passenger_no);
            passengerName = itemView.findViewById(R.id.tv_passenger_name);
            switchStatus.setOnCheckedChangeListener(this);

        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (iAdapterItemCheckListener == null) return;
            iAdapterItemCheckListener.onItemCheckChanged(isChecked, getAdapterPosition());
        }
    }
}
