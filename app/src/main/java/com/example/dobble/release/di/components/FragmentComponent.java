package com.example.dobble.release.di.components;

import androidx.fragment.app.Fragment;

import com.example.dobble.release.di.PerFragmentScope;
import com.example.dobble.release.di.modules.UtilsModule;
import com.example.dobble.release.di.modules.ViewModelModule;
import com.example.dobble.release.ui.fragments.HeaderFragment;
import com.example.dobble.release.ui.fragments.InitialFragment;
import com.example.dobble.release.ui.fragments.LoginFragment;
import com.example.dobble.release.ui.fragments.RegistrationFragment;
import com.example.dobble.release.ui.fragments.content.MessagesFragment;
import com.example.dobble.release.ui.fragments.content.PhotosFragment;
import com.example.dobble.release.ui.fragments.content.ProfileFragment;
import com.example.dobble.release.ui.fragments.content.SendMsgDialogFragment;
import com.example.dobble.release.ui.fragments.content.UsersFragment;
import com.example.dobble.release.ui.fragments.content.WallFragment;
import com.example.dobble.release.ui.fragments.content.friends.FriendsFriendsFragment;
import com.example.dobble.release.ui.fragments.content.friends.FriendsOwnRequestsFragment;
import com.example.dobble.release.ui.fragments.content.friends.FriendsRequestsFragment;

import dagger.BindsInstance;
import dagger.Subcomponent;

@PerFragmentScope
@Subcomponent(modules = {ViewModelModule.class, UtilsModule.class})
public interface FragmentComponent {
    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        Builder with(Fragment fragment);

        FragmentComponent build();
    }

    void inject(InitialFragment f);
    void inject(ProfileFragment f);
    void inject(HeaderFragment f);
    void inject(RegistrationFragment f);
    void inject(LoginFragment f);
    void inject(UsersFragment f);
    void inject(WallFragment f);
    void inject(FriendsFriendsFragment f);
    void inject(FriendsRequestsFragment ff);
    void inject(FriendsOwnRequestsFragment f);
    void inject(PhotosFragment f);
    void inject(MessagesFragment f);
    void inject(SendMsgDialogFragment f);
}
