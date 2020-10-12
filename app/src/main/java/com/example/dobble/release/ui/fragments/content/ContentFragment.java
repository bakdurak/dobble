package com.example.dobble.release.ui.fragments.content;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dobble.databinding.ContentFragmentBinding;

public class ContentFragment extends Fragment {
    private ContentFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ContentFragmentBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}
