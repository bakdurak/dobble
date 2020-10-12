package com.example.dobble.release.vm;

import androidx.lifecycle.LiveData;

import com.example.dobble.release.dto.FriendDto;
import com.example.dobble.release.dto.RxStatusDto;
import com.example.dobble.release.repositories.remote.payload.body.ApproveFriendBody;
import com.example.dobble.release.repositories.remote.payload.response.FriendResponse;

import java.util.List;

import retrofit2.Response;

public interface FriendsViewModel extends BaseViewModel {
    void getFriendsByUserId(long id);
    void getFriendRequests();
    void getFriendOwnRequests();
    void removeFriendByUserId(long userId, FriendResponse friend);
    void approveFriendShipByUserId(ApproveFriendBody body, FriendResponse friend);
    LiveData<RxStatusDto<Response<List<FriendResponse>>>> getFriendsByUserIdLiveData();
    LiveData<RxStatusDto<Response<List<FriendResponse>>>> getFriendRequestsLiveData();
    LiveData<RxStatusDto<Response<List<FriendResponse>>>> getFriendOwnRequestsLiveData();
    LiveData<RxStatusDto<FriendDto>> removeFriendByUserIdLiveData();
    LiveData<RxStatusDto<FriendDto>> approveFriendShipByUserIdLiveData();
}
