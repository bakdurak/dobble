package com.example.dobble.release.ui.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.dobble.R;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Map<Class<? extends Fragment>, IBackPressed> backPressedCallbacks = new HashMap<>();

    @FunctionalInterface
    public interface IBackPressed {
        void backPressed();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.main_activity);
    }

    public void addBackPressedCallback(Class<? extends Fragment> cls, IBackPressed callback) {
        backPressedCallbacks.put(cls, callback);
    }

    public void removeBackPressedCallback(Class<? extends Fragment> cls) {
        backPressedCallbacks.remove(cls);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        for(IBackPressed callback : backPressedCallbacks.values()) {
            callback.backPressed();
        }
    }
}
