package com.example.dobble.release.repositories;

import com.example.dobble.release.repositories.remote.payload.response.GetUserByIdResponse;
import com.example.dobble.release.repositories.remote.payload.body.EditProfileBody;
import com.example.dobble.release.repositories.remote.payload.response.UploadAvatarResponse;

import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.http.Path;

public interface ProfileRepository extends Repository {
    Observable<Response<GetUserByIdResponse>> getUserById(@Path("id") long id);
    Observable<Response<UploadAvatarResponse>> uploadAvatar(MultipartBody.Part body);
    Observable<Response<Void>> editProfile(EditProfileBody editProfileBody);
}
