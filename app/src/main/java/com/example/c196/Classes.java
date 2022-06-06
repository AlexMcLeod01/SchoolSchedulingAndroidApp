package com.example.c196;

import static com.example.c196.R.id.assessFragmentContainerView;
import static com.example.c196.R.id.assessmentDetailButton;
import static com.example.c196.R.id.assessmentListButton;
import static com.example.c196.R.id.assessmentsButton;
import static com.example.c196.R.id.bottomNavView;
import static com.example.c196.R.id.classDetailButton;
import static com.example.c196.R.id.classFragmentContainerView;
import static com.example.c196.R.id.classListButton;
import static com.example.c196.R.id.classesButton;
import static com.example.c196.R.id.homeButton;
import static com.example.c196.R.id.termSelectionSpinner;
import static com.example.c196.R.id.termsButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

public class Classes extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    //views
    private Button listButton;
    private Button detailButton;
    private BottomNavigationView bottomNavigation;
    private Spinner termSpinner;

    private TermDataBase tdb;

    //data to be passed
    private static CourseObj selected;
    private List<TermObj> terms;
    private List<CourseObj> courses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);

        //Database get
        tdb = TermDataBase.getInstance(getApplicationContext());
        terms = tdb.getTerms();
        courses = tdb.getCourseByTermId(0);

        //Views initialize
        listButton = findViewById(classListButton);
        detailButton = findViewById(classDetailButton);
        termSpinner = findViewById(termSelectionSpinner);

        //Set Term List as first fragment
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        //Append fragment to ViewGroup ConstraintLayout
        transaction.add(classFragmentContainerView, ClassListFragment.newInstance(courses));
        transaction.commit();

        //Setup fragment switching due to button presses
        addButtonListeners();
        setupSpinner();

        //Setup Bottom Navigation Menu
        bottomNavigation = findViewById(bottomNavView);
        bottomNavigation.setSelectedItemId(classesButton);
        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case homeButton:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case termsButton:
                        startActivity(new Intent(getApplicationContext(), Terms.class));
                        overridePendingTransition(0,0);
                        return true;
                    case classesButton:
                        return true;
                    case assessmentsButton:
                        startActivity(new Intent(getApplicationContext(), Assessments.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    private void setupSpinner() {
        ArrayAdapter<TermObj> adapter = new ArrayAdapter<TermObj>(getApplicationContext(),
                android.R.layout.simple_spinner_item, terms);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        termSpinner.setAdapter(adapter);
        termSpinner.setOnItemSelectedListener(this);
    }

    /**
     * A listener to cause the buttons to call switch_fragment
     */
    public void addButtonListeners() {

        View.OnClickListener click = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFragment(view);
            }
        };
        listButton.setOnClickListener(click);
        detailButton.setOnClickListener(click);
    }

    /**
     * Switch statement to determine which fragment to switch to
     * @param view the button pressed
     */
    public void switchFragment(View view) {
        Fragment frag = null;
        switch (view.getId()) {
            case classDetailButton:
                termSpinner.setVisibility(View.GONE);
                bottomNavigation.setVisibility(View.GONE);
                if (selected == null) {
                    frag = new ClassDetailsFragment();
                } else {
                    frag = ClassDetailsFragment.newInstance(selected);
                    selected = null;
                }
                break;
            case classListButton:
                termSpinner.setVisibility(View.VISIBLE);
                bottomNavigation.setVisibility(View.VISIBLE);
                frag = ClassListFragment.newInstance(courses);
                break;
        }
        setFragment(frag);
    }

    /**
     * Replaces the current fragment with another
     * @param frag the new fragment
     */
    public void setFragment(Fragment frag) {
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.replace(classFragmentContainerView, frag);
        transaction.commit();
    }

    public static void setSelectedClass(CourseObj course) {
        selected = course;
    }

    /**
     * For the OnItemSelectedListener implementation
     */
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        TermObj term = (TermObj) parent.getItemAtPosition(pos);
        int termId = term.getId();
        courses = tdb.getCourseByTermId(termId);
        termSpinner.setVisibility(View.VISIBLE);
        bottomNavigation.setVisibility(View.VISIBLE);
        Fragment frag = ClassListFragment.newInstance(courses);
        setFragment(frag);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        //Do nothing when nothing selected
    }
}