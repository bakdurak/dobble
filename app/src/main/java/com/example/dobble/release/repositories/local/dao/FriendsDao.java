package com.example.dobble.release.repositories.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.dobble.release.repositories.local.entities.FriendEntity;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface FriendsDao {
    @Query("SELECT * FROM friendentity")
    Single<List<FriendEntity>> getFriends();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<long[]> insert(List<FriendEntity> friendEntityList);

    @Query("DELETE FROM friendentity WHERE id = :id")
    Single<Integer> removeFriendById(long id);
}
