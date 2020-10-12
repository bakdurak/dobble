package com.example.dobble.release.vm;



import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dobble.release.App;
import com.example.dobble.release.dto.RxStatusDto;
import com.example.dobble.release.repositories.UsersRepository;
import com.example.dobble.release.repositories.remote.payload.response.PhotosResponse;
import com.example.dobble.release.repositories.remote.payload.body.SendMessageBody;
import com.example.dobble.release.repositories.remote.payload.response.UsersResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import okhttp3.MultipartBody;
import retrofit2.Response;

import static com.example.dobble.release.Cfg.BODY_MISSING;
import static com.example.dobble.release.Cfg.REQUEST_TIMEOUT;
import static com.example.dobble.release.Cfg.RESPONSE_FIELDS_MISSING;

public class UsersViewModelImpl extends ViewModel implements UsersViewModel {
    @Inject
    UsersRepository repository;

    private Disposable getRandomUsersFlow;

    private Disposable getFilteredUsersFlow;
    private Disposable sendMessageFlow;
    private Disposable getPhotosFlow;
    private Disposable uploadPhotoFlow;
    private MutableLiveData<RxStatusDto<Response<List<UsersResponse>>>> getRandomUsersLiveData = new MutableLiveData<>();
    private MutableLiveData<RxStatusDto<Response<List<UsersResponse>>>> getFilteredUsersLiveData = new MutableLiveData<>();
    private MutableLiveData<RxStatusDto<Response<List<PhotosResponse>>>> getPhotosByUserIdLiveData = new MutableLiveData<>();
    private Map<Long, MutableLiveData<RxStatusDto<Response<Void>>>> friendIdToLiveData = new HashMap<>();
    private MutableLiveData<RxStatusDto<Response<Void>>> sendMessageLiveData = new MutableLiveData<>();
    // We need to use BehaviorSubject instead of PublishSubject because of re subscription to it
    // During class lifetime and it's necessary to have cold observable to not lose data
    private BehaviorSubject<String> userFilterInputSubject = BehaviorSubject.create();
    private MutableLiveData<RxStatusDto<Response<PhotosResponse>>> uploadPhotoLiveData = new MutableLiveData<>();

    @Inject
    public UsersViewModelImpl() {
        App.getAppComponent().inject(this);
    }
    
    @Override
    public void getPhotosByUserId(long id) {
        getPhotosFlow = repository.getPhotosByUserId(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                response -> {
                    if (response.body() == null) {
                        getPhotosByUserIdLiveData.setValue(new RxStatusDto<>(BODY_MISSING));
                        return;
                    }
                    response.body().removeIf(p -> p.getId() == null);

                    getPhotosByUserIdLiveData.setValue(new RxStatusDto<>(response));
                },
                throwable -> getPhotosByUserIdLiveData.setValue(new RxStatusDto<>(throwable.getLocalizedMessage()))
            );
    }

    @Override
    public LiveData<RxStatusDto<Response<List<PhotosResponse>>>> getPhotosByUserIdLiveData() {
        return getPhotosByUserIdLiveData;
    }

    @Override
    public void uploadPhoto(MultipartBody.Part photo) {
        uploadPhotoFlow = repository.uploadPhoto(photo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                response -> {
                    if (response.body() == null) {
                        uploadPhotoLiveData.setValue(new RxStatusDto<>(BODY_MISSING));
                        return;
                    }
                    if (response.body().getId() == null) {
                        uploadPhotoLiveData.setValue(new RxStatusDto<>(RESPONSE_FIELDS_MISSING));
                        return;
                    }

                    uploadPhotoLiveData.setValue(response.body() != null
                        ? new RxStatusDto<>(response) : new RxStatusDto<>(BODY_MISSING));
                },
                throwable -> uploadPhotoLiveData.setValue(new RxStatusDto<>(throwable.getLocalizedMessage()))
            );
    }

    @Override
    public LiveData<RxStatusDto<Response<PhotosResponse>>> uploadPhotoLiveData() {
        return uploadPhotoLiveData;
    }

