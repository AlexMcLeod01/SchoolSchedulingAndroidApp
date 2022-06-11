package com.example.c196;

import static com.example.c196.DateStringFormatter.formatDateForText;
import static com.example.c196.R.id.addInstructorButton;
import static com.example.c196.R.id.addNoteButton;
import static com.example.c196.R.id.bottomNavView;
import static com.example.c196.R.id.classAssessmentsText;
import static com.example.c196.R.id.classEndDateSelectorButton;
import static com.example.c196.R.id.classEndDateText;
import static com.example.c196.R.id.classFragmentContainerView;
import static com.example.c196.R.id.classInstructorsSpinner;
import static com.example.c196.R.id.classInstructorsText;
import static com.example.c196.R.id.classNotesSpinner;
import static com.example.c196.R.id.classNotesText;
import static com.example.c196.R.id.classStartDateSelectorButton;
import static com.example.c196.R.id.classStartDateText;
import static com.example.c196.R.id.classStatusSpinner;
import static com.example.c196.R.id.classTitleInput;
import static com.example.c196.R.id.deleteClassButton;
import static com.example.c196.R.id.saveClassButton;
import static com.example.c196.R.id.updateInstructorButton;
import static com.example.c196.R.id.updateNoteButton;

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
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClassDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassDetailsFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    //Instance of database
    private TermDataBase tdb;

    //ArrayAdapter for statusSpinner
    ArrayAdapter<CharSequence> statusAdapter;

    //Views
    private Button startDate;
    private Button endDate;
    private Button saveButton;
    private Button deleteButton;
    private Button addInstructor;
    private Button updateInstructor;
    private Button addNote;
    private Button updateNote;
    private TextView startText;
    private TextView endText;
    private TextView instructorList;
    private TextView noteList;
    private TextView assessmentList;
    private TextInputEditText classTitle;
    private Spinner statusSpinner;
    private Spinner instructorSpinner;
    private Spinner noteSpinner;

    //Instance variables for saving the data
    private String title;
    private Date start;
    private Date end;
    private String status;
    private CourseObj newCourse;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CLASS_OBJECT_PARAM = "param1";

    //A CourseObj to show the details of
    private CourseObj detailedClass;
    private List<CourseInstructor> detailedInstructors;
    private List<CourseNoteObj> courseNotes;
    private List<AssessmentObj> courseAssessments;

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

        if (detailedClass != null) {
            int id = detailedClass.getId();
            detailedInstructors = tdb.getInstructorByCourseId(id);
            courseNotes = tdb.getNotesByCourseId(id);
            courseAssessments = tdb.getAssessmentByCourseId(id);
        }

        declareViews(root);
        setupStatusSpinner();

        //Setup different uses of this fragment
        if (detailedClass != null) {
            fillDetails();
        } else {
            setupEmptyForm();
        }

        return root;
    }

    private void declareViews(View root) {
        //Declare various views from top to bottom
        classTitle = root.findViewById(classTitleInput);
        startText = root.findViewById(classStartDateText);
        endText = root.findViewById(classEndDateText);
        saveButton = root.findViewById(saveClassButton);
        startDate = root.findViewById(classStartDateSelectorButton);
        endDate = root.findViewById(classEndDateSelectorButton);
        deleteButton = root.findViewById(deleteClassButton);
        statusSpinner = root.findViewById(classStatusSpinner);
        addInstructor = root.findViewById(addInstructorButton);
        updateInstructor = root.findViewById(updateInstructorButton);
        instructorList = root.findViewById(classInstructorsText);
        instructorSpinner = root.findViewById(classInstructorsSpinner);
        addNote = root.findViewById(addNoteButton);
        updateNote = root.findViewById(updateNoteButton);
        noteList = root.findViewById(classNotesText);
        noteSpinner = root.findViewById(classNotesSpinner);
        assessmentList = root.findViewById(classAssessmentsText);
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
        addInstructor.setOnClickListener(v -> {
            if (validateForm()) {
                sendToInstructorForm(newCourse);
            }
        });
        addNote.setOnClickListener(v -> {
            if (validateForm()) {
                sendToNoteForm(newCourse);
            }
        });
        setUpDatePickers();
    }

    /**
     * Input validation for the form before saving
     * @return true if valid
     */
    public boolean validateForm() {
        if (TextUtils.isEmpty(classTitle.getText().toString())) {
            classTitle.setHint("Class Title is Required");
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
        if (start.after(end)) {
            endText.setText("End Date Must Be After Start Date");
            return false;
        }
        newCourse = new CourseObj(title, start, end, status);
        if(detailedClass != null) {
            if(detailedClass.getId() >= 0) {
                newCourse.setId(detailedClass.getId());
            } else {
                newCourse.setId(-1);
            }
        }
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

    private void updateFormButtons() {
        saveButton.setText("Save");
        saveButton.setOnClickListener(v -> {
            if (validateForm()) {
                if (newCourse.getId() >= 0) {
                    newCourse.setTermId(detailedClass.getTermId());
                    tdb.updateCourse(newCourse);
                } else {
                    tdb.addCourse(newCourse);
                }
                returnToListView();
            }
        });
    }

    /**
     * Fills in details when launched with a selected Term
     */
    private void fillDetails() {
        fillButtons();
        fillCalendars();
        fillTextBoxes();
        fillSpinners();
    }

    private void fillButtons() {
        saveButton.setText("Update");
        saveButton.setOnClickListener(v -> {
            classTitle.setHint("e.g. Underwater Basketry");
            classTitle.setEnabled(true);
            statusSpinner.setEnabled(true);
            instructorSpinner.setEnabled(true);
            noteSpinner.setEnabled(true);
            setUpDatePickers();
            updateFormButtons();
        });

        deleteButton.setVisibility(View.VISIBLE);
        deleteButton.setOnClickListener(v -> {
            if (true) {
                tdb.deleteCourse(detailedClass);
                returnToListView();
            }
        });

        addInstructor.setOnClickListener(v -> {
            if (validateForm()) {
                sendToInstructorForm(newCourse);
            }
        });

        updateInstructor.setVisibility(View.VISIBLE);
        updateInstructor.setOnClickListener(v -> {
            if (validateForm()) {
                CourseInstructor instructor = (CourseInstructor) instructorSpinner.getSelectedItem();
                sendToInstructorForm(newCourse, instructor);
            }
        });

        addNote.setOnClickListener(v -> {
            if (validateForm()) {
                sendToNoteForm(newCourse);
            }
        });

        updateNote.setVisibility(View.VISIBLE);
        updateNote.setOnClickListener(v -> {
            if (validateForm()) {
                CourseNoteObj note = (CourseNoteObj) noteSpinner.getSelectedItem();
                sendToNoteForm(newCourse, note);
            }
        });
    }

    private void fillCalendars() {
        start = detailedClass.getStartDate();
        startText.setText(DateStringFormatter.getText(start, true));

        end = detailedClass.getEndDate();
        endText.setText(DateStringFormatter.getText(end, true));
    }

    private void fillTextBoxes() {
        classTitle.setText(detailedClass.getTitle());
        classTitle.setEnabled(false);
        classTitle.setHint("");

        StringBuilder instruct = new StringBuilder();
        for (CourseInstructor instructor : detailedInstructors) {
            instruct.append(instructor.getName() + " " + instructor.getPhoneNumber() + " " + instructor.getEmailAddress() + "\n");
        }
        instructorList.setText(instruct);

        StringBuilder notes = new StringBuilder();
        for (CourseNoteObj note : courseNotes) {
            notes.append(note.getNote() + "\n");
        }
        noteList.setText(notes);

        StringBuilder assessment = new StringBuilder();
        for (AssessmentObj a : courseAssessments) {
            assessment.append(a.toString());
        }
        assessmentList.setText(assessment);
    }

    private void fillSpinners() {
        status = detailedClass.getStatus();
        statusSpinner.setSelection(statusAdapter.getPosition(status));
        statusSpinner.setEnabled(false);

        ArrayAdapter<CourseInstructor> adapter = new ArrayAdapter<CourseInstructor>(getContext(),
                android.R.layout.simple_spinner_item, detailedInstructors);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        instructorSpinner.setAdapter(adapter);
        instructorSpinner.setEnabled(false);

        ArrayAdapter<CourseNoteObj> noteAdapter = new ArrayAdapter<CourseNoteObj>(getContext(),
                android.R.layout.simple_spinner_item, courseNotes);
        noteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        noteSpinner.setAdapter(noteAdapter);
        noteSpinner.setEnabled(false);
    }

    private void setupStatusSpinner() {
        statusAdapter = ArrayAdapter.createFromResource(getContext(), R.array.class_status_array,
                android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusAdapter);
        statusSpinner.setOnItemSelectedListener(this);
    }

    private void returnToListView() {
        BottomNavigationView bNav = getActivity().findViewById(bottomNavView);
        bNav.setVisibility(View.VISIBLE);
        requireActivity().getSupportFragmentManager().beginTransaction().replace(classFragmentContainerView, new ClassListFragment()).commit();
    }

    private void sendToInstructorForm(CourseObj course) {
        requireActivity().getSupportFragmentManager().beginTransaction().replace(classFragmentContainerView, new ClassInstructorFragment().newInstance(course)).commit();
    }

    private void sendToInstructorForm(CourseObj course, CourseInstructor instructor) {
        requireActivity().getSupportFragmentManager().beginTransaction().replace(classFragmentContainerView, new ClassInstructorFragment().newInstance(course, instructor)).commit();
    }

    private void sendToNoteForm(CourseObj course) {
        requireActivity().getSupportFragmentManager().beginTransaction().replace(classFragmentContainerView, new CourseNotesFragment().newInstance(course)).commit();
    }

    private void sendToNoteForm(CourseObj course, CourseNoteObj note) {
        requireActivity().getSupportFragmentManager().beginTransaction().replace(classFragmentContainerView, new CourseNotesFragment().newInstance(course, note)).commit();
    }

    /**
     * For the OnItemSelectedListener implementation
     */
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        status = (String) parent.getItemAtPosition(pos);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        //Do nothing when nothing selected
    }
}