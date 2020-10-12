package com.example.dobble.release.repositories.remote.payload.response;

import com.example.dobble.release.repositories.local.entities.ProfileEntity;
import com.google.gson.annotations.SerializedName;

public class GetUserByIdResponse {
    @SerializedName("user")
    private ProfileResponse profileResponse;

    @SerializedName("upload")
    private UploadResponse uploadResponse;

    private Boolean link;

    public GetUserByIdResponse() { }

    public GetUserByIdResponse(ProfileEntity entity) {
        profileResponse = new ProfileResponse(entity.getId(), entity.getCity(), entity.getState(), entity.getGender(),
            entity.getDob(), entity.getFirstName(), entity.getLastName(), entity.getEmail());
        uploadResponse = new UploadResponse(entity != null ? entity.getAvatarId() : null);
        link = entity.getLink();
    }

    public GetUserByIdResponse(ProfileResponse profileResponse, UploadResponse uploadResponse, boolean link) {
        this.profileResponse = profileResponse;
        this.uploadResponse = uploadResponse;
        this.link = link;
    }

    public ProfileResponse getProfileResponse() {
        return profileResponse;
    }

    public void setProfileResponse(ProfileResponse profileResponse) {
        this.profileResponse = profileResponse;
    }

    public UploadResponse getUploadResponse() {
        return uploadResponse;
    }

    public void setUploadResponse(UploadResponse uploadResponse) {
        this.uploadResponse = uploadResponse;
    }

    public Boolean isLink() {
        return link;
    }

    public void setLink(Boolean link) {
        this.link = link;
    }

    public boolean isUserResponseInconsistent() {
        ProfileResponse u = profileResponse;
        return u.getId() == null || u.getCity() == null || u.getState() == null
            || u.getGender() == null || u.getDob() == null || u.getFirstName() == null
            || u.getLastName() == null;
    }
}
