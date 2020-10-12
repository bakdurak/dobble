package com.example.dobble.release.repositories;

import com.example.dobble.release.repositories.remote.payload.body.ApproveFriendBody;
import com.example.dobble.release.repositories.remote.payload.response.FriendResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;

public interface FriendsRepository {
    Observable<Response<List<FriendResponse>>> getFriendsByUserId(long id);
    Observable<Response<List<FriendResponse>>> getFriendRequests();
    Observable<Response<List<FriendResponse>>> getFriendOwnRequests();
    Observable<Response<Void>> removeFriendByUserId(long userId);
    Observable<Response<Void>> approveFriendShipByUserId(ApproveFriendBody body);
}
