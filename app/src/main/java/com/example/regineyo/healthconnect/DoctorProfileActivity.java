package com.example.regineyo.healthconnect;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DoctorProfileActivity extends AppCompatActivity {

    private TextView nameTV, emailTV, numberTV, genderTV, clinicTV, specialtyTV;
    public static final String TAG = "DoctorProfileActivity";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        final String userID = user.getUid();
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        //gets user data from database
        ValueEventListener doctorDetailsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("doctors").child(userID).child("name").getValue(String.class);
                String email = dataSnapshot.child("doctors").child(userID).child("email").getValue(String.class);
                String number = dataSnapshot.child("doctors").child(userID).child("number").getValue(String.class);
                String gender = dataSnapshot.child("doctors").child(userID).child("gender").getValue(String.class);
                String clinicName = dataSnapshot.child("doctors").child(userID).child("clinic").getValue(String.class);

                nameTV.setText(name);
                emailTV.setText(email);
                numberTV.setText(number);
                genderTV.setText(gender);
                clinicTV.setText(clinicName);
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
        genderTV = findViewById(R.id.genderTV);
        clinicTV = findViewById(R.id.clinicTV);
//        specialtyTV = findViewById(R.id.specialtyTV);

        ImageButton editImageBtn = findViewById(R.id.editImageBtn);
        editImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent RegisterIntent = new Intent(DoctorProfileActivity.this, DoctorEditProfileActivity.class);
                startActivity(RegisterIntent);
            }
        });

    }
}
