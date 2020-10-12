package com.example.dobble.release.vm;

import androidx.lifecycle.LiveData;

import com.example.dobble.release.dto.RxStatusDto;
import com.example.dobble.release.repositories.remote.payload.response.CommentsResponse;
import com.example.dobble.release.repositories.remote.payload.body.SendWallCommentBody;
import com.example.dobble.release.repositories.remote.payload.response.SendWallCommentResponse;
import com.example.dobble.release.ui.adapters.WallAdapter;


import java.util.List;

import retrofit2.Response;

public interface WallViewModel extends BaseViewModel {
    void getCommentsByUserId(long id);
    LiveData<RxStatusDto<Response<List<CommentsResponse>>>> getCommentsByUserIdLiveData();
    void sendWallComment(SendWallCommentBody p);
    LiveData<RxStatusDto<Response<SendWallCommentResponse>>> sendWallCommentLiveData();
    void sendSubComment(SendWallCommentBody p);
    LiveData<RxStatusDto<Response<SendWallCommentResponse>>> sendSubCommentLiveData();
}
