package com.example.c196;

import static com.example.c196.R.*;
import static com.example.c196.R.id.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);

        //Setup Bottom Navigation Menu
        BottomNavigationView bottomNavigation = findViewById(bottomNavView);
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
}