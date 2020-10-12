package com.example.dobble.release.di.modules;

import androidx.fragment.app.Fragment;

import com.example.dobble.release.di.PerFragmentScope;
import com.example.dobble.release.utils.EmailValidatorAdapter;
import com.example.dobble.release.utils.FileManager;
import com.example.dobble.release.utils.IEmailValidator;
import com.example.dobble.release.utils.IFileManager;

import org.apache.commons.validator.routines.EmailValidator;

import dagger.Module;
import dagger.Provides;

@Module
public class UtilsModule {

    @PerFragmentScope
    @Provides
    public IFileManager provideFileManager(Fragment fragment) {
        return new FileManager(fragment.getContext());
    }

    @Provides
    public IEmailValidator provideEmailValidator() {
        return new EmailValidatorAdapter(EmailValidator.getInstance());
    }
}
