package com.example.dobble.release.repositories.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.dobble.release.repositories.local.dao.CommentsDao;
import com.example.dobble.release.repositories.local.dao.FriendsDao;
import com.example.dobble.release.repositories.local.dao.MessagesDao;
import com.example.dobble.release.repositories.local.dao.ProfileDao;
import com.example.dobble.release.repositories.local.entities.CommentEntity;
import com.example.dobble.release.repositories.local.entities.FriendEntity;
import com.example.dobble.release.repositories.local.entities.MessageEntity;
import com.example.dobble.release.repositories.local.entities.ProfileEntity;


@Database(entities = {ProfileEntity.class, CommentEntity.class, FriendEntity.class,
    MessageEntity.class}, version = 7, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProfileDao profileDao();
    public abstract CommentsDao commentDao();
    public abstract FriendsDao friendsDao();
    public abstract MessagesDao messagesDao();
}
