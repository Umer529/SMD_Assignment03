package com.example.mid1;

/**
 * User model class for Firebase authentication and data storage
 */
public class User {
    private String uid;
    private String email;
    private String name;
    private String phone;
    private String address;
    private String gender;
    private String dob;
    private String country;
    private String accountType; // "Buyer" or "Seller"

    // Default constructor for Firebase
    public User() {
    }

    public User(String email, String name, String phone, String address, 
                String gender, String dob, String country, String accountType) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.dob = dob;
        this.country = country;
        this.accountType = accountType;
    }

    // Getters and Setters
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}

