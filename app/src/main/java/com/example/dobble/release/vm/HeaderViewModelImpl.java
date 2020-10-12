package com.example.dobble.release.vm;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dobble.release.App;
import com.example.dobble.release.dto.RxStatusDto;
import com.example.dobble.release.repositories.local.AppDatabase;
import com.example.dobble.release.repositories.remote.api.DobbleApi;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class HeaderViewModelImpl extends ViewModel implements HeaderViewModel {
    @Inject
    DobbleApi api;
    @Inject
    AppDatabase db;

    private MutableLiveData<RxStatusDto<Response<Void>>> logoutLiveData = new MutableLiveData<>();
    private Disposable logoutFlow;

    @Inject
    public HeaderViewModelImpl() {
        App.getAppComponent().inject(this);
    }

    @Override
    public void logout() {
        Observable.just(db)
            .subscribeOn(Schedulers.io())
            .subscribe(db -> {
                db.clearAllTables();
            });

        App.logout();

        logoutFlow = api.logout()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                response -> {
                    logoutLiveData.setValue(new RxStatusDto<>(response));
                },
                throwable -> {
                    logoutLiveData.setValue(new RxStatusDto<>(throwable.getLocalizedMessage()));
                }
            );
    }

    @Override
    public LiveData<RxStatusDto<Response<Void>>> getLogoutLiveData() {
        return logoutLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        releaseResources();
    }

    @Override
    public void releaseResources() {
        if (logoutFlow != null) {
            logoutFlow.dispose();
        }
    }
}
