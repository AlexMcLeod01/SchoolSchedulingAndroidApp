package com.example.c196;

import static com.example.c196.R.id.classFragmentContainerView;
import static com.example.c196.R.id.sendNoteButton;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CourseNotesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseNotesFragment extends Fragment {
    //Declare Views
    private Button save;
    private Button delete;
    private Button send;

    private TextInputEditText noteInput;

    //Database
    private TermDataBase tdb;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String COURSE_PARAM = "course";
    private static final String NOTE_PARAM = "note";

    private CourseObj course;
    private CourseNoteObj note;
    private CourseNoteObj newNote;

    public CourseNotesFragment() {
        // Required empty public constructor
    }

    /**
     * @param courseArg CourseObj
     * @return A new instance of fragment CourseNotesFragment.
     */
    public static CourseNotesFragment newInstance(CourseObj courseArg) {
        CourseNotesFragment fragment = new CourseNotesFragment();
        Bundle args = new Bundle();
        args.putSerializable(COURSE_PARAM, courseArg);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * @param courseArg CourseObj
     * @param noteArg CourseNoteObj
     * @return A new instance of fragment CourseNotesFragment.
     */
    public static CourseNotesFragment newInstance(CourseObj courseArg, CourseNoteObj noteArg) {
        CourseNotesFragment fragment = new CourseNotesFragment();
        Bundle args = new Bundle();
        args.putSerializable(COURSE_PARAM, courseArg);
        args.putSerializable(NOTE_PARAM, noteArg);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            course = (CourseObj) getArguments().getSerializable(COURSE_PARAM);
            if (getArguments() != null) {
                note = (CourseNoteObj) getArguments().getSerializable(NOTE_PARAM);
            }
        }
        tdb = TermDataBase.getInstance(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_course_notes, container, false);

        save = root.findViewById(R.id.saveNoteButton);
        delete = root.findViewById(R.id.deleteNoteButton);
        send = root.findViewById(sendNoteButton);
        noteInput = root.findViewById(R.id.noteInput);

        if (note == null) {
            addEmptyFormListeners();
        } else {
            fillForm();
        }
        return root;
    }

    private void addEmptyFormListeners() {
        save.setOnClickListener(v -> {
            if (validate()) {
                tdb.addNote(newNote);
                returnToDetails();
            }
        });
    }

    private void fillForm() {
        noteInput.setText(note.getNote());
        noteInput.setEnabled(false);
        save.setText("Update");
        save.setOnClickListener(v -> {
            save.setText("Save");
            save.setOnClickListener(w -> {
                if (validate()) {
                    newNote.setId(note.getId());
                    tdb.updateNote(newNote);
                    returnToDetails();
                }
            });
            noteInput.setEnabled(true);
        });
        delete.setVisibility(View.VISIBLE);
        delete.setOnClickListener(v -> {
            tdb.deleteNote(note);
            returnToDetails();
        });
        send.setVisibility(View.VISIBLE);
        send.setOnClickListener(v -> {
            sendEMail();
            returnToDetails();
        });
    }

    private boolean validate() {
        if (noteInput.getText().toString() == null) {
            return false;
        }
        String noteText = noteInput.getText().toString();
        newNote = new CourseNoteObj(noteText, course.getId());
        return true;
    }

    private void sendEMail() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, course.getTitle() + " Note");
        i.putExtra(Intent.EXTRA_TEXT, note.getNote());

        startActivity(Intent.createChooser(i, "Select App To Continue"));
    }

    private void returnToDetails() {
        requireActivity().getSupportFragmentManager().beginTransaction().replace(classFragmentContainerView, new ClassDetailsFragment().newInstance(course)).commit();
    }
}