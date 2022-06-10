package com.example.c196;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment {
    TimePickerDialog.OnTimeSetListener timeSetListener;
    private boolean isModal = false;

    public TimePickerFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     */
    public static TimePickerFragment newInstance() {
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.isModal = true;
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), timeSetListener, hour, min, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate or create view
        if (isModal) {
            return super.onCreateView(inflater, container, savedInstanceState);
        } else {
            View root = inflater.inflate(R.layout.fragment_assessment_details, container, false);
            return root;
        }
    }

    public void callback(TimePickerDialog.OnTimeSetListener timeSetListener) {
        this.timeSetListener = timeSetListener;
    }
}
