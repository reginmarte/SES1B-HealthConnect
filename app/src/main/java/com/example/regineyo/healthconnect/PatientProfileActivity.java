package com.example.regineyo.healthconnect;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PatientProfileActivity extends AppCompatActivity {

    private TextView nameTV, emailTV, numberTV, genderTV, heightTV, weightTV, dateOfBirthTV;
    private String userID;
    public static final String TAG = "PatientProfileActivity";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);

        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        userID = getIntent().getStringExtra("patient");
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        //gets user data from database
        ValueEventListener doctorDetailsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("patients").child(userID).child("name").getValue(String.class);
                String email = dataSnapshot.child("patients").child(userID).child("email").getValue(String.class);
                String number = dataSnapshot.child("patients").child(userID).child("number").getValue(String.class);
                String birthday = dataSnapshot.child("patients").child(userID).child("birthday").getValue(String.class);
                String gender = dataSnapshot.child("patients").child(userID).child("gender").getValue(String.class);
                String height = dataSnapshot.child("patients").child(userID).child("height").getValue(String.class);
                String weight = dataSnapshot.child("patients").child(userID).child("weight").getValue(String.class);

                nameTV.setText(name);
                emailTV.setText(email);
                numberTV.setText(number);
                dateOfBirthTV.setText(birthday);
                genderTV.setText(gender);
                heightTV.setText(height);
                weightTV.setText(weight);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mRef.addValueEventListener(doctorDetailsListener);

        nameTV = findViewById(R.id.nameTV);
        emailTV = findViewById(R.id.emailTV);
        numberTV = findViewById(R.id.numberTV);
        dateOfBirthTV = findViewById(R.id.dateOfBirthTV);
        genderTV = findViewById(R.id.genderTV);
        heightTV = findViewById(R.id.heightTV);
        weightTV = findViewById(R.id.weightTV);

    }
}
