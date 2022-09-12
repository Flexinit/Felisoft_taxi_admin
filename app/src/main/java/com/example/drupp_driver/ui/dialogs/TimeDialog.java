package com.example.drupp_driver.ui.dialogs;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.drupp_driver.R;
import com.example.drupp_driver.Utils.ITimeDialogObserver;

import java.util.Calendar;

public class TimeDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private static int hourOfTheDay;
    private static int minuteOfTheDay;

    private ITimeDialogObserver iTimeDialogObserver;

    public static TimeDialog newInstance(int hour, int minute) {
        hourOfTheDay = hour;
        minuteOfTheDay = minute;
        return new TimeDialog();
    }

    public static TimeDialog newInstance() {
        return new TimeDialog();
    }

    public void setiTimeDialogObserver(ITimeDialogObserver iTimeDialogObserver) {
        this.iTimeDialogObserver = iTimeDialogObserver;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), R.style.CustomDatePickerDialogTheme, this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        iTimeDialogObserver.onTimeSelected(hourOfDay, minute);
    }
}