    @Override
    public void getRandomUsers(int limit) {
        getRandomUsersFlow = repository.getRandomUsers(limit)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                response -> {
                    if (response.body() == null) {
                        getRandomUsersLiveData.setValue(new RxStatusDto<>(BODY_MISSING));
                        return;
                    }
                    response.body().removeIf(u -> u.getId() == null || u.getFirstName() == null
                        || u.getLastName() == null);

                    response.body().removeIf(u -> u.getId() == App.getUserId());
                    getRandomUsersLiveData.setValue(new RxStatusDto<>(response));
                },
                throwable -> getRandomUsersLiveData.setValue(new RxStatusDto<>(throwable.getLocalizedMessage()))
            );
    }

    @Override
    public LiveData<RxStatusDto<Response<List<UsersResponse>>>> getRandomUsersLiveData() {
        return getRandomUsersLiveData;
    }

    @Override
    public void getFilteredUsers(String fullName) {
        if (!userFilterInputSubject.hasObservers()) {
            assignFilteredUsersObserver();
        }

        userFilterInputSubject.onNext(fullName);
    }

    private void assignFilteredUsersObserver() {
        getFilteredUsersFlow =  userFilterInputSubject
            .debounce(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .filter(fullName -> !fullName.isEmpty())
            .switchMap(fullName -> repository.getFilteredUsers(fullName))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                response -> {
                    if (response.body() == null) {
                        getFilteredUsersLiveData.setValue(new RxStatusDto<>(BODY_MISSING));
                        return;
                    }
                    response.body().removeIf(u -> u.getId() == null || u.getFirstName() == null
                        || u.getLastName() == null);

                    response.body().removeIf(u -> u.getId() == App.getUserId());
                    getFilteredUsersLiveData.setValue(new RxStatusDto<>(response));
                },
                throwable -> getFilteredUsersLiveData.setValue(new RxStatusDto<>(throwable.getLocalizedMessage()))
            );
    }

    @Override
    public LiveData<RxStatusDto<Response<List<UsersResponse>>>> getFilteredUsersLiveData() {
        return getFilteredUsersLiveData;
    }

    @Override
    public void addFriend(long target) {
        if (friendIdToLiveData.containsKey(target)) {
            return;
        }

        MutableLiveData<RxStatusDto<Response<Void>>> addFriendResponse = new MutableLiveData<>();
        friendIdToLiveData.put(target, addFriendResponse);
        repository.addFriend(target)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                response -> {
                    addFriendResponse.setValue(new RxStatusDto<>(response));
                    friendIdToLiveData.remove(target);
                },
                throwable -> {
                    addFriendResponse.setValue(new RxStatusDto<>(throwable.getLocalizedMessage()));
                    friendIdToLiveData.remove(target);
                }
            );
    }

    @Override
    public LiveData<RxStatusDto<Response<Void>>> addFriendLiveData(long target) {
        return friendIdToLiveData.get(target);
    }

    @Override
    public void sendMessage(SendMessageBody payload) {
        if (sendMessageFlow != null && !sendMessageFlow.isDisposed()) {
            return;
        }

        sendMessageFlow = repository.sendMessage(payload)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                response -> sendMessageLiveData.setValue(new RxStatusDto<>(response)),
                throwable -> sendMessageLiveData.setValue(new RxStatusDto<>(throwable.getLocalizedMessage()))
            );
    }

    @Override
    public LiveData<RxStatusDto<Response<Void>>> sendMessageLiveData() {
        return sendMessageLiveData;
    }

    @Override
    public void cancelSendMessage() {
        if (sendMessageFlow != null) {
            sendMessageFlow.dispose();
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        releaseResources();
    }

    @Override
    public void releaseResources() {
        if (getRandomUsersFlow != null) {
            getRandomUsersFlow.dispose();
        }
        if (getFilteredUsersFlow != null) {
            getFilteredUsersFlow.dispose();
        }
        if (sendMessageFlow != null) {
            sendMessageFlow.dispose();
        }
        if (getPhotosFlow != null) {
            getPhotosFlow.dispose();
        }
        if (uploadPhotoFlow != null) {
            uploadPhotoFlow.dispose();
        }
    }
}
