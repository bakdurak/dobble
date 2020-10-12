package com.example.dobble.debug.repositories;

import com.example.dobble.release.repositories.FriendsRepository;
import com.example.dobble.release.repositories.remote.payload.body.ApproveFriendBody;
import com.example.dobble.release.repositories.remote.payload.response.FriendResponse;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import retrofit2.Response;

public class FakeFriendsRepository implements FriendsRepository {
    // TODO: Implements class

    @Inject
    public FakeFriendsRepository() { }

    @Override
    public Observable<Response<List<FriendResponse>>> getFriendsByUserId(long id) {
        return null;
    }

    @Override
    public Observable<Response<List<FriendResponse>>> getFriendRequests() {
        return null;
    }

    @Override
    public Observable<Response<List<FriendResponse>>> getFriendOwnRequests() {
        return null;
    }

    @Override
    public Observable<Response<Void>> removeFriendByUserId(long userId) {
        return null;
    }

    @Override
    public Observable<Response<Void>> approveFriendShipByUserId(ApproveFriendBody body) {
        return null;
    }
}
