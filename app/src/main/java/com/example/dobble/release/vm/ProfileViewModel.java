package com.example.dobble.release.vm;

import androidx.lifecycle.LiveData;

import com.example.dobble.release.dto.RxStatusDto;
import com.example.dobble.release.repositories.remote.payload.response.GetUserByIdResponse;
import com.example.dobble.release.repositories.remote.payload.body.EditProfileBody;
import com.example.dobble.release.repositories.remote.payload.response.UploadAvatarResponse;

import okhttp3.MultipartBody;
import retrofit2.Response;

public interface ProfileViewModel extends BaseViewModel {
    void getUserById(long id);
    LiveData<RxStatusDto<Response<GetUserByIdResponse>>> getUserByIdLiveData();
    void editProfile(EditProfileBody payload);
    LiveData<RxStatusDto<Response<Void>>>editProfileLiveData();
    void uploadAvatar(MultipartBody.Part body);
    LiveData<RxStatusDto<Response<UploadAvatarResponse>>> uploadAvatarLiveData();
}
