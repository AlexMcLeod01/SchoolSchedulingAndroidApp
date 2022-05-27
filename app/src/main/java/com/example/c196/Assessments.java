package com.example.c196;

import static com.example.c196.R.id.assessmentsButton;
import static com.example.c196.R.id.bottomNavView;
import static com.example.c196.R.id.classesButton;
import static com.example.c196.R.id.homeButton;
import static com.example.c196.R.id.termsButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Assessments extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessments);

        //Setup Bottom Navigation Menu
        BottomNavigationView bottomNavigation = findViewById(bottomNavView);
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
}