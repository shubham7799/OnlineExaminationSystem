package com.example.onlineexaminationsystem;

public class UserProfile {

    private String email;
    private String phone;
    private String name;
    private int role;
    public UserProfile(){}
    public UserProfile( String email,String phone,String name,int role) {
        this.role=role;
        this.email = email;
        this.name=name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone(){return phone;}

    public String getName() {
        return name;
    }

    public int getRole() {
        return role;
    }
}