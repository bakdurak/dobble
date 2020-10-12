package com.example.dobble.release.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.dobble.R;
import com.example.dobble.databinding.InitialFragmentBinding;
import com.example.dobble.release.App;
import com.example.dobble.release.dto.RxStatusDto;
import com.example.dobble.release.repositories.remote.payload.response.CheckAuthResponse;
import com.example.dobble.release.vm.InitialViewModel;

import java.net.HttpURLConnection;

import javax.inject.Inject;

import retrofit2.Response;

import static com.example.dobble.release.Cfg.PREF_USER;
import static com.example.dobble.release.Cfg.USER_ID_KEY;

public class InitialFragment extends Fragment {
    @Inject
    InitialViewModel vm;

    private InitialFragmentBinding binding;
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        App
            .getAppComponent()
            .fragmentSubComponentBuilder()
            .with(this)
            .build()
            .inject(this);

        binding = InitialFragmentBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        navController = Navigation.findNavController(getView());
        vm.checkAuthLiveData().observe(getViewLifecycleOwner(), this::checkAuthUpdate);
        vm.checkAuth();
    }

    private void checkAuthUpdate(RxStatusDto<Response<CheckAuthResponse>> r) {
        if (!r.isSuccess()) {
            return;
        }
        else {
            navController.navigate(R.id.entranceFragment);
        }

        if (r.getData().code() == HttpURLConnection.HTTP_OK) {
            SharedPreferences.Editor editor = getActivity().getApplicationContext()
                .getSharedPreferences(PREF_USER, Context.MODE_PRIVATE).edit();
            long userId = r.getData().body().getId();
            editor.putString(USER_ID_KEY, String.valueOf(userId));
            editor.apply();

            App.setUserId(userId);

            navController.navigate(R.id.contentFragment);
        }
        else {
            navController.navigate(R.id.entranceFragment);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        vm.releaseResources();
    }
}
