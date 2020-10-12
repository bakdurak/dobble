package com.example.dobble.release.di.components;

import com.example.dobble.debug.repositories.FakeWallRepositoryImpl;
import com.example.dobble.release.di.modules.DatabaseModule;
import com.example.dobble.release.di.modules.NetworkModule;
import com.example.dobble.release.di.modules.RepositoryModule;
import com.example.dobble.release.repositories.FriendsRepositoryImpl;
import com.example.dobble.release.repositories.InitialRepositoryImpl;
import com.example.dobble.release.repositories.MessagesRepositoryImpl;
import com.example.dobble.release.repositories.ProfileRepositoryImpl;
import com.example.dobble.release.repositories.RegistrationRepositoryImpl;
import com.example.dobble.release.repositories.UsersRepositoryImpl;
import com.example.dobble.release.vm.FriendsViewModelImpl;
import com.example.dobble.release.vm.HeaderViewModelImpl;
import com.example.dobble.release.vm.InitialViewModelImpl;
import com.example.dobble.release.vm.LoginViewModelImpl;
import com.example.dobble.release.vm.MessagesViewModelImpl;
import com.example.dobble.release.vm.ProfileViewModelImpl;
import com.example.dobble.release.vm.RegistrationViewModelImpl;
import com.example.dobble.release.vm.UsersViewModelImpl;
import com.example.dobble.release.vm.WallViewModelImpl;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {NetworkModule.class, RepositoryModule.class, DatabaseModule.class})
@Singleton
public interface AppComponent {

    void inject(RegistrationRepositoryImpl r);
    void inject(InitialRepositoryImpl r);
    void inject(ProfileRepositoryImpl r);
    void inject(UsersRepositoryImpl r);
    void inject(FakeWallRepositoryImpl r);
    void inject(FriendsRepositoryImpl r);
    void inject(MessagesRepositoryImpl r);

    void inject(RegistrationViewModelImpl vm);
    void inject(LoginViewModelImpl vm);
    void inject(InitialViewModelImpl vm);
    void inject(ProfileViewModelImpl vm);
    void inject(HeaderViewModelImpl vm);
    void inject(UsersViewModelImpl vm);
    void inject(WallViewModelImpl vm);
    void inject(FriendsViewModelImpl vm);
    void inject(MessagesViewModelImpl vm);

    FragmentComponent.Builder fragmentSubComponentBuilder();
}
