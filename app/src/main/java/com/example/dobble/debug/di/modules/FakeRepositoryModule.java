package com.example.dobble.debug.di.modules;

import com.example.dobble.debug.repositories.FakeFriendsRepository;
import com.example.dobble.debug.repositories.FakeMessagesRepositoryImpl;
import com.example.dobble.debug.repositories.FakeProfileRepositoryImpl;
import com.example.dobble.debug.repositories.FakeUsersRepositoryImpl;
import com.example.dobble.debug.repositories.FakeWallRepositoryImpl;
import com.example.dobble.release.repositories.FriendsRepository;
import com.example.dobble.release.repositories.InitialRepository;
import com.example.dobble.release.repositories.MessagesRepository;
import com.example.dobble.release.repositories.ProfileRepository;
import com.example.dobble.release.repositories.RegistrationRepository;
import com.example.dobble.debug.repositories.FakeInitialRepositoryImpl;
import com.example.dobble.debug.repositories.FakeRegistrationRepositoryImpl;
import com.example.dobble.release.repositories.UsersRepository;
import com.example.dobble.release.repositories.WallRepository;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class FakeRepositoryModule {

    @Binds
    public abstract RegistrationRepository provideRegisterRepository(FakeRegistrationRepositoryImpl r);

    @Binds
    public abstract InitialRepository provideInitialRepository(FakeInitialRepositoryImpl r);

    @Binds
    public abstract ProfileRepository provideProfileRepository(FakeProfileRepositoryImpl r);

    @Binds
    public abstract UsersRepository provideUsersRepository(FakeUsersRepositoryImpl r);

    @Binds
    public abstract WallRepository provideWallRepository(FakeWallRepositoryImpl r);

    @Binds
    public abstract FriendsRepository provideFriendsRepository(FakeFriendsRepository r);

    @Binds
    public abstract MessagesRepository provideMessageRepository(FakeMessagesRepositoryImpl r);
}
