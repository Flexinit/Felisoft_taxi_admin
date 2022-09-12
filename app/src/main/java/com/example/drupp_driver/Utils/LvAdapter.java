package com.example.drupp_driver.Utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.drupp_driver.Models.Trip;
import com.example.drupp_driver.R;

import java.util.List;

public class LvAdapter extends BaseAdapter {

    private static final String TAG = "LvAdapter";
//
//    private static int HEADER_COUNT=0;
//    private static int ITEM_COUNT=0;

    public static final int HEADER=0;
    public static final int ITEM=1;
    private List<Object> objectList;
    private LayoutInflater inflater;
    private Context mContext;

    public LvAdapter(List<Object> objectList, Context context)
    {
        this.objectList=objectList;
        this.mContext=context;
        inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.d(TAG, "LvAdapter: Constructor");
    }

    @Override
    public int getViewTypeCount() {
        //return getCount();
        //return super.getViewTypeCount();
        //return objectList.size();

        return 2;

    }

    @Override
    public int getItemViewType(int position) {

        if (objectList.get(position) instanceof Trip)
        {
           // Log.d(TAG, "getItemViewType: trip");
            return ITEM;

        }
        else
        {
            //Log.d(TAG, "getItemViewType: header");
          return HEADER;}
    }

    @Override
    public int getCount() {
        return objectList.size();
    }

    @Override
    public Object getItem(int position) {
        return objectList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        if (convertView==null)
        {
            grid = new View(mContext);


            if (getItemViewType(position)==ITEM)
            {
                grid=inflater.inflate(R.layout.layout_scheduled_rides_item, parent, false);

                TextView SCity = grid.findViewById(R.id.tvSourceCity);
                TextView DCity = grid.findViewById(R.id.tv_rider_destination);

                SCity.setText(((Trip) objectList.get(position)).getSource());
                DCity.setText(((Trip) objectList.get(position)).getDestination());
                Log.d(TAG, "getView: item inflated");
            }
            else
            {
                grid=inflater.inflate(R.layout.layout_trip_history_header, parent, false);
                TextView header = grid.findViewById(R.id.tvHeaderTripHis);

                header.setText(objectList.get(position).toString());
                Log.d(TAG, "getView: header inflated");
            }

        }
        else
            {
            grid = (View) convertView;
        }
        return grid;

//        else {
//
//            if (getItemViewType(position) == ITEM) {
//
//
//                Log.d(TAG, "getView: item text set");
//            } else {
//                TextView header = convertView.findViewById(R.id.tvHeaderTripHis);
//
////                header.setText(objectList.get(position).toString());
//
//                Log.d(TAG, "getView: header item inflated");
//            }
//
//            return convertView;
//
//        }

    }
}
