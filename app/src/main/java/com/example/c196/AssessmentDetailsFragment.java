package com.example.c196;

import static com.example.c196.DateStringFormatter.formatDateForText;
import static com.example.c196.R.id.assessFragmentContainerView;
import static com.example.c196.R.id.assessmentCourseText;
import static com.example.c196.R.id.assessmentEndDateSelectorButton;
import static com.example.c196.R.id.assessmentEndDateText;
import static com.example.c196.R.id.assessmentStartDateSelectorButton;
import static com.example.c196.R.id.assessmentStartDateText;
import static com.example.c196.R.id.assessmentTitleInput;
import static com.example.c196.R.id.assessmentTypeSwitch;
import static com.example.c196.R.id.bottomNavView;
import static com.example.c196.R.id.classAssessSpinner;
import static com.example.c196.R.id.deleteAssessmentButton;
import static com.example.c196.R.id.saveAssessmentButton;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.lang.reflect.Array;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
    private TextView startText;
    private TextView endText;
    private TextView courseText;
    private TextInputEditText assessTitle;
    private Switch typeSwitch;
    private Spinner spinner;
    private ArrayAdapter<CourseObj> courseAdapter;

    //Debug view
    private TextView debugText;

    //Instance variables for saving the data
    private int id;
    private String title;
    private Date start;
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
        if (TextUtils.isEmpty(assessTitle.getText().toString())) {
            assessTitle.setHint("Assessment Title is Required");
            return false;
        } else {
            title = Objects.requireNonNull(assessTitle.getText()).toString();
        }
        if ((startText.getText().toString().equals("No Start Date Set") || startText.getText().toString().equals("Start Date Required")) || start == null) {
            startText.setText("Start Date Required");
            return false;
        }
        if ((endText.getText().toString().equals("No End Date Set") || endText.getText().toString().equals("End Date Required")) || end == null) {
            endText.setText("End Date Required");
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

    private void setupSwitch() {
        typeSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            isPerformance = b;
        });
    }

    /**
     * Fills in details when launched with a selected Term
     */
    private void fillDetails() {
        int y, m, d;
        id = detailedAssessment.getId();

        title = detailedAssessment.getTitle();
        assessTitle.setText(title);
        assessTitle.setEnabled(false);
        assessTitle.setHint("");

        start = detailedAssessment.getStartDate();
        Calendar cal = new GregorianCalendar();
        cal.setTime(start);
        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH);
        d = cal.get(Calendar.DAY_OF_MONTH);
        startText.setText(formatDateForText(y, m-1, d, true));

        end = detailedAssessment.getEndDate();
        cal.setTime(end);
        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH);
        d = cal.get(Calendar.DAY_OF_MONTH);
        endText.setText(formatDateForText(y, m-1, d, true));

        saveButton.setText("Update");
        saveButton.setOnClickListener(v -> {
            assessTitle.setHint("e.g. Term 1");
            assessTitle.setEnabled(true);
            spinner.setEnabled(true);
            setUpDatePickers();
            updatedSaveButton();
        });

        deleteButton.setVisibility(View.VISIBLE);
        deleteButton.setOnClickListener(v -> {
            if (true) {
                tdb.deleteAssessment(detailedAssessment);
                returnToListView();
            }
        });

        typeSwitch.setChecked(detailedAssessment.isPerformance());
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