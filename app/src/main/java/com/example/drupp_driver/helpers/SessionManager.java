package com.example.drupp_driver.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.drupp_driver.Models.AccessToken;
import com.example.drupp_driver.Models.FireBaseToken;
import com.example.drupp_driver.Models.PopState;
import com.example.drupp_driver.Models.PostRideInfo;
import com.example.drupp_driver.Models.RiderInfo;
import com.example.drupp_driver.Models.UserDetails;
import com.example.drupp_driver.Models.UserLocation;
import com.example.drupp_driver.Models.UserModel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.drupp_driver.helpers.AppConstants.K_FIREBASE_TOKEN;

public class SessionManager {


    private static SessionManager sessionManager;
    private static AccessToken accessToken;

    private final String USER_DATA = "user_data";
    private static final String FULL_USER_DETAIL = "full_user_detail";
    private final String USER_DETAIL = "user_detail";
    private final String USER_POP_STATE = "user_pop_state";
    private final String USER_RIDER_DETAILS = "user_rider_details";
    private final String USER_NOTIFICATION = "user_notification";
    private final String USER_POSTED_RIDE_DETAILS = "user_posted_ride_details";
    private final String CURRENT_RIDE_DETAILS="current ride details";
    private final String USER_LOCATION = "user_location";
    private final String USER_LOGIN_STATE = "login_state";

    public static String RIDERS_DATA = "riders_data";
    private final String RIDERS_DETAIL = "riders_detail";

    private PostRideInfo postRideInfo;
    private PopState popState;
    private UserModel userModel;
    private List<RiderInfo> riderInfos;
    private UserLocation userLocation;

    private Class claz;
    private UserDetails userDetails;

    private SessionManager() {
        if (sessionManager != null) {
            throw new RuntimeException("Use getInstance() method to get Single Instance");
        }
    }

    public static SessionManager getInstance() {
        if (sessionManager == null) {
            sessionManager = new SessionManager();
        }
        return sessionManager;
    }

    public static AccessToken getAccessToken() {
        return accessToken;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public void saveActivity(Class activity) {
        this.claz = activity;
    }

    public Class getActivity() {
        return claz;
    }

    public PopState getPopState() {

            return popState;


    }

    public void setPopState(PopState popState) {
        this.popState = popState;
    }

    public List<RiderInfo> getRiderInfos() {
        if (riderInfos == null) riderInfos = new ArrayList<>();
        return riderInfos;
    }

    public void setRiderInfos(List<RiderInfo> riderInfos) {
        this.riderInfos = riderInfos;
    }

    public PostRideInfo getPostRideInfo() {
        return postRideInfo;
    }

    public void setPostRideInfo(PostRideInfo postRideInfo) {
        this.postRideInfo = postRideInfo;
    }

    public void saveUser(Context context, UserModel userModel) {
        this.userModel = userModel;
        this.setAccessToken(userModel.getToken());
        SharedPreferences preferences = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String json = new Gson().toJson(userModel);
        editor.putString(USER_DETAIL, json);
        editor.apply();
    }

    public void saveUserDetails(Context context, UserDetails userDetails) {
        this.userDetails = userDetails;
        this.setAccessToken(userDetails.getToken());
        SharedPreferences preferences = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String json = new Gson().toJson(userDetails);
        editor.putString(FULL_USER_DETAIL, json);
        editor.apply();
    }

    public UserDetails loadUserDetails(Context context) {
        try {
            SharedPreferences preferences = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
            String userJson = preferences.getString(FULL_USER_DETAIL, null);
            if (userJson != null) {
                UserDetails userDetails = new Gson().fromJson(userJson, UserDetails.class);
                if (userModel != null) {
                    this.setAccessToken(userDetails.getToken());
                    this.userDetails = userDetails;
                    return userDetails;
                }
            }
        } catch (Exception e) {
            Log.e("exception: ", e.getMessage());
        }
        return null;
    }

    public void savePopState(Context context, PopState popState) {
        Log.d("SessionManager","Popstate saved");
        this.popState = popState;
        SharedPreferences preferences = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String json = new Gson().toJson(popState);
        editor.putString(USER_POP_STATE, json);
        editor.apply();
    }

    public void savePostRideDetails(Context context, PostRideInfo postRideInfo) {
        this.postRideInfo = postRideInfo;
        SharedPreferences preferences = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String json = new Gson().toJson(postRideInfo);

        editor.putString(USER_POSTED_RIDE_DETAILS, json);
        editor.apply();
    }
    public void saveCurrentRideInfo(Context context, JSONObject object) {
        SharedPreferences preferences = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String json = object.toString();
        editor.putString(CURRENT_RIDE_DETAILS, json);
        editor.apply();
        Log.d("Session Manager","saved rider info:"+object.toString().substring(0,20));
    }

    public void saveRidersInfo(Context context, List<RiderInfo> riderInfo) {
        this.riderInfos = riderInfo;
        if(riderInfo==null){
            Log.d("Session Manager","RideInfo null");
        }
        Log.d("Session manager","saved ridersInfo");
        SharedPreferences preferences = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String json = new Gson().toJson(riderInfo);
        editor.putString(USER_RIDER_DETAILS, json);
        editor.apply();

    }

    public void saveNotification(Context context, String saveRider) {
        SharedPreferences preferences = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_NOTIFICATION, saveRider);
        editor.apply();
    }

