package com.example.dobble.debug.repositories;

import com.example.dobble.release.repositories.RegistrationRepository;
import com.example.dobble.release.repositories.remote.payload.response.CheckEmailResponse;
import com.example.dobble.release.repositories.remote.payload.body.RegisterUserBody;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Response;

public class FakeRegistrationRepositoryImpl implements RegistrationRepository {

    private Set<String> emails = new HashSet<String>() {{
        add("test@mail.com");
        add("dog@mail.com");
        add("parrot@mail.com");
    }};

    @Inject
    public FakeRegistrationRepositoryImpl() {}

    @Override
    public Observable<Response<Void>> registerUser(RegisterUserBody user) {
        return null;
    }

    @Override
    public Observable<Response<CheckEmailResponse>> checkEmail(String email) {
        retrofit2.Response<CheckEmailResponse> response;
        if (emails.contains(email)) {
            response = Response.success(new CheckEmailResponse(true));
        }
        else {
            response = Response.success(new CheckEmailResponse(false));
        }
        return Observable.just(response)
            .delay(1, TimeUnit.SECONDS);
    }
}
