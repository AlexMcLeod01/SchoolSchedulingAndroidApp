package com.example.c196;

import static com.example.c196.R.id.assessFragmentContainerView;
import static com.example.c196.R.id.assessmentDetailButton;
import static com.example.c196.R.id.assessmentListButton;
import static com.example.c196.R.id.assessmentsButton;
import static com.example.c196.R.id.bottomNavView;
import static com.example.c196.R.id.classFromAssessButton;
import static com.example.c196.R.id.classesButton;
import static com.example.c196.R.id.homeButton;
import static com.example.c196.R.id.homeFromAssessButton;
import static com.example.c196.R.id.termFromAssessButton;
import static com.example.c196.R.id.termsButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Assessments extends AppCompatActivity {
    //Buttons for both portrait and landscape
    private Button listButton;
    private Button detailButton;

    //Nav for portrait
    private BottomNavigationView bottomNavigation;

    //Nav for landscape
    private Button landHomeButton;
    private Button landTermButton;
    private Button landClassButton;

    private static AssessmentObj selectedAssessment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessments);

        //Set Term List as first fragment
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        //Append fragment to ViewGroup ConstraintLayout
        transaction.add(assessFragmentContainerView, new AssessmentListFragment());
        transaction.commit();

        //Setup fragment switching due to button presses
        addButtonListeners();


        if(getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            setupPortraitNav();
        } else {
            setupLandscapeNav();
        }
    }

    private void setupPortraitNav() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Setup Bottom Navigation Menu
        bottomNavigation = findViewById(bottomNavView);
        bottomNavigation.setSelectedItemId(assessmentsButton);
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
                        startActivity(new Intent(getApplicationContext(), Classes.class));
                        overridePendingTransition(0,0);
                        return true;
                    case assessmentsButton:
                        return true;
                }
                return false;
            }
        });
    }

    private void setupLandscapeNav() {
        landHomeButton = findViewById(homeFromAssessButton);
        landTermButton = findViewById(termFromAssessButton);
        landClassButton = findViewById(classFromAssessButton);

        landHomeButton.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            overridePendingTransition(0,0);
        });
        landTermButton.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), Terms.class));
            overridePendingTransition(0,0);
        });
        landClassButton.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), Classes.class));
            overridePendingTransition(0,0);
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if(getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {

        } else {
            ActionBar actionBar = getSupportActionBar();
            actionBar.hide();
        }
    }

    /**
     * A listener to cause the buttons to call switch_fragment
     */
    public void addButtonListeners() {
        listButton = findViewById(assessmentListButton);
        detailButton = findViewById(assessmentDetailButton);

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
            case assessmentDetailButton:
                if(getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                    bottomNavigation.setVisibility(View.GONE);
                }
                if (selectedAssessment == null) {
                    frag = new AssessmentDetailsFragment();
                } else {
                    frag = AssessmentDetailsFragment.newInstance(selectedAssessment);
                    selectedAssessment = null;
                }
                break;
            case assessmentListButton:
                if(getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                    bottomNavigation.setVisibility(View.VISIBLE);
                }
                frag = new AssessmentListFragment();
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
        transaction.replace(assessFragmentContainerView, frag);
        transaction.commit();
    }

    public static void setSelectedAssessment(AssessmentObj assess) {
        selectedAssessment = assess;
    }
}