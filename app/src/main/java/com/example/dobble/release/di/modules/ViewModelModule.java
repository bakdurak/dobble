package com.example.dobble.release.di.modules;


import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.dobble.release.di.PerFragmentScope;
import com.example.dobble.release.vm.FriendsViewModel;
import com.example.dobble.release.vm.FriendsViewModelImpl;
import com.example.dobble.release.vm.HeaderViewModel;
import com.example.dobble.release.vm.HeaderViewModelImpl;
import com.example.dobble.release.vm.InitialViewModel;
import com.example.dobble.release.vm.InitialViewModelImpl;
import com.example.dobble.release.vm.LoginViewModel;
import com.example.dobble.release.vm.LoginViewModelImpl;
import com.example.dobble.release.vm.MessagesViewModel;
import com.example.dobble.release.vm.MessagesViewModelImpl;
import com.example.dobble.release.vm.ProfileViewModel;
import com.example.dobble.release.vm.ProfileViewModelImpl;
import com.example.dobble.release.vm.RegistrationViewModel;
import com.example.dobble.release.vm.RegistrationViewModelImpl;
import com.example.dobble.release.vm.UsersViewModel;
import com.example.dobble.release.vm.UsersViewModelImpl;
import com.example.dobble.release.vm.WallViewModel;
import com.example.dobble.release.vm.WallViewModelImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class ViewModelModule {

    @PerFragmentScope
    @Provides
    public RegistrationViewModel provideRegisterViewModel(Fragment fragment) {
        return new ViewModelProvider(fragment).get(RegistrationViewModelImpl.class);
    }

    @PerFragmentScope
    @Provides
    public InitialViewModel provideInitialViewModel(Fragment fragment) {
        return new ViewModelProvider(fragment).get(InitialViewModelImpl.class);
    }

    @PerFragmentScope
    @Provides
    public ProfileViewModel provideProfileViewModel(Fragment fragment) {
        return new ViewModelProvider(fragment).get(ProfileViewModelImpl.class);
    }

    @PerFragmentScope
    @Provides
    public HeaderViewModel provideHeaderViewModel(Fragment fragment) {
        return new ViewModelProvider(fragment).get(HeaderViewModelImpl.class);
    }

    @PerFragmentScope
    @Provides
    public LoginViewModel provideLoginViewModel(Fragment fragment) {
        return new ViewModelProvider(fragment).get(LoginViewModelImpl.class);
    }

    @PerFragmentScope
    @Provides
    public UsersViewModel provideUsersViewModel(Fragment fragment) {
        return new ViewModelProvider(fragment).get(UsersViewModelImpl.class);
    }

    @PerFragmentScope
    @Provides
    public WallViewModel provideWallViewModel(Fragment fragment) {
        return new ViewModelProvider(fragment).get(WallViewModelImpl.class);
    }

    @PerFragmentScope
    @Provides
    public FriendsViewModel provideFriendsViewModel(Fragment fragment) {
        return new ViewModelProvider(fragment).get(FriendsViewModelImpl.class);
    }

    @PerFragmentScope
    @Provides
    public MessagesViewModel provideMessagesViewModel(Fragment fragment) {
        return new ViewModelProvider(fragment).get(MessagesViewModelImpl.class);
    }
}
