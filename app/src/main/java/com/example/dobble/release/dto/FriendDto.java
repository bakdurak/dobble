package com.example.dobble.release.dto;

import com.example.dobble.release.repositories.remote.payload.response.FriendResponse;

import retrofit2.Response;

public class FriendDto {
    private Response<Void> response;
    private FriendResponse friendResponse;

    public FriendDto(Response<Void> response, FriendResponse friendResponse) {
        this.response = response;
        this.friendResponse = friendResponse;
    }

    public Response<Void> getResponse() {
        return response;
    }

    public void setResponse(Response<Void> response) {
        this.response = response;
    }

    public FriendResponse getFriendResponse() {
        return friendResponse;
    }

    public void setFriendResponse(FriendResponse friendResponse) {
        this.friendResponse = friendResponse;
    }
}
