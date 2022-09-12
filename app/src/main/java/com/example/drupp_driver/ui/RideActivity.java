package com.example.drupp_driver.ui;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.drupp_driver.Models.BaseMessage;
import com.example.drupp_driver.Models.ChatReceivedMessage;
import com.example.drupp_driver.Models.ChatUserMessage;
import com.example.drupp_driver.Models.DriverProfileModel;
import com.example.drupp_driver.Models.FinishFares;
import com.example.drupp_driver.Models.NavigationItemModel;
import com.example.drupp_driver.Models.PopState;
import com.example.drupp_driver.Models.PostRideInfo;
import com.example.drupp_driver.Models.RideAction;
import com.example.drupp_driver.Models.RiderInfo;
import com.example.drupp_driver.Models.RiderInfoModel;
import com.example.drupp_driver.Models.RxMessageEvent;
import com.example.drupp_driver.Models.SingleRideModel;
import com.example.drupp_driver.Models.Token;
import com.example.drupp_driver.Models.UserDetails;
import com.example.drupp_driver.Models.UserLocation;
import com.example.drupp_driver.Models.UserModel;
import com.example.drupp_driver.Models.realtime_models.DriverDetails;
import com.example.drupp_driver.Models.realtime_models.RunningRideInfo;
import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.CountryCodes;
import com.example.drupp_driver.Utils.DatabaseHelper;
import com.example.drupp_driver.Utils.Helper;
import com.example.drupp_driver.Utils.IAdapterItemClickListener;
import com.example.drupp_driver.Utils.MyCustomDialogFragment;
import com.example.drupp_driver.Utils.MyCustomLogOutDailogFragment;
import com.example.drupp_driver.Utils.MyCustomPoolDialogFragment;
import com.example.drupp_driver.Utils.MyCustomPostEditDailogFragment;
import com.example.drupp_driver.Utils.MyCustomRideLaterDailogFragment;
import com.example.drupp_driver.Utils.PoolRideManager;
import com.example.drupp_driver.Utils.RxBus;
import com.example.drupp_driver.adapters.NavigationDrawerAdapter;
import com.example.drupp_driver.connectivity.DruppRequestHandler;
import com.example.drupp_driver.connectivity.http.INetwork;
import com.example.drupp_driver.connectivity.http.INetworkList;
import com.example.drupp_driver.connectivity.http.QualStandardResponse;
import com.example.drupp_driver.connectivity.http.QualStandardResponseList;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.BadgeDrawable;
import com.example.drupp_driver.helpers.ILogOutDialogObserver;
import com.example.drupp_driver.helpers.IMainDialogResponseObserver;
import com.example.drupp_driver.helpers.INotifyEvent;
import com.example.drupp_driver.helpers.MapsHelper;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.helpers.UIHelper;
import com.example.drupp_driver.service.NotificationService;
import com.example.drupp_driver.ui.base.MainBaseActivity;
import com.example.drupp_driver.ui.bookride.BusTripActivity;
import com.example.drupp_driver.ui.completedrides.TripHistoryNew;
import com.example.drupp_driver.ui.dashboard.DashboardActivityNew;
import com.example.drupp_driver.ui.dialogs.CodeVerificationDialog;
import com.example.drupp_driver.ui.dialogs.DriverChatDialog;
import com.example.drupp_driver.ui.dialogs.RideRequestDialog;
import com.example.drupp_driver.ui.payment.PaymentActivity;
import com.example.drupp_driver.ui.support.SupportActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.maps.android.SphericalUtil;
import com.neovisionaries.i18n.CountryCode;
import com.sachinvarma.easylocation.EasyLocationInit;
import com.sachinvarma.easylocation.event.Event;
import com.sachinvarma.easylocation.event.LocationEvent;
import com.txusballesteros.bubbles.BubbleLayout;
import com.txusballesteros.bubbles.BubblesManager;
import com.willy.ratingbar.ScaleRatingBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class RideActivity extends MainBaseActivity
        implements OnMapReadyCallback, RoutingListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, MyCustomDialogFragment.onCustomDialogListener, MyCustomPoolDialogFragment.onActionPostRide
        , IAdapterItemClickListener, IMainDialogResponseObserver, ILogOutDialogObserver, INotifyEvent, LocationListener {


    private String TAG = RideActivity.class.getSimpleName();
    private LinearLayout dyanmic_view;
    private LinearLayout rl;
    private FloatingActionButton fabBus;
    private ImageView btnChat;
    private RecyclerView navigationRecyclerView;
    //---------------------Globals--------------------------
    private UserDetails userDetails;
    private Double toLatitude, toLongitude;
    private NotificationService notificationService;
    private AppConstants.DriverLocationStatus currentDriverLocationStatus = AppConstants.DriverLocationStatus.IDLE;
    private CodeVerificationDialog codeVerificationDialog;
    private ArrayList<NavigationItemModel> navigationItems;
    private NavigationDrawerAdapter navigationDrawerAdapter;

    SupportMapFragment supportMapFragment;
    public GoogleMap mMap;
    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.primary_dark_material_light};
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient googleApiClient;
    public Button xbut;
    public int x;
    String id;
    String my_user_id;

    DatabaseHelper db;

    public static int rider_count;

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    // integer for permissions results request
    private static final int ALL_PERMISSIONS_RESULT = 1011;
    private LocationRequest locationRequest;
    private static final long UPDATE_INTERVAL = 10000, FASTEST_INTERVAL = 10000; // = 5 seconds
    private Location location;

    private Location currentLoc;
    private Location lastLoc;

    private boolean mapStatus = false;
    String phone;

    private static String my_ride_type;

