package com.example.dobble.debug.repositories;

import com.example.dobble.release.repositories.MessagesRepository;
import com.example.dobble.release.repositories.remote.payload.body.MessageBody;
import com.example.dobble.release.repositories.remote.payload.response.MessageResponse;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import retrofit2.Response;

// TODO: Implements methods

public class FakeMessagesRepositoryImpl implements MessagesRepository {

    @Inject
    public FakeMessagesRepositoryImpl() { }

    @Override
    public Observable<Response<List<MessageResponse>>> getOwnMessages() {
        return null;
    }

    @Override
    public Observable<Response<Void>> sendMessage(MessageBody body) {
        return null;
    }
}
