package com.example.dobble.release.vm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dobble.release.App;
import com.example.dobble.release.dto.RxStatusDto;
import com.example.dobble.release.repositories.WallRepository;
import com.example.dobble.release.repositories.remote.payload.response.CommentsResponse;
import com.example.dobble.release.repositories.remote.payload.body.SendWallCommentBody;
import com.example.dobble.release.repositories.remote.payload.response.SendWallCommentResponse;
import com.example.dobble.release.ui.adapters.WallAdapter;


import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.example.dobble.release.Cfg.BODY_MISSING;
import static com.example.dobble.release.Cfg.RESPONSE_FIELDS_MISSING;

public class WallViewModelImpl extends ViewModel implements WallViewModel {

    @Inject
    WallRepository repository;

    private Disposable getCommentsFlow;
    private MutableLiveData<RxStatusDto<Response<List<CommentsResponse>>>> getCommentsByUserIdLiveData = new MutableLiveData<>();
    private Disposable sendWallCommentFlow;
    private MutableLiveData<RxStatusDto<Response<SendWallCommentResponse>>> sendWallCommentLiveData = new MutableLiveData<>();
    private Disposable sendSubCommentFlow;
    private MutableLiveData<RxStatusDto<Response<SendWallCommentResponse>>> sendSubCommentLiveData = new MutableLiveData<>();

    @Inject
    public WallViewModelImpl() {
        App.getAppComponent().inject(this);
    }

    @Override
    public void getCommentsByUserId(long id) {
        getCommentsFlow = repository.getCommentsByUserId(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                response -> {
                    List<CommentsResponse> r = response.body();
                    if (r == null) {
                        getCommentsByUserIdLiveData.setValue(new RxStatusDto<>(BODY_MISSING));
                        return;
                    }
                    r.removeIf(c -> c.getId() == null || c.getUserId() == null
                        || c.getFirstName() == null || c.getLastName() == null || c.getContent() == null);

                    getCommentsByUserIdLiveData.setValue(new RxStatusDto<>(response));
                },
                throwable -> getCommentsByUserIdLiveData.setValue(new RxStatusDto<>(throwable.getLocalizedMessage()))
            );
    }

    @Override
    public LiveData<RxStatusDto<Response<List<CommentsResponse>>>> getCommentsByUserIdLiveData() {
        return getCommentsByUserIdLiveData;
    }

    @Override
    public void sendWallComment(SendWallCommentBody p) {
        sendWallCommentFlow = repository.sendComment(p)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                response -> {
                    SendWallCommentResponse r = response.body();
                    if (r == null) {
                        sendWallCommentLiveData.setValue(new RxStatusDto<>(BODY_MISSING));
                        return;
                    }
                    if (r.getId() == null || r.getContent() == null || r.getSender() == null
                        || r.getWallId() == null || r.getFirstName() == null
                        || r.getLastName() == null) {
                        sendWallCommentLiveData.setValue(new RxStatusDto<>(RESPONSE_FIELDS_MISSING));
                        return;
                    }

                    sendWallCommentLiveData.setValue(new RxStatusDto<>(response));
                },
                throwable -> sendWallCommentLiveData.setValue(new RxStatusDto<>(throwable.getLocalizedMessage()))
            );
    }

    @Override
    public LiveData<RxStatusDto<Response<SendWallCommentResponse>>> sendWallCommentLiveData() {
        return sendWallCommentLiveData;
    }

    @Override
    public void sendSubComment(SendWallCommentBody p) {
        sendSubCommentFlow = repository.sendComment(p)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                response -> {
                    SendWallCommentResponse r = response.body();
                    if (r == null) {
                        sendSubCommentLiveData.setValue(new RxStatusDto<>(BODY_MISSING));
                        return;
                    }
                    if (r.getId() == null || r.getContent() == null || r.getSender() == null
                        || r.getWallId() == null || r.getFirstName() == null
                        || r.getLastName() == null) {
                        sendWallCommentLiveData.setValue(new RxStatusDto<>(RESPONSE_FIELDS_MISSING));
                        return;
                    }

                    sendSubCommentLiveData.setValue(new RxStatusDto<>(response));
                },
                throwable -> sendSubCommentLiveData.setValue(new RxStatusDto<>(throwable.getLocalizedMessage()))
            );
    }

    @Override
    public LiveData<RxStatusDto<Response<SendWallCommentResponse>>> sendSubCommentLiveData() {
        return sendSubCommentLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        releaseResources();
    }

    @Override
    public void releaseResources() {
        if (getCommentsFlow != null) {
            getCommentsFlow.dispose();
        }
        if (sendWallCommentFlow != null) {
            sendWallCommentFlow.dispose();
        }
        if (sendSubCommentFlow != null) {
            sendSubCommentFlow.dispose();
        }
    }
}