//    private ScrollView mScrollView;

    MarkerOptions markerOptions = new MarkerOptions();
    Marker marker_current;

    private final int CANCEL_RIDE = 789;
    private final int TIME_INTERVAL = 10000;
    private final int FASTED_TIME_INTERVAL = 10000;

    private static final int VERIFY_CODE = 454;

    private boolean isMarkerShowAtMap = false;
    private UserLocation userLocation;
    private JSONObject currentRideInfo;
    private String currentRideId;
    private SwitchCompat mDriverStatus;
    //private LinearLayout mRideActionView;
    private Button btAccept, btDecline, mStartRide, mCancelRide, mFinishRide;
    private CardView mRiderInfoView;
    private ConstraintLayout mRideStates;
    private CircleImageView mDriverShowProfile,ivRiderImage;
    private TextView mRiderSource, mRiderDestination, mRiderRating, mRiderName, mRNameInRState, mRSourceInRState, mRDestInRState, mDriverName;
    private TextView driverRating, driverName;
    private ImageView btnCall;
    ScaleRatingBar riderRating;
    private MyCustomDialogFragment myCustomDialog;
    private MyCustomPoolDialogFragment dialogPostEditRide;
    private String rideOption = AppConstants.RIDE_OPTION.RIDE_NOW;
    private boolean dialogSavedInstanceState;
    private Disposable rideCancelDisposable;
    private CircleImageView carImage;
    //----------------------View----------------------
    private CircleImageView userImage;
    /*FireBase Database Reference*/
    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private DriverChatDialog driverChatDialog;


    private boolean isBound;
    private Disposable headerUpdateDisposable;
    private BubblesManager bubblesManager;
    private String currentNotificationTag = "";
    private String textReviews;
    // recieve notifications
    private BroadcastReceiver listener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {

                currentRideId = intent.getStringExtra(AppConstants.K_RIDE_ID);
                currentNotificationTag = intent.getStringExtra(AppConstants.K_NOTIFICATION_TITLE);

                currentRideInfo = new JSONObject(intent.getStringExtra(AppConstants.K_RIDE_INFO_MODEL));
                SessionManager.getInstance().saveCurrentRideInfo(context,currentRideInfo);


            } catch (Exception e) {
                    Log.e(RideActivity.class.getSimpleName(),"Error parsing json object");
            }

            handleDriverAvailableState(intent);
        }
    };

    private BroadcastReceiver cancelRideListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            RiderInfo riderInfo = SessionManager.getInstance().getRiderInfos().get(0);
            UserModel userModel = SessionManager.getInstance().getUserModel();
            updateFirebaseDatabaseReferences(DATABASE_REF.RIDE_INFO).child(String.valueOf(riderInfo.getRideId())).removeValue();
            updateFirebaseDatabaseReferences(DATABASE_REF.DRIVERS).child(String.valueOf(userModel.getId())).child(AppConstants.CANCELLED_RIDES).child(String.valueOf(riderInfo.getRideId())).setValue(riderInfo);
            showCancelledDailog();
        }
    };
    private Queue<Marker> markers = new ArrayDeque<>();
    private boolean isArrivedActionComplete;
    private TextView textCityAndCountry;
    private TextView editProfile;
    private ScaleRatingBar ratingBar;
    private String cityAndCountry="";
    private int driverType;
    private int vehicleTypeId;
    private TextView textStatus;
    private ConstraintLayout containerOffline;
    private ImageView ivGo;
    private TextView tvOffline;
    private String countryName;
    private FrameLayout containerSelectRideType;
    private RadioGroup rgRidePreference;
    private TextView textGreetings;
    private RideRequestDialog rideRequestDialog;


    private void handleDriverAvailableState(Intent intent) {
        if (mDriverStatus.isChecked()) {
            PopState popState = SessionManager.getInstance().getPopState();
            if (popState != null) {
                handleDriverAvailableState();
            } else {
                Log.e(TAG,"popstate null");
                if (intent != null && intent.hasExtra(AppConstants.K_NOTIFICATION_TYPE)) {
                    int type = intent.getIntExtra(AppConstants.K_NOTIFICATION_TYPE, 1);
                    switch (type) {
                        case AppConstants.RIDE_NOW:
                            showDialogFragment();
                            Log.d(TAG,"popstate ride now");
                            break;
                        case AppConstants.RIDE_LATER:
                            showDailogFragmentRideLater();
                            break;
                        case AppConstants.POST_RIDE_ACCEPT:
                            showDialogFragmentPostRide();
                            break;
                    }
                }

            }

        }

    }

    public void handleDriverAvailableState() {
        UserDetails userInfo = SessionManager.getInstance().loadUserDetails(this);
        PopState popState = SessionManager.getInstance().getPopState();
        if (popState != null) {
            Log.d(TAG,"popstate not null");
            switch (popState.getStateType()) {
                case 0:
                case 5:

                    if (userInfo.getDriverType() == AppConstants.BUS_DRIVER) {
                        fabBus.show();
                    }
                    mRiderInfoView.setVisibility(View.GONE);
                    mRideStates.setVisibility(View.GONE);
                    containerSelectRideType.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    containerSelectRideType.setVisibility(View.GONE);
                    showDialogFragment();
                    break;
                case 2:
                    containerSelectRideType.setVisibility(View.GONE);
                    showDailogFragmentRideLater();
                    break;
                case 3:
                case 4:
                    fabBus.hide();
                    containerSelectRideType.setVisibility(View.GONE);
                    mRiderInfoView.setVisibility(View.GONE);
                    mRideStates.setVisibility(View.VISIBLE);
                    showRideStateData();
                    break;
                case 6:
                    showDialogFragmentPostRide();
                    break;
            }
        }
    }

    //Notification Service
    private ServiceConnection notificationServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            NotificationService.LocalBinder binderBridge = (NotificationService.LocalBinder) service;
            notificationService = binderBridge.getService();
            notificationService.setiNotifyEvent(RideActivity.this);
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
            notificationService = null;


        }
    };

    private void showDailogFragmentRideLater() {
        MyCustomRideLaterDailogFragment dialogFragment = new MyCustomRideLaterDailogFragment();
        dialogFragment.show(getSupportFragmentManager(), "asdf");
        // this.id=id;
        dialogFragment.setCancelable(false);
    }

    // dialog to show when user cancels the ride
    private void showCancelledDailog() {

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_alert_dailog_ride_canceled, null);
        AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = alertDialogbuilder.create();
        alertDialog.setView(alertLayout);
        // alertDialog.setContentView(R.layout.custom_alert_dailog);

        //alertDialog.getWindow().setLayout(1000,900);
        // alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);


        alertDialog.findViewById(R.id.btOk_clicked).setOnClickListener(v -> {
            recreate();
            currentDriverLocationStatus = AppConstants.DriverLocationStatus.IDLE;
            Log.d(TAG, "onClick: ok clicked");

            if (codeVerificationDialog != null) {
                codeVerificationDialog.dismiss();
            }
            try {
                PopState popState = SessionManager.getInstance().getPopState();
                popState.setStateType(0);
                SessionManager.getInstance().savePopState(this, popState);

                mRideStates.setVisibility(View.GONE);
                mRiderInfoView.setVisibility(View.GONE);
                //findViewById(R.id.rel_profile).setVisibility(View.VISIBLE);
                mMap.clear();
                polylines.clear();
                for (Marker marker : markers) {
                    marker.remove();
                }


                findViewById(R.id.btDecline).performClick();
                alertDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        alertDialog.show();
    }

    private synchronized void showDialogFragment() {
        if (myCustomDialog != null) {
            myCustomDialog.dismiss();
        }
        myCustomDialog = new MyCustomDialogFragment();
        myCustomDialog.show(getSupportFragmentManager(), MyCustomDialogFragment.class.getSimpleName());
        myCustomDialog.setOnCustomDialogListener(this);
        myCustomDialog.setCancelable(false);


    }

    //Show casing preferences of rider for ride posted by driver
    private void showDialogFragmentPostRide() {
        dialogPostEditRide = new MyCustomPoolDialogFragment();
        dialogPostEditRide.show(getSupportFragmentManager(), MyCustomPoolDialogFragment.class.getSimpleName());
        dialogPostEditRide.setOnActionPostRide(this);
        dialogPostEditRide.setCancelable(false);
    }

    // dialog to show when user edits the ride
    private void showDialogPostEditRide(String type, String co_rider_pref, String pick_up, String pref, String driver_type) {
        MyCustomPostEditDailogFragment dialogFragmentEdit = new MyCustomPostEditDailogFragment();
        Bundle args = new Bundle();

        args.putString(AppConstants.K_TYPE, type);
        args.putString(AppConstants.K_CO_RIDERS_PREFERENCE, co_rider_pref);
        args.putString(AppConstants.K_PICK_UP_LOCATION, pick_up);
        args.putString(AppConstants.K_PREFERENCE, pref);
        args.putString(AppConstants.K_PICK_UP_LOCATION, pick_up);
        args.putString(AppConstants.K_TYPE_OF_DRIVER, driver_type);

        dialogFragmentEdit.setArguments(args);
        dialogFragmentEdit.setCancelable(false);
        dialogFragmentEdit.show(getSupportFragmentManager(), "post edit");
    }

    // logout dialog
    private void showDialogLogOut() {
        MyCustomLogOutDailogFragment dialogFragment4 = new MyCustomLogOutDailogFragment();
        dialogFragment4.setiLogOutDialogObserver(this);
        dialogFragment4.setCancelable(false);
        dialogFragment4.show(getSupportFragmentManager(), "log out");
    }


    // dialog for cancelling the ride
    private void cancelConfirmDialog(final RiderInfo riderInfo) {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_alert_dailog_cancel_ride, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setView(alertLayout);
        alertDialog.show();

        alertDialog.findViewById(R.id.btYesCancel).setOnClickListener(v -> {
            driverChatDialog = null;
            alertDialog.dismiss();
            Intent intent = new Intent(RideActivity.this, CancelRide.class);
            intent.putExtra(AppConstants.K_ID, riderInfo.getRideId());
            startActivityForResult(intent, CANCEL_RIDE);
        });

        alertDialog.findViewById(R.id.btDontCancel).setOnClickListener(v -> alertDialog.dismiss());
    }

    // showing dialog when emergency icon is clicked
    private void showEmergencyDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_alert_dailog_emergancy, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setView(alertLayout);
        alertDialog.show();
        alertDialog.findViewById(R.id.btOk).setOnClickListener(v -> alertDialog.dismiss());
    }

    @Override
    protected void onStart() {
        super.onStart();
        dialogSavedInstanceState = false;
        // for current location on map
        if (googleApiClient != null) {
            googleApiClient.connect();
            try {
                startLocationUpdates();
            } catch (Exception e) {
                Log.d(TAG, "onStart: start location exception");
            }
        }

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        dialogSavedInstanceState = true;
    }

    @Override
    protected void setUp() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride);

        Toolbar toolbar = findViewById(R.id.profileToolbar);
        setSupportActionBar(toolbar);


        //carImage = findViewById(R.id.image_car);

        bubblesManager = new BubblesManager.Builder(this)
                .setTrashLayout(R.layout.bubble_trash)
                .build();
        bubblesManager.initialize();

        //FireBase Ref Added
        mDatabase = FirebaseDatabase.getInstance().getReference().child(AppConstants.FIREBASE_DATABASE.USERS);

        dyanmic_view = findViewById(R.id.dyanmic_view);
        fabBus = findViewById(R.id.fab_bus);
        btnChat = findViewById(R.id.btn_chat);
        //mDriverProfileView = findViewById(R.id.rel_profile);
        containerSelectRideType = findViewById(R.id.container_select_ride_type);
        mRiderInfoView = findViewById(R.id.botRel1);
        mDriverShowProfile = findViewById(R.id.profile_image);
        ivRiderImage=findViewById(R.id.iv_rider_image);
        mRideStates = findViewById(R.id.botRel2);
        mRiderSource = findViewById(R.id.tvSourceCity);
        mRiderDestination = findViewById(R.id.tvDestinationCity1);
        mRiderName = findViewById(R.id.tvName1);
        //userImage = findViewById(R.id.user_image);
        mRiderRating = findViewById(R.id.tvRatingUser);
        mRNameInRState = findViewById(R.id.tv_rider_name);
        btnCall = findViewById(R.id.btn_call);
        mRSourceInRState = findViewById(R.id.tv_rider_source);
        mRDestInRState = findViewById(R.id.tvDestination);
        btAccept = findViewById(R.id.btAccept);
        btDecline = findViewById(R.id.btDecline);
        //mRideActionView = findViewById(R.id.lin_bts);
        mStartRide = findViewById(R.id.btStartRide);
        mCancelRide = findViewById(R.id.btCancelRide);
        mFinishRide = findViewById(R.id.btFinishRide);
        driverName = findViewById(R.id.tv_driver_name);
        //driverRating = findViewById(R.id.tv_rating);
        riderRating=findViewById(R.id.rating_bar_rider);
        mDriverStatus =findViewById(R.id.driver_status);
        textStatus = findViewById(R.id.tv_status);
        containerOffline = findViewById(R.id.container_top);
        ivGo = findViewById(R.id.iv_go);
        tvOffline = findViewById(R.id.tv_offline);
        rgRidePreference = findViewById(R.id.rg_ride_preference);
        textGreetings=findViewById(R.id.tv_greetings);

        polylines = new ArrayList<>();


        Intent intent = getIntent();
        String birth = intent.getStringExtra(AppConstants.K_BIRTH);

        UserModel um = SessionManager.getInstance().loadUser(this);
        userDetails = SessionManager.getInstance().loadUserDetails(this);
        if (userDetails != null) {
            try {
                if (userDetails.getDriverType() == AppConstants.BUS_DRIVER) {
                    fabBus.show();
                } else {
                    fabBus.hide();
                }
                driverName.setText(getString(R.string.full_name, userDetails.getFirstName(), userDetails.getLastName()));
                //driverRating.setText(String.valueOf(userDetails.getAverageRating()));
                String countryCode=CountryCodes.country2phone.get("+"+userDetails.getCountryCode());
                countryName=CountryCode.getByCode(countryCode).getName();
            } catch (Exception e) {
                Log.d(getClass().getSimpleName(), "Error");
            }
        }
        //TextView veh_num = findViewById(R.id.tvVeh_Num);
        //TextView veh_name = findViewById(R.id.tvVeh_Name);
        if (um != null) {
            //veh_num.setText(um.getVehichle_number());
            //veh_name.setText(um.getVehichle_name());
            //TextView veh_color = findViewById(R.id.tvVeh_Color);
            //veh_color.setText(um.getColor());
            if(um.getRidePreference()!=null){
                switch (um.getRidePreference()){
                    case AppConstants.K_PREFERENCE_SINGLE_RIDE:
                        rgRidePreference.check(R.id.rb_single_ride);
                    case AppConstants.K_PREFERENCE_POOL_RIDE:
                        rgRidePreference.check(R.id.rb_pool_ride);
                    case AppConstants.K_PREFERENCE_ANY:
                        rgRidePreference.check(R.id.rb_any);
                }
            }
            else{
                rgRidePreference.check(R.id.rb_single_ride);
            }
        }
        setUpGreetings();
        setUpRidePreference();
        navigationRecyclerView = findViewById(R.id.recycle_toolbar);
        navigationRecyclerView.setBackgroundColor(getResources().getColor(R.color.app_theme_color));

        navigationItems = Helper.getNavigationItems(this);
        navigationDrawerAdapter = new NavigationDrawerAdapter(navigationItems, this);
        navigationDrawerAdapter.setiAdapterItemClickListener(this);
        navigationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        navigationRecyclerView.setAdapter(navigationDrawerAdapter);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (slideOffset == 1.0f) {
                    //TODO: GET DRIVER PROFILE
                    manageHeaderContent();

                }
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(true);
        toggle.setToolbarNavigationClickListener(view -> drawer.openDrawer(GravityCompat.START));
        //toggle.setHomeAsUpIndicator(setBadgeCount(this, R.drawable.ic_menu, Helper.getNotificationCount(this)));
        //toggle.setHomeAsUpIndicator(getDrawable(R.drawable.ic_toggle));
        View header = findViewById(R.id.header_navbar);
        mDriverName = header.findViewById(R.id.tv_driver_name);
        textCityAndCountry = header.findViewById(R.id.tv_city_country);
        editProfile = header.findViewById(R.id.tv_EditProfile);
        ratingBar = header.findViewById(R.id.simpleRatingBar);
        String textOffline="You are offline\n Go online to start accepting rides";

        tvOffline.setText(textOffline);
        //Event RxBus to update header
        headerUpdateDisposable = RxBus.getInstance().getIntentEvent()
                .subscribe(o -> {
                    if (o instanceof RxMessageEvent) {
                        RxMessageEvent event = (RxMessageEvent) o;
                        switch (event.getType()) {
                            case AppConstants.RX_EVENT.PROFILE_PIC_UPDATE:
                                Glide.with(this).load(event.getMessage()).apply(new RequestOptions()
                                        .error(R.drawable.ic_account)
                                        .centerCrop().placeholder(R.drawable.ic_account)).into(mDriverShowProfile);

                              /*  Glide.with(this).load(event.getMessage()).apply(new RequestOptions()
                                        .error(R.drawable.ic_account)
                                        .centerCrop().placeholder(R.drawable.ic_account)).into(userImage);*/

                                break;
                            case AppConstants.RX_EVENT.NAME_UPDATE:
                                driverName.setText(event.getMessage());
                                mDriverName.setText(getString(R.string.simple_name, event.getMessage()));

                                break;
                            case AppConstants.RX_EVENT.CITY_UPDATE:
                                    cityAndCountry=event.getMessage()+", "+countryName;
                                    textCityAndCountry.setText(cityAndCountry);


                                break;

                        }
                    }
                });

        header.findViewById(R.id.profile_image).setOnClickListener(v -> {
            Intent intent1 = new Intent(RideActivity.this, ProfileActivity.class);
            startActivity(intent1);
        });

        //this.textCityAndCountry.setText(cityAndCountry);
        editProfile.setOnClickListener(v -> {
            Intent intent1 = new Intent(RideActivity.this, ProfileActivity.class);
            startActivity(intent1);
        });
        ratingBar.setEnabled(false);
        ratingBar.setRating(userDetails.getAverageRating());
        //header.findViewById(R.id.iv_back).setOnClickListener(v -> closeDrawer());

        // driver finishes ride
        findViewById(R.id.btFinishRide).setOnClickListener(v -> {
        });

        //findViewById(R.id.image_Emergency).setOnClickListener(v -> showEmergencyDialog());

        // map initialization
        getDriverProfile();
        if (mMap == null) {
            // initializing and setting up map
            supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.image_Map);
            supportMapFragment.getMapAsync(this);
            supportMapFragment.getMapAsync(googleMap -> {
                googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(22.686386, 75.859929), 10));
            });
        }

        // managing user-permissions (for location,here)
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionsToRequest = permissionsToRequest(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.toArray(
                        new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            }
        }

        // google api client for current location
        googleApiClient = new GoogleApiClient.Builder(this).
                addApi(LocationServices.API).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).build();

        if (googleApiClient != null) {
            googleApiClient.connect();
            try {
                startLocationUpdates();
            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }
        }

        userLocation = SessionManager.getInstance().getUserLocation();
        new EasyLocationInit(RideActivity.this, TIME_INTERVAL, FASTED_TIME_INTERVAL, true);

        handleDriverAvailableState();
        manageHeaderContent();
        fabBus.setOnClickListener(v -> {
            UIHelper.getInstance().switchActivity(this, BusTripActivity.class, null, null, null, false);
        });


        btnChat.setOnClickListener(v -> {
            driverChatDialog = DriverChatDialog.newInstance();
            driverChatDialog.setiNotifyEvent(this);
            driverChatDialog.show(getSupportFragmentManager(), DriverChatDialog.class.getSimpleName());

        });

        if (getIntent() != null) {
            UserLocation userLocation = SessionManager.getInstance().getUserLocation();
            if (userLocation != null) {
                if (userLocation.getCurrentLatLng() != null) {
                    saveAndUpdateRealtimeLocation(userLocation.getCurrentLatLng().latitude, userLocation.getCurrentLatLng().longitude);
                }

            }
        }


        //FireBase Setup

        //For Firebase SignIn
        mFirebaseAuth = FirebaseAuth.getInstance();
        signIntoFirebase();

        //Ride Later Handle
        if (getIntent() != null) {
            if (getIntent().hasExtra(AppConstants.K_RIDE_OPTION)) {
                getSingleRide(Integer.valueOf(getIntent().getStringExtra(AppConstants.K_RIDE_ID)));
            } else {
                rideOption = AppConstants.RIDE_OPTION.RIDE_NOW;

            }
        }

        //Chat Head Service
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            askFloatingPermission();

        }
        getDriverProfile();

        //Listen for ride action events
        rideCancelDisposable = RxBus.getInstance().getIntentEvent()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (o instanceof RideAction) {
                        RideAction rideAction = (RideAction) o;
                        //Show canceled ride
                        if (rideAction.getAction() == AppConstants.RIDE_STATUS.RIDE_CANCELLED) {
                            try {
                                mMap.clear();
                                RiderInfo riderInfo = SessionManager.getInstance().getRiderInfos().get(0);
                                UserModel userModel = SessionManager.getInstance().getUserModel();

                                updateFirebaseDatabaseReferences(DATABASE_REF.RIDE_INFO).child(String.valueOf(riderInfo.getRideId())).removeValue();
                                updateFirebaseDatabaseReferences(DATABASE_REF.DRIVERS).child(String.valueOf(userModel.getId())).child(AppConstants.CANCELLED_RIDES).child(String.valueOf(riderInfo.getRideId())).setValue(riderInfo);
                                showCancelledDailog();

                            } catch (Exception e) {

                            }
                        } else if (rideAction.getAction() == AppConstants.RIDE_STATUS.RIDE_CHANGED) {
                            try {

                                double lat = rideAction.getData().getDoubleExtra(AppConstants.K_DESTINATION_LATITUDE, 0.0f);
                                double lng = rideAction.getData().getDoubleExtra(AppConstants.K_DESTINATION_LONGITUDE, 0.0f);

                                float fare = rideAction.getData().getFloatExtra(AppConstants.K_TOTAL_FARE, 0.0f);
                                String destination = rideAction.getData().getStringExtra(AppConstants.K_DESTINATION);

                                toLatitude = lat;
                                toLongitude = lng;
                                mRDestInRState.setText(destination);

                                getMapRoute(currentLoc.getLatitude(), currentLoc.getLongitude()
                                        , lat, lng);

                            } catch (Exception e) {

                            }

                        }

                    }
                });


    }

    private void setUpGreetings() {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm");
        Date date=new Date();
        String time=simpleDateFormat.format(date);
        int hour=Integer.parseInt(time.substring(0,2));
        if(hour>=4&&hour<=11){
            String greetings=getString(R.string.good_morning);
            textGreetings.setText(greetings);
        }
        else if(hour>=12&&hour<=21){
            String greetings=getString(R.string.good_afternoon);
            textGreetings.setText(greetings);
        }
        else{
          String greetings=getString(R.string.good_night);
            textGreetings.setText(greetings);
        }
    }

    private void setUpRidePreference() {

        rgRidePreference.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                    HashMap<String, String> params = new HashMap<>();
                switch (checkedId){
                    case R.id.rb_single_ride:
                        params.put(AppConstants.K_RIDE_PREFERENCE, AppConstants.K_PREFERENCE_SINGLE_RIDE);
                            editRidePreference(params);

                    case R.id.rb_pool_ride:
                        params.put(AppConstants.K_RIDE_PREFERENCE, AppConstants.K_PREFERENCE_POOL_RIDE);
                        editRidePreference(params);
                    case R.id.rb_any:
                        params.put(AppConstants.K_RIDE_PREFERENCE, AppConstants.K_PREFERENCE_ANY);
                        editRidePreference(params);
                }
            }
        });

    }
    public  void editRidePreference(HashMap<String, String> params){
        showDialogProgressBar();
        UserModel userModel=SessionManager.getInstance().loadUser(RideActivity.this);
        userModel.setRidePreference(params.get(AppConstants.K_RIDE_PREFERENCE));
        SessionManager.getInstance().saveUser(RideActivity.this,userModel);

        showMessage("ride preference successfully updated");
        hideDialogProgressBar();

    }


    private void signIntoFirebase() {
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        if (user != null) {
            //          onSignedInInitialize(user.getDisplayName());
            startService();
        } else {
            //     onSignedOutCleanup();
            mFirebaseAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(getClass().getSimpleName(), "signInAnonymously:success");
                        FirebaseUser user = mFirebaseAuth.getCurrentUser();
//                        onSignedInInitialize(user.getDisplayName());
                        startService();
                        //updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(getClass().getSimpleName(), "signInAnonymously:failure", task.getException());
                        showMessage(R.string.authentication_failed);
//                            updateUI(null);

                    }
                }
            });
        }
    }


    private void manageHeaderContent() {
        UserModel userModel = SessionManager.getInstance().getUserModel();
        UserDetails userDetails = SessionManager.getInstance().loadUserDetails(this);

        if (userModel != null){
            mDriverName.setText(userModel.getName());
        }



        mDriverStatus.setChecked(userModel.getDriverStatus() == 1);
        ivGo.setOnClickListener(v -> mDriverStatus.performClick());
        if (userModel.getDriverStatus() == 1) {
            textStatus.setText("Online");
            containerOffline.setVisibility(View.GONE);
        } else {
            textStatus.setText("Offline");
            containerOffline.setVisibility(View.VISIBLE);
        }
        //TODO : CHANGE DRIVER STATUS
        mDriverStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mDatabase.child(AppConstants.FIREBASE_DATABASE.DRIVERS)
                    .child(String.valueOf(userModel.getId()))
                    .child(AppConstants.FIREBASE_DATABASE.AVAILABILITY).setValue(isChecked ? 1 : 0);
            changeDriverStatus(isChecked ? 1 : 0);
        });
        if (userDetails !=null){
            cityAndCountry=userDetails.getCity()+ ", Nigeria";
            //textCityAndCountry.setText(cityAndCountry);
            ratingBar.setRating(userDetails.getAverageRating());

            try {
                Glide.with(this).load(AppConstants.IMAGE_URL + userDetails.getProfilePicture()).apply(new RequestOptions()
                        .error(R.drawable.ic_account)
                        .centerCrop().placeholder(R.drawable.ic_account)).into(mDriverShowProfile);

                /*Glide.with(this).load(AppConstants.IMAGE_URL + userDetails.getProfilePicture()).apply(new RequestOptions()
                        .error(R.drawable.ic_account)
                        .centerCrop().placeholder(R.drawable.ic_account)).into(userImage);*/
            } catch (Exception e) {

            }
        }



    }


    @Override
    protected void onPause() {
        super.onPause();

        try {
            unregisterReceiver(cancelRideListener);
            unregisterReceiver(listener);
        } catch (IllegalArgumentException e) {

            Log.d(TAG, "onDestroy: unable to uregister");
        }

        // unregister broadcast-receiver for preventing opening dialogs when on other activity
        // removing it may cause app crash
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (Settings.canDrawOverlays(this)) {
//                startFloatingService();
//            } else {
//                askFloatingPermission();
//                showMessage(R.string.you_need_system);
//            }
//        }
        try {

        } catch (IllegalArgumentException e) {

        }
        // stop location updates
        if (googleApiClient != null && googleApiClient.isConnected()) {
//            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.removeUpdates(this);
    }

    private void askFloatingPermission() {
        //If the draw over permission is not available open the settings screen
        //to grant the permission.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {


            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent chatHeadIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(chatHeadIntent, AppConstants.CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        //Listener for Broadcast Manager

        registerReceiver(cancelRideListener, new IntentFilter(AppConstants.I_CANCEL_RIDE));
        registerReceiver(listener, new IntentFilter(AppConstants.K_UPDATE_NOTIFICATION));
        dialogSavedInstanceState = false;


    }

    private boolean isDialogSavedInstanceState() {
        return dialogSavedInstanceState;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //reloading data for pool rides when coming back to activity
        // details are stored in class- Shared Preference with the help of session manager class
        // details are based on data model class- PoolRidersDetails

        if (my_ride_type != null && my_ride_type.equals("2")) {
//            SessionManager.getInstance().loadPoolRider(RideActivity.this);
//            List<PoolRidersDeatils> prd = SessionManager.getInstance().getRidersDetails();
//            rider_count = prd.size();

            if (rider_count == 0) {
                // if there is rider left remove pool ride details and show driver profile
                dyanmic_view.removeAllViews();
                findViewById(R.id.rel_pool).setVisibility(View.GONE);
                containerSelectRideType.setVisibility(View.VISIBLE);
            }
            // if there are any riders on ride, show there details
//                insertRideDetails(prd);
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        rideCancelDisposable.dispose();
        if (isBound) {
            unbindService(notificationServiceConnection);
            isBound = false;
        }


        bubblesManager.recycle();

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.removeUpdates(this);
        headerUpdateDisposable.dispose();
    }


    // requesting users permission
    private ArrayList<String> permissionsToRequest(ArrayList<String> wantedPermissions) {
        ArrayList<String> result = new ArrayList<>();

        for (String perm : wantedPermissions) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    // check if user has already provided permission
    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }

    // check google play services for getting location
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {
                finish();
            }

            return false;
        }

        return true;
    }


    // when user is re-connected to google api client for location
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (location != null) {
            currentLoc = location;
            lastLoc = location;
            final LatLng latLng = new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude());
            if (marker_current != null)
                marker_current.remove();

            if (userDetails.getDriverType() == AppConstants.KEKE_DRIVER) {
                marker_current = mMap.addMarker(markerOptions.position(new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude()))
                        .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_rickshaw)));
            } else {
                marker_current = mMap.addMarker(markerOptions.position(new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude()))
                        .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_car)));
            }

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        }

