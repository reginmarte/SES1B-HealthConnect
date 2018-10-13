package com.example.regineyo.healthconnect;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorChatListFragment extends Fragment implements View.OnClickListener{

    ListView patientsLV;
    ArrayList<String> listOfPatients = new ArrayList<String>();
    ArrayAdapter arrayAdapt;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String mUserID = mAuth.getCurrentUser().getUid();
    private DatabaseReference dbr = FirebaseDatabase.getInstance().getReference()
            .child("doctors")
            .child(mUserID)
            .child("patient_chats");
//            .child("patients");

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    public DoctorChatListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doctor_chat_list, container, false);

        patientsLV = view.findViewById(R.id.patientList);
        arrayAdapt = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, listOfPatients);
        patientsLV.setAdapter(arrayAdapt);

        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<>();

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    if(ds.child("name").exists()) {
                        set.add(ds.child("name").getValue(String.class));
                    } else {
                        set.add("No current chats available");
                    }
                }
                arrayAdapt.clear();
                arrayAdapt.addAll(set);
                arrayAdapt.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        patientsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), DoctorChatActivity.class);
                i.putExtra("selected_patient", ((TextView)view).getText().toString());
                startActivity(i);
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
    }
}
