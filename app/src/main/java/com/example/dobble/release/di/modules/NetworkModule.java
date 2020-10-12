package com.example.dobble.release.di.modules;

import android.content.Context;

import com.example.dobble.release.repositories.remote.api.DobbleApi;
import com.example.dobble.release.repositories.remote.interceptors.ReceiveCookieInterceptor;
import com.example.dobble.release.repositories.remote.interceptors.SendCookieInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ApplicationModule.class)
public class NetworkModule {

    @Provides
    @Singleton
    public DobbleApi provideRetrofit(Context context) {
        OkHttpClient.Builder okHttpClient = new OkHttpClient().newBuilder();
        okHttpClient
            .addInterceptor(new ReceiveCookieInterceptor(context))
            .addInterceptor(new SendCookieInterceptor(context));

        Gson gson = new GsonBuilder().serializeNulls().create();

        return new Retrofit.Builder()
            .baseUrl(DobbleApi.BASE_URL)
            .client(okHttpClient.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(DobbleApi.class);
    }
}
