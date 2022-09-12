package com.example.drupp_driver.helpers;

import com.google.android.libraries.places.api.model.Place;

public interface IFragmentEventObserver {
    void onPlaceSelected(Place place, int locationType);
}
