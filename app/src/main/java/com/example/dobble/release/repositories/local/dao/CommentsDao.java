package com.example.dobble.release.repositories.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.dobble.release.repositories.local.entities.CommentEntity;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface CommentsDao {
    @Query("SELECT * FROM commentEntity ORDER BY id DESC")
    Single<List<CommentEntity>> getComments();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<long[]> insert(List<CommentEntity> entities);
}
