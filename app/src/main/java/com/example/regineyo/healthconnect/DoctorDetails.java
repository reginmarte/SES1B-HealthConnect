package com.example.regineyo.healthconnect;

public class DoctorDetails {

    private String id;
    private String name;

    public DoctorDetails() { }
    public DoctorDetails(String name) { this.name = name; }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

}