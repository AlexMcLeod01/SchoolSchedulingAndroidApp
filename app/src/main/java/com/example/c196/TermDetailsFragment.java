package com.example.c196;

import static com.example.c196.R.id.endDateSelectorButton;
import static com.example.c196.R.id.endDateText;
import static com.example.c196.R.id.startDateSelectorButton;
import static com.example.c196.R.id.startDateText;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TermDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TermDetailsFragment extends Fragment {
    private Button startDate;
    private Button endDate;
    private Button saveButton;
    private TextView startText;
    private TextView endText;

    //Instance variables for saving the data
    private String title;
    private Date start;
    private Date end;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TermDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TermDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TermDetailsFragment newInstance(String param1, String param2) {
        TermDetailsFragment fragment = new TermDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_term_details, container, false);

        //Declare various views
        startText = root.findViewById(startDateText);
        endText = root.findViewById(endDateText);
        saveButton = root.findViewById(R.id.saveButton);
        startDate = root.findViewById(startDateSelectorButton);
        endDate = root.findViewById(endDateSelectorButton);

        //saveButton button OnClickListener
        saveButton.setOnClickListener(v -> {
            //TermObj newTerm = getPageValues();
        });

        //startDate button OnClickListener
        startDate.setOnClickListener(v -> {
            DatePickerFragment datePickerFragment = new DatePickerFragment().newInstance();
            datePickerFragment.callback(dateSetListener);
            datePickerFragment.show(getChildFragmentManager().beginTransaction(), "DatePickerFragment");
        });

        //endDate button OnClickListener
        endDate.setOnClickListener(v -> {
            DatePickerFragment datePickerFragment = new DatePickerFragment().newInstance();
            datePickerFragment.callback(endDateSetListener);
            datePickerFragment.show(getChildFragmentManager().beginTransaction(), "DatePickerFragment");
        });

        return root;
    }

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            startText.setText(new StringBuilder().append(formatMonth(month)).append(" ").append(day).append(" ").append(year).toString());
        }
    };

    DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            endText.setText(new StringBuilder().append(formatMonth(month)).append(" ").append(day).append(" ").append(year).toString());
        }
    };


    private String formatMonth(int month) {
        String m = "";
        switch (month) {
            case 0: m = "Jan"; break;
            case 1: m = "Feb"; break;
            case 2: m = "Mar"; break;
            case 3: m = "Apr"; break;
            case 4: m = "May"; break;
            case 5: m = "Jun"; break;
            case 6: m = "Jul"; break;
            case 7: m = "Aug"; break;
            case 8: m = "Sep"; break;
            case 9: m = "Oct"; break;
            case 10: m = "Nov"; break;
            case 11: m = "Dec"; break;
        }
        return m;
    }
    /**private TermObj getPageValues() {
        TermObj t;

        return t;
    }*/
}