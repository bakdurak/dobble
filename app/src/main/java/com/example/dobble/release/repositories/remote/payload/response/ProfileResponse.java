package com.example.dobble.release.repositories.remote.payload.response;


public class ProfileResponse {
    private Long id;

    private String  city;

    private String state;

    private String gender;

    private String dob;

    private String firstName;

    private String lastName;

    private String email;

    public ProfileResponse() { }

    public ProfileResponse(Long id, String city, String state, String gender, String dob, String firstName, String lastName, String email) {
        this.id = id;
        this.city = city;
        this.state = state;
        this.gender = gender;
        this.dob = dob;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
