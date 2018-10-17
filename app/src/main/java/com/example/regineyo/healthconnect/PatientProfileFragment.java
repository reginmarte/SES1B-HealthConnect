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
 * Activities that contain this fragment must implement the
 * {@link PatientProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PatientProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientProfileFragment extends Fragment implements View.OnClickListener{

    private TextView nameTV, emailTV, numberTV, genderTV, heightTV, weightTV, dateOfBirthTV;
    public static final String TAG = "PatientProfileFragment";
    private FirebaseAuth mAuth;

    public PatientProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PatientProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
//    public static PatientProfileFragment newInstance(String param1, String param2) {
//        PatientProfileFragment fragment = new PatientProfileFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_patient_profile, container, false);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        final String userID = user.getUid();
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        //gets user data from database
        ValueEventListener patientDetailsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("patients").child(userID).child("name").getValue(String.class);
                String email = dataSnapshot.child("patients").child(userID).child("email").getValue(String.class);
                String number = dataSnapshot.child("patients").child(userID).child("number").getValue(String.class);
                String birthday = dataSnapshot.child("patients").child(userID).child("birthday").getValue(String.class);
                String gender = dataSnapshot.child("patients").child(userID).child("gender").getValue(String.class);
                String height = dataSnapshot.child("patients").child(userID).child("height").getValue(String.class);
                String weight = dataSnapshot.child("patients").child(userID).child("weight").getValue(String.class);

                nameTV.setText(name);
                emailTV.setText(email);
                numberTV.setText(number);
                dateOfBirthTV.setText(birthday);
                genderTV.setText(gender);
                heightTV.setText(height);
                weightTV.setText(weight);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mRef.addListenerForSingleValueEvent(patientDetailsListener);

        nameTV = view.findViewById(R.id.nameTV);
        emailTV = view.findViewById(R.id.emailTV);
        numberTV = view.findViewById(R.id.numberTV);
        dateOfBirthTV = view.findViewById(R.id.dateOfBirthTV);
        genderTV = view.findViewById(R.id.genderTV);
        heightTV = view.findViewById(R.id.heightTV);
        weightTV = view.findViewById(R.id.weightTV);

        ImageButton editImageBtn = view.findViewById(R.id.editImageBtn);
        editImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent RegisterIntent = new Intent(getActivity(), PatientEditProfileActivity.class);
                startActivity(RegisterIntent);
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {

    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
