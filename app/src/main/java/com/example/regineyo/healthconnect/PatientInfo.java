package com.example.regineyo.healthconnect;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class PatientInfo {
    public String name, email, number, birthday, gender, height, weight;

    public PatientInfo() {
        // Default constructor required for calls to DataSnapshot.getValue(DoctorInfo.class)
    }

    public PatientInfo(String name, String email, String number, String birthday, String gender, String height, String weight) {
        this.name = name;
        this.email = email;
        this.number = number;
        this.birthday = birthday;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("email", email);
        result.put("number", number);
        result.put("birthday", birthday);
        result.put("gender", gender);
        result.put("height", height);
        result.put("weight", weight);

        return result;
    }
}
