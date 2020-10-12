package com.example.dobble.release.vm;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dobble.release.App;
import com.example.dobble.release.dto.RxStatusDto;
import com.example.dobble.release.repositories.ProfileRepository;
import com.example.dobble.release.repositories.remote.payload.response.GetUserByIdResponse;
import com.example.dobble.release.repositories.remote.payload.body.EditProfileBody;
import com.example.dobble.release.repositories.remote.payload.response.UploadAvatarResponse;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import retrofit2.Response;

import static com.example.dobble.release.Cfg.BODY_MISSING;
import static com.example.dobble.release.Cfg.DATE_FORMAT;
import static com.example.dobble.release.Cfg.FEMALE;
import static com.example.dobble.release.Cfg.MALE;
import static com.example.dobble.release.Cfg.MALE_ID;
import static com.example.dobble.release.Cfg.RESPONSE_FIELDS_MISSING;

public class ProfileViewModelImpl extends ViewModel implements ProfileViewModel {
    @Inject
    ProfileRepository repository;

    private Disposable getUserByIdFlow;
    private MutableLiveData<RxStatusDto<Response<GetUserByIdResponse>>> getUserByIdLiveData = new MutableLiveData<>();
    private Disposable uploadAvatarFlow;
    private MutableLiveData<RxStatusDto<Response<UploadAvatarResponse>>> uploadAvatarLiveData = new MutableLiveData<>();
    private Disposable editProfileFlow;
    private MutableLiveData<RxStatusDto<Response<Void>>> editProfileLiveData = new MutableLiveData<>();

    @Inject
    public ProfileViewModelImpl() {
        App.getAppComponent().inject(this);
    }

    @Override
    public void uploadAvatar(MultipartBody.Part body) {
        uploadAvatarFlow = repository.uploadAvatar(body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                response -> {
                    UploadAvatarResponse r = response.body();
                    if (r == null) {
                        uploadAvatarLiveData.setValue(new RxStatusDto<>(BODY_MISSING));
                        return;
                    }
                    if (r.getAvatarId() == null || r.getAvatarUrl() == null) {
                        uploadAvatarLiveData.setValue(new RxStatusDto<>(RESPONSE_FIELDS_MISSING));
                        return;
                    }

                    uploadAvatarLiveData.setValue(new RxStatusDto<>(response));
                },
                throwable -> uploadAvatarLiveData.setValue(new RxStatusDto<>(throwable.getLocalizedMessage()))
            );
    }

    @Override
    public LiveData<RxStatusDto<Response<UploadAvatarResponse>>> uploadAvatarLiveData() {
        return uploadAvatarLiveData;
    }

    @Override
    public void getUserById(long id) {
        getUserByIdFlow = repository.getUserById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                this::getUserByIdResponse,
                throwable -> {
                    getUserByIdLiveData.setValue(new RxStatusDto<>(throwable.getLocalizedMessage()));
                }
            );
    }

    private void getUserByIdResponse(Response<GetUserByIdResponse> response) {
        GetUserByIdResponse r = response.body();
        if (r == null) {
            getUserByIdLiveData.setValue(new RxStatusDto<>(BODY_MISSING));
            return;
        }
        if (r.isLink() == null || r.isUserResponseInconsistent()) {
            getUserByIdLiveData.setValue(new RxStatusDto<>(RESPONSE_FIELDS_MISSING));
            return;
        }

        GetUserByIdResponse getUserByIdResponse = response.body();
        getUserByIdResponse.getProfileResponse().setGender(getUserByIdResponse.getProfileResponse().getGender()
            .equals(MALE_ID) ? MALE : FEMALE);

        DateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        try {
            Date dob = format.parse(getUserByIdResponse.getProfileResponse().getDob());
            getUserByIdResponse.getProfileResponse().setDob((dob.getYear() + 1900) + "/" + dob.getMonth()
                + "/" +dob.getDay());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        getUserByIdLiveData.setValue(new RxStatusDto<>(response));
    }

    @Override
    public LiveData<RxStatusDto<Response<GetUserByIdResponse>>> getUserByIdLiveData() {
        return getUserByIdLiveData;
    }

    @Override
    public void editProfile(EditProfileBody payload) {
        editProfileFlow = repository.editProfile(payload)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                response -> editProfileLiveData.setValue(new RxStatusDto<>(response)),
                throwable -> editProfileLiveData.setValue(new RxStatusDto<>(throwable.getLocalizedMessage()))
            );
    }

    @Override
    public LiveData<RxStatusDto<Response<Void>>> editProfileLiveData() {
        return editProfileLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        releaseResources();
    }

    @Override
    public void releaseResources() {
        if (getUserByIdFlow != null) {
            getUserByIdFlow.dispose();
        }
        if (uploadAvatarFlow != null) {
            uploadAvatarFlow.dispose();
        }
        if (editProfileFlow != null) {
            editProfileFlow.dispose();
        }

        repository.onDestroy();
    }
}
