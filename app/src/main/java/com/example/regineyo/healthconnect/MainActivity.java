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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    static{ System.loadLibrary("opencv_java3"); }
    private static final String TAG = "MainActivity" ;
    private TextInputEditText emailET, passwordET;
    private FirebaseAuth mAuth;
    private Boolean isPatient = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        //user selects forgot password button
        findViewById(R.id.forgotPasswordBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent RegisterIntent = new Intent(MainActivity.this, ForgotPasswordActivity.class);
                startActivity(RegisterIntent);
            }
        });

        //user selects register button
        findViewById(R.id.registerBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent RegisterIntent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(RegisterIntent);
            }
        });
    }

    @Override
    public void onClick(View v) {}

    public void loginUser(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            DatabaseReference dbr = FirebaseDatabase.getInstance().getReference().child("patients");
                            isPatient = false;
                            dbr.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                        if (ds.getKey().equalsIgnoreCase(mAuth.getCurrentUser().getUid())) {
                                            //user is a patient
                                            updateUI(mAuth.getCurrentUser());
                                            Toast.makeText(MainActivity.this, "Welcome " + mAuth.getCurrentUser().getDisplayName(),
                                                    Toast.LENGTH_SHORT).show();
                                            isPatient = true;
                                        }
                                    }

                                    if(!isPatient) {
                                        //user is a doctor
//                                        FirebaseAuth.getInstance().signOut();
                                        updateUI(mAuth.getCurrentUser());
                                        Toast.makeText(MainActivity.this, "Welcome Dr " + mAuth.getCurrentUser().getDisplayName(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {}
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Email and password does not match. Please try again.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if(user != null) {
            DatabaseReference dbr = FirebaseDatabase.getInstance().getReference().child("patients");
            dbr.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    isPatient = false;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.getKey().equalsIgnoreCase(mAuth.getCurrentUser().getUid())) {
                            //user is a doctor
                            isPatient = true;
                            Intent intent = new Intent(MainActivity.this, PatientHomePage.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    if (!isPatient) {
                        Intent intent = new Intent(MainActivity.this, DoctorHomePage.class);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

//    private void updateDoctorUI(FirebaseUser user) {
//        if(user != null) {
//
//        }
//    }

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
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null) {
//            if (isPatient) {
                DatabaseReference dbr = FirebaseDatabase.getInstance().getReference().child("patients");
                dbr.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        isPatient = false;
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            if (ds.getKey().equalsIgnoreCase(mAuth.getCurrentUser().getUid())) {
                                //user is a doctor
                                isPatient = true;
                                updateUI(mAuth.getCurrentUser());
                            }
                        }
                        if (!isPatient) {
                            Intent intent = new Intent(MainActivity.this, DoctorHomePage.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
//            }
        }
    }
}
