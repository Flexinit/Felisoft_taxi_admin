package com.example.drupp_driver.Utils;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.example.drupp_driver.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CommonUtils {

    private static final String TAG = "CommonUtils";

    private CommonUtils() {
        // This utility class is not publicly instantiable
    }

    public static ProgressDialog showLoadingDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }

//    public static AlertDialog showAlertDialog(Context context) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context)
//                .setMessage(context.getString(R.string.delete_history))
//                .setTitle(context.getString(R.string.delete_title))
//                .setCancelable(true);
//        return builder.create();
//
//    }

    public static AlertDialog showCustomAlertDialog(Context context, @LayoutRes int view) {
        View alertLayout = LayoutInflater.from(context).inflate(view, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setCancelable(true);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setView(alertLayout);
        alertLayout.findViewById(R.id.btn_cancel).setOnClickListener(v -> alertDialog.dismiss());
        return alertDialog;
    }

    public static AlertDialog showDialogProgressBar(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setView(view);
        return builder.create();
    }

    @SuppressLint("all")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static boolean isEmailValid(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static int getResourceColor(Context context, @ColorRes int id) {
        return ContextCompat.getColor(context, id);
    }

    public static boolean isMobileNumber(String email) {
        Pattern pattern;
        Matcher matcher;
        final String MOBILE_NUMBER_PATTERN = "^[0-9]{6,14}$";
        pattern = Pattern.compile(MOBILE_NUMBER_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}