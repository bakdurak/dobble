package com.example.dobble.release.vm;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dobble.release.App;
import com.example.dobble.release.dto.RxStatusDto;
import com.example.dobble.release.repositories.RegistrationRepository;
import com.example.dobble.release.repositories.remote.payload.body.RegisterUserBody;
import com.example.dobble.release.repositories.remote.payload.response.CheckEmailResponse;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class RegistrationViewModelImpl extends ViewModel implements RegistrationViewModel {

    private static final int SEND_EMAIL_DELAY = 1;

    @Inject
    RegistrationRepository repository;

    private Disposable registerFlow;
    private Disposable checkEmailTimerFlow;
    private MutableLiveData<RxStatusDto<Response<Void>>> registerUserLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> checkEmailTimerExpiredLiveData = new MutableLiveData<>();
    private MutableLiveData<RxStatusDto<EmailCheckResponse>> checkEmailLiveData = new MutableLiveData<>();

    @Inject
    public RegistrationViewModelImpl() {
        App.getAppComponent().inject(this);
    }

    @Override
    public void registerUser(RegisterUserBody user) {
        registerFlow = repository.registerUser(user)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                response -> registerUserLiveData.setValue(new RxStatusDto<>(response)),
                throwable -> registerUserLiveData.setValue(new RxStatusDto<>(throwable.getLocalizedMessage()))
            );
    }

    @Override
    public void checkEmail(String email) {
        if (checkEmailTimerFlow != null) {
            if (checkEmailTimerFlow.isDisposed()) {
                queueCheckEmailRequest(email);
            }
            else {
                checkEmailTimerFlow.dispose();
                queueCheckEmailRequest(email);
            }
        }
        else {
            queueCheckEmailRequest(email);
        }
    }

    @Override
    public boolean cancelCheckEmailTimer() {
        if (checkEmailTimerFlow.isDisposed()) {
            return false;
        }
        else {
            checkEmailTimerFlow.dispose();
            return true;
        }
    }

    @Override
    public LiveData<Boolean> timerLiveData() {
        return checkEmailTimerExpiredLiveData;
    }

    // TODO: Replace timer() with debounce()
    private void queueCheckEmailRequest(String email) {
        checkEmailTimerFlow = Observable.timer(SEND_EMAIL_DELAY, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap(t -> {
                checkEmailTimerExpiredLiveData.setValue(true);
                return repository.checkEmail(email).subscribeOn(Schedulers.io());
            })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                this::checkEmailResponse,
                throwable -> {
                    if (throwable instanceof TimeoutException) {
                        checkEmailLiveData.setValue(new RxStatusDto<>(EmailCheckResponse.TIMEOUT_EXPIRED));
                    }
                }
            );
    }

    private void checkEmailResponse(Response<CheckEmailResponse> response) {
        if (response.body() != null) {
            if (response.body().isExists()) {
                checkEmailLiveData.setValue(new RxStatusDto<>(EmailCheckResponse.EXISTS));
            }
            else {
                checkEmailLiveData.setValue(new RxStatusDto<>(EmailCheckResponse.DOES_NOT_EXIST));
            }
        } else {
            checkEmailLiveData.setValue(new RxStatusDto<>(EmailCheckResponse.SERVER_ERROR));
        }
    }

    @Override
    public LiveData<RxStatusDto<Response<Void>>> registerUserLiveData() {
        return registerUserLiveData;
    }

    @Override
    public LiveData<RxStatusDto<EmailCheckResponse>> checkEmailLiveData() {
        return checkEmailLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        releaseResources();
    }

    @Override
    public void releaseResources() {
        if (registerFlow != null) {
            registerFlow.dispose();
        }
        if (checkEmailTimerFlow != null) {
            checkEmailTimerFlow.dispose();
        }
    }
}
