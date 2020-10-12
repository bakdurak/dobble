package com.example.dobble.release.ui.fragments.content;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dobble.R;
import com.example.dobble.databinding.WallFragmentBinding;
import com.example.dobble.release.App;
import com.example.dobble.release.ui.adapters.WallAdapter;
import com.example.dobble.release.vm.WallViewModel;


import javax.inject.Inject;

import static com.example.dobble.release.Cfg.NAV_BUNDLE_USER_ID_KEY;

public class WallFragment extends Fragment {
    @Inject
    WallViewModel vm;

    private WallFragmentBinding binding;
    private RecyclerView recyclerView;
    private WallAdapter adapter;
    private NavController navController;
    private long wallOwnerId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = WallFragmentBinding.inflate(inflater, container, false);

        App
            .getAppComponent()
            .fragmentSubComponentBuilder()
            .with(this)
            .build()
            .inject(this);

        Bundle bundle = getArguments();
        if (bundle != null) {
            wallOwnerId = bundle.getLong(NAV_BUNDLE_USER_ID_KEY);
        }
        else {
            wallOwnerId = App.getUserId();
        }

        Fragment contentFragmentContainer = getParentFragment().getParentFragmentManager()
            .findFragmentById(R.id.nav_content_fragment_container);
        navController = Navigation.findNavController(contentFragmentContainer.getView());

        recyclerView = binding.wallCommentRecyclerView;
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new WallAdapter(Glide.with(this), navController, wallOwnerId,
            getViewLifecycleOwner(), getResources(), vm);
        recyclerView.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        recyclerView.setAdapter(null);
        vm.releaseResources();
    }
}
