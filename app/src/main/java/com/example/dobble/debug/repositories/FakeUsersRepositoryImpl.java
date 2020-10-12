package com.example.dobble.debug.repositories;


import android.util.Log;

import com.example.dobble.release.repositories.UsersRepository;
import com.example.dobble.release.repositories.remote.payload.response.PhotosResponse;
import com.example.dobble.release.repositories.remote.payload.body.SendMessageBody;
import com.example.dobble.release.repositories.remote.payload.response.UsersResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Response;

public class FakeUsersRepositoryImpl implements UsersRepository {
    private final List<UsersResponse> randomUsers = new ArrayList<UsersResponse>() {{
        add(new UsersResponse(1L, "Name1", "surname1"));
        add(new UsersResponse(2L, "Name2", "surname2"));
        add(new UsersResponse(3L, "Name3", "surname3"));
        add(new UsersResponse(4L, "Name4", "surname4"));
    }};


    @Inject
    public FakeUsersRepositoryImpl() {}

    @Override
    public Observable<Response<List<UsersResponse>>> getRandomUsers(int limit) {
//        return null;
        return Observable.just(Response.success(randomUsers));
    }

    @Override
    public Observable<Response<List<UsersResponse>>> getFilteredUsers(String fullName) {
        // TODO: Implement method
        return null;
    }

    @Override
    public Observable<Response<Void>> addFriend(long target) {
        // TODO: Implement method
        return null;
    }

    @Override
    public Observable<Response<Void>> sendMessage(SendMessageBody payload) {
        // TODO: Implement method
        return null;
    }

    @Override
    public Observable<Response<List<PhotosResponse>>> getPhotosByUserId(long id) {
        return null;
    }

    @Override
    public Observable<Response<PhotosResponse>> uploadPhoto(MultipartBody.Part photo) {
        return null;
    }
}