    public void saveUserLocation(Context context, UserLocation userLocation) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = new Gson().toJson(userLocation);
        editor.putString("user_location", json);
        editor.apply();
    }

    public String getSavedNotification(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
        return preferences.getString(USER_NOTIFICATION, null);
    }

    private void setAccessToken(String accessToken) {
        if (SessionManager.accessToken == null) {
            SessionManager.accessToken = new AccessToken(AppConstants.K_BEARER, accessToken);
        } else {
            SessionManager.accessToken.setAccessToken(accessToken);
        }
    }

    public UserModel loadUser(Context context) {
        try {
            SharedPreferences preferences = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
            String userJson = preferences.getString(USER_DETAIL, null);
            if (userJson != null) {
                UserModel userModel = new Gson().fromJson(userJson, UserModel.class);
                if (userModel != null) {
                    this.setAccessToken(userModel.getToken());
                    this.userModel = userModel;
                    return userModel;
                }
            }
        } catch (Exception e) {
            Log.e("exception: ", e.getMessage());
        }
        return null;
    }

    public PopState loadPopState(Context context) {
        try {
            SharedPreferences preferences = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
            String popState = preferences.getString(USER_POP_STATE, null);
            if (popState != null) {
                PopState popStatus = new Gson().fromJson(popState, PopState.class);
                if (popStatus != null) {
                    this.popState = popStatus;
                    return popStatus;
                }
            }
        } catch (Exception e) {
            Log.e("exception: ", e.getMessage());
        }
        return null;
    }

    public List<RiderInfo> loadRiderInformation(Context context) {
        try {
            SharedPreferences preferences = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
            String riderInfo = preferences.getString(USER_RIDER_DETAILS, null);
            if (riderInfo != null) {
                List<RiderInfo> ridersInfo = new Gson().fromJson(riderInfo, new TypeToken<List<RiderInfo>>() {
                }.getType());
                if (ridersInfo != null) {
                    this.riderInfos = ridersInfo;
                    return ridersInfo;
                }
            }
        } catch (Exception e) {
            Log.e("exception: ", e.getMessage());
        }
        return null;
    }

    public PostRideInfo loadPostRideInfo(Context context) {
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);

            String json = sharedPref.getString(USER_POSTED_RIDE_DETAILS, null);
            PostRideInfo postRide = new Gson().fromJson(json, PostRideInfo.class);
            if (postRide == null) {
                postRide = new PostRideInfo();
            }
            this.postRideInfo = postRide;
            return postRide;
        } catch (Exception e) {
            return new PostRideInfo();
        }
    }

    public UserLocation loadUserLocation(Context context) {
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);

            String json = sharedPref.getString(USER_LOCATION, null);
            UserLocation userLocation = new Gson().fromJson(json, UserLocation.class);
            if (userLocation == null) {
                userLocation = new UserLocation();
            }
            this.userLocation = userLocation;
            return userLocation;
        } catch (Exception e) {
            return new UserLocation();
        }
    }
    public String loadCurrentRideDetails(Context context) {

            SharedPreferences preferences = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
            return preferences.getString(CURRENT_RIDE_DETAILS, null);


    }

    public void removeUserData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(USER_DETAIL);
        editor.apply();
    }

    public void removeUserDetails(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(FULL_USER_DETAIL);
        editor.apply();
    }

    public void removeFireBaseToken(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(K_FIREBASE_TOKEN);
        editor.apply();
    }

    public void removeLocations(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(USER_LOCATION);
        editor.apply();
        userLocation = new UserLocation();
    }

    public void removePopState(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(USER_POP_STATE);
        editor.apply();
        popState=null;
   //     popState = new PopState();
    }

    public void removeRidersInfo(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(USER_RIDER_DETAILS);
        editor.apply();
        riderInfos = new ArrayList<>();
    }
    public void removeCurrentRideInfo(Context context) {

        SharedPreferences preferences = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(CURRENT_RIDE_DETAILS);
        editor.apply();
    }

    public void removeNotification(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(USER_NOTIFICATION);
        editor.apply();
    }

    public UserLocation getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(UserLocation userLocation) {
        this.userLocation = userLocation;
    }

    public void saveFireBaseToken(Context context, FireBaseToken fireBaseToken) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        String json = new Gson().toJson(fireBaseToken);
        editor.putString(K_FIREBASE_TOKEN, json);
        editor.apply();
    }

    public FireBaseToken loadFireBaseToken(Context context) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String userJson = preferences.getString(K_FIREBASE_TOKEN, null);
            if (userJson != null) {
                FireBaseToken fireBaseToken = new Gson().fromJson(userJson, FireBaseToken.class);
                if (fireBaseToken != null) {
                    return fireBaseToken;
                }
            }
        } catch (Exception e) {
            Log.e("exception: ", e.getMessage());
        }
        return new FireBaseToken("", "");
    }
}