package com.example.dobble.release.vm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dobble.release.App;
import com.example.dobble.release.dto.FriendDto;
import com.example.dobble.release.dto.RxStatusDto;
import com.example.dobble.release.repositories.FriendsRepository;
import com.example.dobble.release.repositories.remote.payload.body.ApproveFriendBody;
import com.example.dobble.release.repositories.remote.payload.response.FriendResponse;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.example.dobble.release.Cfg.BODY_MISSING;

public class FriendsViewModelImpl extends ViewModel implements FriendsViewModel {
    @Inject
    FriendsRepository repository;

    private MutableLiveData<RxStatusDto<Response<List<FriendResponse>>>> getFriendsByUserIdLiveData = new MutableLiveData<>();
    private MutableLiveData<RxStatusDto<Response<List<FriendResponse>>>> getFriendRequestsLiveData = new MutableLiveData<>();
    private MutableLiveData<RxStatusDto<Response<List<FriendResponse>>>> getFriendOwnRequestsLiveData = new MutableLiveData<>();
    private MutableLiveData<RxStatusDto<FriendDto>> removeFriendByUserIdLiveData = new MutableLiveData<>();
    private MutableLiveData<RxStatusDto<FriendDto>> approveFriendShipByUserIdLiveData = new MutableLiveData<>();
    private Disposable getFriendsByUserIdFlow;
    private Disposable getFriendRequestsFlow;
    private Disposable getFriendOwnRequestsFlow;
    private Disposable removeFriendByUserIdFlow;
    private Disposable approveFriendShipByUserIdFlow;

    @Inject
    public FriendsViewModelImpl() {
        App.getAppComponent().inject(this);
    }

    @Override
    public void getFriendsByUserId(long id) {
        getFriendsByUserIdFlow = repository.getFriendsByUserId(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                friends -> {
                    if (friends.body() == null) {
                        getFriendsByUserIdLiveData.setValue(new RxStatusDto<>(BODY_MISSING));
                        return;
                    }
                    friends.body().removeIf(f -> f.getId() == null || f.getFirstName() == null
                    || f.getLastName() == null);

                    getFriendsByUserIdLiveData.setValue(new RxStatusDto<>(friends));
                },
                throwable -> getFriendsByUserIdLiveData.setValue(new RxStatusDto<>(throwable.getLocalizedMessage()))
            );
    }

    @Override
    public void getFriendRequests() {
        getFriendRequestsFlow = repository.getFriendRequests()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                friends -> {
                    if (friends.body() == null) {
                        getFriendRequestsLiveData.setValue(new RxStatusDto<>(BODY_MISSING));
                        return;
                    }
                    friends.body().removeIf(f -> f.getId() == null || f.getFirstName() == null
                        || f.getLastName() == null);

                    getFriendRequestsLiveData.setValue(new RxStatusDto<>(friends));
                },
                throwable -> getFriendRequestsLiveData.setValue(new RxStatusDto<>(throwable.getLocalizedMessage()))
            );
    }

    @Override
    public void getFriendOwnRequests() {
        getFriendOwnRequestsFlow = repository.getFriendOwnRequests()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                friends -> {
                    if (friends.body() == null) {
                        getFriendOwnRequestsLiveData.setValue(new RxStatusDto<>(BODY_MISSING));
                        return;
                    }

                    getFriendOwnRequestsLiveData.setValue(new RxStatusDto<>(friends));
                },
                throwable -> getFriendOwnRequestsLiveData.setValue(new RxStatusDto<>(throwable.getLocalizedMessage()))
            );
    }

    @Override
    public void removeFriendByUserId(long userId, FriendResponse friendResponse) {
        removeFriendByUserIdFlow = repository.removeFriendByUserId(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                response -> removeFriendByUserIdLiveData.setValue(new RxStatusDto<>(new FriendDto(response, friendResponse))),
                throwable -> removeFriendByUserIdLiveData.setValue(new RxStatusDto<>(throwable.getLocalizedMessage()))
            );
    }

    @Override
    public void approveFriendShipByUserId(ApproveFriendBody body, FriendResponse friend) {
        approveFriendShipByUserIdFlow = repository.approveFriendShipByUserId(body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                response -> approveFriendShipByUserIdLiveData.setValue(new RxStatusDto<>(new FriendDto(response, friend))),
                throwable -> approveFriendShipByUserIdLiveData.setValue(new RxStatusDto<>(throwable.getLocalizedMessage()))
            );
    }

    @Override
    public LiveData<RxStatusDto<Response<List<FriendResponse>>>> getFriendsByUserIdLiveData() {
        return getFriendsByUserIdLiveData;
    }

    @Override
    public LiveData<RxStatusDto<Response<List<FriendResponse>>>> getFriendRequestsLiveData() {
        return getFriendRequestsLiveData;
    }

    @Override
    public LiveData<RxStatusDto<Response<List<FriendResponse>>>> getFriendOwnRequestsLiveData() {
        return getFriendOwnRequestsLiveData;
    }

    @Override
    public LiveData<RxStatusDto<FriendDto>> removeFriendByUserIdLiveData() {
        return removeFriendByUserIdLiveData;
    }

    @Override
    public LiveData<RxStatusDto<FriendDto>> approveFriendShipByUserIdLiveData() {
        return approveFriendShipByUserIdLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        releaseResources();
    }

    @Override
    public void releaseResources() {
        if (getFriendsByUserIdFlow != null) {
            getFriendsByUserIdFlow.dispose();
        }
        if (getFriendRequestsFlow != null) {
            getFriendRequestsFlow.dispose();
        }
        if (getFriendOwnRequestsFlow != null) {
            getFriendOwnRequestsFlow.dispose();
        }
        if (removeFriendByUserIdFlow != null) {
            removeFriendByUserIdFlow.dispose();
        }
        if (approveFriendShipByUserIdFlow != null) {
            approveFriendShipByUserIdFlow.dispose();
        }
    }
}
