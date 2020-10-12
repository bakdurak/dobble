package com.example.dobble.release.ui.fragments.content.friends;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.dobble.R;
import com.example.dobble.databinding.FriendsNavbarFragmentBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class FriendsNavbarFragment extends Fragment {
    private FriendsNavbarFragmentBinding binding;
    private NavController navController;

    private class TabListener implements TabLayout.OnTabSelectedListener{

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            String tabId = tab.getText().toString();
            Resources r = getResources();

            if (tabId.equals(r.getString(R.string.friends_navbar_friends))) {
                navController.navigate(R.id.friendsFriendsFragment);
            }
            else if(tabId.equals(r.getString(R.string.friends_navbar_requests))) {
                navController.navigate(R.id.friendsRequestsFragment);
            }
            else if(tabId.equals(r.getString(R.string.friends_navbar_own_requets))) {
                navController.navigate(R.id.friendsOwnRequestsFragment);
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
        binding = FriendsNavbarFragmentBinding.inflate(inflater, container, false);

        binding.friendsTabLayout.addOnTabSelectedListener(new TabListener());

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        Fragment fragmentContainer = getParentFragmentManager()
            .findFragmentById(R.id.nav_friends_fragment_container);
        navController = Navigation.findNavController(fragmentContainer.getView());
    }
}
