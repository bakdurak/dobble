package com.example.dobble.debug.di;

import com.example.dobble.release.di.components.AppComponent;
import com.example.dobble.release.di.modules.DatabaseModule;
import com.example.dobble.release.di.modules.NetworkModule;
import com.example.dobble.release.di.modules.ViewModelModule;
import com.example.dobble.debug.di.modules.FakeRepositoryModule;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {NetworkModule.class, FakeRepositoryModule.class, DatabaseModule.class})
@Singleton
public interface TestAppComponent extends AppComponent {}
