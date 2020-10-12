package com.example.dobble.release.repositories;

import com.example.dobble.release.repositories.remote.payload.body.MessageBody;
import com.example.dobble.release.repositories.remote.payload.response.MessageResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;

public interface MessagesRepository {
    Observable<Response<List<MessageResponse>>> getOwnMessages();
    Observable<Response<Void>> sendMessage(MessageBody body);
}
