package com.example.regineyo.healthconnect;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
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

public class DoctorRegistrationActivity extends AppCompatActivity implements View.OnClickListener {
//        DatePickerDialog.OnDateSetListener

    private static final String TAG = "RegistrationActivity";
    private TextInputEditText nameET, emailET, numberET, passwordET, sCodeET;
    private RadioGroup genderRadioGroup;
    private RadioButton genderRadioButton;
    //    private TextView dateOfBirthTV;
//    private int age;
//    private String birthday;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_registration);
        mAuth = FirebaseAuth.getInstance();

        nameET = findViewById(R.id.nameET);
        emailET = findViewById(R.id.emailET);
        numberET = findViewById(R.id.numberET);
        passwordET = findViewById(R.id.passwordET);
//        dateOfBirthTV = findViewById(R.id.showDate);
        genderRadioGroup = findViewById(R.id.genderRG);
//        ageTV = findViewById(R.id.showAge);
        sCodeET = findViewById(R.id.sCodeET);

        RadioButton maleBtn = findViewById(R.id.maleRB);
        maleBtn.setChecked(true);

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
        final TextInputLayout passwordTIL = findViewById(R.id.TIL_password);
        final TextInputLayout sCodeTIL = findViewById(R.id.TIL_sCode);

        nameTIL.setError(null);
        emailTIL.setError(null);
        numberTIL.setError(null);
        passwordTIL.setError(null);
//        dateOfBirthTV.setError(null);
        sCodeTIL.setError(null);

        final String email = emailET.getText().toString().trim();
        final String password = passwordET.getText().toString().trim();

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

        if (password.isEmpty()) {
            passwordTIL.setError("Please enter a password");
            passwordET.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passwordTIL.setError("Minimum of 6 characters required");
            passwordET.requestFocus();
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


        DatabaseReference clinicsRef = FirebaseDatabase.getInstance().getReference().child("health_care_centre");
        clinicsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.child(sCodeET.getText().toString().trim()).exists()) {
                    //clinic doesn't exist
                    sCodeTIL.setError("Clinic code does not exist");
                    sCodeET.requestFocus();
                } else {
                    createAccount(email, password);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(DoctorRegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");

                            String name = nameET.getText().toString().trim();
                            String email = emailET.getText().toString().trim();
                            String number = numberET.getText().toString().trim();
                            int genderID = genderRadioGroup.getCheckedRadioButtonId();
                            genderRadioButton = findViewById(genderID);
                            String genderSelect = genderRadioButton.getText().toString();
                            String sCode = sCodeET.getText().toString().trim();

                            FirebaseUser user = mAuth.getCurrentUser();
                            String userID = mAuth.getCurrentUser().getUid();
                            DatabaseReference currentUser_db = FirebaseDatabase.getInstance().getReference()
                                    .child("health_care_centre")
                                    .child(sCode)
                                    .child("doctor")
                                    .child(userID);

                            //adds user to database
                            Map newUser = new HashMap();
                            newUser.put("name", name);
                            newUser.put("email", email);
                            newUser.put("number", number);
                            newUser.put("gender", genderSelect);
                            newUser.put("clinic", sCode);
                            newUser.put("type", "doctor");

                            currentUser_db.setValue(newUser);
                            updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(DoctorRegistrationActivity.this, "Registration failed",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(this, HomePage.class);
            startActivity(intent);
            finish();
        }
    }
}