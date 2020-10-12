package com.example.dobble.release.ui.fragments.content.friends;

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
import com.bumptech.glide.RequestManager;
import com.example.dobble.R;
import com.example.dobble.databinding.FriendsRequestsFragmentBinding;
import com.example.dobble.release.App;
import com.example.dobble.release.di.components.FragmentComponent;
import com.example.dobble.release.dto.FriendDto;
import com.example.dobble.release.dto.RxStatusDto;
import com.example.dobble.release.repositories.remote.payload.body.ApproveFriendBody;
import com.example.dobble.release.ui.adapters.FriendsAdapter;
import com.example.dobble.release.vm.FriendsViewModel;
import com.example.dobble.release.vm.UsersViewModel;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.dobble.release.Cfg.REMOVE_FRIEND_VIEW_HOLDER_DELAY;

public class FriendsRequestsFragment extends Fragment implements FriendsViewHolder.ActionHandler {
    private static final String ACTION_BTN_TEXT = "Approve";

    @Inject
    FriendsViewModel friendsViewModel;
    @Inject
    UsersViewModel usersViewModel;

    private FriendsRequestsFragmentBinding binding;
    private NavController navController;
    private RequestManager glide;
    private RecyclerView recyclerView;
    private FriendsAdapter adapter;
    private Set<Disposable> timers = new HashSet<>();
    private FragmentComponent fragmentComponent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentComponent = App
            .getAppComponent()
            .fragmentSubComponentBuilder()
            .with(this)
            .build();
        fragmentComponent.inject(this);

        glide = Glide.with(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FriendsRequestsFragmentBinding.inflate(inflater, container, false);

        friendsViewModel.approveFriendShipByUserIdLiveData().observe(getViewLifecycleOwner(), this::approveFriendShipByUserIdUpdate);

        return binding.getRoot();
    }

    private void approveFriendShipByUserIdUpdate(RxStatusDto<FriendDto> r) {
        FriendsViewHolder friendsViewHolder = (FriendsViewHolder) recyclerView
            .findViewHolderForItemId(r.getData().getFriendResponse().getId());

        if (!r.isSuccess()) {
            if (friendsViewHolder != null) {
                friendsViewHolder.setActionResultText(getString(R.string.friends_friends_approve_friend_error));
            }
            return;
        }

        if (friendsViewHolder != null) {
            friendsViewHolder.setActionResultText(getString(R.string.friends_friends_approve_friend_success));
        }

        Disposable disposable = Single.timer(REMOVE_FRIEND_VIEW_HOLDER_DELAY, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                t -> adapter.removeFriend(r.getData().getFriendResponse())
            );
        timers.add(disposable);
    }

    @Override
    public void onStart() {
        super.onStart();

        Fragment contentFragmentContainer = getParentFragment().getParentFragment().getParentFragment()
            .getParentFragmentManager().findFragmentById(R.id.nav_content_fragment_container);
        navController = Navigation.findNavController(contentFragmentContainer.getView());

        recyclerView = binding.friendsRequestsRecyclerView;
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FriendsAdapter(this,this, friendsViewModel,
            getParentFragmentManager(), usersViewModel, navController, glide, ACTION_BTN_TEXT,
            FriendsAdapter.Type.FRIENDS_REQUESTS, getResources(), fragmentComponent);
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void handle(FriendsViewHolder vh) {
        friendsViewModel.approveFriendShipByUserId(new ApproveFriendBody(vh.getUserId()),
            vh.getFriendResponse());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        recyclerView.setAdapter(null);
        for(Disposable disposable : timers) {
            disposable.dispose();
        }
        friendsViewModel.releaseResources();
        usersViewModel.releaseResources();
    }
}
