package com.example.dobble.release.repositories.remote.payload.response;

public class UploadAvatarResponse {
    String avatarUrl;
    Long avatarId;

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public Long getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(Long avatarId) {
        this.avatarId = avatarId;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
