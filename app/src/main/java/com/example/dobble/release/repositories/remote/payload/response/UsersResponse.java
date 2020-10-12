package com.example.dobble.release.repositories.remote.payload.response;

import com.example.dobble.release.repositories.local.entities.FriendEntity;

public class UsersResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private Long avatarId;

    public UsersResponse() { }

    public UsersResponse(Long id, String firstName, String lastName, Long avatarId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatarId = avatarId;
    }

    public UsersResponse(Long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public UsersResponse(FriendEntity entity) {
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
