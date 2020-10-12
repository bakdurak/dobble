package com.example.dobble.release.ui.fragments.content.friends;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.dobble.R;
import com.example.dobble.release.di.components.FragmentComponent;
import com.example.dobble.release.repositories.remote.payload.response.FriendResponse;
import com.example.dobble.release.ui.fragments.content.SendMsgDialogFragment;
import com.example.dobble.release.vm.UsersViewModel;
import com.example.dobble.release.utils.Helpers;

import static com.example.dobble.release.Cfg.IMG_RESOLUTION_LOW;
import static com.example.dobble.release.Cfg.NAV_BUNDLE_USER_ID_KEY;

public class FriendsViewHolder extends RecyclerView.ViewHolder {
    private static final int COMMENT_AVATAR_RADIUS = 10;

    private Long userId;
    private ImageView avatarImageView;
    private TextView nameTextView;
    private UsersViewModel usersViewModel;
    private NavController navController;
    private ActionHandler handler;
    private FragmentManager fragmentManager;
    private TextView friendItemResultTextView;
    private FriendResponse friendResponse;
    private Resources resources;
    private FragmentComponent fragmentComponent;
    private final RequestManager glide;

    @FunctionalInterface
    public interface ActionHandler{
        void handle(FriendsViewHolder friendsViewHolder);
    }

    public FriendsViewHolder(@NonNull View itemView, ActionHandler actionHandler,
                             FragmentManager fragmentManager, UsersViewModel vm, NavController nc,
                             RequestManager glide, String btmTitle, Resources resources,
                             FragmentComponent fragmentComponent) {
        super(itemView);

        avatarImageView = itemView.findViewById(R.id.friendItemAvatar);
        nameTextView = itemView.findViewById(R.id.friendItemName);
        friendItemResultTextView = itemView.findViewById(R.id.friendItemResult);
        this.handler = actionHandler;
        this.fragmentManager = fragmentManager;
        usersViewModel = vm;
        navController = nc;
        this.glide = glide;
        this.resources = resources;
        this.fragmentComponent = fragmentComponent;

        Button actionBtn = itemView.findViewById(R.id.friendActionBtn);
        actionBtn.setOnClickListener(this::onParametrizedButtonClick);
        actionBtn.setText(btmTitle);
        itemView.findViewById(R.id.friendSendMsgBtn).setOnClickListener(this::onSendMessageClick);
        itemView.findViewById(R.id.friendViewProfileBtn).setOnClickListener(this::onViewProfileClick);
    }

    public void init(FriendResponse friendResponse) {
        this.friendResponse = friendResponse;

        this.userId = friendResponse.getId();
        nameTextView.setText(friendResponse.getFirstName() + " " + friendResponse.getLastName());
        if (friendResponse.getAvatarId() != null) {
            String avatarUrl = Helpers.buildPathByImgId(friendResponse.getAvatarId(),
                IMG_RESOLUTION_LOW);
            glide
                .load(avatarUrl)
                .placeholder(R.drawable.user_avatar_placeholder_icon)
                .error(R.drawable.user_avatar_placeholder_icon)
                .transform(new RoundedCorners(COMMENT_AVATAR_RADIUS))
                .into(avatarImageView);
        }
        else {
            Drawable emptyAvatar = resources.getDrawable(R.drawable.user_avatar_placeholder_icon);
            avatarImageView.setImageDrawable(emptyAvatar);
        }
    }

    private void onSendMessageClick(View v) {
        if (userId == null) {
            return;
        }

        SendMsgDialogFragment sendDialog = new SendMsgDialogFragment(userId,
            avatarImageView.getDrawable(), nameTextView.getText().toString(), fragmentComponent);
        sendDialog.show(fragmentManager, null);
    }

    private void onViewProfileClick(View v) {
        if (userId == null) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putLong(NAV_BUNDLE_USER_ID_KEY, userId);

        navController.navigate(R.id.profileFragment, bundle);
    }

    private void onParametrizedButtonClick(View v) {
        if (handler != null) {
            handler.handle(this);
        }
    }

    public void recycle() {
        glide.clear(avatarImageView);
    }

    public Long getUserId() {
        return userId;
    }

    public void setActionResultText(String result) {
        friendItemResultTextView.setText(result);
    }

    public FriendResponse getFriendResponse() {
        return friendResponse;
    }
}
