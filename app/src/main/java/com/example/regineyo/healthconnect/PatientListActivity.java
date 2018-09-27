package com.example.regineyo.healthconnect;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PatientListActivity extends AppCompatActivity {

    ListView patientsLV;
    ArrayList<String> listofPatients = new ArrayList<String>();
    ArrayAdapter arrayAdpt;
    private DatabaseReference dbr = FirebaseDatabase.getInstance().getReference().child("patients");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);

        patientsLV = (ListView) findViewById(R.id.patientList);
        arrayAdpt = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listofPatients);
        patientsLV.setAdapter(arrayAdpt);

        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<>();

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    set.add(ds.child("name").getValue(String.class));
                }

                arrayAdpt.clear();
                arrayAdpt.addAll(set);
                arrayAdpt.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        patientsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(PatientListActivity.this, DoctorChatActivity.class);
                i.putExtra("selected_patient", ((TextView)view).getText().toString());
                startActivity(i);
            }
        });
    }
}

