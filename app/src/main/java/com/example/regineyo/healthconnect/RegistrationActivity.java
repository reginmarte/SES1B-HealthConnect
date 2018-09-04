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
    private TextInputEditText nameET;
    private TextInputEditText emailET;
    private TextInputEditText numberET;
    private TextInputEditText passwordET;
    private TextInputEditText confirmPasswordET;
    private TextView dateOfBirthTV;
    private TextInputEditText heightET;
    private TextInputEditText weightET;
    private DatePicker date;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        nameET = findViewById(R.id.nameET);
        emailET = findViewById(R.id.emailET);
        numberET = findViewById(R.id.numberET);
        passwordET = findViewById(R.id.passwordET);
        dateOfBirthTV = findViewById(R.id.showDate);
        heightET = findViewById(R.id.heightET);
        weightET = findViewById(R.id.weightET);

        RadioButton maleBtn = findViewById(R.id.maleRB);
        maleBtn.setChecked(true);

        findViewById(R.id.submitBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();

                nameET.setError(null);
                emailET.setError(null);
                numberET.setError(null);
                passwordET.setError(null);
                confirmPasswordET.setError(null);
                dateOfBirthTV.setError(null);
                heightET.setError(null);
                weightET.setError(null);

                String email = emailET.getText().toString().trim();
                String password = passwordET.getText().toString().trim();

                if(nameET.getText().toString().trim().isEmpty()){
                    nameET.setError("Please enter name");
                    nameET.requestFocus();
                    return;
                }

                if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){   // THIS METHOD CHECKS IF ITS A REAL EMAIL
                    emailET.setError("Invalid email");
                    emailET.requestFocus();
                    return;
                }

                if(numberET.getText().toString().trim().isEmpty()
                        || numberET.getText().length() < 8){
                    numberET.setError("Invalid number");
                    numberET.requestFocus();
                    return;
                }
                if(password.isEmpty()){
                    passwordET.setError("Please enter password");
                    passwordET.requestFocus();
                    return;
                }

                if(dateOfBirthTV.getText().toString().equalsIgnoreCase("day/month/year")) {
                    dateOfBirthTV.setError("Please select date of birth");
                    return;
                }

                if(heightET.getText().toString().trim().isEmpty()
                        || Integer.parseInt(heightET.getText().toString().trim()) <= 0){
                    heightET.setError("Please enter height");
                    heightET.requestFocus();
                    return;
                }
                if(weightET.getText().toString().trim().isEmpty()
                        || Integer.parseInt(weightET.getText().toString().trim()) <= 0){
                    weightET.setError("Please enter weight");
                    weightET.requestFocus();
                    return;
                }

//                mAuth.createUserWithEmailAndPassword(email, password)
//                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                if (task.isSuccessful()) {
//                                    Toast.makeText(RegistrationActivity.this, "User Registered",
//                                            Toast.LENGTH_SHORT).show();
//                                    goToMain();
//                                    // Sign in success, update UI with the signed-in user's information
//                                } else if(task.getException() instanceof FirebaseAuthUserCollisionException){
//                                    // If sign in fails, display a message to the user.
//                                    Toast.makeText(RegistrationActivity.this, "User Exists",
//                                            Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        }

                goToMain();
            }
        });
    }

    @Override
    public void onClick(View v) {
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

    public void goToMain() {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }

}