//        startLocationUpdates();
    }

    private void pointCurrentLocationAtMap(float zoomLevel) {
        if(userLocation==null){
            userLocation = SessionManager.getInstance().getUserLocation();
        }
        if (userLocation.getCurrentLatLng() != null && mMap != null) {
            try {
                mMap.clear();
                isMarkerShowAtMap = true;
                saveAndUpdateRealtimeLocation(userLocation.getCurrentLatLng().latitude, userLocation.getCurrentLatLng().longitude);
                if (userDetails.getDriverType() != null && userDetails.getDriverType() == AppConstants.KEKE_DRIVER) {
                    mMap.addMarker(markerOptions.position(new LatLng(userLocation.getCurrentLatLng().latitude, userLocation.getCurrentLatLng().longitude))
                            .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_rickshaw))
                            .title(getString(R.string.current_location)));
                } else {
                    mMap.addMarker(markerOptions.position(new LatLng(userLocation.getCurrentLatLng().latitude, userLocation.getCurrentLatLng().longitude))
                            .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_car))
                            .title(getString(R.string.current_location)));

                }

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation.getCurrentLatLng(), zoomLevel));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void startLocationUpdates() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You need to enable permissions to display location !", Toast.LENGTH_SHORT).show();
        }
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, AppConstants.LOCATION_UPDATE_TIME_INTERVAL, AppConstants.LOCATION_UPDATE_MIN_DISTANCE, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, AppConstants.LOCATION_UPDATE_TIME_INTERVAL, AppConstants.LOCATION_UPDATE_MIN_DISTANCE, this);

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

    // over-riden method of fuse api
    // when current location of driver is changed
    @Override
    public void onLocationChanged(Location location) {
        currentLoc = location;
        if(location!=null&&marker_current!=null){
            marker_current.setPosition(new LatLng(location.getLatitude(),location.getLatitude()));
        }

        if (location != null && lastLoc != null) {
            if (toLongitude != null && toLatitude != null) {
                getMapRoute(currentLoc.getLatitude(), currentLoc.getLongitude(), toLatitude, toLongitude);
            }

            final LatLng last_latLng = new LatLng(lastLoc.getLatitude(), lastLoc.getLongitude());
            Log.d(TAG, "onLocationChanged: " + "Latitude : " + location.getLatitude() + "\nLongitude : " + location.getLongitude());
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

    private void updateLocationToServer(LatLng latLng) {
        HashMap<String, Double> parse = new HashMap<>();
        parse.put(AppConstants.K_LATITUDE, latLng.latitude);
        parse.put(AppConstants.K_LONGITUDE, latLng.longitude);
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<Token>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<Token>> response) {
                if (response.isSuccessful() && response.body() != null) {

                }
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<Token>> response) {

            }

            @Override
            public void onNullListResponse() {

            }

            @Override
            public void onFailureList(Throwable t) {
                Log.e("onFailureList", "Fail");
            }
        }, SessionManager.getAccessToken()).driverLiveLocation(parse);
    }


    // when map is ready
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mapStatus = true;
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
        mMap.setMyLocationEnabled(true);
        pointCurrentLocationAtMap(15);
    }


    // getting route in map between source and destination
    private void getMapRoute(double sourceLat, double sourceLong, double destLat, double destLong) {

        try {
            final int padding = 300;
            LatLng source = new LatLng(sourceLat, sourceLong);
            LatLng destination = new LatLng(destLat, destLong);
            LatLng currentLocation = new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude());

            mMap.clear();
            markers.add(mMap.addMarker(new MarkerOptions().visible(true).position(source).title(getString(R.string.source)).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_pin_green))));
            markers.add(mMap.addMarker(new MarkerOptions().visible(true).position(destination).title(getString(R.string.source)).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_pin_red))));

            if (userDetails.getDriverType() == AppConstants.KEKE_DRIVER) {
                mMap.addMarker(new MarkerOptions().visible(true).position(currentLocation).title(getString(R.string.source)).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_rickshaw)));
            } else {
                mMap.addMarker(new MarkerOptions().visible(true).position(currentLocation).title(getString(R.string.source)).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_car)));
            }


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

    private void getMapRoute(RiderInfo riderInfo) {
        try {
            final int padding = 300;
            LatLng source = new LatLng(Double.valueOf(riderInfo.getSourceLatitude()), Double.valueOf(riderInfo.getSourceLongitude()));
            LatLng destination = new LatLng(Double.valueOf(riderInfo.getDestinationLatitude()), Double.valueOf(riderInfo.getDestinationLongitude()));
            LatLng currentLocation = new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude());

            mMap.clear();
            markers.add(mMap.addMarker(new MarkerOptions().visible(true).position(source).title(getString(R.string.source)).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_pin_green))));
            markers.add(mMap.addMarker(new MarkerOptions().visible(true).position(destination).title(getString(R.string.source)).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_pin_red))));

            if (userDetails.getDriverType() == AppConstants.KEKE_DRIVER) {
                mMap.addMarker(new MarkerOptions().visible(true).position(currentLocation).title(getString(R.string.source)).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_rickshaw)));
            } else {
                mMap.addMarker(new MarkerOptions().visible(true).position(currentLocation).title(getString(R.string.source)).icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_car)));
            }


            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(currentLocation);
            builder.include(source);
            builder.include(destination);
            final LatLngBounds bounds = builder.build();

            mMap.setOnMapLoadedCallback(() -> mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding)));

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

    // overriden methods for routing map(showing path between map)
    @Override
    public void onRoutingFailure(RouteException e) {
        if (e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.d(TAG, "onRoutingFailure: " + e.getMessage());
        } else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onRoutingFailure: something went wrong");
        }
    }

    @Override
    public void onRoutingStart() {

        Log.d(TAG, "onRoutingStart: called");

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
        Log.d(TAG, "onRoutingCancelled: ");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    startFloatingService();
                } else {
                    askFloatingPermission();
                    showMessage(R.string.you_need_system);
                }
            }
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ride, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            LayoutInflater inflater = getLayoutInflater();
            View alertLayout = inflater.inflate(R.layout.custom_alert_dailog_emergancy, null);
            AlertDialog.Builder alertDialogbuilder = new AlertDialog.Builder(this);
            final AlertDialog alertDialog = alertDialogbuilder.create();
            alertDialog.setView(alertLayout);
            alertDialog.show();
            alertDialog.findViewById(R.id.btOk).setOnClickListener(v -> alertDialog.dismiss());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // requesting user-permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                for (String perm : permissionsToRequest) {
                    if (!hasPermission(perm)) {
                        permissionsRejected.add(perm);
                    }
                }

                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            new AlertDialog.Builder(RideActivity.this).
                                    setMessage("These permissions are mandatory to get your location. You need to allow them.").
                                    setPositiveButton("OK", (dialogInterface, i) -> requestPermissions(permissionsRejected.
                                            toArray(new String[0]), ALL_PERMISSIONS_RESULT)).setNegativeButton("Cancel", null).create().show();

                            return;
                        }
                    }
                } else {
                    if (googleApiClient != null) {
                        googleApiClient.connect();
                    }
                }
                break;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onViewClick() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(currentNotificationTag, 001);

        Queue<RiderInfoModel> riders = PoolRideManager.getInstance().getRiders();
        if (!riders.isEmpty()) {
            //Pool Ride
            showRideAcceptRejectView();

        } else {
            try {
                if (currentDriverLocationStatus == AppConstants.DriverLocationStatus.IDLE) {
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

                currentDriverLocationStatus = AppConstants.DriverLocationStatus.VIEW_RIDE;

            } catch (Exception e) {
                Log.e("Exception: ", e.getMessage());
            }
        }

    }

    @Override
    public void onCancelClick() {
        if (myCustomDialog != null)
            myCustomDialog.dismiss();
        PopState popState=new PopState();
        popState.setStateType(0);
        SessionManager.getInstance().setPopState(popState);
        handleDriverAvailableState();
        //   removeNotificationAndPopStates();
    }

    private void showRideAcceptRejectView() {
        if (myCustomDialog != null) {
            myCustomDialog.dismiss();
        }
        if(rideRequestDialog!=null){
            rideRequestDialog.dismiss();
        }
        textStatus.setVisibility(View.INVISIBLE);
        rideRequestDialog = new RideRequestDialog(this);
        FragmentTransaction transaction=getSupportFragmentManager()
                .beginTransaction()
                .add(rideRequestDialog,RideRequestDialog.class.getSimpleName());
        rideRequestDialog.onDismiss(new DialogInterface() {
            @Override
            public void cancel() {

            }

            @Override
            public void dismiss() {

                textStatus.setVisibility(View.VISIBLE);
            }
        });

        transaction.commit();






        /*List<RiderInfo> ridersInfo = SessionManager.getInstance().getRiderInfos();
        final RiderInfo newAddedRider = ridersInfo.get(ridersInfo.size() - 1);

        mRiderSource.setText(newAddedRider.getSource());
        mRiderDestination.setText(newAddedRider.getDestination());
        mRiderName.setText(newAddedRider.getName());
        mRiderRating.setText(String.valueOf(newAddedRider.getRating()));
        fabBus.hide();
        mRiderInfoView.setVisibility(View.VISIBLE);
        containerSelectRideType.setVisibility(View.VISIBLE);

        toLatitude = Double.valueOf(newAddedRider.getDestinationLatitude());
        toLongitude = Double.valueOf(newAddedRider.getDestinationLongitude());


        getMapRoute(newAddedRider);

        btAccept.setOnClickListener(v -> acceptRide(newAddedRider.getRideId()));

        btDecline.setOnClickListener(v -> {
            currentDriverLocationStatus = AppConstants.DriverLocationStatus.IDLE;
            removeNotificationAndPopStates();
            if (ActivityCompat.checkSelfPermission(RideActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(RideActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (location != null) {
                currentLoc = location;
                lastLoc = location;
                mMap.clear();
                final LatLng latLng = new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude());
                if (marker_current != null)
                    marker_current.remove();

                if (userDetails.getDriverType() == AppConstants.KEKE_DRIVER) {
                    marker_current = mMap.addMarker(markerOptions.position(new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude()))
                            .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_rickshaw))
                            .title("Current Location"));
                } else {
                    marker_current = mMap.addMarker(markerOptions.position(new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude()))
                            .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_car))
                            .title("Current Location"));

                }

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            }
            startLocationUpdates();
        });*/
    }
    public void declineRide(){
        currentDriverLocationStatus = AppConstants.DriverLocationStatus.IDLE;
        removeNotificationAndPopStates();
        if (ActivityCompat.checkSelfPermission(RideActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(RideActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            currentLoc = location;
            lastLoc = location;
            mMap.clear();
            final LatLng latLng = new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude());
            if (marker_current != null)
                marker_current.remove();

            if (userDetails.getDriverType() == AppConstants.KEKE_DRIVER) {
                marker_current = mMap.addMarker(markerOptions.position(new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude()))
                        .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_rickshaw))
                        .title("Current Location"));
            } else {
                marker_current = mMap.addMarker(markerOptions.position(new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude()))
                        .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.ic_car))
                        .title("Current Location"));

            }

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        }
        startLocationUpdates();
    }

    public void acceptRide(String id) {
        try {
            showLoading();
            HashMap<String, Integer> parse = new HashMap<>();
            parse.put(AppConstants.K_STATUS, 2);
            parse.put(AppConstants.K_RIDE_ID, Integer.valueOf(id));
            DruppRequestHandler.clearInstance();
            DruppRequestHandler.getInstance(new INetworkList<Token>() {
                @Override
                public void onResponseList(Response<QualStandardResponseList<Token>> response) {
                    hideLoading();
                    if (response.isSuccessful() && response.body() != null) {
                        mRiderInfoView.setVisibility(View.GONE);
                        PopState popState = SessionManager.getInstance().getPopState();
                        popState.setStateType(3);
                        SessionManager.getInstance().savePopState(RideActivity.this, popState);
                        handleDriverAvailableState();
                        giveInputsForStartRide();
                    }
                }

                @Override
                public void onErrorList(Response<QualStandardResponseList<Token>> response) {
                    hideLoading();
                    showErrorPrompt(response);
                    removeNotificationAndPopStates();
                }

                @Override
                public void onNullListResponse() {
                    hideLoading();
                }

                @Override
                public void onFailureList(Throwable t) {
                    hideLoading();
                    showErrorPrompt(R.string.some_error);
                }
            }, SessionManager.getAccessToken()).driverAcceptRide(parse);
        } catch (Exception e) {
            Log.e("Exception: ", e.getMessage());
        }
    }

    private void removeNotificationAndPopStates() {
        SessionManager.getInstance().removeRidersInfo(RideActivity.this);
        SessionManager.getInstance().removePopState(RideActivity.this);
        handleDriverAvailableState();
    }

    private void showRideStateData() {

            final RiderInfo newAddedRider;
            List<RiderInfo> ridersInfo = SessionManager.getInstance().getRiderInfos();
            HashMap<String,String> map=Helper.getRideNotification(getActivity());
            newAddedRider = ridersInfo.get(ridersInfo.size() - 1);
            mRNameInRState.setText(map.get(AppConstants.K_NAME));
            //Listeners
            //btnCall.setText(newAddedRider.getPhone());
            btnCall.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", newAddedRider.getPhone(), null));
                startActivity(intent);
            });
            Glide.with(this)
                    .load(AppConstants.IMAGE_URL + map.get(AppConstants.K_PROFILE_PICTURE))
                    .apply(new RequestOptions()
                    .error(R.drawable.ic_user_silhouette)
                    .centerCrop()
                    .placeholder(R.drawable.ic_user_silhouette))
                    .into(ivRiderImage);
            riderRating.setRating(Float.parseFloat(map.get(AppConstants.K_AVERAGE_RATING) + "f"));

            mRSourceInRState.setText(map.get(AppConstants.K_SOURCE));
            mRDestInRState.setText(map.get(AppConstants.K_DESTINATION));

            PopState popState = SessionManager.getInstance().getPopState();
            if (popState.getStateType() == 4) {
                mStartRide.setVisibility(View.GONE);
                mCancelRide.setVisibility(View.GONE);
                mFinishRide.setVisibility(View.VISIBLE);
            } else {
                mStartRide.setVisibility(View.VISIBLE);
                mCancelRide.setVisibility(View.VISIBLE);
                mFinishRide.setVisibility(View.GONE);
            }

            mCancelRide.setOnClickListener(v -> cancelConfirmDialog(newAddedRider));

            mStartRide.setOnClickListener(v -> {

                        Bundle bundle = new Bundle();
                        bundle.putString(AppConstants.K_RIDE_ID, newAddedRider.getRideId());
                        //TODO : change this check posted by driver

                        if (!isArrivedActionComplete) {

                            driverArrivedAction(newAddedRider.getRideId(), 0);
                        }

                        codeVerificationDialog = CodeVerificationDialog.newInstance(bundle);
                        codeVerificationDialog.setiMainDialogResponseObserver(this);
                        codeVerificationDialog.show(getSupportFragmentManager(), CodeVerificationDialog.class.getSimpleName());
                        //                        startActivityForResult(new Intent(RideActivity.this, CodeVerificationActivity.class)
//                                .putExtra(AppConstants.K_RIDE_ID, newAddedRider.getRideId()), VERIFY_CODE);

                    }
            );
