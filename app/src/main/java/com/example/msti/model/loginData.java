package com.example.msti.model;

public class loginData {
    private String phone_NO;
    private String enrollment_NO;
    private String gender;

    public loginData(String phone_NO, String enrollment_NO, String gender) {
        this.phone_NO = phone_NO;
        this.enrollment_NO = enrollment_NO;
        this.gender = gender;
    }

    public String getPhone_NO() {
        return phone_NO;
    }

    public void setPhone_NO(String phone_NO) {
        this.phone_NO = phone_NO;
    }

    public String getEnrollment_NO() {
        return enrollment_NO;
    }

    public void setEnrollment_NO(String enrollment_NO) {
        this.enrollment_NO = enrollment_NO;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
