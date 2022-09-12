package com.example.drupp_driver.ui.poolride;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.drupp_driver.Models.PopState;
import com.example.drupp_driver.Models.RiderInfo;
import com.example.drupp_driver.Models.RiderInfoModel;
import com.example.drupp_driver.Models.UserDetails;
import com.example.drupp_driver.Models.UserLocation;
import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.LocationUtil;
import com.example.drupp_driver.Utils.MyCustomDialogFragment;
import com.example.drupp_driver.Utils.NoSwipeViewPager;
import com.example.drupp_driver.Utils.PoolRideManager;
import com.example.drupp_driver.adapters.PagerHomeAdapter;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.ui.RideActivity;
import com.example.drupp_driver.ui.base.CommonDrawerActivity;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.SphericalUtil;
import com.sachinvarma.easylocation.event.Event;
import com.sachinvarma.easylocation.event.LocationEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import butterknife.BindView;

public class PoolRideActivity extends CommonDrawerActivity implements OnMapReadyCallback
        , LocationListener, MyCustomDialogFragment.onCustomDialogListener, RoutingListener {


    //------------------Views------------------------
    private NoSwipeViewPager viewPager;
    private PagerHomeAdapter pagerHomeAdapter;
    private SupportMapFragment supportMapFragment;
    private Location currentBestLocation;
    public GoogleMap mMap;
    private MarkerOptions markerOptions = new MarkerOptions();
    private boolean isMarkerShowAtMap;
    //DialogFragment
    private MyCustomDialogFragment myCustomDialog;
    //----------------Globals----------------------
    private static final int[] COLORS = new int[]{R.color.primary_dark_material_light};
    //-----------------------Receivers-----------------------
    private BroadcastReceiver listener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            currentRideId = intent.getStringExtra(AppConstants.K_RIDE_ID);
            handleDriverAvailableState(intent);
        }
    };


    private BroadcastReceiver cancelRideListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            RiderInfo riderInfo = SessionManager.getInstance().getRiderInfos().get(0);
            //  updateFirebaseDatabaseReferences(DATABASE_REF.RIDE_INFO).child(String.valueOf(riderInfo.getRideId())).removeValue();
            // showCancelledDailog();
        }
    };


    private String currentRideId;
    private AppConstants.DriverLocationStatus currentDriverStatus;
    private ArrayList<Polyline> polylines = new ArrayList<>();

    @Override
    protected void showDialogLogOut() {

    }

    @Override
    protected void setUp() {
        viewPager = findViewById(R.id.main_pager);
        pagerHomeAdapter = new PagerHomeAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        viewPager.setAdapter(pagerHomeAdapter);
        viewPager.setCurrentItem(0);
        // initializing and setting up map
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.image_Map);
        supportMapFragment.getMapAsync(this);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_navigation_drawer_layout);
        getLayoutInflater().inflate(R.layout.activity_pool_ride, frameLayout);

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(cancelRideListener, new IntentFilter(AppConstants.I_CANCEL_RIDE));
        registerReceiver(listener, new IntentFilter(AppConstants.K_UPDATE_NOTIFICATION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(cancelRideListener);
        unregisterReceiver(listener);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
        if (fetchCurrentLocation() != null) {
            pointCurrentLocationAtMap();

        }

    }

    //Pop Up Handling Here
    private void handleDriverAvailableState() {
        //This is for last time screen open
        UserDetails userInfo = SessionManager.getInstance().loadUserDetails(this);
        PopState popState = SessionManager.getInstance().getPopState();
        if (popState != null) {
            switch (popState.getStateType()) {
                case 0:
                    viewPager.setCurrentItem(AppConstants.PAGER_RIDE.DRIVER_PROFILE);
                    break;
                case 1:
                    showDialogFragment();
                    break;
                case 2:
                    break;
                case 3:
                case 4:
                    break;
                case 6:
                    break;
            }
        }
    }

    private void handleDriverAvailableState(Intent intent) {
        //This is for when screen open Pop Up Handling
        if (mDriverStatus.isChecked()) {
            PopState popState = SessionManager.getInstance().getPopState();
            if (popState != null) {
                handleDriverAvailableState();
            } else {
                if (intent != null && intent.hasExtra(AppConstants.K_NOTIFICATION_TYPE)) {
                    int type = intent.getIntExtra(AppConstants.K_NOTIFICATION_TYPE, 1);
                    switch (type) {
                        case AppConstants.RIDE_NOW:
                            showDialogFragment();
                            break;
                        case AppConstants.RIDE_LATER:
                            //  showDailogFragmentRideLater();
                            break;
                        case AppConstants.POST_RIDE_ACCEPT:
                            //showDialogFragmentPostRide();
                            break;
                    }
                }

            }

        }
    }

    public Location fetchCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                    , AppConstants.REQUEST_ACCESS_LOCATION);
            return null;
        } else {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            LocationSettingsRequest.Builder builder = LocationUtil.showLocationRequestPopUp(this);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                //Ask user to enable GPS
                Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());
                result.addOnCompleteListener(task -> {
                    try {
                        LocationSettingsResponse response = task.getResult(ApiException.class);
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                    } catch (ApiException e) {
                        switch (e.getStatusCode()) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                // Location settings are not satisfied. But could be fixed by showing the
                                // user a dialog.
                                try {
                                    // Cast to a resolvable exception.
                                    ResolvableApiException resolvable = (ResolvableApiException) e;
                                    // Show the dialog by calling startResolutionForResult(),
                                    // and check the result in onActivityResult().
                                    resolvable.startResolutionForResult(
                                            PoolRideActivity.this,
                                            AppConstants.REQUEST_ACCESS_LOCATION);
                                } catch (IntentSender.SendIntentException e1) {
                                    // Ignore the error.
                                } catch (ClassCastException e2) {
                                    // Ignore, should be an impossible error.
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                // Location settings are not satisfied. However, we have no way to fix the
                                // settings so we won't show the dialog.

                                break;
                        }

                    }
                });

                return null;

            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, AppConstants.LOCATION_UPDATE_TIME_INTERVAL, AppConstants.LOCATION_UPDATE_MIN_DISTANCE, this);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, AppConstants.LOCATION_UPDATE_TIME_INTERVAL, AppConstants.LOCATION_UPDATE_MIN_DISTANCE, this);

                Criteria criteria = new Criteria();
                int currentapiVersion = android.os.Build.VERSION.SDK_INT;

                if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {

                    criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
                    criteria.setSpeedAccuracy(Criteria.ACCURACY_HIGH);
                    criteria.setAccuracy(Criteria.ACCURACY_FINE);
                    criteria.setAltitudeRequired(true);
                    criteria.setBearingRequired(true);
                    criteria.setSpeedRequired(true);

                }
                String provider = locationManager.getBestProvider(criteria, true);
                //Sets Places api to return only for radius
                currentBestLocation = locationManager.getLastKnownLocation(provider);
                return currentBestLocation;
            }

        }
    }

    //------------------------Helper-----------------------------
    private void pointCurrentLocationAtMap() {
        if (mMap != null) {
            mMap.clear();
            isMarkerShowAtMap = true;
            mMap.addMarker(markerOptions.position(new LatLng(currentBestLocation.getLatitude(), currentBestLocation.getLongitude()))
                    .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_car))
                    .title(getString(R.string.current_location)));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentBestLocation.getLatitude(), currentBestLocation.getLongitude()), 14));
        }
    }


    // method for setting custom icons as marker in map
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    //-------------------Pop ups-----------------------
    private synchronized void showDialogFragment() {
        if (myCustomDialog != null) {
            myCustomDialog.dismiss();
        }
        myCustomDialog = new MyCustomDialogFragment();
        myCustomDialog.show(getSupportFragmentManager(), MyCustomDialogFragment.class.getSimpleName());
        myCustomDialog.setOnCustomDialogListener(this);
        myCustomDialog.setCancelable(false);


    }


    private void getMapRoute(RiderInfo riderInfo) {
        try {
            final int padding = 300;
            LatLng source = new LatLng(Double.valueOf(riderInfo.getSourceLatitude()), Double.valueOf(riderInfo.getSourceLongitude()));
            LatLng destination = new LatLng(Double.valueOf(riderInfo.getDestinationLatitude()), Double.valueOf(riderInfo.getDestinationLongitude()));
            LatLng currentLocation = new LatLng(currentBestLocation.getLatitude(), currentBestLocation.getLongitude());

            mMap.clear();
            mMap.addMarker(new MarkerOptions().visible(true).position(source).title(getString(R.string.source)).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_pin_green)));
            mMap.addMarker(new MarkerOptions().visible(true).position(destination).title(getString(R.string.source)).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_pin_red)));
            mMap.addMarker(new MarkerOptions().visible(true).position(currentLocation).title(getString(R.string.source)).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_car)));

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(currentLocation);
            builder.include(source);
            builder.include(destination);
            final LatLngBounds bounds = builder.build();

            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
                }
            });

            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .key(getString(R.string.google_maps_key))
                    .alternativeRoutes(false)
                    .waypoints(currentLocation, source, destination)
                    .build();
            routing.execute();
        } catch (Exception e) {
            Log.e("Exception: ", e.getMessage());
        }
    }


    private void showRideAcceptRejectView() {

        if (myCustomDialog != null) {
            myCustomDialog.dismiss();
        }
        List<RiderInfo> ridersInfo = SessionManager.getInstance().getRiderInfos();
        final RiderInfo newAddedRider = ridersInfo.get(ridersInfo.size() - 1);
        //Show Accept Reject View
        viewPager.setCurrentItem(AppConstants.PAGER_RIDE.RIDE_ACCEPT_REJECT);
        getMapRoute(newAddedRider);

//        btAccept.setOnClickListener(v -> acceptRide(newAddedRider.getRideId()));
//
//        btDecline.setOnClickListener(v -> {
//            currentDriverLocationStatus = AppConstants.DriverLocationStatus.IDLE;
//            removeNotificationAndPopStates();
//            if (ActivityCompat.checkSelfPermission(RideActivity.this,
//                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                    && ActivityCompat.checkSelfPermission(RideActivity.this,
//                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//
//            location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
//            if (location != null) {
//                currentLoc = location;
//                lastLoc = location;
//                mMap.clear();
//                final LatLng latLng = new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude());
//                if (marker_current != null)
//                    marker_current.remove();
//                marker_current = mMap.addMarker(markerOptions.position(new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude()))
//                        .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_car))
//                        .title("Current Location"));
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
//            }
//            startLocationUpdates();
//        });
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getEvent(final Event event) {
        if (event instanceof LocationEvent) {
            if (((LocationEvent) event).location != null) {
                UserLocation userLocation = SessionManager.getInstance().getUserLocation();
                if (userLocation.getCurrentLatLng() == null) {
                    UserLocation location = new UserLocation();
                    location.setCurrentLatLng(new LatLng(((LocationEvent) event).location.getLatitude(), ((LocationEvent) event).location.getLongitude()));
                    SessionManager.getInstance().saveUserLocation(PoolRideActivity.this, location);
                    userLocation = SessionManager.getInstance().getUserLocation();
                }
                /* If Distance Will in 10 will update in Meters. */
                final float[] results = new float[1];
                Location.distanceBetween(userLocation.getCurrentLatLng().latitude, userLocation.getCurrentLatLng().longitude,
                        ((LocationEvent) event).location.getLatitude(), ((LocationEvent) event).location.getLongitude(), results);
                if (results[0] > 10) {

                    saveAndUpdateRealtimeLocation(((LocationEvent) event).location.getLatitude(), ((LocationEvent) event).location.getLongitude());

                    if (!isMarkerShowAtMap) pointCurrentLocationAtMap();
                }
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null && currentBestLocation != null) {
            final LatLng last_latLng = new LatLng(currentBestLocation.getLatitude(), currentBestLocation.getLongitude());
            final LatLng current_latLng = new LatLng(location.getLatitude(), location.getLongitude());
            Thread background = new Thread() {
                public void run() {
                    try {
                        // Thread will sleep for 5 seconds
                        //  sleep(30 * 1000);
                        Double distance = SphericalUtil.computeDistanceBetween(last_latLng, current_latLng);
                        if (distance > 50) {
                            saveAndUpdateRealtimeLocation(location.getLatitude(), location.getLongitude());

                            //updateLocationToServer(current_latLng);
                        }
                    } catch (Exception e) {
                        Log.d(getClass().getSimpleName(), e.getMessage());
                    }
                }
            };
            // start thread
            background.start();
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    //-------------------Firebase Helper methods------
    private void saveAndUpdateRealtimeLocation(double lat, double lng) {
//        userLocation.setCurrentLatLng(new LatLng(lat, lng));
//        SessionManager.getInstance().saveUserLocation(RideActivity.this, userLocation);
////                    updateLocationToServer(userLocation.getCurrentLatLng());
//        updateLocationToRealTimeDB(userLocation.getCurrentLatLng());
    }

    @Override
    public void onViewClick() {
        Queue<RiderInfoModel> riders = PoolRideManager.getInstance().getRiders();
        if (!riders.isEmpty()) {
            //Pool Ride
            showRideAcceptRejectView();

        } else {
            try {
                if (currentDriverStatus == AppConstants.DriverLocationStatus.IDLE) {
                    showRideAcceptRejectView();
                } else {
                    showAlertDialog(R.layout.dialog_network_error);
                    if (mAlertDialog != null) {
                        TextView title = mAlertDialog.findViewById(R.id.tv_title);
                        title.setText(getString(R.string.are_you_sure_you_want_to));
                        Button btnOk = mAlertDialog.findViewById(R.id.btn_ok);
                        btnOk.setText(getString(R.string.ok));
                        btnOk.setOnClickListener(v -> {
                            hideAlertDialog();
                            showRideAcceptRejectView();
                        });
                    }

                }

                currentDriverStatus = AppConstants.DriverLocationStatus.VIEW_RIDE;

            } catch (Exception e) {
                Log.e("Exception: ", e.getMessage());
            }
        }

    }

    @Override
    public void onCancelClick() {

    }

    @Override
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i < route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);

        }
    }

    @Override
    public void onRoutingCancelled() {

    }
}
