package com.example.dobble.debug.repositories;

import com.example.dobble.release.repositories.InitialRepository;
import com.example.dobble.release.repositories.remote.payload.response.CheckAuthResponse;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Response;

public class FakeInitialRepositoryImpl implements InitialRepository {
    private static final int USER_ID = 15;

    @Inject
    public FakeInitialRepositoryImpl() { }

    @Override
    public Observable<Response<CheckAuthResponse>> checkAuth() {
        Response<CheckAuthResponse> response = Response.success(new CheckAuthResponse(USER_ID));
        return Observable.just(response).delay(1, TimeUnit.SECONDS);
    }
}
