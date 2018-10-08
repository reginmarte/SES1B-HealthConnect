package com.example.regineyo.healthconnect;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class DoctorInfo {

    public String name, email, number, gender, sCode, clinic;

    public DoctorInfo() {
        // Default constructor required for calls to DataSnapshot.getValue(DoctorInfo.class)
    }

    public DoctorInfo(String name, String email, String number, String gender, String sCode, String clinic) {
        this.name = name;
        this.email = email;
        this.number = number;
        this.gender = gender;
        this.sCode = sCode;
        this.clinic = clinic;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("email", email);
        result.put("number", number);
        result.put("gender", gender);
        result.put("sCode", sCode);
        result.put("clinic", clinic);

        return result;
    }

}
