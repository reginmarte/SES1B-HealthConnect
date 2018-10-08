package com.example.regineyo.healthconnect;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class DoctorHomePage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView mTextMessage;
    private Button logoutBtn;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_messages:
                    mTextMessage.setText(R.string.title_messages);
                {
                    Intent RegisterIntent = new Intent(DoctorHomePage.this, PatientListActivity.class);
                    startActivity(RegisterIntent);
                }
                return true;
//                case R.id.navigation_map:
//                    mTextMessage.setText(R.string.title_map);
//                {
//                    Intent RegisterIntent = new Intent(DoctorHomePage.this, MapActivity.class);
//                    startActivity(RegisterIntent);
//                }
//                return true;

                case R.id.navigation_profile:
                    mTextMessage.setText(R.string.title_profile);
                {
                    Intent RegisterIntent = new Intent(DoctorHomePage.this, DoctorProfileActivity.class);
                    startActivity(RegisterIntent);
                }

                case R.id.navigation_settings:
                    mTextMessage.setText(R.string.title_setting);
                {
                    findViewById(R.id.logoutBtn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FirebaseAuth.getInstance().signOut();
                            Intent RegisterIntent = new Intent(DoctorHomePage.this, MainActivity.class);
                            startActivity(RegisterIntent);
                            finish();
                        }
                    });
                }
                return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_home_page);
        mAuth = FirebaseAuth.getInstance();

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

}
