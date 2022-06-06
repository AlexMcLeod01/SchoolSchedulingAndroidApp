package com.example.c196;

import static com.example.c196.DateStringFormatter.formatDateForText;
import static com.example.c196.R.id.endDateSelectorButton;
import static com.example.c196.R.id.endDateText;
import static com.example.c196.R.id.fragmentContainerView;
import static com.example.c196.R.id.startDateSelectorButton;
import static com.example.c196.R.id.startDateText;
import static com.example.c196.R.id.termTitleInput;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TermDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TermDetailsFragment extends Fragment {
    //Instance of DB
    private TermDataBase tdb;

    //Views
    private Button startDate;
    private Button endDate;
    private Button saveButton;
    private Button deleteButton;
    private TextView startText;
    private TextView endText;
    private TextInputEditText termTitle;

    //Debug view
    private TextView debugText;

    //Instance variables for saving the data
    private String title;
    private Date start;
    private Date end;
    private TermObj newTerm;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TERM_OBJECT_PARAM = "param1";

    // A TermObj to show the details of
    private TermObj detailedTerm;

    public TermDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param termToShowDetails a TermObj to show the details of
     * @return A new instance of fragment TermDetailsFragment.
     */
    public static TermDetailsFragment newInstance(TermObj termToShowDetails) {
        TermDetailsFragment fragment = new TermDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(TERM_OBJECT_PARAM, termToShowDetails);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            detailedTerm = (TermObj) getArguments().getSerializable(TERM_OBJECT_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_term_details, container, false);

        //Get the instance of the database
        tdb = TermDataBase.getInstance(root.getContext());

        //Declare various views
        termTitle = root.findViewById(termTitleInput);
        startText = root.findViewById(startDateText);
        endText = root.findViewById(endDateText);
        saveButton = root.findViewById(R.id.saveButton);
        startDate = root.findViewById(startDateSelectorButton);
        endDate = root.findViewById(endDateSelectorButton);
        deleteButton = root.findViewById(R.id.deleteButton);

        //Debug text
        debugText = root.findViewById(R.id.debugText);

        //Setup different uses of this fragment
        if (detailedTerm != null) {
            fillDetails();
        } else {
            setupEmptyForm();
        }


        return root;
    }

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            start = DateStringFormatter.formatDateForDatabase(year, month, day);
            startText.setText(formatDateForText(year, month, day, false));
        }
    };

    DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            end = DateStringFormatter.formatDateForDatabase(year, month, day);
            endText.setText(formatDateForText(year, month, day, false));
        }
    };

    /**
     * Input validation for the form before saving
     * @return
     */
    public boolean validateForm() {
        if (TextUtils.isEmpty(termTitle.getText().toString())) {
            termTitle.setHint("Term Title is Required");
            return false;
        } else {
            title = Objects.requireNonNull(termTitle.getText()).toString();
        }
        if ((startText.getText().toString().equals("No Start Date Set") || startText.getText().toString().equals("Start Date Required")) || start == null) {
            startText.setText("Start Date Required");
            return false;
        }
        if ((endText.getText().toString().equals("No End Date Set") || endText.getText().toString().equals("End Date Required")) || end == null) {
            endText.setText("End Date Required");
            return false;
        }
        newTerm = new TermObj(title, start, end);
        return true;
    }

    /**
     * Sets up the empty form
     */
    private void setupEmptyForm() {
        //saveButton button OnClickListener
        saveButton.setOnClickListener(v -> {
            if (validateForm()) {
                tdb.addTerm(newTerm);
                returnToListView();
            }
        });

        setUpDatePickers();
    }

    /**
     * Sets up the Date Pickers
     */
    private void setUpDatePickers() {
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
    }

    /**
     * Fills in details when launched with a selected Term
     */
    private void fillDetails() {
        int y, m, d;

        termTitle.setText(detailedTerm.getTitle());
        termTitle.setEnabled(false);
        termTitle.setHint("");

        start = detailedTerm.getStartDate();
        Calendar cal = new GregorianCalendar();
        cal.setTime(start);
        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH);
        d = cal.get(Calendar.DAY_OF_MONTH);
        startText.setText(formatDateForText(y, m-1, d, true));

        end = detailedTerm.getEndDate();
        cal.setTime(end);
        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH);
        d = cal.get(Calendar.DAY_OF_MONTH);
        endText.setText(formatDateForText(y, m-1, d, true));

        saveButton.setText("Update");
        saveButton.setOnClickListener(v -> {
            termTitle.setHint("e.g. Term 1");
            termTitle.setEnabled(true);
            setUpDatePickers();
            updatedSaveButton();
        });

        deleteButton.setVisibility(View.VISIBLE);
        deleteButton.setOnClickListener(v -> {
            if (tdb.getCourseByTermId(detailedTerm.getId()).size() == 0) {
                tdb.deleteTerm(detailedTerm);
                returnToListView();
            } else {
                //TODO error message
            }
        });
    }

    private void updatedSaveButton() {
        saveButton.setText("Save");
        saveButton.setOnClickListener(v -> {
            if (validateForm()) {
                newTerm.setId(detailedTerm.getId());
                tdb.updateTerm(newTerm);
                returnToListView();
            }
        });
    }

    private void returnToListView() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(fragmentContainerView, new TermListFragment()).commit();
    }
}