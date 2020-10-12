package com.example.dobble.release.repositories.local.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.dobble.release.repositories.remote.payload.response.ProfileResponse;

@Entity
public class ProfileEntity {

    @PrimaryKey
    private long id;

    private String  city;

    private String state;

    private String gender;

    private String dob;

    private String firstName;

    private String lastName;

    private String email;

    private Long avatarId;

    private Boolean link;

    public ProfileEntity() { }

    public ProfileEntity(ProfileResponse profileResponse, Long avatarId, Boolean link) {
        id = profileResponse.getId();
        city = profileResponse.getCity();
        state = profileResponse.getState();
        gender = profileResponse.getGender();
        dob = profileResponse.getDob();
        firstName = profileResponse.getFirstName();
        lastName = profileResponse.getLastName();
        email = profileResponse.getEmail();
        this.avatarId = avatarId;
        this.link = link;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public Long getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(Long avatarId) {
        this.avatarId = avatarId;
    }

    public Boolean getLink() {
        return link;
    }

    public void setLink(Boolean link) {
        this.link = link;
    }
}
