package com.example.boinobolsov02.HelperClasses.Models;

public class User {

    String fullname;
    String username;
    String email;
    String phoneNo;
    String password;
    String date;
    String gender;
    String imageUrl;

    public User(){}

    public User(String fullname, String username, String email, String phoneNo, String password, String date, String gender, String imageUrl) {
        this.fullname = fullname;
        this.username = username;
        this.email = email;
        this.phoneNo = phoneNo;
        this.password = password;
        this.date = date;
        this.gender = gender;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
