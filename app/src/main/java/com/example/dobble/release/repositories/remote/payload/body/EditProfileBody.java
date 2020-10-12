package com.example.dobble.release.repositories.remote.payload.body;

public class EditProfileBody {
    private String password;
    private String firstName;
    private String lastName;
    private String city;

    public EditProfileBody(String password, String firstName, String lastName, String city) {
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
    }
}
