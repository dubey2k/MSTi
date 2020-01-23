package com.example.msti.model;

public class userData {
    private String name;
    private String phoneNo;
    private String enroll;
    private String college;
    private String shift;
    private String branch;
    private String year;
    private String description;
    private String Gender;
    private String dob;
    private String profilePic;
    private String [] morePics;


    public userData(String name, String phoneNo, String enroll,
                    String college, String shift, String branch,
                    String year, String gender, String profilePic) {
        this.name = name;
        this.phoneNo = phoneNo;
        this.enroll = enroll;
        this.college = college;
        this.shift = shift;
        this.branch = branch;
        this.year = year;
        Gender = gender;
        this.profilePic = profilePic;
    }

    public userData(String name, String phoneNo, String enroll
            , String college, String shift, String branch, String year
            , String description, String gender, String dob,
                    String profilePic, String[] morePics) {
        this.name = name;
        this.phoneNo = phoneNo;
        this.enroll = enroll;
        this.college = college;
        this.shift = shift;
        this.branch = branch;
        this.year = year;
        this.description = description;
        Gender = gender;
        this.dob = dob;
        this.profilePic = profilePic;
        this.morePics = morePics;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getEnroll() {
        return enroll;
    }

    public String getCollege() {
        return college;
    }

    public String getShift() {
        return shift;
    }

    public String getBranch() {
        return branch;
    }

    public String getYear() {
        return year;
    }

    public String getDescription() {
        return description;
    }

    public String getGender() {
        return Gender;
    }

    public String getDob() {
        return dob;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public String[] getMorePics() {
        return morePics;
    }



}
