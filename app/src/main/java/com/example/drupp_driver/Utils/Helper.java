package com.example.drupp_driver.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

import androidx.core.content.ContextCompat;

import com.example.drupp_driver.FireBase.MyFirebaseMessagingService;
import com.example.drupp_driver.Models.NavigationItemModel;
import com.example.drupp_driver.R;
import com.example.drupp_driver.helpers.AppConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Helper {
    private static Helper helper;
    private Context context;

    private Helper(Context context) {
        this.context = context;
    }


    public static ArrayList<String> getVehicleTypes(Context context) {
        ArrayList<String> categories = new ArrayList<>();

        categories.add(context.getString(R.string.without_ac));
        categories.add(context.getString(R.string.with_ac));

        return categories;
    }

    public static ArrayList<String> getDriverType(Context context) {
        return new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.driver_types)));
    }
    public static ArrayList<String> getCarNames(Context context) {
        return new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.car_names)));
    }
    public static ArrayList<String> getCarModels(Context context) {
        return new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.car_models)));
    }
    public static ArrayList<String> getCarYear(Context context) {
        return new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.year)));
    }


    public static ArrayList<String> getMaritalStatus(Context context) {
        return new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.marital_statuses)));
    }

    public static ArrayList<String> getCarCondition(Context context) {
        return new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.car_condition)));
    }

    public static ArrayList<NavigationItemModel> getNavigationItems(Context context) {
        ArrayList<NavigationItemModel> mList = new ArrayList<>();

        //mList.add(new NavigationItemModel(context.getString(R.string.profile), R.drawable.ic_profile, 12));
        mList.add(new NavigationItemModel(context.getString(R.string.dashboard), R.drawable.ic_dashboard_black_24dp, 13));
        mList.add(new NavigationItemModel(context.getString(R.string.notification), R.drawable.ic_alarm, 4));
        mList.add(new NavigationItemModel(context.getString(R.string.scheduled_rides), R.drawable.ic_calendar_vector, 1));
        mList.add(new NavigationItemModel(context.getString(R.string.trip_history), R.drawable.ic_time, 2));
        mList.add(new NavigationItemModel(context.getString(R.string.post_a_ride), R.drawable.ic_post_office, 3));
        mList.add(new NavigationItemModel(context.getString(R.string.wallet), R.drawable.ic_credit_card, 5));
        mList.add(new NavigationItemModel(context.getString(R.string.settings), R.drawable.ic_settings, 6));
        mList.add(new NavigationItemModel(context.getString(R.string.support), R.drawable.ic_support, 8));
        mList.add(new NavigationItemModel(context.getString(R.string.legal), R.drawable.ic_script_xml, 9));
        mList.add(new NavigationItemModel(context.getString(R.string.logout), R.drawable.ic_logout, 7));
        return mList;
    }


    public static void savePostRideId(int rideId, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor shEditor = sharedPreferences.edit();
        shEditor.putInt(AppConstants.K_POST_RIDE_ID, rideId);
        shEditor.apply();
    }

    public static int getPostRideId(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(AppConstants.K_POST_RIDE_ID, 0);
    }

    public static void saveRideNotification(HashMap<String, String> rideNotification, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AppConstants.F_RIDE_NOTIFICATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor shEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String data = gson.toJson(rideNotification);
        shEditor.putString(AppConstants.K_NOTIFICATION_RIDE, data);
        shEditor.apply();
    }


    public static void saveNotificationCount(int count, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(AppConstants.K_NOTIFICATION_COUNT, count);
        editor.apply();
    }

    public static int getNotificationCount(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(AppConstants.K_NOTIFICATION_COUNT, 0);
    }

    public static void saveSignUpState(int state, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(AppConstants.K_SIGN_UP_STATE, state);
        editor.apply();
    }

    public static void saveRideType(int type, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(AppConstants.K_TYPE, type);
        editor.apply();
    }

    public static int getRideType(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(AppConstants.K_RIDE_TYPE, 0);
    }
    public static void savePaymentOption(int paymentOption, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(AppConstants.K_PAYMENT_OPTION, paymentOption);
        editor.apply();
    }
    public static int getPaymentOption(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(AppConstants.K_PAYMENT_OPTION, 0);
    }

    public static int getSignUpState(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(AppConstants.K_SIGN_UP_STATE, -1);
    }

    public static void saveRideId(String id, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AppConstants.K_RIDE_ID, id);
        editor.apply();
    }

    public static String getRideId(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(AppConstants.K_RIDE_ID, "0");
    }

    public static void saveCurrentRiderId(String id, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AppConstants.K_RIDER_ID, id);
        editor.apply();
    }

    public static String getCurrentRiderId(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(AppConstants.K_RIDER_ID, "0");
    }

    public static HashMap<String, String> getRideNotification(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AppConstants.F_RIDE_NOTIFICATION, Context.MODE_PRIVATE);
        String data = sharedPreferences.getString(AppConstants.K_NOTIFICATION_RIDE, "");
        Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        Gson gson = new Gson();
        return gson.fromJson(data, type);

    }



    public <T> void writeToJson(String fileName, T data) {
        Gson gson = new Gson();
        String s = gson.toJson(data);

        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(s.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public <T> T readFromJson(String fileName, Class<T> type) {
        FileInputStream fis;
        try {
            fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            String json = sb.toString();
            Gson gson = new Gson();
            return gson.fromJson(json, type);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Helper getInstance(Context context) {
        if (helper == null) {
            helper = new Helper(context);
        }
        synchronized (helper) {
            return helper;
        }
    }


    public Integer getAge(String dobString) {

        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            date = sdf.parse(dobString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date == null) return 0;

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.setTime(date);

        int year = dob.get(Calendar.YEAR);
        int month = dob.get(Calendar.MONTH);
        int day = dob.get(Calendar.DAY_OF_MONTH);

        dob.set(year, month + 1, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }


        return age;
    }


    public static boolean checkPermissions(Context context, String[] permissions) {
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(context, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static void requestPermissions(Context context, String[] permissions) {
        //       ActivityCompat.requestPermissions((Activity) context, permissions, AppConstant.REQUESTS.USER_PERMISSION);
    }

}
