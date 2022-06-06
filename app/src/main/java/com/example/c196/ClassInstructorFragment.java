package com.example.c196;

import static com.example.c196.R.id.cancelInstructorButton;
import static com.example.c196.R.id.classFragmentContainerView;
import static com.example.c196.R.id.classInstructorEmailInput;
import static com.example.c196.R.id.classInstructorNameInput;
import static com.example.c196.R.id.classInstructorPhoneInput;
import static com.example.c196.R.id.deleteInstructorButton;
import static com.example.c196.R.id.saveInstructorButton;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClassInstructorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassInstructorFragment extends Fragment {
    //Views
    private TextInputEditText instructorName;
    private TextInputEditText instructorPhone;
    private TextInputEditText instructorEmail;
    private Button save;
    private Button cancel;
    private Button delete;

    //Data for the Instructor
    private String name;
    private String phone;
    private String email;
    private CourseInstructor newInstructor;

    //The Database
    TermDataBase tdb;


    // the fragment initialization parameters
    private static final String COURSE_PARAM = "course";
    private static final String INSTRUCTOR_PARAM = "instructor";

    private CourseObj course;
    private CourseInstructor instructor;

    public ClassInstructorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param course CourseObj
     * @param instructor CourseInstructor
     * @return A new instance of fragment ClassInstructorFragment.
     */
    public static ClassInstructorFragment newInstance(CourseObj course, CourseInstructor instructor) {
        ClassInstructorFragment fragment = new ClassInstructorFragment();
        Bundle args = new Bundle();
        args.putSerializable(COURSE_PARAM, course);
        args.putSerializable(INSTRUCTOR_PARAM, instructor);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param course CourseObj
     * @return A new instance of fragment ClassInstructorFragment.
     */
    public static ClassInstructorFragment newInstance(CourseObj course) {
        ClassInstructorFragment fragment = new ClassInstructorFragment();
        Bundle args = new Bundle();
        args.putSerializable(COURSE_PARAM, course);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            course = (CourseObj) getArguments().getSerializable(COURSE_PARAM);
            if (getArguments() != null) {
                instructor = (CourseInstructor) getArguments().getSerializable(INSTRUCTOR_PARAM);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_class_instructor, container, false);

        // Get the Database instance
        tdb = TermDataBase.getInstance(getContext());

        //Text Fields
        instructorName = root.findViewById(classInstructorNameInput);
        instructorPhone = root.findViewById(classInstructorPhoneInput);
        instructorEmail = root.findViewById(classInstructorEmailInput);

        //Buttons
        save = root.findViewById(saveInstructorButton);
        cancel = root.findViewById(cancelInstructorButton);
        delete = root.findViewById(deleteInstructorButton);

        if (instructor == null) {
            setupNewButtons();
        } else {
            populateForm();
            setupUpdateButtons();
        }
        //Setup the listeners
        return root;
    }

    private boolean validateInstructorInputs() {
        if (TextUtils.isEmpty(instructorName.getText().toString())) {
            instructorName.setHint("Instructor name is required to set an instructor");
            return false;
        }
        if (TextUtils.isEmpty(instructorEmail.getText().toString())) {
            instructorEmail.setHint("Instructor email is required to set an instructor");
            return false;
        }
        if (TextUtils.isEmpty(instructorPhone.getText().toString())) {
            instructorPhone.setHint("Instructor phone is required to set an instructor");
            return false;
        }
        name = instructorName.getText().toString();
        phone = instructorPhone.getText().toString();
        email = instructorEmail.getText().toString();
        newInstructor = new CourseInstructor(name, phone, email);
        if (instructor != null) {
            newInstructor.setId(instructor.getId());
        }
        newInstructor.setCourseId(course.getId());
        return true;
    }

    private void setupNewButtons() {
        cancel.setOnClickListener(v -> {
            returnToClassDetails(course);
        });
        save.setOnClickListener(v -> {
            if (validateInstructorInputs()) {
                tdb.addInstructor(newInstructor);
                returnToClassDetails(course);
            }
        });
    }

    public void populateForm() {
        name = instructor.getName();
        phone = instructor.getPhoneNumber();
        email = instructor.getEmailAddress();
        instructorName.setText(name);
        instructorName.setEnabled(false);
        instructorPhone.setText(phone);
        instructorPhone.setEnabled(false);
        instructorEmail.setText(email);
        instructorEmail.setEnabled(false);
    }

    public void setupUpdateButtons() {
        cancel.setOnClickListener(v -> {
            returnToClassDetails(course);
        });
        save.setText("Update");
        save.setOnClickListener(v -> {
            setupUpdateSaveButton();
            instructorName.setEnabled(true);
            instructorPhone.setEnabled(true);
            instructorEmail.setEnabled(true);
        });
        delete.setVisibility(View.VISIBLE);
        delete.setOnClickListener(v -> {
            tdb.deleteInstructor(instructor);
            returnToClassDetails(course);
        });
    }

    public void setupUpdateSaveButton() {
        save.setText("Save");
        save.setOnClickListener(v -> {
            if (validateInstructorInputs()) {
                tdb.updateInstructor(newInstructor);
                returnToClassDetails(course);
            }
        });
    }

    public void returnToClassDetails(CourseObj course) {
        requireActivity().getSupportFragmentManager().beginTransaction().replace(classFragmentContainerView, ClassDetailsFragment.newInstance(course)).commit();
    }
}