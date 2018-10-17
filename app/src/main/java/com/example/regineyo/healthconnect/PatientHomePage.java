package com.example.regineyo.healthconnect;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.maps.MapFragment;
import com.google.firebase.auth.FirebaseAuth;

public class PatientHomePage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;

    private PatientChatListFragment chatFragment;
    private MapFragment mapFragment;
    private PatientProfileFragment profileFragment;
    private SettingsFragment settingsFragment;
    private WelcomeFragment welcomeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home_page);
        mAuth = FirebaseAuth.getInstance();

        mMainFrame = (FrameLayout) findViewById(R.id.frame_container);
        mMainNav = (BottomNavigationView) findViewById(R.id.navigation);
        mMainNav.getMenu().getItem(0).setCheckable(false);

        chatFragment = new PatientChatListFragment();
        mapFragment = new MapFragment();
        profileFragment = new PatientProfileFragment();
        settingsFragment = new SettingsFragment();
        welcomeFragment = new WelcomeFragment();

        loadFragment(welcomeFragment);

        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.navigation_messages:
                        item.setCheckable(true);
                        loadFragment(chatFragment);
                        return true;
                    case R.id.navigation_map:
                        item.setCheckable(true);
                        Intent intent = new Intent(PatientHomePage.this, MapActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.navigation_profile:
                        item.setCheckable(true);
                        loadFragment(profileFragment);
                        return true;
                    case R.id.navigation_settings:
                        item.setCheckable(true);
                        loadFragment(settingsFragment);
                        return true;
                }
                return false;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}

