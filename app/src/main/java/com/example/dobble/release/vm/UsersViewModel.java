package com.example.dobble.release.vm;


import androidx.lifecycle.LiveData;

import com.example.dobble.release.dto.RxStatusDto;
import com.example.dobble.release.repositories.remote.payload.response.PhotosResponse;
import com.example.dobble.release.repositories.remote.payload.body.SendMessageBody;
import com.example.dobble.release.repositories.remote.payload.response.UsersResponse;


import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Response;

public interface UsersViewModel extends BaseViewModel {
    void getRandomUsers(int limit);
    LiveData<RxStatusDto<Response<List<UsersResponse>>>> getRandomUsersLiveData();
    void getFilteredUsers(String fullName);
    LiveData<RxStatusDto<Response<List<UsersResponse>>>> getFilteredUsersLiveData();
    void addFriend(long target);
    LiveData<RxStatusDto<Response<Void>>> addFriendLiveData(long target);
    void sendMessage(SendMessageBody payload);
    LiveData<RxStatusDto<Response<Void>>> sendMessageLiveData();
    void cancelSendMessage();
    void getPhotosByUserId(long id);
    LiveData<RxStatusDto<Response<List<PhotosResponse>>>> getPhotosByUserIdLiveData();
    void uploadPhoto(MultipartBody.Part photo);
    LiveData<RxStatusDto<Response<PhotosResponse>>> uploadPhotoLiveData();
}
