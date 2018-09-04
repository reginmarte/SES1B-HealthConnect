package com.example.regineyo.healthconnect;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private static final String TAG = "RegistrationActivity";
    private TextInputEditText nameET, emailET, numberET, passwordET, heightET, weightET;
    private TextView dateOfBirthTV;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();

        nameET = findViewById(R.id.nameET);
        emailET = findViewById(R.id.emailET);
        numberET = findViewById(R.id.numberET);
        passwordET = findViewById(R.id.passwordET);
        dateOfBirthTV = findViewById(R.id.showDate);
        heightET = findViewById(R.id.heightET);
        weightET = findViewById(R.id.weightET);

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

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void checkInputs() {
        TextInputLayout nameTIL = findViewById(R.id.TIL_name);
        TextInputLayout emailTIL = findViewById(R.id.TIL_email);
        TextInputLayout numberTIL = findViewById(R.id.TIL_number);
        TextInputLayout passwordTIL = findViewById(R.id.TIL_password);
        TextInputLayout heightTIL = findViewById(R.id.TIL_height);
        TextInputLayout weightTIL = findViewById(R.id.TIL_weight);

        nameTIL.setError(null);
        emailTIL.setError(null);
        numberTIL.setError(null);
        passwordTIL.setError(null);
        dateOfBirthTV.setError(null);
        heightTIL.setError(null);
        weightTIL.setError(null);

        String email = emailET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();

        if(nameET.getText().toString().trim().isEmpty()){
            nameTIL.setError("Please enter your name");
            nameET.requestFocus();
            return;
        }

        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){   // THIS METHOD CHECKS IF ITS A REAL EMAIL
            emailTIL.setError("Please enter a valid email");
            emailET.requestFocus();
            return;
        }

        if(numberET.getText().toString().trim().isEmpty()
                || numberET.getText().length() < 8){
            numberTIL.setError("Please enter a valid contact number");
            numberET.requestFocus();
            return;
        }
        if(password.isEmpty()){
            passwordTIL.setError("Please enter a password");
            passwordET.requestFocus();
            return;
        }

        if(dateOfBirthTV.getText().toString().equalsIgnoreCase("day/month/year")) {
            dateOfBirthTV.setError("Please select date of birth");
            return;
        }

        if(heightET.getText().toString().trim().isEmpty()
                || Integer.parseInt(heightET.getText().toString().trim()) <= 0){
            heightTIL.setError("Please enter your height");
            heightET.requestFocus();
            return;
        }

        if(weightET.getText().toString().trim().isEmpty()
                || Integer.parseInt(weightET.getText().toString().trim()) <= 0){
            weightTIL.setError("Please enter your weight");
            weightET.requestFocus();
            return;
        }

        createAccount(email, password);
    }

    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegistrationActivity.this, "Registration failed",
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
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new RegistrationActivity.DatePickerFragment();
        newFragment.show(getSupportFragmentManager(),"datePicker" );
    }

    public void setDate(final Calendar calendar) {
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        dateOfBirthTV.setText(dateFormat.format(calendar.getTime()));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar cal = new GregorianCalendar(year, month, dayOfMonth);
        setDate(cal);
    }

    public static class DatePickerFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), year,month, day);
        }
    }
}
