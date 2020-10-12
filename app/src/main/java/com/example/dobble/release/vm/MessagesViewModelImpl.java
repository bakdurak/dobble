package com.example.dobble.release.vm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dobble.release.App;
import com.example.dobble.release.dto.RxStatusDto;
import com.example.dobble.release.repositories.MessagesRepository;
import com.example.dobble.release.repositories.remote.payload.body.MessageBody;
import com.example.dobble.release.repositories.remote.payload.response.MessageResponse;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.example.dobble.release.Cfg.BODY_MISSING;

public class MessagesViewModelImpl extends ViewModel implements MessagesViewModel {
    @Inject
    MessagesRepository repository;

    private Disposable getOwnMessagesFlow;
    private MutableLiveData<RxStatusDto<Response<List<MessageResponse>>>> getOwnMessagesLiveData = new MutableLiveData<>();
    private Disposable sendMessageFlow;
    private MutableLiveData<RxStatusDto<Response<Void>>> sendMessageLiveData = new MutableLiveData<>();

    @Inject
    public MessagesViewModelImpl() {
        App.getAppComponent().inject(this);
    }

    @Override
    public void getOwnMessages() {
        getOwnMessagesFlow = repository.getOwnMessages()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                this::getOwnMessagesHandler,
                throwable -> getOwnMessagesLiveData.setValue(new RxStatusDto<>(throwable.getLocalizedMessage()))
            );
    }

    private void getOwnMessagesHandler(Response<List<MessageResponse>> response) {
        if (response.body() == null) {
            getOwnMessagesLiveData.setValue(new RxStatusDto<>(BODY_MISSING));
            return;
        }
        response.body().removeIf(msg -> msg.getId() == null || msg.getContent() == null
        || msg.getDate() == null || msg.getSrcId() == null || msg.getSrcFirstName() == null
        || msg.getSrcLastName() == null || msg.getDstId() == null || msg.getDstFirstName() == null
        || msg.getDstLastName() == null);

        Collections.reverse(response.body());
        getOwnMessagesLiveData.setValue(new RxStatusDto<>(response));
    }

    @Override
    public LiveData<RxStatusDto<Response<List<MessageResponse>>>> getOwnMessagesLiveData() {
        return getOwnMessagesLiveData;
    }

    @Override
    public void sendMessage(MessageBody body) {
        sendMessageFlow = repository.sendMessage(body)
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
    protected void onCleared() {
        super.onCleared();

        releaseResources();
    }

    @Override
    public void releaseResources() {
        if (getOwnMessagesFlow != null) {
            getOwnMessagesFlow.dispose();
        }
        if (sendMessageFlow != null) {
            sendMessageFlow.dispose();
        }
    }
}
