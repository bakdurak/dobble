package com.example.dobble.release.vm;

import androidx.lifecycle.LiveData;

import com.example.dobble.release.dto.RxStatusDto;
import com.example.dobble.release.repositories.remote.payload.response.CheckAuthResponse;

import retrofit2.Response;

public interface InitialViewModel extends BaseViewModel {
    void checkAuth();
    LiveData<RxStatusDto<Response<CheckAuthResponse>>> checkAuthLiveData();
}
