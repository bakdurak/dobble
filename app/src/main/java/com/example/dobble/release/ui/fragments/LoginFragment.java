package com.example.dobble.release.ui.fragments;

import android.content.res.Resources;
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
import com.example.dobble.databinding.LoginFragmentBinding;
import com.example.dobble.release.App;
import com.example.dobble.release.dto.RxStatusDto;
import com.example.dobble.release.repositories.remote.payload.body.LoginBody;
import com.example.dobble.release.repositories.remote.payload.response.CheckAuthResponse;
import com.example.dobble.release.vm.LoginViewModel;

import java.net.HttpURLConnection;

import javax.inject.Inject;

import retrofit2.Response;

public class LoginFragment extends Fragment {
    @Inject
    LoginViewModel vm;
    private LoginFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        App
            .getAppComponent()
            .fragmentSubComponentBuilder()
            .with(this)
            .build()
            .inject(this);

        binding = LoginFragmentBinding.inflate(inflater, container, false);
        binding.loginSubmit.setOnClickListener(this::onLoginSubmitBtnClick);
        vm.loginLiveData().observe(getViewLifecycleOwner(), this::loginUpdate);

        return binding.getRoot();
    }

    private void loginUpdate(RxStatusDto<Response<CheckAuthResponse>> response) {
        Resources r = getResources();
        if (!response.isSuccess()) {
            binding.loginErrorView.setText(r.getString(R.string.login_rx_error));
            return;
        }

        if (response.getData().code() == HttpURLConnection.HTTP_CREATED) {
            App.setUserId(response.getData().body().getId());

            NavController navController = Navigation.findNavController(getActivity(),
                R.id.nav_host_fragment_container);
            navController.navigate(R.id.contentFragment);
        }
        else if(response.getData().code() == HttpURLConnection.HTTP_BAD_REQUEST) {
            binding.loginErrorView.setText(r.getString(R.string.login_wrong_credentials));
        }
    }

    private void onLoginSubmitBtnClick(View v) {
        String email = binding.loginInputEmail.getText().toString();
        String password = binding.loginInputPassword.getText().toString();

        String error = "";
        Resources r = getResources();
        if (email.isEmpty()) {
            error += r.getString(R.string.login_empty_email);
        }
        if (password.isEmpty()) {
            if (error.isEmpty()) {
                error += r.getString(R.string.login_empty_password);
            }
            else {
                error += "\n";
                error += r.getString(R.string.login_empty_password);
            }
        }
        if (!error.isEmpty()) {
            binding.loginErrorView.setText(error);
            return;
        }

        LoginBody payload = new LoginBody(email, password);
        binding.loginErrorView.setText("");
        vm.login(payload);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        vm.releaseResources();
    }
}
