package com.example.dobble.release.repositories;

import com.example.dobble.release.repositories.remote.payload.response.CommentsResponse;
import com.example.dobble.release.repositories.remote.payload.body.SendWallCommentBody;
import com.example.dobble.release.repositories.remote.payload.response.SendWallCommentResponse;


import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;

public interface WallRepository {
    Observable<Response<List<CommentsResponse>>> getCommentsByUserId(long id);
    Observable<Response<SendWallCommentResponse>> sendComment(SendWallCommentBody p);
}
