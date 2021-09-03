package com.example.papu.user;

public class User {

    public String username;
    public String email;
    public String company;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, String company) {
        this.username = username;
        this.email = email;
        this.company = company;
    }

}