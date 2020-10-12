package com.example.dobble.release.repositories;

import com.example.dobble.release.repositories.remote.api.DobbleApi;
import com.example.dobble.release.repositories.remote.payload.response.CheckEmailResponse;
import com.example.dobble.release.repositories.remote.payload.body.RegisterUserBody;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Response;

public class RegistrationRepositoryImpl implements RegistrationRepository {

    @Inject
    DobbleApi api;

    @Inject
    public RegistrationRepositoryImpl() { }

    @Override
    public Observable<Response<Void>> registerUser(RegisterUserBody user) {
        return api.registerUser(user);
    }

    @Override
    public Observable<Response<CheckEmailResponse>> checkEmail(String email) {
        return api.checkEmail(email);
    }
}
