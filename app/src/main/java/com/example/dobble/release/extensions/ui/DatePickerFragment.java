package com.example.dobble.release.extensions.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.List;

public class DatePickerFragment extends DialogFragment
    implements DatePickerDialog.OnDateSetListener, IValidatable {

    private int year;
    private int month;
    private int day;
    private boolean isValid;
    private List<IValidatableSubscriber> subscribers = new ArrayList<>();

    public DatePickerFragment(int year, int month, int day, boolean isValid) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.isValid = isValid;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        for(IValidatableSubscriber subscriber  : subscribers){
            subscriber.notify(this);
        }
        setValid(true);
    }

    @Override
    public boolean isValid() {
        return isValid;
    }

    private void setValid(boolean value) {
        isValid = value;
        for(IValidatableSubscriber subscriber  : subscribers){
            subscriber.notify(this);
        }
    }

    @Override
    public void subscribe(IValidatableSubscriber subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void unsubscribe(IValidatableSubscriber subscriber) {
        subscribers.remove(subscriber);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        for(IValidatableSubscriber subscriber  : subscribers){
            subscriber.removeValidatable(this);
        }
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
