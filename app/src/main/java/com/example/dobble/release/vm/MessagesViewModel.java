package com.example.dobble.release.vm;

import androidx.lifecycle.LiveData;

import com.example.dobble.release.dto.RxStatusDto;
import com.example.dobble.release.repositories.remote.payload.body.MessageBody;
import com.example.dobble.release.repositories.remote.payload.response.MessageResponse;

import java.util.List;

import retrofit2.Response;

public interface MessagesViewModel extends BaseViewModel {
    void getOwnMessages();
    LiveData<RxStatusDto<Response<List<MessageResponse>>>> getOwnMessagesLiveData();
    void sendMessage(MessageBody body);
    LiveData<RxStatusDto<Response<Void>>> sendMessageLiveData();
}
