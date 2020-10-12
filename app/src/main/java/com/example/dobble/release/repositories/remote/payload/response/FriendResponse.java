package com.example.dobble.release.repositories.remote.payload.response;

import com.example.dobble.release.repositories.local.entities.FriendEntity;

public class FriendResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private Long avatarId;

    public FriendResponse() { }

    public FriendResponse(FriendEntity entity) {
        id = entity.getId();
        firstName = entity.getFirstName();
        lastName = entity.getLastName();
        avatarId = entity.getAvatarId();
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
