package com.example.dobble.release.repositories;

import com.example.dobble.release.repositories.remote.api.DobbleApi;
import com.example.dobble.release.repositories.remote.payload.response.CheckAuthResponse;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Response;

public class InitialRepositoryImpl implements InitialRepository {
    @Inject
    DobbleApi api;

    @Inject
    public InitialRepositoryImpl() { }

    @Override
    public Observable<Response<CheckAuthResponse>> checkAuth() {
        return api.checkAuth();
    }
}
