package com.example.dobble.release.di.modules;

import com.example.dobble.release.repositories.FriendsRepository;
import com.example.dobble.release.repositories.FriendsRepositoryImpl;
import com.example.dobble.release.repositories.InitialRepository;
import com.example.dobble.release.repositories.InitialRepositoryImpl;
import com.example.dobble.release.repositories.MessagesRepository;
import com.example.dobble.release.repositories.MessagesRepositoryImpl;
import com.example.dobble.release.repositories.ProfileRepository;
import com.example.dobble.release.repositories.ProfileRepositoryImpl;
import com.example.dobble.release.repositories.RegistrationRepository;
import com.example.dobble.release.repositories.RegistrationRepositoryImpl;
import com.example.dobble.release.repositories.UsersRepository;
import com.example.dobble.release.repositories.UsersRepositoryImpl;
import com.example.dobble.release.repositories.WallRepository;
import com.example.dobble.release.repositories.WallRepositoryImpl;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class RepositoryModule {

    @Binds
    public abstract RegistrationRepository provideRegisterRepository(RegistrationRepositoryImpl r);

    @Binds
    public abstract InitialRepository provideInitialRepository(InitialRepositoryImpl r);

    @Binds
    public abstract ProfileRepository provideProfileRepository(ProfileRepositoryImpl r);

    @Binds
    public abstract UsersRepository provideUsersRepository(UsersRepositoryImpl r);

    @Binds
    public abstract WallRepository provideWallRepository(WallRepositoryImpl r);

    @Binds
    public abstract FriendsRepository provideFriendsRepository(FriendsRepositoryImpl r);

    @Binds
    public abstract MessagesRepository provideMessagesRepository(MessagesRepositoryImpl r);
}
