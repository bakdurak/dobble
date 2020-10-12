package com.example.dobble.release.repositories.remote.payload.body;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterUserBody {
    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("firstName")
    @Expose
    private String firstName;

    @SerializedName("lastName")
    @Expose
    private String lastName;

    @SerializedName("city")
    @Expose
    private String city;

    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("gender")
    @Expose
    private String gender;

    @SerializedName("dob")
    @Expose
    private String dob;

    public RegisterUserBody() {}

    public RegisterUserBody(String email, String password, String firstName, String lastName, String city, String state, String gender, String dob) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.state = state;
        this.gender = gender;
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
