package com.example.dobble.release.repositories;



import com.example.dobble.release.repositories.remote.api.DobbleApi;
import com.example.dobble.release.repositories.remote.payload.body.AddFriendBody;
import com.example.dobble.release.repositories.remote.payload.response.PhotosResponse;
import com.example.dobble.release.repositories.remote.payload.body.SendMessageBody;
import com.example.dobble.release.repositories.remote.payload.response.UsersResponse;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import retrofit2.Response;

public class UsersRepositoryImpl implements UsersRepository {
    @Inject
    DobbleApi api;

    @Override
    public Observable<Response<PhotosResponse>> uploadPhoto(MultipartBody.Part photo) {
        return api.uploadPhoto(photo);
    }

    @Inject
    public UsersRepositoryImpl() {}

    @Override
    public Observable<Response<List<UsersResponse>>> getRandomUsers(int limit) {
        return api.getRandomUsers(limit);
    }

    @Override
    public Observable<Response<List<UsersResponse>>> getFilteredUsers(String fullName) {
        return api.getFilteredUsers(fullName);
    }

    @Override
    public Observable<Response<Void>> addFriend(long target) {
        return api.addFriend(new AddFriendBody(target));
    }

    @Override
    public Observable<Response<Void>> sendMessage(SendMessageBody payload) {
        return api.sendMessage(payload);
    }

    @Override
    public Observable<Response<List<PhotosResponse>>> getPhotosByUserId(long id) {
        return api.getPhotosByUserId(id);
    }
}
