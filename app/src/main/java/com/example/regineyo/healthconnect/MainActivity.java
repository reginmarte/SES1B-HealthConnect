package com.example.regineyo.healthconnect;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText usernameET;
    private EditText passwordET;
    private Button forgotPasswordBtn;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameET = findViewById(R.id.usernameET);
        passwordET = findViewById(R.id.passwordET);

        findViewById(R.id.registerBtn).setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        Intent RegisterIntent = new Intent(MainActivity.this, RegistrationActivity.class);
        startActivity(RegisterIntent);
    }

}
