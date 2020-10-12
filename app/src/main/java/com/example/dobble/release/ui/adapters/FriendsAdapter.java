package com.example.dobble.release.ui.adapters;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.dobble.R;
import com.example.dobble.release.App;
import com.example.dobble.release.di.components.FragmentComponent;
import com.example.dobble.release.dto.RxStatusDto;
import com.example.dobble.release.repositories.remote.payload.response.FriendResponse;
import com.example.dobble.release.ui.fragments.content.friends.FriendsViewHolder;
import com.example.dobble.release.vm.FriendsViewModel;
import com.example.dobble.release.vm.UsersViewModel;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsViewHolder> {
    public enum Type { FRIENDS, FRIENDS_REQUESTS, FRIENDS_OWN_REQUESTS }

    private List<FriendResponse> friends = new ArrayList<>();
    private FriendsViewHolder.ActionHandler actionHandler;
    private FriendsViewModel friendsViewModel;
    private FragmentManager fragmentManager;
    private UsersViewModel usersViewModel;
    private NavController navController;
    private RequestManager glide;
    private String btnTitle;
    private Resources resources;
    private FragmentComponent fragmentComponent;
    private Type type;

    public FriendsAdapter(FriendsViewHolder.ActionHandler actionHandler, Fragment fragment,
                          FriendsViewModel friendsViewModel, FragmentManager fragmentManager,
                          UsersViewModel usersViewModel,
                          NavController navController, RequestManager glide, String btnTitle, Type type,
                          Resources resources, FragmentComponent fragmentComponent) {
        this.actionHandler = actionHandler;
        this.friendsViewModel = friendsViewModel;
        this.fragmentManager = fragmentManager;
        this.usersViewModel = usersViewModel;
        this.navController = navController;
        this.glide = glide;
        this.btnTitle = btnTitle;
        this.type = type;
        this.resources = resources;
        this.fragmentComponent = fragmentComponent;

        switch (type) {
            case FRIENDS:
                friendsViewModel.getFriendsByUserIdLiveData().observe(fragment.getViewLifecycleOwner(), this::handleFriendsResponse);
                friendsViewModel.getFriendsByUserId(App.getUserId());
                break;
            case FRIENDS_REQUESTS:
                friendsViewModel.getFriendRequestsLiveData().observe(fragment.getViewLifecycleOwner(), this::handleFriendsResponse);
                friendsViewModel.getFriendRequests();
                break;
            case FRIENDS_OWN_REQUESTS:
                friendsViewModel.getFriendOwnRequestsLiveData().observe(fragment.getViewLifecycleOwner(), this::handleFriendsResponse);
                friendsViewModel.getFriendOwnRequests();
                break;
            default:
                break;
        }
    }

    @NonNull
    @Override
    public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.friends_view_holder, parent,false);
        return new FriendsViewHolder(item, actionHandler, fragmentManager, usersViewModel,
            navController, glide, btnTitle, resources, fragmentComponent);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsViewHolder holder, int position) {
        holder.init(friends.get(position));
    }

    @Override
    public void onViewRecycled(@NonNull FriendsViewHolder holder) {
        super.onViewRecycled(holder);

        holder.recycle();
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    @Override
    public long getItemId(int position) {
        return friends.get(position).getId();
    }

    public void removeFriend(FriendResponse friend) {
        int pos = friends.indexOf(friend);
        friends.remove(friend);
        notifyItemRemoved(pos);
    }

    public void handleFriendsResponse(RxStatusDto<Response<List<FriendResponse>>> r) {
        if (!r.isSuccess() || r.getData().code() == HttpURLConnection.HTTP_BAD_REQUEST) {
            return;
        }

        friends = r.getData().body();
        notifyDataSetChanged();
    }
}