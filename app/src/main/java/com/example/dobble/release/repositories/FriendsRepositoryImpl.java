package com.example.dobble.release.repositories;

import android.util.Log;

import com.example.dobble.release.repositories.local.AppDatabase;
import com.example.dobble.release.repositories.local.entities.FriendEntity;
import com.example.dobble.release.repositories.remote.api.DobbleApi;
import com.example.dobble.release.repositories.remote.payload.body.ApproveFriendBody;
import com.example.dobble.release.repositories.remote.payload.response.CommentsResponse;
import com.example.dobble.release.repositories.remote.payload.response.FriendResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import retrofit2.Response;

public class FriendsRepositoryImpl implements FriendsRepository {
    @Inject
    DobbleApi api;
    @Inject
    AppDatabase db;

    private Disposable getFriendsByUserIdDbFlow;
    private Disposable getFriendsByUserIdApiFlow;
    private PublishSubject<Response<List<FriendResponse>>> getFriendsByUserIdPublisher = PublishSubject.create();

    @Inject
    public FriendsRepositoryImpl() { }

    @Override
    public Observable<Response<List<FriendResponse>>> getFriendsByUserId(long id) {
        getFriendsByUserIdDbFlow = db.friendsDao().getFriends()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                this::getFriendsDbHandler,
                throwable -> {
                    // TODO: Error handling
                }
            );

        getFriendsByUserIdApiFlow = api.getFriendsByUserId(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                this::getFriendsApiHandler,
                throwable -> {
                    // TODO: Error handling
                }
            );

        return getFriendsByUserIdPublisher;
    }

    private void getFriendsDbHandler(List<FriendEntity> entities) {
        if (entities.isEmpty()) {
            return;
        }

        List<FriendResponse> friendResponses = new ArrayList<>();
        for(FriendEntity e : entities) {
            friendResponses.add(new FriendResponse(e));
        }

        Response<List<FriendResponse>> response = Response.success(friendResponses);
        getFriendsByUserIdPublisher.onNext(response);
    }

    private void getFriendsApiHandler(Response<List<FriendResponse>> response) {
        if (response.body() == null) {
            return;
        }

        if (getFriendsByUserIdDbFlow != null) {
            getFriendsByUserIdDbFlow.dispose();
        }

        // TODO: Wrap with transaction
        if (!response.body().isEmpty()) {
            List<FriendEntity> entities = new ArrayList<>();
            for(FriendResponse r : response.body()) {
                entities.add(new FriendEntity(r));
            }
            db.friendsDao().insert(entities).subscribeOn(Schedulers.io()).subscribe();
        }

        getFriendsByUserIdPublisher.onNext(response);
    }

    @Override
    public Observable<Response<List<FriendResponse>>> getFriendRequests() {
        return api.getFriendRequests();
    }

    @Override
    public Observable<Response<List<FriendResponse>>> getFriendOwnRequests() {
        return api.getFriendOwnRequests();
    }

    @Override
    public Observable<Response<Void>> removeFriendByUserId(long userId) {
        db.friendsDao().removeFriendById(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe((rowCnt, throwable) -> {
                // TODO: Error handling
            });

        return api.removeFriendByUserId(userId);
    }

    @Override
    public Observable<Response<Void>> approveFriendShipByUserId(ApproveFriendBody body) {
        return api.approveFriendShipByUserId(body);
    }
}
