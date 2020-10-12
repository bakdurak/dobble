package com.example.dobble.release.repositories;

import com.example.dobble.release.repositories.local.AppDatabase;
import com.example.dobble.release.repositories.local.entities.MessageEntity;
import com.example.dobble.release.repositories.remote.api.DobbleApi;
import com.example.dobble.release.repositories.remote.payload.body.MessageBody;
import com.example.dobble.release.repositories.remote.payload.response.MessageResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import retrofit2.Response;

public class MessagesRepositoryImpl implements MessagesRepository {
    @Inject
    DobbleApi api;
    @Inject
    AppDatabase db;

    private Disposable getOwnMessagesDbFlow;
    private Disposable getOwnMessagesApiFlow;
    private PublishSubject<Response<List<MessageResponse>>> getOwnMessagesPublishSubject = PublishSubject.create();

    @Inject
    public MessagesRepositoryImpl() { }

    @Override
    public Observable<Response<List<MessageResponse>>> getOwnMessages() {
        getOwnMessagesDbFlow = db.messagesDao().getMessages()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                this::getOwnMessagesDbHandler,
                throwable -> {
                    // TODO: Error handling
                }
            );

        getOwnMessagesApiFlow = api.getOwnMessages()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                this::getOwnMessagesApiHandler,
                throwable -> {
                    // TODO: Error handling
                }
            );


        return getOwnMessagesPublishSubject;
    }

    private void getOwnMessagesDbHandler(List<MessageEntity> entities) {
        if (entities.isEmpty()) {
            return;
        }

        List<MessageResponse> messages = new ArrayList<>();
        for(MessageEntity e : entities) {
            messages.add(new MessageResponse(e));
        }
        Response<List<MessageResponse>> response = Response.success(messages);
        getOwnMessagesPublishSubject.onNext(response);
    }

    private void getOwnMessagesApiHandler(Response<List<MessageResponse>> response) {
        if (response.body() == null) {
            return;
        }

        if (getOwnMessagesDbFlow != null) {
            getOwnMessagesDbFlow.dispose();
        }

        // TODO: Wrap with transaction
        if (!response.body().isEmpty()) {
            List<MessageEntity> entities = new ArrayList<>();
            for(MessageResponse r : response.body()) {
                entities.add(new MessageEntity(r));
            }
            db.messagesDao().insert(entities).subscribeOn(Schedulers.io()).subscribe();
        }

        getOwnMessagesPublishSubject.onNext(response);
    }

    @Override
    public Observable<Response<Void>> sendMessage(MessageBody body) {
        return api.sendMessage(body);
    }
}
