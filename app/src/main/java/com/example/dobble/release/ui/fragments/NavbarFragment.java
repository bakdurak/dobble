package com.example.dobble.release.ui.fragments;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
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
import com.example.dobble.databinding.NavbarFragmentBinding;
import com.example.dobble.release.App;
import com.example.dobble.release.ui.activities.MainActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import static com.example.dobble.release.Cfg.NAV_BUNDLE_USER_ID_KEY;

public class NavbarFragment extends Fragment {
    private NavbarFragmentBinding binding;
    private NavController navController;
    private TabListener tabListener = new TabListener();
    private ColorStateList navBarTextColors;

    private class TabListener implements TabLayout.OnTabSelectedListener {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            onSelectedTab(tab);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) { }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            onSelectedTab(tab);
        }
    }

    private void onSelectedTab(TabLayout.Tab tab) {
        binding.contentTabLayout.setTabTextColors(navBarTextColors);
        binding.contentTabLayout.setSelectedTabIndicatorColor(getResources()
            .getColor(R.color.navbar_selected_indicator));

        String tabId = tab.getText().toString();
        Resources r = getResources();
        if (tabId.equals(r.getString(R.string.navbar_wall))) {
            Bundle bundle = new Bundle();
            bundle.putLong(NAV_BUNDLE_USER_ID_KEY, App.getUserId());
            navController.navigate(R.id.wallFragment);
        }
        else if(tabId.equals(r.getString(R.string.navbar_users))) {
            navController.navigate(R.id.usersFragment);
        }
        else if(tabId.equals(r.getString(R.string.navbar_friends))) {
            navController.navigate(R.id.friendsFragment);
        }
        else if(tabId.equals(r.getString(R.string.navbar_photos))) {
            navController.navigate(R.id.photosFragment);
        }
        else if(tabId.equals(r.getString(R.string.navbar_messages))) {
            navController.navigate(R.id.messagesFragment);
        }
        else if(tabId.equals(r.getString(R.string.navbar_profile))) {
            Bundle bundle = new Bundle();
            bundle.putLong(NAV_BUNDLE_USER_ID_KEY, App.getUserId());
            navController.navigate(R.id.profileFragment, bundle);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = NavbarFragmentBinding.inflate(inflater, container, false);

        binding.contentTabLayout.addOnTabSelectedListener(tabListener);
        navBarTextColors = binding.contentTabLayout.getTabTextColors();

        MainActivity.IBackPressed backPressedCallback = this::backPressed;
        ((MainActivity)getActivity()).addBackPressedCallback(NavbarFragment.class,
            backPressedCallback);

        return binding.getRoot();
    }

    public void deselectAll() {
        Resources r = getResources();
        binding.contentTabLayout.setSelectedTabIndicatorColor(r.getColor(R.color.transparent));
        binding.contentTabLayout.setTabTextColors(r.getColor(R.color.gray), r.getColor(R.color.gray));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ((MainActivity)getActivity()).removeBackPressedCallback(NavbarFragment.class);
    }

    private void backPressed() {
        int curFragmentContentId = navController.getCurrentDestination().getId();
        TabLayout tabLayout = binding.contentTabLayout;
        // Remove and add tab click listener to avoid undesirable behavior
        tabLayout.removeOnTabSelectedListener(tabListener);
        switch (curFragmentContentId)
        {
            case R.id.wallFragment:
                tabLayout.selectTab(tabLayout.getTabAt(0));
                break;
            case R.id.usersFragment:
                tabLayout.selectTab(tabLayout.getTabAt(1));
                break;
            case R.id.profileFragment:
                tabLayout.selectTab(tabLayout.getTabAt(7));
                break;
            case R.id.photosFragment:
                tabLayout.selectTab(tabLayout.getTabAt(4));
                break;
            case R.id.messagesFragment:
                tabLayout.selectTab(tabLayout.getTabAt(5));
                break;
            case R.id.friendsFragment:
                tabLayout.selectTab(tabLayout.getTabAt(2));
                break;
            default:
                break;
        }
        binding.contentTabLayout.addOnTabSelectedListener(tabListener);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Fragment contentFragmentContainer = getParentFragmentManager()
            .findFragmentById(R.id.nav_content_fragment_container);
        navController = Navigation.findNavController(contentFragmentContainer.getView());
    }
}
