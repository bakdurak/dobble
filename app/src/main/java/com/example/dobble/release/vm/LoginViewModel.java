package com.example.dobble.release.vm;

import androidx.lifecycle.LiveData;

import com.example.dobble.release.dto.RxStatusDto;
import com.example.dobble.release.repositories.remote.payload.response.CheckAuthResponse;
import com.example.dobble.release.repositories.remote.payload.body.LoginBody;

import retrofit2.Response;

public interface LoginViewModel extends BaseViewModel {
    void login(LoginBody payload);
    LiveData<RxStatusDto<Response<CheckAuthResponse>>> loginLiveData();
}
