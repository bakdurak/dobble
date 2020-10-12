package com.example.dobble.release.repositories.remote.interceptors;

import android.content.Context;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.dobble.release.Cfg.PREF_NAME_COOKIE;

public class SendCookieInterceptor implements Interceptor {
    private Context context;

    public SendCookieInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();

        Map<String, String> preferences = (Map<String, String>) context
            .getSharedPreferences(PREF_NAME_COOKIE, Context.MODE_PRIVATE).getAll();

        for(String cookie : preferences.values()) {
            builder.addHeader("Cookie", cookie);
        }

        return chain.proceed(builder.build());
    }
}
