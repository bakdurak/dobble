package com.example.dobble.release.repositories.remote.interceptors;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.net.HttpCookie;

import okhttp3.Interceptor;
import okhttp3.Response;

import static com.example.dobble.release.Cfg.PREF_NAME_COOKIE;

public class ReceiveCookieInterceptor implements Interceptor {
    private Context context;
    private static final String SET_COOKIE = "Set-cookie";

    public ReceiveCookieInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        String setCookieHeader = originalResponse.header(SET_COOKIE);

        boolean cookiesReceived = setCookieHeader != null && !setCookieHeader.isEmpty();
        if (cookiesReceived) {
            SharedPreferences.Editor editor = context
                .getSharedPreferences(PREF_NAME_COOKIE, Context.MODE_PRIVATE).edit();
            for(String responseHeader : originalResponse.headers(SET_COOKIE)) {
                HttpCookie cookie = HttpCookie.parse(responseHeader).get(0);
                editor.putString(cookie.getName(), responseHeader);
            }
            editor.apply();
        }

        return originalResponse;
    }
}
