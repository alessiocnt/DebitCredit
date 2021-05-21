package com.simoale.debitcredit.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Calendar;

public class DatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private TextView dateDisplay;
    private MutableLiveData<Boolean> dataReady = new MutableLiveData<>(false);
    private int year;
    private int month;
    private int day;

    public DatePicker(TextView dateDisplay) {
        this.dateDisplay = dateDisplay;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }


    public void onDateSet(android.widget.DatePicker view, int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.dateDisplay.setText("Date: " + day + "/" + month + "/" + year);
        this.dataReady.setValue(true);
    }

    public LiveData<Boolean> getDataReady() {
        return this.dataReady;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }
}
