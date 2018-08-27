package com.example.regineyo.healthconnect;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        findViewById(R.id.nextBtn).setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        Intent Register2Intent = new Intent(RegistrationActivity.this, Registration2Activity.class);
        startActivity(Register2Intent);
    }
}
