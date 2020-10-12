package com.example.dobble.release.repositories.local.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.dobble.release.repositories.remote.payload.response.FriendResponse;
import com.example.dobble.release.repositories.remote.payload.response.UsersResponse;

@Entity
public class FriendEntity {
    @PrimaryKey
    private Long id;
    private String firstName;
    private String lastName;
    private Long avatarId;

    public FriendEntity() { }

    public FriendEntity(FriendResponse friendResponse) {
        id = friendResponse.getId();
        firstName = friendResponse.getFirstName();
        lastName = friendResponse.getLastName();
        avatarId = friendResponse.getAvatarId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(Long avatarId) {
        this.avatarId = avatarId;
    }
}
