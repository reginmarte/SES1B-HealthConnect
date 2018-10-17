package com.example.regineyo.healthconnect;

import android.content.Intent;
import android.support.annotation.NonNull;
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

public class ClinicListActivity extends AppCompatActivity {

//    String clinicID;
    TextView clinicTitle;
    ListView doctorsLV;
    ArrayList<String> listofDoctors = new ArrayList<String>();
    ArrayAdapter arrayAdapt;
    DatabaseReference centreRef = FirebaseDatabase.getInstance().getReference().child("health_care_centre");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_list);

        doctorsLV = findViewById(R.id.doctorList);
        arrayAdapt = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listofDoctors);
        doctorsLV.setAdapter(arrayAdapt);
        doctorsLV.setEmptyView(findViewById(R.id.empty));

        final String clinicName = getIntent().getStringExtra("clinicName");
        clinicTitle = findViewById(R.id.clinicNameTV);
        clinicTitle.setText(clinicName);

        centreRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("name").getValue(String.class).equalsIgnoreCase(clinicName)) {
                        String clinicID = ds.getKey();
                        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference()
                                .child("health_care_centre")
                                .child(clinicID)
                                .child("doctor");

                        dbr.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Set<String> set = new HashSet<>();

                                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                    if(ds.child("name").exists()) {
                                        set.add(ds.child("name").getValue(String.class));
                                    } else {
                                        set.add("No doctors currently available");
                                    }
                                }
                                arrayAdapt.clear();
                                arrayAdapt.addAll(set);
                                arrayAdapt.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {}
                        });
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        doctorsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ClinicListActivity.this, PatientChatActivity.class);
                i.putExtra("selected_doctor", ((TextView)view).getText().toString());
                startActivity(i);
            }
        });
    }
}
