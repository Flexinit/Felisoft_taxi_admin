package com.example.drupp_driver.helpers;

import android.view.View;

import com.google.android.libraries.places.api.model.Place;

public interface IAdapterPlaceItemClickListener {
    void onAdapterItemClick(View v, int position);

    void onAdapterItemClick(Place place);
}
