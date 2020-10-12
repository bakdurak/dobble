package com.example.dobble.release.ui.fragments.content;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.dobble.R;
import com.example.dobble.databinding.UsersFragmentBinding;
import com.example.dobble.release.App;
import com.example.dobble.release.di.components.FragmentComponent;
import com.example.dobble.release.dto.RxStatusDto;
import com.example.dobble.release.repositories.remote.payload.response.UsersResponse;
import com.example.dobble.release.ui.fragments.NavbarFragment;
import com.example.dobble.release.vm.UsersViewModel;
import com.example.dobble.release.utils.Helpers;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;

import static com.example.dobble.release.Cfg.GET_RANDOM_USERS_LIMIT;
import static com.example.dobble.release.Cfg.IMG_RESOLUTION_LOW;
import static com.example.dobble.release.Cfg.NAV_BUNDLE_USER_ID_KEY;

public class UsersFragment extends Fragment {
    private static final int AVATAR_RADIUS = 15;

    @Inject
    UsersViewModel vm;

    private RecyclerView recyclerView;
    private UsersAdapter adapter;
    private NavbarFragment navbarFragment;
    private NavController navController;
    private UsersFragmentBinding binding;
    private FragmentComponent fragmentComponent;
    private TextWatcher userNameEditViewTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString().isEmpty()) {
                adapter.setUsers(new ArrayList<>());
            }
            vm.getFilteredUsers(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) { }
    };

    public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {
        private List<UsersResponse> users = new ArrayList<>();
        private final RequestManager glide;

        public class UserViewHolder extends RecyclerView.ViewHolder {
            private Long userId = null;
            private ImageView avatarImageView;
            private TextView nameTextView;
            private Button addFriendBtn;
            private Button sendMsgBtn;
            private Button viewProfileBtn;
            private TextView resultTextView;
            private LiveData<RxStatusDto<Response<Void>>> addFriendLiveData;

            public UserViewHolder(View userRecyclerItem) {
                super(userRecyclerItem);

                avatarImageView = userRecyclerItem.findViewById(R.id.friendItemAvatar);
                nameTextView = userRecyclerItem.findViewById(R.id.friendItemName);
                addFriendBtn = userRecyclerItem.findViewById(R.id.friendActionBtn);
                sendMsgBtn = userRecyclerItem.findViewById(R.id.friendSendMsgBtn);
                viewProfileBtn = userRecyclerItem.findViewById(R.id.friendViewProfileBtn);
                resultTextView = userRecyclerItem.findViewById(R.id.friendItemResult);

                addFriendBtn.setOnClickListener(this::onAddFriendBtnClick);
                sendMsgBtn.setOnClickListener(this::onSendMsgClick);
                viewProfileBtn.setOnClickListener(this::onViewProfileBtnClick);
            }

            private void onViewProfileBtnClick(View v) {
                if (userId == null) {
                    return;
                }

                Bundle bundle = new Bundle();
                bundle.putLong(NAV_BUNDLE_USER_ID_KEY, userId);
                navController.navigate(R.id.profileFragment, bundle);

                navbarFragment.deselectAll();
            }

            private void onSendMsgClick(View v) {
                if (userId == null) {
                    return;
                }

                SendMsgDialogFragment sendDialog = new SendMsgDialogFragment(userId,
                    avatarImageView.getDrawable(), nameTextView.getText().toString(), fragmentComponent);
                sendDialog.show(getParentFragmentManager(), null);
            }

            private void onAddFriendBtnClick(View v) {
                if (userId == null) {
                    return;
                }

                vm.addFriend(userId);
                addFriendLiveData = vm.addFriendLiveData(userId);
                addFriendLiveData.removeObservers(getViewLifecycleOwner());
                addFriendLiveData.observe(getViewLifecycleOwner(), response -> {
                    if (response.getData().code() == HttpURLConnection.HTTP_BAD_REQUEST) {
                        resultTextView.setText(getString(R.string.users_add_Friend_err));
                    }
                    else {
                        resultTextView.setText(getString(R.string.users_add_Friend_success));
                    }
                    addFriendBtn.setEnabled(true);
                    addFriendBtn.setAlpha(1);
                    addFriendLiveData = null;
                });
                addFriendBtn.setEnabled(false);
                addFriendBtn.setAlpha(0.5f);
            }

            public void setUser(UsersResponse user) {
                userId = user.getId();
                nameTextView.setText(user.getFirstName() + " " + user.getLastName());
            }

            public void recycle() {
                glide.clear(avatarImageView);
            }

            public void reset() {
                addFriendBtn.setEnabled(true);
                addFriendBtn.setAlpha(1);
                resultTextView.setText("");
                if (addFriendLiveData != null) {
                    addFriendLiveData.removeObservers(getViewLifecycleOwner());
                }
            }

            public ImageView getAvatarImageView() {
                return avatarImageView;
            }
        }

        public UsersAdapter(RequestManager glide) {
            this.glide = glide;
        }

        public void init() {
            if (binding.usersUserNameInput.getText().toString().isEmpty()) {
                vm.getRandomUsers(GET_RANDOM_USERS_LIMIT);
            }
            vm.getRandomUsersLiveData().observe(getViewLifecycleOwner(),this::getUsersUpdate);
            vm.getFilteredUsersLiveData().observe(getViewLifecycleOwner(), this::getUsersUpdate);
        }

        private void getUsersUpdate(RxStatusDto<Response<List<UsersResponse>>> response) {
            if (!response.isSuccess()) {
                return;
            }

            users = response.getData().body();
            this.notifyDataSetChanged();
        }

        @NonNull
        @Override
        public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View recyclerViewItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_recycler_item, parent, false);
            return new UserViewHolder(recyclerViewItem);
        }

        @Override
        public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
            holder.reset();
            UsersResponse user = users.get(position);
            if (user.getAvatarId() != null) {
                String avatarUrl = Helpers.buildPathByImgId(user.getAvatarId(), IMG_RESOLUTION_LOW);
                glide
                    .load(avatarUrl)
                    .placeholder(R.drawable.user_avatar_placeholder_icon)
                    .error(R.drawable.user_avatar_placeholder_icon)
                    .transform(new RoundedCorners(AVATAR_RADIUS))
                    .into(holder.getAvatarImageView());
            }
            holder.setUser(user);
        }

        @Override
        public void onViewRecycled(@NonNull UserViewHolder holder) {
            super.onViewRecycled(holder);
            holder.recycle();
        }

        @Override
        public int getItemCount() {
            return users.size();
        }

        public void setUsers(List<UsersResponse> users) {
            this.users = users;
            notifyDataSetChanged();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentComponent = App
            .getAppComponent()
            .fragmentSubComponentBuilder()
            .with(this)
            .build();
        fragmentComponent.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = UsersFragmentBinding.inflate(inflater, container, false);

        recyclerView = binding.usersRecyclerView;
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new UsersAdapter(Glide.with(this));
        recyclerView.setAdapter(adapter);

        binding.usersUserNameInput.addTextChangedListener(userNameEditViewTextWatcher);

        navbarFragment = (NavbarFragment) getParentFragment().getParentFragmentManager()
            .findFragmentById(R.id.navbar_fragment);

        Fragment contentFragmentContainer = getParentFragment().getParentFragmentManager()
            .findFragmentById(R.id.nav_content_fragment_container);
        navController = Navigation.findNavController(contentFragmentContainer.getView());

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        adapter.init();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Recycle views
        if (recyclerView != null) {
            recyclerView.setAdapter(null);
        }
        vm.releaseResources();
    }
}
