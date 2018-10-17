package com.example.regineyo.healthconnect;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class DoctorEditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "DoctorEditProfileActivity";
    private TextInputEditText nameET, emailET, numberET, sCodeET;
//    private String clinic;
    private RadioGroup genderRadioGroup;
    private RadioButton genderRadioButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_edit_profile);
        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();
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
                String sCode = dataSnapshot.child("doctors").child(userID).child("sCode").getValue(String.class);
                RadioButton maleBtn = findViewById(R.id.maleRB);
                RadioButton femaleBtn = findViewById(R.id.femaleRB);

                nameET.setText(name);
                emailET.setText(email);
                numberET.setText(number);
                if(gender.equalsIgnoreCase("male")){
                    maleBtn.setChecked(true);
                } else {
                    femaleBtn.setChecked(true);
                }
                sCodeET.setText(sCode);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mRef.addValueEventListener(doctorDetailsListener);

        nameET = findViewById(R.id.nameET);
        emailET = findViewById(R.id.emailET);
        numberET = findViewById(R.id.numberET);
        genderRadioGroup = findViewById(R.id.genderRG);
        sCodeET = findViewById(R.id.sCodeET);

        findViewById(R.id.passwordBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent RegisterIntent = new Intent(DoctorEditProfileActivity.this, ChangePasswordActivity.class);
                startActivity(RegisterIntent);
            }
        });

        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.confirmBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInputs();
            }
        });
    }

    @Override
    public void onClick(View v) {
    }

    private void checkInputs() {
        TextInputLayout nameTIL = findViewById(R.id.TIL_name);
        final TextInputLayout emailTIL = findViewById(R.id.TIL_email);
        TextInputLayout numberTIL = findViewById(R.id.TIL_number);
        final TextInputLayout sCodeTIL = findViewById(R.id.TIL_sCode);

        nameTIL.setError(null);
        emailTIL.setError(null);
        numberTIL.setError(null);
        sCodeTIL.setError(null);

        final String email = emailET.getText().toString().trim();

        if (nameET.getText().toString().trim().isEmpty()) {
            nameTIL.setError("Please enter your name");
            nameET.requestFocus();
            return;
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {   // THIS METHOD CHECKS IF ITS A REAL EMAIL
            emailTIL.setError("Please enter a valid email");
            emailET.requestFocus();
            return;
        }

        if (numberET.getText().toString().trim().isEmpty()
                || numberET.getText().length() < 8) {
            numberTIL.setError("Please enter a valid contact number");
            numberET.requestFocus();
            return;
        }

        if (sCodeET.getText().toString().trim().isEmpty()
                || Integer.parseInt(sCodeET.getText().toString().trim()) <= 0) {
            sCodeTIL.setError("Please enter your clinic code");
            sCodeET.requestFocus();
            return;
        }

        DatabaseReference centreRef = FirebaseDatabase.getInstance().getReference().child("health_care_centre");
        centreRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.child(sCodeET.getText().toString().trim()).exists()) {
                    //clinic doesn't exist
                    sCodeTIL.setError("Clinic code does not exist");
                    sCodeET.requestFocus();
                } else {
                    saveDetails();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void saveDetails() {

        final String sCode = sCodeET.getText().toString().trim();
        final String name = nameET.getText().toString().trim();
        final String email = emailET.getText().toString().trim();
        final String number = numberET.getText().toString().trim();
        int genderID = genderRadioGroup.getCheckedRadioButtonId();
        genderRadioButton = findViewById(genderID);
        final String genderSelect = genderRadioButton.getText().toString();

        user = mAuth.getCurrentUser();
        final String userID = mAuth.getCurrentUser().getUid();
        final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("doctors").child(userID);

        //update clinic's doctors list
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot snapshot) {
                String currentCode = snapshot.child("sCode").getValue(String.class);
                DatabaseReference doctorClinicRef = FirebaseDatabase.getInstance().getReference()
                        .child("health_care_centre")
                        .child(currentCode)
                        .child("doctor")
                        .child(userID);

                if (currentCode.equalsIgnoreCase(sCodeET.getText().toString().trim())) {
                    //same clinic
                    Map dbUpdates = new HashMap();
                    dbUpdates.put("name", name);
                    doctorClinicRef.updateChildren(dbUpdates);

                } else {
                    //different clinic
                    Map dbUpdates = new HashMap();
                    dbUpdates.put("name", name);
                    FirebaseDatabase.getInstance().getReference()
                            .child("health_care_centre")
                            .child(sCodeET.getText().toString().trim())
                            .child("doctor")
                            .child(userID)
                            .updateChildren(dbUpdates);
                    doctorClinicRef.removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        //adds user to database
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name).build();
        user.updateProfile(profileUpdates);

        user.updateEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User email address updated.");
                        }
                    }
                });

        FirebaseDatabase.getInstance().getReference().child("health_care_centre").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getKey().equalsIgnoreCase(sCode)) {
                        String clinic = ds.child("name").getValue(String.class);
                        DoctorInfo doctorInfo = new DoctorInfo(name, email, number, genderSelect, sCode, clinic);
                        Map<String, Object> doctorValues = doctorInfo.toMap();
                        userRef.updateChildren(doctorValues);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        updateUI(user);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            finish();
        }
    }
}
