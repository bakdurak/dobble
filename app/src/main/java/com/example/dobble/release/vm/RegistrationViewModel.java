package com.example.dobble.release.vm;

import androidx.lifecycle.LiveData;

import com.example.dobble.release.dto.RxStatusDto;
import com.example.dobble.release.repositories.remote.payload.body.RegisterUserBody;

import retrofit2.Response;

public interface RegistrationViewModel extends BaseViewModel {
    enum EmailCheckResponse {EXISTS, DOES_NOT_EXIST, SERVER_ERROR, TIMEOUT_EXPIRED}

    void registerUser(RegisterUserBody user);
    void checkEmail(String email);
    boolean cancelCheckEmailTimer();
    LiveData<Boolean> timerLiveData();
    LiveData<RxStatusDto<Response<Void>>> registerUserLiveData();
    LiveData<RxStatusDto<EmailCheckResponse>> checkEmailLiveData();
}
