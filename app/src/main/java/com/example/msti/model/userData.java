package com.example.msti.model;

public class userData {
    private String name;
    private String description;
    private String Gender;
    private String dob;
    private String profilePic;
    private String [] morePics;

    public userData(String name, String description, String gender, String dob, String profilePic, String[] morePics) {
        this.name = name;
        this.description = description;
        Gender = gender;
        this.dob = dob;
        this.profilePic = profilePic;
        this.morePics = morePics;
    }
}
