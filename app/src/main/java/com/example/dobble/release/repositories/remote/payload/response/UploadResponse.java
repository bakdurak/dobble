package com.example.dobble.release.repositories.remote.payload.response;

public class UploadResponse {
    private Long id;

    public UploadResponse() { }

    public UploadResponse(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
