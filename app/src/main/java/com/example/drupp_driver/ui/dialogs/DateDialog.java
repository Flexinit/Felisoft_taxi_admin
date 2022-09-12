package com.example.drupp_driver.ui.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.IDateDialogObserver;
import com.example.drupp_driver.helpers.AppConstants;

import java.util.Calendar;

public class DateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private IDateDialogObserver iDateDialogObserver;


    public static DateDialog newInstance(Bundle bundle) {
        DateDialog dateDialog = new DateDialog();
        dateDialog.setArguments(bundle);
        return dateDialog;
    }

    public void setiDateDialogObserver(IDateDialogObserver iDateDialogObserver) {
        this.iDateDialogObserver = iDateDialogObserver;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog;
        // Create a new instance of DatePickerDialog and return it

        datePickerDialog = new DatePickerDialog(getActivity(), R.style.CustomDatePickerDialogTheme, this, year, month, day);


        if (getArguments() != null) {
            if (getArguments().get(AppConstants.K_MIN_DATE) != null) {
                datePickerDialog.getDatePicker().setMinDate(getArguments().getLong(AppConstants.K_MIN_DATE));
            }
            if (getArguments().get(AppConstants.K_MAX_DATE) != null) {
                datePickerDialog.getDatePicker().setMaxDate(getArguments().getLong(AppConstants.K_MAX_DATE));
            }
        }

        return datePickerDialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        iDateDialogObserver.onDateSelected(year, month + 1, dayOfMonth);

    }

}
