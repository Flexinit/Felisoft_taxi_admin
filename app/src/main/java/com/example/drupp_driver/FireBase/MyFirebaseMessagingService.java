package com.example.drupp_driver.FireBase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.drupp_driver.Models.PopState;
import com.example.drupp_driver.Models.PostRideInfo;
import com.example.drupp_driver.Models.RideAction;
import com.example.drupp_driver.Models.RiderInfo;
import com.example.drupp_driver.Models.RiderInfoModel;
import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.Helper;
import com.example.drupp_driver.Utils.PoolRideManager;
import com.example.drupp_driver.Utils.RxBus;
import com.example.drupp_driver.helpers.AppConstants;
import com.example.drupp_driver.helpers.SessionManager;
import com.example.drupp_driver.ui.MainActivity;
import com.example.drupp_driver.ui.RideActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    // For recieving notifications send by firebase
    private static final String TAG = "MyFirebaseMessagingServ";
    private static final String CHANNEL_ID = "1";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData() != null) {
            //For Showing Total Notifications received
            int notificationCount = Helper.getNotificationCount(this);
            notificationCount++;

            String notificationTitle = remoteMessage.getData().get(AppConstants.K_TITLE);
            String notificationBody = remoteMessage.getData().get(AppConstants.K_BODY);
            try {

                Map<String, String> params = remoteMessage.getData();
                JSONObject jObjects = new JSONObject(params);
                String type = jObjects.getString(AppConstants.K_TYPE);

                switch (type) {
                    case AppConstants.NOTIFICATION.DRIVER_POSTED_RIDE:
                    case AppConstants.NOTIFICATION.USER_POSTED_RIDE: {
                        PopState popState = new PopState();
                        String DCity = jObjects.getString(AppConstants.K_DESTINATION);
                        String SCity = jObjects.getString(AppConstants.K_SOURCE);
                        String name = jObjects.getString(AppConstants.K_NAME);
                        String message = jObjects.getString(AppConstants.K_MESSAGE);
                        String id = jObjects.getString(AppConstants.K_RIDE_ID);
                        String ride_option = jObjects.getString(AppConstants.K_RIDE_OPTION);
                        int typeOfRide = jObjects.getInt(AppConstants.K_RIDE_TYPE);
                        int rideType = jObjects.getInt(AppConstants.K_TYPE);
                        int paymentOption=jObjects.getInt(AppConstants.K_PAYMENT_OPTION);
                        Double averageRatingUser = jObjects.getDouble(AppConstants.K_AVERAGE_RATING);

                        createNotification(notificationTitle, message, id, SCity, DCity, name);



                        popState.setStateType(1);
                        if (ride_option.equals("2")) {
                            popState.setStateType(2);

                        }

                        List<RiderInfo> details = SessionManager.getInstance().getRiderInfos();
                        details.add(new Gson().fromJson(String.valueOf(jObjects), RiderInfo.class));

                        SessionManager.getInstance().saveRidersInfo(getApplicationContext(), details);
                        Log.d(TAG,"saved ridersInfo");
                        SessionManager.getInstance().savePopState(getApplicationContext(), popState);
                        SessionManager.getInstance().saveCurrentRideInfo(getApplicationContext(),jObjects);
                        Log.d(TAG,"saved Popstate");
                        //Save this Notification
                        HashMap<String, String> newMap = new HashMap<>();
                        newMap.put(AppConstants.K_SOURCE, jObjects.getString(AppConstants.K_SOURCE));
                        newMap.put(AppConstants.K_DESTINATION, jObjects.getString(AppConstants.K_DESTINATION));
                        newMap.put(AppConstants.K_NAME, jObjects.getString(AppConstants.K_NAME));
                        newMap.put(AppConstants.K_RIDE_ID, jObjects.getString(AppConstants.K_RIDE_ID));
                        newMap.put(AppConstants.K_RIDE_DATE, jObjects.getString(AppConstants.K_RIDE_DATE));
                        newMap.put(AppConstants.K_AVERAGE_RATING, jObjects.getString(AppConstants.K_AVERAGE_RATING));
                        newMap.put(AppConstants.K_PROFILE_PICTURE,jObjects.getString(AppConstants.K_PROFILE_PICTURE));
                        newMap.put(AppConstants.K_RIDE_OPTION,jObjects.getString(AppConstants.K_RIDE_OPTION));


                        Helper.saveRideNotification(newMap, this);
                        Helper.saveRideType(rideType, this);
                        Helper.savePaymentOption(paymentOption,this);
                        RiderInfoModel riderInfoModel = new RiderInfoModel();
                        riderInfoModel.setPhone(jObjects.getString(AppConstants.K_PHONE));
                        riderInfoModel.setRiderId(jObjects.getString(AppConstants.K_USER_ID));
                        riderInfoModel.setRiderName(name);




                        Helper.getInstance(this).writeToJson(AppConstants.K_RIDER_DETAILS, riderInfoModel);
                        Intent intent = new Intent(AppConstants.K_UPDATE_NOTIFICATION);
                        intent.putExtra(AppConstants.K_RIDE_INFO_MODEL, jObjects.toString());
                        intent.putExtra(AppConstants.K_RIDE_ID, id);
                        intent.putExtra(AppConstants.K_RIDE_TYPE, rideType);
                        intent.putExtra(AppConstants.K_NOTIFICATION_TITLE, notificationTitle);
                        if (ride_option.equalsIgnoreCase("2")) {
                            intent.putExtra(AppConstants.K_NOTIFICATION_TYPE, AppConstants.RIDE_LATER);
                        } else {
                            intent.putExtra(AppConstants.K_NOTIFICATION_TYPE, AppConstants.RIDE_NOW);
                        }
                        //Pool Rider Manager
                        if (typeOfRide == 2) {
                            //Pool Ride
                            PoolRideManager.getInstance().addToQueue(riderInfoModel);
                        }

                        sendBroadcast(intent);
                    }
                    break;
                    case "8": {
                        PopState popState = new PopState();
                        String coRiderPref = jObjects.getString(AppConstants.K_CO_RIDERS_PREFERENCE);
                        String pickUpLocation = jObjects.getString(AppConstants.K_PICK_UP_LOCATION);
                        String preferences = jObjects.getString(AppConstants.K_PREFERENCE);
                        String typeOFDriver = jObjects.getString(AppConstants.K_TYPE_OF_DRIVER);

                        popState.setStateType(6);
                        Log.d(TAG,"saved Popstate corider");
                        createNotificationPostRide(notificationTitle, "Post Ride Acceptance", coRiderPref, pickUpLocation, "", typeOFDriver);
                        SessionManager.getInstance().savePostRideDetails(getApplicationContext(), new Gson().fromJson(String.valueOf(jObjects), PostRideInfo.class));

                        SessionManager.getInstance().savePopState(getApplicationContext(), popState);
                        Intent intent = new Intent(AppConstants.K_UPDATE_NOTIFICATION);

                        intent.putExtra(AppConstants.K_NOTIFICATION_TYPE, AppConstants.POST_RIDE_ACCEPT);
                        sendBroadcast(intent);
                    }
                    break;
                    case "12":
                        String message = jObjects.getString("message");
                        String co_riders_preference = jObjects.getString("co_riders_preference");
                        String pick_up_location = jObjects.getString("pick_up_location");
                        String type_of_driver = jObjects.getString("type_of_driver");
                        String preference = jObjects.getString("preference");
                        Log.d(TAG,"create Notification postride");
                        createNotification(notificationTitle, "Post Ride Acceptance", co_riders_preference, pick_up_location, preference, type_of_driver);

                        Intent intent = new Intent("update_notification");
                        intent.putExtra("co-riders_preference", co_riders_preference);
                        intent.putExtra("pick_up_location", pick_up_location);
                        intent.putExtra("preference", preference);
                        intent.putExtra("type_of_driver", type_of_driver);
                        intent.putExtra("type", type);
                        intent.putExtra(AppConstants.K_NOTIFICATION_TYPE, AppConstants.POST_RIDE_ACCEPT);
                        sendBroadcast(intent);
                        break;
                    case "13":
                        break;
                    case "15":
                        Intent intent15 = new Intent(AppConstants.I_CANCEL_RIDE);
                        intent15.putExtra("type", type);
                        RxBus.getInstance().setIntentEvent(new RideAction(AppConstants.RIDE_STATUS.RIDE_CANCELLED, intent15));
                        //sendBroadcast(intent15);
                        break;
                    case "18":
                        Intent changeDestinationIntent = new Intent(AppConstants.I_CHANGE_DESTINATION);
                        changeDestinationIntent.putExtra(AppConstants.K_DESTINATION, jObjects.getString(AppConstants.K_DESTINATION));
                        changeDestinationIntent.putExtra(AppConstants.K_DESTINATION_LATITUDE, jObjects.getDouble(AppConstants.K_DESTINATION_LATITUDE));
                        changeDestinationIntent.putExtra(AppConstants.K_DESTINATION_LONGITUDE, jObjects.getDouble(AppConstants.K_DESTINATION_LONGITUDE));
                        changeDestinationIntent.putExtra(AppConstants.K_TOTAL_FARE, jObjects.getDouble(AppConstants.K_TOTAL_FARE));

                        RxBus.getInstance().setIntentEvent(new RideAction(AppConstants.RIDE_STATUS.RIDE_CHANGED, changeDestinationIntent));

                        break;

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Helper.saveNotificationCount(notificationCount, this);
        }
    }

    private void createNotification(String title, String body, String id, String SCity, String DCity, String Name) {
        Log.d(TAG,"create notification");
        Class claz = SessionManager.getInstance().getActivity();
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intentz = new Intent(this, MainActivity.class);
        intentz.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        String origin = "myFirebaseMesaggingService";
        intentz.putExtra("show_dialog", "1");
        intentz.putExtra("main", origin);
        intentz.putExtra("id", id);
        intentz.putExtra("name", Name);
        intentz.putExtra("scity", SCity);
        intentz.putExtra("birth", origin);
        intentz.putExtra("dcity", DCity);


        PendingIntent pendingIntentz = PendingIntent.getActivity(this, 0, intentz, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_account)
                .setContentTitle(title)
                .setSound(uri)
                .setAutoCancel(true)
                .setVibrate(new long[]{0, 100, 200, 300})
                .setStyle(new NotificationCompat.BigTextStyle().bigText(Name + "\n" + SCity + " to " + DCity+".")
                                        .setSummaryText("Ride posted")
                                        .setBigContentTitle(title))
                .setContentIntent(pendingIntentz)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);




        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel mChannel = new NotificationChannel("1", "ch__name", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(mChannel);
            notificationManager.notify(title, 001, builder.build());
        } else {

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(title, 001, builder.build());
        }
    }


    private void createNotificationPostRide(String title, String body, String id, String SCity, String DCity, String Name) {

        Class claz = SessionManager.getInstance().getActivity();

        Intent intentx = new Intent(this, RideActivity.class);
        intentx.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        intentx.putExtra("show_dialog", "1");
        intentx.putExtra("id", id);
        intentx.putExtra("name", Name);
        intentx.putExtra("scity", SCity);
        intentx.putExtra("dcity", DCity);

        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intentx, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_account)
                .setContentTitle("New Ride")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(Name + "\n" + SCity))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel mChannel = new NotificationChannel("1", "ch__name", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(mChannel);
            notificationManager.notify(001, builder.build());
        } else {

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(001, builder.build());
        }
    }

    public static Intent newLauncherIntent(final Context context) {
        final Intent intent = new Intent(context, RideActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        return intent;
    }
}
