package com.example.dobble.release.repositories;



import com.example.dobble.release.repositories.local.AppDatabase;
import com.example.dobble.release.repositories.local.entities.ProfileEntity;
import com.example.dobble.release.repositories.remote.api.DobbleApi;
import com.example.dobble.release.repositories.remote.payload.response.GetUserByIdResponse;
import com.example.dobble.release.repositories.remote.payload.body.EditProfileBody;
import com.example.dobble.release.repositories.remote.payload.response.UploadAvatarResponse;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import okhttp3.MultipartBody;
import retrofit2.Response;

public class ProfileRepositoryImpl implements ProfileRepository {
    @Inject
    DobbleApi api;
    @Inject
    AppDatabase db;

    private Disposable getUserByIdDbFlow;
    private Disposable getUserByIdApiFlow;
    private PublishSubject<Response<GetUserByIdResponse>> getUserByIdPublisher = PublishSubject.create();

    @Inject
    public ProfileRepositoryImpl() { }

    @Override
    public Observable<Response<UploadAvatarResponse>> uploadAvatar(MultipartBody.Part body) {
        return api.uploadAvatar(body);
    }

    @Override
    public Observable<Response<Void>> editProfile(EditProfileBody editProfileBody) {
        return api.editProfile(editProfileBody);
    }

    @Override
    public Observable<Response<GetUserByIdResponse>> getUserById(long id) {
        getUserByIdDbFlow = db.profileDao().getById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                this::getUserByIdDbHandler,
                throwable -> {
                    // TODO: Error handling
                }
            );

        getUserByIdApiFlow = api.getUserById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                this::getUserByUdApiHandler,
                throwable -> {
                    // TODO: Error handling
                }
            );

        return getUserByIdPublisher;
    }

    private void getUserByIdDbHandler(ProfileEntity entity) {
        GetUserByIdResponse getUserByIdResponse = new GetUserByIdResponse(entity);
        Response<GetUserByIdResponse> response = Response.success(getUserByIdResponse);
        getUserByIdPublisher.onNext(response);
    }

    private void getUserByUdApiHandler(Response<GetUserByIdResponse> response) {
        if (getUserByIdDbFlow != null) {
            getUserByIdDbFlow.dispose();
        }

        if (response.body() == null) {
            return;
        }

        ProfileEntity entity = new ProfileEntity(response.body().getProfileResponse(),
            response.body().getUploadResponse() != null ? response.body().getUploadResponse().getId() : null,
            response.body().isLink());
        db.profileDao().insert(entity).subscribeOn(Schedulers.io()).subscribe();

        getUserByIdPublisher.onNext(response);
    }

    @Override
    public void onDestroy() {
        if (getUserByIdDbFlow != null) {
            getUserByIdDbFlow.dispose();
        }
        if (getUserByIdApiFlow != null) {
            getUserByIdApiFlow.dispose();
        }
    }
}
