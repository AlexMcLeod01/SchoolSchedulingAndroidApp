package com.example.c196;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DatePickerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DatePickerFragment extends DialogFragment {
    DatePickerDialog.OnDateSetListener dateSetListener;
    private boolean isModal = false;

    /**
     * Required empty constructor
     */
    public DatePickerFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     */
    public static DatePickerFragment newInstance() {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.isModal = true;
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), dateSetListener, year, month, day);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate or create view
        if (isModal) {
            return super.onCreateView(inflater, container, savedInstanceState);
        } else {
            View root = inflater.inflate(R.layout.fragment_term_details, container, false);
            return root;
        }
    }

    public void callback(DatePickerDialog.OnDateSetListener dateSetListener) {
        this.dateSetListener = dateSetListener;
    }
}