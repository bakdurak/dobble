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
import com.example.dobble.databinding.EntranceFragmentBinding;
import com.google.android.material.tabs.TabLayout;


public class EntranceFragment extends Fragment {
    private EntranceFragmentBinding binding;
    private TabListener tabListener = new TabListener();
    private NavController navController;
    
    private class TabListener implements TabLayout.OnTabSelectedListener {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            String tabId = tab.getText().toString();
            Resources r = getResources();
            if (tabId.equals(r.getString(R.string.register_navbar_login))) {
                navController.navigate(R.id.navLoginFragment);
            }
            else if(tabId.equals(r.getString(R.string.register_navbar_register))) {
                navController.navigate(R.id.navRegistrationFragment);
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) { }

        @Override
        public void onTabReselected(TabLayout.Tab tab) { }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = EntranceFragmentBinding.inflate(inflater, container, false);
        binding.registerTabLayout.addOnTabSelectedListener(tabListener);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Fragment entranceFragmentContainer = getChildFragmentManager()
            .findFragmentById(R.id.entranceFragmentContainer);
        navController = Navigation.findNavController(entranceFragmentContainer.getView());
    }
}
