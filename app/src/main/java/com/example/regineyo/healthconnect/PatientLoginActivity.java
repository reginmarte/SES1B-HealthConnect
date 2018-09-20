package com.example.regineyo.healthconnect;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PatientLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "PatientLoginActivity" ;
    private TextInputEditText emailET, passwordET;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_login);
        mAuth = FirebaseAuth.getInstance();

        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);

        //user selects login
        findViewById(R.id.loginBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInputs();
            }
        });

        //user selects register button
        findViewById(R.id.registerBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent RegisterIntent = new Intent(PatientLoginActivity.this, PatientRegistrationActivity.class);
                startActivity(RegisterIntent);
            }
        });

        //user selects forgot password button
        findViewById(R.id.forgotPasswordBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent RegisterIntent = new Intent(PatientLoginActivity.this, ForgotPasswordActivity.class);
                startActivity(RegisterIntent);
            }
        });
    }

    @Override
    public void onClick(View v) {
    }

    public void loginUser(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
//                            String userID = mAuth.getCurrentUser().getUid();
//                            DatabaseReference patientsRef = FirebaseDatabase.getInstance().getReference().child("health_care_centre").child(child(userID);
//                            patientsRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(DataSnapshot snapshot) {
//                                    if (snapshot.child("type").getValue().toString().equalsIgnoreCase("doctor")) {
//                                        //patient login
//                                        Toast.makeText(PatientLoginActivity.this, "Incorrect login",
//                                                Toast.LENGTH_SHORT).show();
//                                    } else {
//
//                                    }
//                                }
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {}
//                            });
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(PatientLoginActivity.this, "Email and password does not match. Please try again.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if(user != null) {
            Intent intent = new Intent(this, HomePage.class);
            startActivity(intent);
            finish();
        }
    }

    public void checkInputs() {
        TextInputLayout til = findViewById(R.id.TIL_email);
        TextInputLayout til2 = findViewById(R.id.TIL_password);
        til.setError(null);
        til2.setError(null);

        String email = emailET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();

        // checks for valid input
        if (email.isEmpty()
                || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            til.setError("Please enter a valid email");
            emailET.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            til2.setError("Please enter password");
            passwordET.requestFocus();
            return;
        }

        loginUser(email, password);
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

}
