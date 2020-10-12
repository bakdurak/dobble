package com.example.dobble.release.vm;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dobble.release.App;
import com.example.dobble.release.dto.RxStatusDto;
import com.example.dobble.release.repositories.InitialRepository;
import com.example.dobble.release.repositories.remote.payload.response.CheckAuthResponse;

import java.net.HttpURLConnection;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.example.dobble.release.Cfg.BODY_MISSING;
import static com.example.dobble.release.Cfg.RESPONSE_FIELDS_MISSING;

public class InitialViewModelImpl extends ViewModel implements InitialViewModel {
    @Inject
    InitialRepository repository;

    private MutableLiveData<RxStatusDto<Response<CheckAuthResponse>>> checkAuthLiveData = new MutableLiveData<>();
    private Disposable checkAuthFlow;

    @Inject
    public InitialViewModelImpl() {
        App.getAppComponent().inject(this);
    }

    @Override
    public void checkAuth() {
        checkAuthFlow = repository.checkAuth()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                response -> {
                    if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        checkAuthLiveData.setValue(new RxStatusDto<>(response));
                        return;
                    }

                    if (response.body() == null) {
                        checkAuthLiveData.setValue(new RxStatusDto<>(BODY_MISSING));
                        return;
                    }
                    if (response.body().getId() == null) {
                        checkAuthLiveData.setValue(new RxStatusDto<>(RESPONSE_FIELDS_MISSING));
                        return;
                    }

                    checkAuthLiveData.setValue(new RxStatusDto<>(response));
                },
                throwable -> checkAuthLiveData.setValue(new RxStatusDto<>(throwable.getLocalizedMessage()))
            );
    }

    @Override
    public LiveData<RxStatusDto<Response<CheckAuthResponse>>> checkAuthLiveData() {
        return checkAuthLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        releaseResources();
    }

    @Override
    public void releaseResources() {
        if (checkAuthFlow != null) {
            checkAuthFlow.dispose();
        }
    }
}
