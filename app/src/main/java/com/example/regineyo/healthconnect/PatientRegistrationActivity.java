package com.example.regineyo.healthconnect;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.InputStream;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class PatientRegistrationActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private static final String TAG = "PatientRegistrationActivity";
    private TextInputEditText nameET, emailET, numberET, passwordET, heightET, weightET;
    private RadioGroup genderRadioGroup;
    private RadioButton genderRadioButton;
    private TextView dateOfBirthTV;
    private int age;
    private String birthday;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_registration);
        mAuth = FirebaseAuth.getInstance();

        nameET = findViewById(R.id.nameET);
        emailET = findViewById(R.id.emailET);
        numberET = findViewById(R.id.numberET);
        passwordET = findViewById(R.id.passwordET);
        dateOfBirthTV = findViewById(R.id.showDate);
        genderRadioGroup = findViewById(R.id.genderRG);
//        ageTV = findViewById(R.id.showAge);
        heightET = findViewById(R.id.heightET);
        weightET = findViewById(R.id.weightET);

        RadioButton maleBtn = findViewById(R.id.maleRB);
        maleBtn.setChecked(true);

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

        if(numberET.getText().toString().trim().isEmpty()
                || numberET.getText().length() < 8){
            numberTIL.setError("Please enter a valid contact number");
            numberET.requestFocus();
            return;
        }

        if(age <= 0 || dateOfBirthTV.getText().toString().equalsIgnoreCase("day/month/year")) {
            dateOfBirthTV.setError("Please select date of birth");
            dateOfBirthTV.requestFocus();
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
                .addOnCompleteListener(PatientRegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userID = mAuth.getCurrentUser().getUid();
                            DatabaseReference currentUser_db = FirebaseDatabase.getInstance().getReference().child("patients").child(userID);

                            String name = nameET.getText().toString().trim();
                            String email = emailET.getText().toString().trim();
                            String number = numberET.getText().toString().trim();
                            int genderID = genderRadioGroup.getCheckedRadioButtonId();
                            genderRadioButton = findViewById(genderID);
                            String genderSelect = genderRadioButton.getText().toString();
                            String height = heightET.getText().toString().trim();
                            String weight = weightET.getText().toString().trim();

                            //adds user to database
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();
                            user.updateProfile(profileUpdates);

                            PatientInfo patientInfo = new PatientInfo(name, email, number, birthday, genderSelect, height, weight);

                            currentUser_db.setValue(patientInfo);
                            updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(PatientRegistrationActivity.this, "This email is already registered",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if(user != null) {
//            Intent intent = new Intent(this, PatientHomePage.class);
//            startActivity(intent);
            finish();
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new PatientRegistrationActivity.DatePickerFragment();
        newFragment.show(getSupportFragmentManager(),"datePicker" );
    }

    public void setDate(final Calendar calendar) {
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        dateOfBirthTV.setText(dateFormat.format(calendar.getTime()));
        birthday = dateFormat.format(calendar.getTime());
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar cal = new GregorianCalendar(year, month, dayOfMonth);
//        ageTV.setText(calculateAge(year,month,dayOfMonth));
        age = calculateAge(year, month, dayOfMonth);
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

    public int calculateAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

//        Integer ageInt = new Integer(age);
//        String ageS = ageInt.toString();
//        return ageS;

        return age;
    }
}