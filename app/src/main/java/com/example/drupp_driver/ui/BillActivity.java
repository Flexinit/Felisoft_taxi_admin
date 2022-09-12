package com.example.drupp_driver.ui;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.drupp_driver.Models.RideInfoModel;
import com.example.drupp_driver.Models.RiderInfo;
import com.example.drupp_driver.Models.RiderInfoModel;
import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.Helper;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.INotifyEvent;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.ui.base.BaseActivity;
import com.example.drupp_driver.ui.dialogs.DriverChatDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.gson.Gson;
import com.willy.ratingbar.ScaleRatingBar;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class BillActivity extends BaseActivity implements INotifyEvent {

    private int id, userID;
    private static JSONObject currentRideInfo;
    private TextView mSource, mDestination, mBillAmount;
    private TextView mRiderName, mRiderNumber;
    private CircleImageView riderProfile;
    private ScaleRatingBar riderRating;
    private ImageView btnCall;
    private ImageView btnChat;
    private FragmentTripSummary fragmentTripSummary;
    private Button btnDone;
    String auth = "Bearer " + SessionManager.getInstance().getUserModel().getToken();
    private String name;
    private RideInfoModel rideInfoModel;
    private int currentRideType;

    @Override
    protected void setUp() {

    }

    @Override
    protected boolean isRequireHideKeyboard() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_new);

        mSource = findViewById(R.id.tv_rider_source);
        mDestination = findViewById(R.id.tvDestination);
        mRiderName = findViewById(R.id.tv_rider_name);
        mRiderNumber = findViewById(R.id.tv_driver_number);
        mBillAmount = findViewById(R.id.tvBillAmount_Value);
        riderProfile = findViewById(R.id.rider_image);
        btnCall=findViewById(R.id.btn_call);
        btnChat=findViewById(R.id.btn_chat);
        btnDone = findViewById(R.id.btDone);
        riderRating=findViewById(R.id.rating_bar_rider);
        TextView toolBarTitle=findViewById(R.id.tv_toolbar_title);
        toolBarTitle.setText("End Trip");

        if (getIntent() != null) {

            try {

                id = getIntent().getIntExtra(AppConstants.K_ID, 0);

                userID = Integer.valueOf(getIntent().getStringExtra(AppConstants.K_USER_ID));
                if(getIntent().hasExtra(AppConstants.K_RIDE_INFO_MODEL)){
                    rideInfoModel = (RideInfoModel)getIntent().getSerializableExtra(AppConstants.K_RIDE_INFO_MODEL);
                    name=rideInfoModel.getRiderName();

                }
                else if(getIntent().hasExtra(AppConstants.K_RIDE_INFO)){
                    currentRideInfo = new JSONObject(getIntent().getStringExtra(AppConstants.K_RIDE_INFO));
                    rideInfoModel=new Gson().fromJson(String.valueOf(currentRideInfo), RideInfoModel.class);
                    name = rideInfoModel.getRiderNameOption2();

                }

                int paymentMethod = rideInfoModel.getPaymentOption();
                String fareAmount = rideInfoModel.getTotalFare();
                String destination = rideInfoModel.getDestination();
                String source=rideInfoModel.getSource();


                fragmentTripSummary = new FragmentTripSummary(source, destination, fareAmount, paymentMethod);
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.container, fragmentTripSummary, FragmentTripSummary.class.getSimpleName())
                        .commit();
                mRiderName.setText(name);
                btnCall.setOnClickListener(v -> {
                    Intent intent = null;
                        intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", rideInfoModel.getPhone(), null));

                    startActivity(intent);
                });
                btnChat.setOnClickListener(v -> {
                    DriverChatDialog driverChatDialog = DriverChatDialog.newInstance();
                    driverChatDialog.setiNotifyEvent(this);
                    driverChatDialog.show(getSupportFragmentManager(), DriverChatDialog.class.getSimpleName());

                });
                String sRating = rideInfoModel.getAverageRating() +"f";
                riderRating.setRating(Float.parseFloat(sRating));


                Glide.with(this).load(AppConstants.IMAGE_URL + rideInfoModel.getProfilePicture()).apply(new RequestOptions()
                        .error(R.drawable.ic_user_silhouette)
                        .centerCrop().placeholder(R.drawable.ic_user_silhouette)).into(riderProfile);
                    /*name = currentRideInfo.getString(AppConstants.K_NAME);
                    int paymentMethod = currentRideInfo.getInt(AppConstants.K_PAYMENT_OPTION);
                    String fareAmount = currentRideInfo.getString(AppConstants.K_TOTAL_FARE);
                    String destination = currentRideInfo.getString(AppConstants.K_DESTINATION);
                    String source = currentRideInfo.getString(AppConstants.K_SOURCE);


                    fragmentTripSummary = new FragmentTripSummary(source, destination, fareAmount, paymentMethod);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.container, fragmentTripSummary, FragmentTripSummary.class.getSimpleName())
                            .commit();
                    mRiderName.setText(name);
                    btnCall.setOnClickListener(v -> {
                        Intent intent = null;
                        try {
                            intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", currentRideInfo.getString(AppConstants.K_PHONE), null));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        startActivity(intent);
                    });
                    btnChat.setOnClickListener(v -> {
                        DriverChatDialog driverChatDialog = DriverChatDialog.newInstance();
                        driverChatDialog.setiNotifyEvent(this);
                        driverChatDialog.show(getSupportFragmentManager(), DriverChatDialog.class.getSimpleName());

                    });
                    String sRating = currentRideInfo.getString(AppConstants.K_AVERAGE_RATING) + "f";
                    riderRating.setRating(Float.parseFloat(sRating));


                    Glide.with(this).load(AppConstants.IMAGE_URL + currentRideInfo.getString(AppConstants.K_PROFILE_PICTURE)).apply(new RequestOptions()
                            .error(R.drawable.ic_user_silhouette)
                            .centerCrop().placeholder(R.drawable.ic_user_silhouette)).into(riderProfile);
*/

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        btnDone.setOnClickListener(v -> {

            showDialogCustomerMadePayment();

        });
    }

    private void loadInfoFromRideInfoModel() {

    }

    private void showDialogCustomerMadePayment(){

        AlertDialog alertDialog=new AlertDialog.Builder(this).create();

        View view= getLayoutInflater().inflate(R.layout.custom_alert_dailog,null);
        alertDialog.setView(view);
        TextView textView=view.findViewById(R.id.textView);
        Button buttonYes=view.findViewById(R.id.btYes);
        Button buttonNo=view.findViewById(R.id.btNo);
        textView.setText(R.string.has_client_paid);
        buttonYes.setOnClickListener(v -> {
            alertDialog.dismiss();

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new FragmentRateRider(name,id,userID,auth), FragmentRateRider.class.getSimpleName())
                    .commit();
        });
        buttonNo.setOnClickListener(v -> {
            alertDialog.dismiss();
            Toast.makeText(BillActivity.this,getString(R.string.ensure_client_has_paid),Toast.LENGTH_SHORT).show();
        });

        alertDialog.setCancelable(false);
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        alertDialog.show();
    }

    @Override
    public void onNotificationReceived(String title, String message, int type) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, AppConstants.CHANNEL_ID)
                .setAutoCancel(true)   //Automatically delete the notification
                .setSmallIcon(R.drawable.drupp_logo) //Notification icon
                .setContentTitle(title)
                .setContentText(message)
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

    @Override
    public void onChatReceived(DataSnapshot message, int type) {

    }


}