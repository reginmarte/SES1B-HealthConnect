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

public class DoctorListActivity extends AppCompatActivity {

    ListView doctorsLV;
    ArrayList<String> listOfDoctors = new ArrayList<String>();
    ArrayAdapter arrayAdpt;
    private DatabaseReference dbr = FirebaseDatabase.getInstance().getReference().child("doctors");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_list);

        doctorsLV = findViewById(R.id.doctorList);
        arrayAdpt = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listOfDoctors);
        doctorsLV.setAdapter(arrayAdpt);

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

        doctorsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(DoctorListActivity.this, PatientChatActivity.class);
                i.putExtra("selected_doctor", ((TextView)view).getText().toString());
                startActivity(i);
            }
        });
    }
}