package com.example.outla.myapplication;



public class User {
    private String FirstName;
    private String LastName;
    private String URL;

    public User(String fName, String lName, String url) {
        FirstName = fName;
        LastName = lName;
        URL = url;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String url) { URL = url; }
}