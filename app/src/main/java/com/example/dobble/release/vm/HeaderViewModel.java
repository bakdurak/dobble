package com.example.dobble.release.vm;

import androidx.lifecycle.LiveData;

import com.example.dobble.release.dto.RxStatusDto;

import retrofit2.Response;

public interface HeaderViewModel extends BaseViewModel {
    void logout();
    LiveData<RxStatusDto<Response<Void>>> getLogoutLiveData();
}
