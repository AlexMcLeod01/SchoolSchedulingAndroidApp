package com.example.c196;

import static com.example.c196.R.id.assessmentFromTermButton;
import static com.example.c196.R.id.assessmentsButton;
import static com.example.c196.R.id.bottomNavView;
import static com.example.c196.R.id.classFromTermButton;
import static com.example.c196.R.id.classesButton;
import static com.example.c196.R.id.fragmentContainerView;
import static com.example.c196.R.id.homeButton;
import static com.example.c196.R.id.homeFromTermButton;
import static com.example.c196.R.id.termDetailButton;
import static com.example.c196.R.id.termListButton;
import static com.example.c196.R.id.termsButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Terms extends AppCompatActivity {
    private Button listButton;
    private Button detailButton;

    //Landscape Nav Buttons
    private Button landHomeButton;
    private Button landClassButton;
    private Button landAssessButton;

    //Portrait Nav
    private BottomNavigationView bottomNavigation;

    private static TermObj selectedTerm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        //Set Term List as first fragment
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        //Append fragment to ViewGroup ConstraintLayout
        transaction.add(fragmentContainerView, new TermListFragment());
        transaction.commit();

        initViews();
        addButtonListeners();
        initNavs();
    }

    private void initViews() {
        listButton = findViewById(termListButton);
        detailButton = findViewById(termDetailButton);
    }

    private void initNavs() {
        if(getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            initPortraitNav();
        } else {
            initLandscapeNav();
        }
    }

    private void initPortraitNav() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.show();
        //Setup Bottom Navigation Menu
        bottomNavigation = findViewById(bottomNavView);
        bottomNavigation.setSelectedItemId(termsButton);
        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case homeButton:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case termsButton:
                        return true;
                    case classesButton:
                        startActivity(new Intent(getApplicationContext(), Classes.class));
                        overridePendingTransition(0,0);
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

    private void initLandscapeNav() {
        getSupportActionBar().hide();
        landHomeButton = findViewById(homeFromTermButton);
        landClassButton = findViewById(classFromTermButton);
        landAssessButton = findViewById(assessmentFromTermButton);

        landHomeButton.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            overridePendingTransition(0,0);
        });

        landClassButton.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), Classes.class));
            overridePendingTransition(0,0);
        });

        landAssessButton.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), Assessments.class));
            overridePendingTransition(0,0);
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        ActionBar actionBar = getSupportActionBar();
        if(getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.show();
        } else {
            actionBar.hide();
        }
    }

    /**
     * Switch statement to determine which fragment to switch to
     * @param view the button pressed
     */
    public void switchFragment(View view) {
        Fragment frag = null;
        switch (view.getId()) {
            case termDetailButton:
                setBottomNavVisible(View.GONE);
                if (selectedTerm == null) {
                    frag = new TermDetailsFragment();
                } else {
                    frag = TermDetailsFragment.newInstance(selectedTerm);
                    selectedTerm = null;
                }
                break;
            case termListButton:
                setBottomNavVisible(View.VISIBLE);
                frag = new TermListFragment();
                break;
        }
        setFragment(frag);
    }

    private void setBottomNavVisible(int visible) {
        if(getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            bottomNavigation.setVisibility(visible);
        }
    }


    /**
     * Replaces the current fragment with another
     * @param frag the new fragment
     */
    public void setFragment(Fragment frag) {
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.replace(fragmentContainerView, frag);
        transaction.commit();
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

    public static void setSelectedTerm(TermObj term) {
        selectedTerm = term;
    }
}