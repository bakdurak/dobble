package com.example.dobble.release.repositories.remote.payload.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckAuthResponse {

    @SerializedName("userId")
    @Expose
    private Long userId;

    public CheckAuthResponse() {}

    public CheckAuthResponse(long id) {
        this.userId = id;
    }

    public Long getId() {
        return userId;
    }

    public void setId(Long id) {
        this.userId = id;
    }
}
