package com.example.dobble.release.repositories;


import com.example.dobble.release.repositories.remote.payload.response.PhotosResponse;
import com.example.dobble.release.repositories.remote.payload.body.SendMessageBody;
import com.example.dobble.release.repositories.remote.payload.response.UsersResponse;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Response;

public interface UsersRepository {
    Observable<Response<List<UsersResponse>>> getRandomUsers(int limit);
    Observable<Response<List<UsersResponse>>> getFilteredUsers(String fullName);
    Observable<Response<Void>> addFriend(long target);
    Observable<Response<Void>> sendMessage(SendMessageBody payload);
    Observable<Response<List<PhotosResponse>>> getPhotosByUserId(long id);
    Observable<Response<PhotosResponse>> uploadPhoto(MultipartBody.Part photo);
}
