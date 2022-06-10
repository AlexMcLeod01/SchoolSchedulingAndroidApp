package com.example.c196;

import static com.example.c196.R.*;
import static com.example.c196.R.id.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //Portrait Navigation
    private BottomNavigationView bottomNavigation;

    //Landscape Navigation
    private Button landTermButton;
    private Button landClassButton;
    private Button landAssessButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        //Append fragment to ViewGroup ConstraintLayout
        transaction.add(mainFragmentContainer, new MainFragment());
        transaction.commit();

        initNavs();
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
        bottomNavigation.setSelectedItemId(homeButton);
        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case homeButton:
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
        landTermButton = findViewById(termFromHomeButton);
        landClassButton = findViewById(classFromHomeButton);
        landAssessButton = findViewById(assessmentFromHomeButton);

        landTermButton.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), Terms.class));
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
}