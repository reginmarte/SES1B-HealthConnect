package com.example.regineyo.healthconnect;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordActivity extends AppCompatActivity {

    private static final String TAG = "ChangePasswordActivity";
    private TextInputEditText passwordET, confirmPasswordET;
    private FirebaseAuth mAuth;
    private Boolean isPatient = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        mAuth = FirebaseAuth.getInstance();

        passwordET = findViewById(R.id.passwordET);
        confirmPasswordET = findViewById(R.id.confirmPasswordET);

        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.saveBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInputs();
            }
        });
    }

    private void checkInputs() {
        TextInputLayout passwordTIL = findViewById(R.id.TIL_password);
        TextInputLayout confirmPasswordTIL = findViewById(R.id.TIL_confirmPassword);
        String password = passwordET.getText().toString().trim();
        String confirmPassword = confirmPasswordET.getText().toString().trim();

        passwordTIL.setError(null);
        confirmPasswordTIL.setError(null);

        if(password.isEmpty()){
            passwordTIL.setError("Please enter a password");
            passwordET.requestFocus();
            return;
        }

        if(password.length() < 6){
            passwordTIL.setError("Minimum of 6 characters required");
            passwordET.requestFocus();
            return;
        }

        if(confirmPassword.isEmpty()){
            confirmPasswordTIL.setError("Please confirm password");
            confirmPasswordET.requestFocus();
            return;
        }

        if(!confirmPassword.equalsIgnoreCase(password)){
            confirmPasswordTIL.setError("Password does not match");
            confirmPasswordET.requestFocus();
            return;
        }

        saveDetails();
    }

    private void saveDetails() {
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = mAuth.getCurrentUser().getUid();

        String newPassword = passwordET.getText().toString().trim();
        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User password updated.");
                        }
                    }
                });
        updateUI(user);
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
                            Intent intent = new Intent(ChangePasswordActivity.this, PatientHomePage.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    if (!isPatient) {
                        Intent intent = new Intent(ChangePasswordActivity.this, DoctorHomePage.class);
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
}
