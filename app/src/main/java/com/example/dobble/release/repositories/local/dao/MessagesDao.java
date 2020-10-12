package com.example.dobble.release.repositories.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.dobble.release.repositories.local.entities.MessageEntity;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface MessagesDao {
    @Query("SELECT * FROM messageEntity ORDER BY msgId DESC")
    Single<List<MessageEntity>> getMessages();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<long[]> insert(List<MessageEntity> messageEntities);
}
