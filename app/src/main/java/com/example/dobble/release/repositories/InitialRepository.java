package com.example.dobble.release.repositories;

import com.example.dobble.release.repositories.remote.payload.response.CheckAuthResponse;

import io.reactivex.Observable;
import retrofit2.Response;

public interface InitialRepository {
    Observable<Response<CheckAuthResponse>> checkAuth();
}
