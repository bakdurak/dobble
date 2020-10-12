package com.example.dobble.release.ui.fragments.content.friends;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dobble.databinding.FriendsFragmentBinding;

public class FriendsFragment extends Fragment {
    private FriendsFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FriendsFragmentBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}
