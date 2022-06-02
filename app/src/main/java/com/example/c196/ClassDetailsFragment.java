package com.example.c196;

import static com.example.c196.DateStringFormatter.formatDateForText;
import static com.example.c196.R.id.assessFragmentContainerView;
import static com.example.c196.R.id.assessmentDebugText;
import static com.example.c196.R.id.assessmentEndDateSelectorButton;
import static com.example.c196.R.id.assessmentEndDateText;
import static com.example.c196.R.id.assessmentStartDateSelectorButton;
import static com.example.c196.R.id.assessmentStartDateText;
import static com.example.c196.R.id.assessmentTitleInput;
import static com.example.c196.R.id.classDebugText;
import static com.example.c196.R.id.classEndDateSelectorButton;
import static com.example.c196.R.id.classEndDateText;
import static com.example.c196.R.id.classFragmentContainerView;
import static com.example.c196.R.id.classStartDateSelectorButton;
import static com.example.c196.R.id.classStartDateText;
import static com.example.c196.R.id.classTitleInput;
import static com.example.c196.R.id.deleteAssessmentButton;
import static com.example.c196.R.id.deleteClassButton;
import static com.example.c196.R.id.saveAssessmentButton;
import static com.example.c196.R.id.saveClassButton;

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

import java.util.Date;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClassDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassDetailsFragment extends Fragment {
    //Instance of database
    private TermDataBase tdb;

    //Views
    private Button startDate;
    private Button endDate;
    private Button saveButton;
    private Button deleteButton;
    private TextView startText;
    private TextView endText;
    private TextInputEditText classTitle;

    //Debug view
    private TextView debugText;

    //Instance variables for saving the data
    private String title;
    private Date start;
    private Date end;
    private String status;
    private CourseObj newCourse;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CLASS_OBJECT_PARAM = "param1";

    // An AssessmentObj to show the details of
    private CourseObj detailedClass;

    public ClassDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param course CourseObj
     * @return A new instance of fragment ClassDetailsFragment.
     */
    public static ClassDetailsFragment newInstance(CourseObj course) {
        ClassDetailsFragment fragment = new ClassDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(CLASS_OBJECT_PARAM, course);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            detailedClass = (CourseObj) getArguments().getSerializable(CLASS_OBJECT_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_class_details, container, false);

        //Get the instance of the database
        tdb = TermDataBase.getInstance(root.getContext());

        //Declare various views
        classTitle = root.findViewById(classTitleInput);
        startText = root.findViewById(classStartDateText);
        endText = root.findViewById(classEndDateText);
        saveButton = root.findViewById(saveClassButton);
        startDate = root.findViewById(classStartDateSelectorButton);
        endDate = root.findViewById(classEndDateSelectorButton);
        deleteButton = root.findViewById(deleteClassButton);

        //Debug text
        debugText = root.findViewById(classDebugText);

        //Setup different uses of this fragment
        if (detailedClass != null) {
            //fillDetails();
        } else {
            setupEmptyForm();
        }

        return root;
    }

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            start = DateStringFormatter.formatDateForDatabase(year, month, day);
            startText.setText(formatDateForText(year, month, day));
        }
    };

    DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            end = DateStringFormatter.formatDateForDatabase(year, month, day);
            endText.setText(formatDateForText(year, month, day));
        }
    };

    /**
     * Sets up the empty form
     */
    private void setupEmptyForm() {
        //saveButton button OnClickListener
        saveButton.setOnClickListener(v -> {
            if (validateForm()) {
                tdb.addCourse(newCourse);
                returnToListView();
            }
        });
        setUpDatePickers();
    }

    /**
     * Input validation for the form before saving
     * @return
     */
    public boolean validateForm() {
        if (TextUtils.isEmpty(classTitle.getText().toString())) {
            classTitle.setHint("Assessment Title is Required");
            return false;
        } else {
            title = Objects.requireNonNull(classTitle.getText()).toString();
        }
        if ((startText.getText().toString().equals("No Start Date Set") || startText.getText().toString().equals("Start Date Required")) || start == null) {
            startText.setText("Start Date Required");
            return false;
        }
        if ((endText.getText().toString().equals("No End Date Set") || endText.getText().toString().equals("End Date Required")) || end == null) {
            endText.setText("End Date Required");
            return false;
        }
        newCourse = new CourseObj(title, start, end, "");
        return true;
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

    private void updatedSaveButton() {
        saveButton.setText("Save");
        saveButton.setOnClickListener(v -> {
            if (validateForm()) {
                newCourse.setId(detailedClass.getId());
                tdb.updateCourse(newCourse);
                returnToListView();
            }
        });
    }

    private void returnToListView() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(classFragmentContainerView, new AssessmentListFragment()).commit();
    }
}