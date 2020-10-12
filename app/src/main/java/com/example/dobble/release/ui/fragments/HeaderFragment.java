package com.example.dobble.release.ui.fragments;

import android.app.Activity;
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
import com.example.dobble.databinding.HeaderFragmentBinding;
import com.example.dobble.release.App;
import com.example.dobble.release.vm.HeaderViewModel;

import javax.inject.Inject;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.example.dobble.release.Cfg.COOKIE_SID;
import static com.example.dobble.release.Cfg.PREF_NAME_COOKIE;


public class HeaderFragment extends Fragment {
    @Inject
    HeaderViewModel vm;

    private HeaderFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        App
            .getAppComponent()
            .fragmentSubComponentBuilder()
            .with(this)
            .build()
            .inject(this);

        binding = HeaderFragmentBinding.inflate(inflater, container, false);
        binding.buttonLogout.setOnClickListener(this::onLogoutBtnClick);
        
        if (App.isAuthorized()) {
            binding.buttonLogout.setVisibility(VISIBLE);
        }
        else {
            binding.buttonLogout.setVisibility(INVISIBLE);
        }
        
        return binding.getRoot();
    }

    private void onLogoutBtnClick(View v) {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }

        vm.logout();

        SharedPreferences.Editor editor = activity.getApplicationContext()
            .getSharedPreferences(PREF_NAME_COOKIE, Context.MODE_PRIVATE).edit();
        editor.remove(COOKIE_SID);
        editor.apply();

        App.logout();

        NavController navController = Navigation.findNavController(getActivity(),
            R.id.nav_host_fragment_container);
        navController.navigate(R.id.entranceFragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        vm.releaseResources();
    }
}
