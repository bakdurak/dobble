package com.example.dobble.release.repositories.remote.payload.response;

public class PhotosResponse {
    private Long id;

    public PhotosResponse() { }

    public PhotosResponse(long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
