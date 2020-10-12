package com.example.dobble.release.di.modules;

import android.content.Context;

import androidx.room.Room;

import com.example.dobble.release.repositories.local.AppDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static com.example.dobble.release.Cfg.DATABASE_NAME;

@Module(includes = ApplicationModule.class)
public class DatabaseModule {

    @Provides
    @Singleton
    public AppDatabase provideDatabase(Context context) {
        return Room
            .databaseBuilder(context, AppDatabase.class, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build();
    }
}
