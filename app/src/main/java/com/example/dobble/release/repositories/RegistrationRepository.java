package com.example.dobble.release.repositories;

import com.example.dobble.release.repositories.remote.payload.response.CheckEmailResponse;
import com.example.dobble.release.repositories.remote.payload.body.RegisterUserBody;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Response;

public interface RegistrationRepository {

    Observable<Response<Void>> registerUser(RegisterUserBody user);

    Observable<Response<CheckEmailResponse>> checkEmail(String email);
}
