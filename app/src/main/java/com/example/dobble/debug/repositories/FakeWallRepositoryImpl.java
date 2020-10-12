package com.example.dobble.debug.repositories;

import com.example.dobble.release.repositories.WallRepository;
import com.example.dobble.release.repositories.remote.payload.response.CommentsResponse;
import com.example.dobble.release.repositories.remote.payload.body.SendWallCommentBody;
import com.example.dobble.release.repositories.remote.payload.response.SendWallCommentResponse;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import retrofit2.Response;

public class FakeWallRepositoryImpl implements WallRepository {
    private final List<CommentsResponse> comments = new ArrayList<CommentsResponse>() {{
        add(new CommentsResponse(1, null, 1, 0L, "Name1", "Surname1", "Content1", null));
        add(new CommentsResponse(2, null, 2, 0L, "Name2", "Surname2", "Content2", null));
        add(new CommentsResponse(3, null, 3, 0L, "Name3", "Surname3", "Content3", null));
        add(new CommentsResponse(4, null, 4, 0L, "Name4", "Surname4", "Content4", null));
    }};

    @Inject
    public FakeWallRepositoryImpl() {}

    @Override
    public Observable<Response<List<CommentsResponse>>> getCommentsByUserId(long id) {
        return Observable.just(Response.success(comments));
    }

    @Override
    public Observable<Response<SendWallCommentResponse>> sendComment(SendWallCommentBody p) {
        // TODO: Implement method
        return null;
    }
}
