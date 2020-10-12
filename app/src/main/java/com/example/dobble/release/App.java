package com.example.dobble.release;

import android.app.Application;
import android.content.Context;
import android.util.Log;


import com.example.dobble.release.di.components.AppComponent;
import com.example.dobble.release.di.components.DaggerAppComponent;
import com.example.dobble.release.di.modules.ApplicationModule;
import com.example.dobble.BuildConfig;
import com.example.dobble.debug.di.DaggerTestAppComponent;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.engineio.client.Transport;

import static com.example.dobble.release.Cfg.BASE_URL;
import static com.example.dobble.release.Cfg.PREF_NAME_COOKIE;


public class App extends Application {
    public final static long UNAUTHORIZED = -1;

    private static AppComponent appComponent;
    private static Context context;
    private static long userId = UNAUTHORIZED;
    private static Socket socket;

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

        appComponent = buildAppComponent();
    }

    protected AppComponent buildAppComponent() {
        String env = BuildConfig.ENV;
        if (env.equals("debug")) {
            return DaggerTestAppComponent.builder()
                .applicationModule(new ApplicationModule(context))
                .build();
        }
        else {
            return DaggerAppComponent.builder()
                .applicationModule(new ApplicationModule(context))
                .build();
        }
//        return null;
    }

    private static void initWebSocket() {
        try {
            socket = IO.socket(BASE_URL);
            socket.io().on(Manager.EVENT_TRANSPORT, args -> {
                Transport transport = (Transport)args[0];

                transport.on(Transport.EVENT_REQUEST_HEADERS, args1 -> {
                    Map<String, List<String>> headers = (Map<String, List<String>>) args1[0];

                    Map<String, String> preferences = (Map<String, String>) context
                        .getSharedPreferences(PREF_NAME_COOKIE, Context.MODE_PRIVATE).getAll();
                    for(String cookie : preferences.values()) {
                        // modify request headers
                        headers.put("Cookie", Arrays.asList(cookie));
                    }
                });
            });

            socket.connect();
        } catch (URISyntaxException e) {
            // TODO: Error handling

            Log.d("", e.getLocalizedMessage());
        }
    }

    public static long getUserId() {
        return userId;
    }

    public static void setUserId(long id) {
        userId = id;

        if (socket == null || !socket.connected()) {
            initWebSocket();
        }
    }

    public static boolean isAuthorized() {
        return userId != UNAUTHORIZED;
    }

    public static void logout() {
        userId = UNAUTHORIZED;

        if (socket != null) {
            socket.disconnect();
        }
    }

    public static Socket getSocket() {
        return socket;
    }
}
