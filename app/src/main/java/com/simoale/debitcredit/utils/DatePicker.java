package com.simoale.debitcredit.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Calendar;

public class DatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private MutableLiveData<Boolean> dataReady = new MutableLiveData<>(false);
    private int year;
    private int month;
    private int day;

    public DatePicker(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        // Create a new instance of DatePickerDialog and return it
        return this.year == 0 ? new DatePickerDialog(getActivity(), this, year, month, day) : new DatePickerDialog(getActivity(), this, this.year, this.month, this.day);
    }


    public void onDateSet(android.widget.DatePicker view, int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
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
