package com.example.dobble.release.repositories.remote.payload.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckEmailResponse {
    @SerializedName("exists")
    @Expose
    private boolean exists;

    public CheckEmailResponse() { }

    public CheckEmailResponse(boolean exists) {
        this.exists = exists;
    }

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }
}
