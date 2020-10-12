package com.example.dobble.release.repositories.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.dobble.release.repositories.local.entities.ProfileEntity;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface ProfileDao {
    @Query("SELECT * FROM profileEntity WHERE id = :id")
    Single<ProfileEntity> getById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> insert(ProfileEntity entity);
}