//                    showCustomDialog(newAddedRider));

            mFinishRide.setOnClickListener(v -> saveFinishRideDetails(newAddedRider));
    }

    private void driverArrivedAction(String rideId, int action) {
        HashMap<String, Object> param = new HashMap<>();
        param.put(AppConstants.K_RIDE_ID, rideId);
        param.put(AppConstants.K_POSTED_BY_DRIVER, action);

        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetworkList<String>() {
            @Override
            public void onResponseList(Response<QualStandardResponseList<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    isArrivedActionComplete = true;
                    showMessage(response.body().getErrorResponse());
                }
            }

            @Override
            public void onErrorList(Response<QualStandardResponseList<String>> response) {
                hideLoading();
                showErrorPrompt(response);
            }

            @Override
            public void onNullListResponse() {
                hideLoading();
            }

            @Override
            public void onFailureList(Throwable t) {
                hideLoading();
            }
        }, SessionManager.getAccessToken()).driverArrivedAction(param);

    }

    private void showCustomDialog(final RiderInfo riderInfo) {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_alert_dailog_arrived_location, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setView(alertLayout);
        alertDialog.setCancelable(false);
        alertDialog.show();

        alertDialog.findViewById(R.id.btStartRide).setOnClickListener(v -> {
            alertDialog.dismiss();
            startActivityForResult(new Intent(RideActivity.this, CodeVerificationActivity.class)
                    .putExtra(AppConstants.K_RIDE_ID, riderInfo.getRideId()), VERIFY_CODE);
        });
    }

    private void startRideNow(RiderInfo riderInfo) {
        try {
            showLoading();
            DruppRequestHandler.clearInstance();
            HashMap<String, Integer> parse = new HashMap<>();
            parse.put(AppConstants.K_STATUS, 3);
            parse.put(AppConstants.K_RIDE_ID, Integer.valueOf(riderInfo.getRideId()));
            DruppRequestHandler.getInstance(new INetworkList<Token>() {
                @Override
                public void onResponseList(Response<QualStandardResponseList<Token>> response) {
                    hideLoading();
                    if (response.isSuccessful() && response.body() != null) {
                        currentDriverLocationStatus = AppConstants.DriverLocationStatus.STARTED;
                        PopState popState = SessionManager.getInstance().getPopState();
                        popState.setStateType(4);
                        SessionManager.getInstance().savePopState(RideActivity.this, popState);
                        handleDriverAvailableState();

                    }
                }

                @Override
                public void onErrorList(Response<QualStandardResponseList<Token>> response) {
                    hideLoading();
                    showErrorPrompt(response);
                    PopState popState = SessionManager.getInstance().getPopState();
                    popState.setStateType(4);
                    SessionManager.getInstance().savePopState(RideActivity.this, popState);
                    handleDriverAvailableState();
                }

                @Override
                public void onNullListResponse() {
                    hideLoading();
                }

                @Override
                public void onFailureList(Throwable t) {
                    hideLoading();
                    showErrorPrompt(R.string.some_thing_went_wrong);
                }
            }, SessionManager.getAccessToken()).driverStartRide(parse);
        } catch (Exception e) {
            Log.e("Exception: ", e.getMessage());
        }
    }

    private void giveInputForStartRideLater(SingleRideModel riderInfo) {
        try {
            currentDriverLocationStatus = AppConstants.DriverLocationStatus.ACCEPTED;
            UserDetails userDetails = SessionManager.getInstance().loadUserDetails(RideActivity.this);
            RunningRideInfo runningRideInfo = new RunningRideInfo(Integer.parseInt(currentRideId.equals("") ? "0" : riderInfo.getId().toString()), riderInfo.getName(), userDetails.getFirstName() + " " + userDetails.getLastName(),
                    Double.parseDouble(riderInfo.getSourceLatitude()), Double.parseDouble(riderInfo.getSourceLongitude()), Double.parseDouble(riderInfo.getDestinationLatitude()), Double.parseDouble(riderInfo.getDestinationLongitude()),
                    userLocation.getCurrentLatLng().latitude, userLocation.getCurrentLatLng().longitude);
            mDatabase.child(AppConstants.FIREBASE_DATABASE.RIDE_INFO).child(currentRideId).setValue(runningRideInfo);


        } catch (Exception e) {
            Log.e("Exception: ", e.getMessage());
        }
    }

    private void giveInputsForStartRide() {
        try {
            currentDriverLocationStatus = AppConstants.DriverLocationStatus.ACCEPTED;
            RiderInfo riderInfo;
            riderInfo = SessionManager.getInstance().getRiderInfos().get(0);
            UserDetails userDetails = SessionManager.getInstance().loadUserDetails(RideActivity.this);
            RunningRideInfo runningRideInfo = new RunningRideInfo(Integer.parseInt(currentRideId.equals("") ? "0" : riderInfo.getRideId()), riderInfo.getName(), userDetails.getFirstName() + " " + userDetails.getLastName(),
                    Double.parseDouble(riderInfo.getSourceLatitude()), Double.parseDouble(riderInfo.getSourceLongitude()), Double.parseDouble(riderInfo.getDestinationLatitude()), Double.parseDouble(riderInfo.getDestinationLongitude()),
                    userLocation.getCurrentLatLng().latitude, userLocation.getCurrentLatLng().longitude);
            mDatabase.child(AppConstants.FIREBASE_DATABASE.RIDE_INFO).child(currentRideId).setValue(runningRideInfo);


        } catch (Exception e) {
            Log.e("Exception: ", e.getMessage());
        }
//        new TimerF().schedule(new TimerTask() {
//            @Override
//            public void run() {
//
//            }
//        }, AppConstants.DELAY_FIREBASE_LOCATION_NOTIFY);


    }

    private void saveFinishRideDetails(final RiderInfo riderInfo) {
        if(riderInfo==null){
            Log.d(TAG,"RideInfo is null");
        }

        showLoading();
        UserModel userModel = SessionManager.getInstance().getUserModel();
        updateFirebaseDatabaseReferences(DATABASE_REF.DRIVERS)
                .child(String.valueOf(userModel.getId()))
                .child(AppConstants.COMPLETED_TRIPS)
                .child(String.valueOf(riderInfo.getRideId()))
                .setValue(riderInfo)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        try {

                            DruppRequestHandler.clearInstance();
                            HashMap<String, Integer> parse = new HashMap<>();
                            parse.put(AppConstants.K_STATUS, 4);
                            parse.put(AppConstants.K_RIDE_ID, Integer.valueOf(riderInfo.getRideId()));
                            DruppRequestHandler.getInstance(new INetwork<FinishFares>() {
                                @Override
                                public void onResponse(Response<QualStandardResponse<FinishFares>> response) {
                                    hideLoading();
                                    if (response.isSuccessful() && response.body() != null) {

                                        mMap.clear();
                                        //removeNotificationAndPopStates();

                                        Intent intent = new Intent(RideActivity.this, BillActivity.class);
                                        if(currentRideInfo==null){
                                           String json=SessionManager.getInstance().loadCurrentRideDetails(RideActivity.this);

                                           intent.putExtra(AppConstants.K_RIDE_INFO, json);
                                        }
                                        else{
                                            intent.putExtra(AppConstants.K_RIDE_INFO, currentRideInfo.toString());
                                        }
                                        Log.d(TAG,"id is:"+riderInfo.getRideId());
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.putExtra(AppConstants.K_ID, Integer.valueOf(riderInfo.getRideId()));
                                        intent.putExtra(AppConstants.K_USER_ID, riderInfo.getUserId());
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onError(Response<QualStandardResponse<FinishFares>> response) {
                                    hideLoading();
                                    showErrorPrompt(response);
                                    if (response.code() == 401) {
                                        mMap.clear();
                                        removeNotificationAndPopStates();
                                    }
                                }

                                @Override
                                public void onNullResponse() {
                                    hideLoading();
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    hideLoading();
                                    showErrorPrompt(R.string.some_thing_went_wrong);
                                }
                            }, SessionManager.getAccessToken()).driverFinishRide(parse);
                        } catch (Exception e) {
                            Log.e("Exception: ", e.getMessage());
                        }
                    }
                    else{
                        showMessage("Something went wrong");
                    }
                });

    }

    private void changeDriverStatus(final int status) {
        try {
            if(status==0){
                textStatus.setText("Offline");
                containerOffline.setVisibility(View.VISIBLE);
            }
            else{
                containerOffline.setVisibility(View.GONE);
                textStatus.setText("Online");
            }
            showLoading();
            DruppRequestHandler.clearInstance();
            HashMap<String, Integer> parse = new HashMap<>();
            parse.put(AppConstants.K_DRIVER_STATUS, status);
            DruppRequestHandler.getInstance(new INetworkList<Token>() {
                @Override
                public void onResponseList(Response<QualStandardResponseList<Token>> response) {
                    hideLoading();
                    if (response.isSuccessful() && response.body() != null) {
                        UserModel userModel = SessionManager.getInstance().getUserModel();
                        userModel.setDriverStatus(status);
                        SessionManager.getInstance().saveUser(RideActivity.this, userModel);
                    }
                }

                @Override
                public void onErrorList(Response<QualStandardResponseList<Token>> response) {
                    hideLoading();
                    showErrorPrompt(response);
                }

                @Override
                public void onNullListResponse() {
                    hideLoading();
                }

                @Override
                public void onFailureList(Throwable t) {
                    hideLoading();
                    showErrorPrompt(R.string.some_thing_went_wrong);
                }
            }, SessionManager.getAccessToken()).managerDriverStatus(parse);
        } catch (Exception e) {
            Log.e("Exception: ", e.getMessage());
        }
    }

    private void startFloatingService() {
        BubbleLayout bubbleView = (BubbleLayout) LayoutInflater
                .from(this).inflate(R.layout.bubble_chat_head_layout, null);
        bubblesManager.addBubble(bubbleView, 60, 20);
        //  startService(new Intent(RideActivity.this, ChatHeadService.class));
        //finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.CODE_DRAW_OVER_OTHER_APP_PERMISSION) {

            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK) {
                startFloatingService();
            } else { //Permission is not available
                Toast.makeText(this,
                        "Draw over other app permission not available. Closing the application",
                        Toast.LENGTH_SHORT).show();
                moveTaskToBack(true);

                //  finish();
            }
        } else if (requestCode == CANCEL_RIDE && resultCode == Activity.RESULT_OK) {

            //If Ride Cancelled
            if (mMap != null) mMap.clear();
            handleDriverAvailableState();
            currentDriverLocationStatus = AppConstants.DriverLocationStatus.IDLE;
        } else if (requestCode == VERIFY_CODE && resultCode == Activity.RESULT_OK) {
            List<RiderInfo> ridersInfo = SessionManager.getInstance().getRiderInfos();
            final RiderInfo newAddedRider = ridersInfo.get(ridersInfo.size() - 1);
            startRideNow(newAddedRider);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getEvent(final Event event) {
        if (event instanceof LocationEvent) {
            if (((LocationEvent) event).location != null) {
                UserLocation userLocation = SessionManager.getInstance().getUserLocation();
                if (userLocation.getCurrentLatLng() == null) {
                    UserLocation location = new UserLocation();
                    location.setCurrentLatLng(new LatLng(((LocationEvent) event).location.getLatitude(), ((LocationEvent) event).location.getLongitude()));
                    SessionManager.getInstance().saveUserLocation(RideActivity.this, location);
                    userLocation = SessionManager.getInstance().getUserLocation();
                }
                /* If Distance Will in 10 will update in Meters. */
                final float[] results = new float[1];
                Location.distanceBetween(userLocation.getCurrentLatLng().latitude, userLocation.getCurrentLatLng().longitude,
                        ((LocationEvent) event).location.getLatitude(), ((LocationEvent) event).location.getLongitude(), results);
                if (results[0] > 10) {

                    saveAndUpdateRealtimeLocation(((LocationEvent) event).location.getLatitude(), ((LocationEvent) event).location.getLongitude());

                    if (!isMarkerShowAtMap) pointCurrentLocationAtMap(15);
                }
            }
        }
    }


    private void saveAndUpdateRealtimeLocation(double lat, double lng) {
        userLocation.setCurrentLatLng(new LatLng(lat, lng));
        SessionManager.getInstance().saveUserLocation(RideActivity.this, userLocation);
//                    updateLocationToServer(userLocation.getCurrentLatLng());
        updateLocationToRealTimeDB(userLocation.getCurrentLatLng());
    }


    private boolean isExistingOrNewUser() {
        return getIntent() != null;

    }

    @Override
    public void onLogout() {
        UserModel userModel = SessionManager.getInstance().getUserModel();
        mDatabase.child(AppConstants.FIREBASE_DATABASE.DRIVERS)
                .child(AppConstants.DRIVER_DETAILS)
                .child(String.valueOf(userModel.getId())).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                Log.d(getClass().getSimpleName(), "Complete");
            }
        });
        updateFirebaseDatabaseReferences(DATABASE_REF.DRIVERS).child(String.valueOf(userModel.getId())).child(AppConstants.FIREBASE_DATABASE.AVAILABILITY).removeValue();

    }

    @Override
    public void onNotificationReceived(String title, String message, int type) {
        sendNotificationChat(title, message);
    }

    @Override
    public void onChatReceived(DataSnapshot dataSnapshot, int type) {

        if (type == AppConstants.CHAT_TYPE.ADMIN_CHAT) {
            AsyncTask.execute(() -> {
                try {
                    Gson gson = new Gson();
                    JsonElement snapshotJsonElement = gson.toJsonTree(dataSnapshot.getValue());

                    ChatUserMessage currentDataSnapshot = dataSnapshot.getValue(ChatUserMessage.class);
                    BaseMessage message;
                    UserModel userInfo = SessionManager.getInstance().getUserModel();

                    if (currentDataSnapshot.getSender_id() == userInfo.getId()) {
                        //This is Driver Sent Message
                        message = gson.fromJson(snapshotJsonElement, ChatUserMessage.class);

                        if (currentDataSnapshot.getIsType().equals(String.valueOf(AppConstants.IS_IMAGE))) {
                            ((ChatUserMessage) message).setIsType(String.valueOf(AppConstants.IS_IMAGE));
                            hideDialogProgressBar();
                        } else {
                            ((ChatUserMessage) message).setIsType(String.valueOf(AppConstants.IS_TEXT));
                        }

                    } else {
                        //This is Admin  Sent Message
                        message = gson.fromJson(snapshotJsonElement, ChatReceivedMessage.class);
                        if (currentDataSnapshot.getIsType().equals(String.valueOf(AppConstants.IS_IMAGE))) {
                            hideDialogProgressBar();
                            ((ChatReceivedMessage) message).setIsType(String.valueOf(AppConstants.IS_IMAGE));

                            onNotificationReceived(AppConstants.NEW_MESSAGE, getString(R.string.received_a_file), AppConstants.CHAT_TYPE.RIDER_CHAT);

                        } else {
                            ((ChatReceivedMessage) message).setIsType(String.valueOf(AppConstants.IS_TEXT));
                            onNotificationReceived(AppConstants.NEW_MESSAGE, ((ChatReceivedMessage) message).getMessage(), AppConstants.CHAT_TYPE.RIDER_CHAT);
                        }

                    }
                    EventBus.getDefault().postSticky(message);
                } catch (Exception e) {

                }


            });


        } else {
            if (driverChatDialog != null) {
                driverChatDialog.onChatReceived(dataSnapshot, type);
            }

        }
    }

    public void sendNotificationChat(String notificationTitle, String notificationBody) {

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, AppConstants.CHANNEL_ID)
                .setAutoCancel(true)   //Automatically delete the notification
                .setSmallIcon(R.drawable.drupp_logo) //Notification icon
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .setSound(defaultSoundUri);


        PendingIntent pendingIntent;
        Intent intentBuyer = new Intent(this, MainActivity.class);
        intentBuyer.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        pendingIntent = PendingIntent.getActivity(this, 0, intentBuyer, 0);
        notificationBuilder.setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel mChannel = new NotificationChannel("1", "ch__name", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(mChannel);
            notificationManager.notify(002, notificationBuilder.build());
        } else {

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(002, notificationBuilder.build());
        }


    }



    public enum DATABASE_REF {
        DRIVERS,
        RIDE_INFO
    }

    private DatabaseReference updateFirebaseDatabaseReferences(DATABASE_REF value) {
        mDatabase = baseDatabaseReference();
        return mDatabase.child(value.toString().toLowerCase());
    }

    private DatabaseReference baseDatabaseReference() {
        return FirebaseDatabase.getInstance().getReference().child(AppConstants.FIREBASE_DATABASE.USERS);
    }

    private void updateLocationToRealTimeDB(LatLng latLng) {

        try {

            switch (currentDriverLocationStatus) {
                case IDLE:
                    UserModel user = SessionManager.getInstance().getUserModel();
                    DriverDetails driver = new DriverDetails(user.getId(), user.getDriverStatus(), userDetails.getDriverType(), user.getName(), latLng.latitude, latLng.longitude);
                    updateFirebaseDatabaseReferences(DATABASE_REF.DRIVERS).child(AppConstants.DRIVER_DETAILS).child(String.valueOf(driver.getId())).setValue(driver);
                    break;
                case ACCEPTED:
                case STARTED:
                    updateFirebaseDatabaseReferences(DATABASE_REF.RIDE_INFO).child(String.valueOf(currentRideId)).child(AppConstants.FIREBASE_DATABASE.C_LATITUDE).setValue(latLng.latitude);
                    updateFirebaseDatabaseReferences(DATABASE_REF.RIDE_INFO).child(String.valueOf(currentRideId)).child(AppConstants.FIREBASE_DATABASE.C_LONGITTUDE).setValue(latLng.longitude);
                    break;
            }

        } catch (Exception e) {
            Log.e("Exception: ", e.getMessage());
        }
    }


    @Override
    public void onAccept() {
        manageRiderPrefState(2, SessionManager.getInstance().getPostRideInfo());
    }

    @Override
    public void onReject() {
        manageRiderPrefState(3, SessionManager.getInstance().getPostRideInfo());
    }

    private void manageRiderPrefState(int status, PostRideInfo postRideInfo) {
        try {
            showLoading();
            HashMap<String, Integer> parse = new HashMap<>();
            parse.put(AppConstants.K_STATUS, status);
            parse.put(AppConstants.K_ID, Integer.valueOf(postRideInfo.getRequestRideId()));
            DruppRequestHandler.clearInstance();
            DruppRequestHandler.getInstance(new INetworkList<Token>() {
                @Override
                public void onResponseList(Response<QualStandardResponseList<Token>> response) {
                    hideLoading();
                    if (response.isSuccessful() && response.body() != null) {
                        if (dialogPostEditRide != null) dialogPostEditRide.dismiss();
                        showErrorPrompt(status == 2 ? R.string.accept_rider_pref_with_post_ride : R.string.reject_rider_pref_with_post_ride);
                    }
                }

                @Override
                public void onErrorList(Response<QualStandardResponseList<Token>> response) {
                    hideLoading();
                    showErrorPrompt(response);
                    if (response.code() == 400) {
                        if (dialogPostEditRide != null) dialogPostEditRide.dismiss();
                        PopState popState = SessionManager.getInstance().getPopState();
                        popState.setStateType(0);
                        SessionManager.getInstance().savePopState(RideActivity.this, popState);
                        handleDriverAvailableState();
                    }
                }

                @Override
                public void onNullListResponse() {
                    hideLoading();
                }

                @Override
                public void onFailureList(Throwable t) {
                    hideLoading();
                    showErrorPrompt(R.string.some_thing_went_wrong);
                }
            }, SessionManager.getAccessToken()).driverAcceptRidePost(parse);
        } catch (Exception e) {
            Log.e("Exception: ", e.getMessage());
        }
    }

    @Override
    public void onItemClick(View v, int position) {
        switch (navigationItems.get(position).getId()) {
            case 1:

                startActivity(new Intent(this, ScheduledRidesActivity.class)
                        .putExtra(ScheduledRidesActivity.DRIVER_TYPE,driverType)
                        .putExtra(ScheduledRidesActivity.VEHICLE_TYPEID,vehicleTypeId));
                break;
            case 2:
                startActivity(new Intent(this, TripHistoryNew.class));
                break;
            case 3:
                startActivity(new Intent(this, PostRideActivity.class));
                break;
            case 4:
                startActivity(new Intent(this, NotificationActivity.class));
                break;
            case 5:
                startActivity(new Intent(this, PaymentActivity.class));
                break;
            case 6:
                startActivity(new Intent(this, UserSettingActivity.class));
                break;
            case 7:
                showDialogLogOut();
                break;
            case 8:
                startActivity(new Intent(this, SupportActivity.class));
                break;
            case 9:
            case 10:
                startActivity(new Intent(this, PrivacyPolicyActivity.class));
                break;
            case 11:
                startActivity(new Intent(this, TermsConditionActivity.class));
                break;
            case 12:
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            case 13:

               startActivity(new Intent(this, DashboardActivityNew.class));
                break;
        }
        closeDrawer();

    }

    private Drawable setBadgeCount(Context context, int res, int badgeCount) {
        LayerDrawable icon = (LayerDrawable) ContextCompat.getDrawable(context, R.drawable.badge_drawable);
        Drawable mainIcon = ContextCompat.getDrawable(context, res);
        BadgeDrawable badge = new BadgeDrawable(context);
        badge.setCount(String.valueOf(badgeCount));
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
        icon.setDrawableByLayerId(R.id.ic_main_icon, mainIcon);

        return icon;
    }

    private void closeDrawer() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


    private void getDriverProfile() {
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<DriverProfileModel>() {
            @Override
            public void onResponse(Response<QualStandardResponse<DriverProfileModel>> response) {
                hideDialogProgressBar();
                if (response.isSuccessful() && response.body() != null) {
                    DriverProfileModel driverProfileModel = response.body().getResponse();
                    try {


                        mDriverName.setText(getString(R.string.full_name, driverProfileModel.getFirstName(), driverProfileModel.getLastName()));
                        driverName.setText(getString(R.string.full_name, driverProfileModel.getFirstName(), driverProfileModel.getLastName()));
                        //driverRating.setText(response.body().getResponse().getAverageRating().toString());
                        cityAndCountry=driverProfileModel.getCity() +", "+ countryName;
                        textCityAndCountry.setText(cityAndCountry);
                        driverType = driverProfileModel.getDriverType();
                        vehicleTypeId = driverProfileModel.getVehicleTypeId();

                    } catch (Exception e) {
                        Log.d(getClass().getSimpleName(), "error");
                    }


                }
            }

            @Override
            public void onError(Response<QualStandardResponse<DriverProfileModel>> response) {
                hideDialogProgressBar();
                showErrorPrompt(response);
            }

            @Override
            public void onNullResponse() {
                hideDialogProgressBar();
            }

            @Override
            public void onFailure(Throwable t) {
                hideDialogProgressBar();
                showAlertDialog(R.layout.dialog_network_error);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            hideAlertDialog();
                            getDriverProfile();
                        }
                    });
                }
            }
        }, SessionManager.getAccessToken()).getDriverProfile();
    }

    public void startService() {

        Intent intent = new Intent(this, NotificationService.class);
        RiderInfoModel riderData = Helper.getInstance(this).readFromJson(AppConstants.K_RIDER_DETAILS, RiderInfoModel.class);

        if (SessionManager.getInstance().getUserModel() != null) {
            UserModel userInfo = SessionManager.getInstance().getUserModel();
            if (riderData != null) {
                intent.putExtra(AppConstants.K_CHAT_ID, riderData.getRiderId() + "_" + userInfo.getId());
            }
        }
        startService(intent);
        bindService(intent, notificationServiceConnection, BIND_AUTO_CREATE);

    }

    @Override
    public void onDialogResult(int result) {
        List<RiderInfo> ridersInfo = SessionManager.getInstance().getRiderInfos();
        final RiderInfo newAddedRider = ridersInfo.get(ridersInfo.size() - 1);
        AlertDialog alertDialog=new AlertDialog.Builder(this).create();

        View view= getLayoutInflater().inflate(R.layout.custom_alert_dailog,null);
        alertDialog.setView(view);
        TextView textView=view.findViewById(R.id.textView);
        Button buttonYes=view.findViewById(R.id.btYes);
        Button buttonNo=view.findViewById(R.id.btNo);
        textView.setText(R.string.open_google_maps);
        buttonYes.setOnClickListener(v -> {
            alertDialog.dismiss();
            if (result == IMainDialogResponseObserver.RESULTOK) {

                startRideNow(newAddedRider);
                MapsHelper.startNavigation(
                        RideActivity.this,
                        newAddedRider.getDestinationLatitude(),
                        newAddedRider.getDestinationLongitude()
                );
            }
        });
        buttonNo.setOnClickListener(v -> {
            alertDialog.dismiss();
            startRideNow(newAddedRider);
        });

       alertDialog.setCancelable(false);
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        alertDialog.show();

    }

    private void getSingleRide(Integer rideId) {
        showDialogProgressBar();
        DruppRequestHandler.clearInstance();
        DruppRequestHandler.getInstance(new INetwork<SingleRideModel>() {
            @Override
            public void onResponse(Response<QualStandardResponse<SingleRideModel>> response) {
                hideDialogProgressBar();
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        SingleRideModel singleRideModel = response.body().getResponse();
                        rideOption = getIntent().getStringExtra(AppConstants.K_RIDE_OPTION);
                        currentRideId = rideId.toString();
                        mRiderInfoView.setVisibility(View.GONE);
                        PopState popState = SessionManager.getInstance().getPopState();
                        if (popState == null) {
                            popState = new PopState();
                        }
                        popState.setStateType(3);
                        SessionManager.getInstance().savePopState(RideActivity.this, popState);

                        List<RiderInfo> ridersInfo = SessionManager.getInstance().getRiderInfos();
                        RiderInfo riderInfo = new RiderInfo();
                        riderInfo.setSource(singleRideModel.getSource());
                        riderInfo.setDestination(singleRideModel.getDestination());
                        riderInfo.setSourceLatitude(singleRideModel.getSourceLatitude());
                        riderInfo.setSourceLongitude(singleRideModel.getSourceLongitude());
                        riderInfo.setDestinationLatitude(singleRideModel.getDestinationLatitude());
                        riderInfo.setDestinationLongitude(singleRideModel.getDestinationLongitude());
                        riderInfo.setName(singleRideModel.getName());
                        riderInfo.setPhone(singleRideModel.getPhone());
                        riderInfo.setRideId(currentRideId);
                        riderInfo.setUserId(singleRideModel.getUserId().toString());
                        riderInfo.setRideDate(singleRideModel.getRideDate());
                        riderInfo.setRideType(singleRideModel.getRideType().toString());
                        riderInfo.setTotalFare(singleRideModel.getTotalFare());
                        riderInfo.setRideOption(singleRideModel.getRideOption().toString());


                        ridersInfo.add(riderInfo);
                        handleDriverAvailableState();
                        giveInputForStartRideLater(response.body().getResponse());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onError(Response<QualStandardResponse<SingleRideModel>> response) {
                hideDialogProgressBar();
                showErrorPrompt(response);
            }

            @Override
            public void onNullResponse() {
                hideDialogProgressBar();

            }

            @Override
            public void onFailure(Throwable t) {
                hideDialogProgressBar();
                showAlertDialog(R.layout.dialog_network_error);
                if (mAlertDialog != null) {
                    mAlertDialog.findViewById(R.id.btn_ok).setOnClickListener(v -> {
                        hideAlertDialog();
                        getSingleRide(rideId);
                    });
                }
            }
        }, SessionManager.getAccessToken()).getSingleScheduledUserRide(rideId);
    }
}
