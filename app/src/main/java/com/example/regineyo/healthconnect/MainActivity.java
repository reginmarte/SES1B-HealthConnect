package com.example.regineyo.healthconnect;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText emailET;
    private TextInputEditText passwordET;
    private Button forgotPasswordBtn;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);

        findViewById(R.id.loginBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputLayout til = findViewById(R.id.TIL_email);
                TextInputLayout til2 = findViewById(R.id.TIL_password);
                til.setError(null);
                til2.setError(null);

                // THIS CHECKS FOR VALID INPUT
                if (emailET.getText().toString().trim().isEmpty()
                        || !Patterns.EMAIL_ADDRESS.matcher(emailET.getText().toString().trim()).matches()) {
                    til.setError("Please enter a valid email");
                    return;
                }
                if (passwordET.getText().toString().trim().isEmpty()) {
                    til2.setError("Please enter password");
                    return;
                }
                goToMain();
            }
        });

        //user selects register button
        findViewById(R.id.registerBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent RegisterIntent = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(RegisterIntent);
            }
        });

        //user selects forgot password button
        findViewById(R.id.forgotPasswordBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    //user goes to homepage
    public void goToMain() {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

    }
}
