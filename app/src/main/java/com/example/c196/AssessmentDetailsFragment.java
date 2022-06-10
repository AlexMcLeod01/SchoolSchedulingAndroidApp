package com.example.c196;

import static com.example.c196.DateStringFormatter.formatDateForText;
import static com.example.c196.R.id.assessFragmentContainerView;
import static com.example.c196.R.id.assessmentCourseText;
import static com.example.c196.R.id.assessmentEndDateSelectorButton;
import static com.example.c196.R.id.assessmentEndDateText;
import static com.example.c196.R.id.assessmentEndTimeSelectorButton;
import static com.example.c196.R.id.assessmentEndTimeText;
import static com.example.c196.R.id.assessmentStartDateSelectorButton;
import static com.example.c196.R.id.assessmentStartDateText;
import static com.example.c196.R.id.assessmentStartTimeSelectorButton;
import static com.example.c196.R.id.assessmentStartTimeText;
import static com.example.c196.R.id.assessmentTitleInput;
import static com.example.c196.R.id.assessmentTypeSwitch;
import static com.example.c196.R.id.bottomNavView;
import static com.example.c196.R.id.classAssessSpinner;
import static com.example.c196.R.id.deleteAssessmentButton;
import static com.example.c196.R.id.saveAssessmentButton;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AssessmentDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssessmentDetailsFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    //Instance of database
    private TermDataBase tdb;

    //Views
    private Button startDate;
    private Button endDate;
    private Button saveButton;
    private Button deleteButton;
    private Button startTime;
    private Button endTime;
    private TextView startText;
    private TextView endText;
    private TextView courseText;
    private TextView startTimeText;
    private TextView endTimeText;
    private TextInputEditText assessTitle;
    private Switch typeSwitch;
    private Spinner spinner;
    private ArrayAdapter<CourseObj> courseAdapter;

    //Debug view
    private TextView debugText;

    //Instance variables for saving the data
    private int id;
    private String title;
    private String sDate;
    private String sTime;
    private Date start;
    private String eDate;
    private String eTime;
    private Date end;
    private boolean isPerformance;
    private int courseId;
    private AssessmentObj newAssessment;
    private CourseObj assessmentCourse;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ASSESSMENT_OBJECT_PARAM = "param1";

    // An AssessmentObj to show the details of
    private AssessmentObj detailedAssessment;

    //A list of CourseObj to pick from to assign an assessment to
    private List<CourseObj> courses;

    public AssessmentDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param assessmentToDisplay the assessment whose details should be shown
     * @return A new instance of fragment AssessmentDetailsFragment.
     */
    public static AssessmentDetailsFragment newInstance(AssessmentObj assessmentToDisplay) {
        AssessmentDetailsFragment fragment = new AssessmentDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ASSESSMENT_OBJECT_PARAM, assessmentToDisplay);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            detailedAssessment = (AssessmentObj) getArguments().getSerializable(ASSESSMENT_OBJECT_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_assessment_details, container, false);

        //Get the instance of the database and course data
        tdb = TermDataBase.getInstance(root.getContext());
        courses = tdb.getAllCourses();

        assignViews(root);

        setupSpinner();

        //Setup different uses of this fragment
        if (detailedAssessment != null) {
            fillDetails();
        } else {
            setupEmptyForm();
        }
        return root;
    }

    private void assignViews(View root) {
        //Declare various views
        assessTitle = root.findViewById(assessmentTitleInput);
        startText = root.findViewById(assessmentStartDateText);
        endText = root.findViewById(assessmentEndDateText);
        courseText = root.findViewById(assessmentCourseText);
        saveButton = root.findViewById(saveAssessmentButton);
        startDate = root.findViewById(assessmentStartDateSelectorButton);
        endDate = root.findViewById(assessmentEndDateSelectorButton);
        deleteButton = root.findViewById(deleteAssessmentButton);
        typeSwitch = root.findViewById(assessmentTypeSwitch);
        spinner = root.findViewById(classAssessSpinner);
        startTimeText = root.findViewById(assessmentStartTimeText);
        startTime = root.findViewById(assessmentStartTimeSelectorButton);
        endTimeText = root.findViewById(assessmentEndTimeText);
        endTime = root.findViewById(assessmentEndTimeSelectorButton);
    }

    /**
     * A private class didn't work, so implemented OnItemSelectedListener in the overall class
     * and just set everything else up in this method call
     */
    private void setupSpinner() {
        courseAdapter = new ArrayAdapter<CourseObj>(getContext(),
                android.R.layout.simple_spinner_item, courses);
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(courseAdapter);
        spinner.setOnItemSelectedListener(this);
    }

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            sDate = formatDateForText(year, month, day, false);
            startText.setText(sDate);
        }
    };

    TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            sTime = DateStringFormatter.formatTimeForText(i, i1);
            startTimeText.setText(sTime);
        }
    };

    DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            eDate = formatDateForText(year, month, day, false);
            endText.setText(eDate);
        }
    };

    TimePickerDialog.OnTimeSetListener endTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            eTime = DateStringFormatter.formatTimeForText(i, i1);
            endTimeText.setText(eTime);
        }
    };

    /**
     * Input validation for the form before saving
     * @return
     */
    public boolean validateForm() {
        if (TextUtils.isEmpty(assessTitle.getText().toString())) {
            assessTitle.setHint("Assessment Title is Required");
            return false;
        } else {
            title = Objects.requireNonNull(assessTitle.getText()).toString();
        }
        if ((sDate.equals("No Start Date Set") || sDate.equals("Start Date Required")) || sDate == null) {
            startText.setText("Start Date Required");
            return false;
        }
        if ((sTime.equals("No Start Time Set") || sTime.equals("Start Time Required")) || sTime == null) {
            startTimeText.setText("Start Time Required");
            return false;
        }
        start = DateStringFormatter.formatDateTimeForDatabase(sDate, sTime);
        if ((eDate.equals("No End Date Set") || eDate.equals("End Date Required")) || eDate == null) {
            endText.setText("End Date Required");
            return false;
        }
        if ((eTime.equals("No End Time Set") || eTime.equals("End Time Required")) || eTime == null) {
            endTimeText.setText("End Time Required");
            return false;
        }
        end = DateStringFormatter.formatDateTimeForDatabase(eDate, eTime);
        if (start.after(end)) {
            endText.setText("End Date Must Be After Start Date");
            return false;
        }
        if (assessmentCourse == null) {
            assessmentCourse = tdb.getCourseById(0);
            courseId = 0;
        }
        newAssessment = new AssessmentObj(title, start, end, isPerformance);
        newAssessment.setCourseId(courseId);
        return true;
    }

    /**
     * Sets up the empty form
     */
    private void setupEmptyForm() {
        //saveButton button OnClickListener
        saveButton.setOnClickListener(v -> {
            if (validateForm()) {
                tdb.addAssessment(newAssessment);
                returnToListView();
            }
        });

        setupSwitch();
        setUpDatePickers();
        setupTimePickers();
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

    private void setupTimePickers() {
        startTime.setOnClickListener(v -> {
            TimePickerFragment timePickerFragment = TimePickerFragment.newInstance();
            timePickerFragment.callback(timeSetListener);
            timePickerFragment.show(getChildFragmentManager().beginTransaction(), "TimePickerFragment");
        });

        endTime.setOnClickListener(v -> {
            TimePickerFragment timePickerFragment = TimePickerFragment.newInstance();
            timePickerFragment.callback(endTimeSetListener);
            timePickerFragment.show(getChildFragmentManager().beginTransaction(), "TimePickerFragment");
        });
    }

    private void setupSwitch() {
        typeSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            isPerformance = b;
        });
    }

    /**
     * Fills in details when launched with a selected Term
     */
    private void fillDetails() {
        id = detailedAssessment.getId();

        title = detailedAssessment.getTitle();
        assessTitle.setText(title);
        assessTitle.setEnabled(false);
        assessTitle.setHint("");

        start = detailedAssessment.getStartDate();
        sDate = DateStringFormatter.getDateText(start, true);
        startText.setText(sDate);
        sTime = DateStringFormatter.getTimeText(start);
        startTimeText.setText(sTime);

        end = detailedAssessment.getEndDate();
        eDate = DateStringFormatter.getDateText(end, true);
        endText.setText(eDate);
        eTime = DateStringFormatter.getTimeText(end);
        endTimeText.setText(eTime);

        saveButton.setText("Update");
        saveButton.setOnClickListener(v -> {
            assessTitle.setHint("e.g. Term 1");
            assessTitle.setEnabled(true);
            spinner.setEnabled(true);
            setUpDatePickers();
            setupTimePickers();
            updatedSaveButton();
        });

        deleteButton.setVisibility(View.VISIBLE);
        deleteButton.setOnClickListener(v -> {
            if (true) {
                tdb.deleteAssessment(detailedAssessment);
                returnToListView();
            }
        });

        isPerformance = detailedAssessment.isPerformance();
        typeSwitch.setChecked(isPerformance);
        setupSwitch();

        courseId = detailedAssessment.getCourseId();
        assessmentCourse = tdb.getCourseById(courseId);
        spinner.setEnabled(false);
        if (courseId >= 0) {
            int pos = courseAdapter.getPosition(assessmentCourse);
            spinner.setSelection(pos);
            courseText.setText(assessmentCourse.toString());
        }
    }

    private void updatedSaveButton() {
        saveButton.setText("Save");
        saveButton.setOnClickListener(v -> {
            if (validateForm()) {
                newAssessment.setId(id);
                tdb.updateAssessment(newAssessment);
                returnToListView();
            }
        });
    }

    private void returnToListView() {
        BottomNavigationView bNav = getActivity().findViewById(bottomNavView);
        bNav.setVisibility(View.VISIBLE);
        getActivity().getSupportFragmentManager().beginTransaction().replace(assessFragmentContainerView, new AssessmentListFragment()).commit();
    }

    /**
     * For the OnItemSelectedListener implementation
     */
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        assessmentCourse = (CourseObj) parent.getItemAtPosition(pos);
        courseId = assessmentCourse.getId();
        courseText.setText(assessmentCourse.toString());
    }

    public void onNothingSelected(AdapterView<?> parent) {
        //Do nothing when nothing selected
    }
}