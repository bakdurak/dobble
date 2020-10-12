package com.example.dobble.release.repositories;


import com.example.dobble.release.repositories.local.AppDatabase;
import com.example.dobble.release.repositories.local.entities.CommentEntity;
import com.example.dobble.release.repositories.remote.api.DobbleApi;
import com.example.dobble.release.repositories.remote.payload.response.CommentsResponse;
import com.example.dobble.release.repositories.remote.payload.body.SendWallCommentBody;
import com.example.dobble.release.repositories.remote.payload.response.SendWallCommentResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import retrofit2.Response;

public class WallRepositoryImpl implements WallRepository {
    @Inject
    DobbleApi api;
    @Inject
    AppDatabase db;

    private Disposable getCommentsByUserIdDbFlow;
    private Disposable getCommentsByUserIdApiFlow;
    private PublishSubject<Response<List<CommentsResponse>>> getCommentsByUserIdPublisher = PublishSubject.create();

    @Inject
    public WallRepositoryImpl() {}

    @Override
    public Observable<Response<List<CommentsResponse>>> getCommentsByUserId(long id) {
        getCommentsByUserIdDbFlow = db.commentDao().getComments()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                this::getCommentsDbHandler,
                throwable -> {
                    // TODO: Error handling
                }
            );

        getCommentsByUserIdApiFlow = api.getCommentsByUserId(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                this::getCommentsApiHandler,
                throwable -> {
                    // TODO: Error handling
                }
            );

        return getCommentsByUserIdPublisher;
    }

    private void getCommentsDbHandler(List<CommentEntity> entities) {
        if (entities.isEmpty()) {
            return;
        }
        
        List<CommentsResponse> comments = new ArrayList<>();
        for(CommentEntity entity : entities) {
            comments.add(new CommentsResponse(entity));
        }
        Response<List<CommentsResponse>> response = Response.success(comments);
        getCommentsByUserIdPublisher.onNext(response);
    }

    private void getCommentsApiHandler(Response<List<CommentsResponse>> response) {
        if (response.body() == null) {
            return;
        }

        if (getCommentsByUserIdDbFlow != null) {
            getCommentsByUserIdDbFlow.dispose();
        }

        // TODO: Wrap with transaction
        if (!response.body().isEmpty()) {
            List<CommentEntity> entities = new ArrayList<>();
            for(CommentsResponse comment : response.body()) {
                CommentEntity entity = new CommentEntity(comment);
                entities.add(entity);
            }
            db.commentDao().insert(entities).subscribeOn(Schedulers.io()).subscribe();
        }

        getCommentsByUserIdPublisher.onNext(response);
    }

    @Override
    public Observable<Response<SendWallCommentResponse>> sendComment(SendWallCommentBody p) {
        return api.sendWallComment(p);
    }
}
