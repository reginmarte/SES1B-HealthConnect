package com.example.regineyo.healthconnect;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class DoctorProfileFragment extends Fragment {

    //This is for doctor's own use. They view this and edit this information.

    private TextView nameTV, emailTV, numberTV, genderTV, clinicTV, specialtyTV;
    public static final String TAG = "DoctorProfileFragment";
    private FirebaseAuth mAuth;

    public DoctorProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment DoctorProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
//    public static DoctorProfileFragment newInstance(String param1, String param2) {
//        DoctorProfileFragment fragment = new DoctorProfileFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doctor_profile, container, false);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        final String userID = user.getUid();
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        //gets user data from database
        ValueEventListener doctorDetailsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("doctors").child(userID).child("name").getValue(String.class);
                String email = dataSnapshot.child("doctors").child(userID).child("email").getValue(String.class);
                String number = dataSnapshot.child("doctors").child(userID).child("number").getValue(String.class);
                String gender = dataSnapshot.child("doctors").child(userID).child("gender").getValue(String.class);
                String clinicName = dataSnapshot.child("doctors").child(userID).child("clinic").getValue(String.class);

                nameTV.setText(name);
                emailTV.setText(email);
                numberTV.setText(number);
                genderTV.setText(gender);
                clinicTV.setText(clinicName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mRef.addValueEventListener(doctorDetailsListener);

        nameTV = view.findViewById(R.id.nameTV);
        emailTV = view.findViewById(R.id.emailTV);
        numberTV = view.findViewById(R.id.numberTV);
        genderTV = view.findViewById(R.id.genderTV);
        clinicTV = view.findViewById(R.id.clinicTV);
//        specialtyTV = findViewById(R.id.specialtyTV);

        ImageButton editImageBtn = view.findViewById(R.id.editImageBtn);
        editImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent RegisterIntent = new Intent(getActivity(), DoctorEditProfileActivity.class);
                startActivity(RegisterIntent);
            }
        });

        return view;
    }

}
