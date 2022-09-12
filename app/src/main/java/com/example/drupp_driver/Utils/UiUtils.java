package com.example.drupp_driver.Utils;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drupp_driver.DruppDriverApp;
import com.example.drupp_driver.R;

import java.util.Calendar;

public class UiUtils {

    public static void showToast(String msg) {
        Toast.makeText(DruppDriverApp.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(String msg) {
        Toast.makeText(DruppDriverApp.getContext(), msg, Toast.LENGTH_LONG).show();
    }

    @SuppressLint("SetTextI18n")
    public static void selectExpiry(TextView textView) {
        Context context = textView.getContext();
        Calendar cal = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(
                context,
                R.style.CustomDatePickerDialogTheme,
                (datePicker, year, month, day) -> textView.setText(
                        day + "/" + month + "/" + year
                ),
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
        );
        dialog.getDatePicker().setMinDate(System.currentTimeMillis());
        dialog.show();
    }

}
