package com.example.dobble.release.vm;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dobble.release.App;
import com.example.dobble.release.dto.RxStatusDto;
import com.example.dobble.release.repositories.local.AppDatabase;
import com.example.dobble.release.repositories.remote.api.DobbleApi;
import com.example.dobble.release.repositories.remote.payload.response.CheckAuthResponse;
import com.example.dobble.release.repositories.remote.payload.body.LoginBody;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.example.dobble.release.Cfg.BODY_MISSING;
import static com.example.dobble.release.Cfg.RESPONSE_FIELDS_MISSING;

public class LoginViewModelImpl extends ViewModel implements LoginViewModel {
    @Inject
    DobbleApi api;
    @Inject
    AppDatabase db;

    private Disposable loginFlow;
    private MutableLiveData<RxStatusDto<Response<CheckAuthResponse>>> loginLiveData = new MutableLiveData<>();

    @Inject
    public LoginViewModelImpl() {
        App.getAppComponent().inject(this);
    }

    @Override
    public void login(LoginBody payload) {
        Observable.just(db)
            .subscribeOn(Schedulers.io())
            .subscribe(db -> {
                db.clearAllTables();
            });

        loginFlow = api.login(payload)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                response -> {
                    if (response.body() == null) {
                        loginLiveData.setValue(new RxStatusDto<>(BODY_MISSING));
                        return;
                    }
                    if (response.body().getId() == null) {
                        loginLiveData.setValue(new RxStatusDto<>(RESPONSE_FIELDS_MISSING));
                        return;
                    }

                    loginLiveData.setValue(new RxStatusDto<>(response));
                },
                throwable -> loginLiveData.setValue(new RxStatusDto<>(throwable.getLocalizedMessage()))
            );
    }

    @Override
    public LiveData<RxStatusDto<Response<CheckAuthResponse>>> loginLiveData() {
        return loginLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        releaseResources();
    }

    @Override
    public void releaseResources() {
        if (loginFlow != null) {
            loginFlow.dispose();
        }
    }
}
