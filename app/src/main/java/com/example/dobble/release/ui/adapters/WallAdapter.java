package com.example.dobble.release.ui.adapters;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.dobble.R;
import com.example.dobble.release.App;
import com.example.dobble.release.dto.RxStatusDto;
import com.example.dobble.release.repositories.remote.payload.response.CommentsResponse;
import com.example.dobble.release.repositories.remote.payload.response.SubCommentResponse;
import com.example.dobble.release.repositories.remote.payload.body.SendWallCommentBody;
import com.example.dobble.release.repositories.remote.payload.response.SendWallCommentResponse;
import com.example.dobble.release.vm.WallViewModel;
import com.example.dobble.release.utils.Helpers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Response;

import static com.example.dobble.release.Cfg.IMG_RESOLUTION_LOW;
import static com.example.dobble.release.Cfg.NAV_BUNDLE_USER_ID_KEY;

public class WallAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int COMMENT_AVATAR_RADIUS = 10;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int HEADER_POS = 1;
    private static final int NEW_ITEM_POSITION = 0;

    private NavController navController;
    private long wallOwnerId;
    private LifecycleOwner lifecycleOwner;
    private Resources resources;
    private WallViewModel vm;
    private List<CommentsResponse> comments = new LinkedList<>();
    private final RequestManager glide;
    private Button submitCommentBtn;
    private EditText commentEditText;

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        private EditText commentEditText;
        private Button submitBtn;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);

            commentEditText = itemView.findViewById(R.id.wallCommentInput);
            submitBtn = itemView.findViewById(R.id.wallCommentSubmit);
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private long id;
        private long userId;
        private LinearLayout itemLinearLayout;
        private ImageView avatarImageView;
        private TextView firstNameTextView;
        private TextView lastNameTextView;
        private TextView contentTextView;
        private EditText subCommentEditText;
        private LinearLayout subCommentLinearLayout;
        private LinearLayout wallCommentUserLinearLayout;
        private Button sendSubCommentBtn;
        private List<SubComment> subComments = new ArrayList<>();

        public class SubComment {
            private long userId;
            private View rootView;
            private TextView contentTextView;
            private ImageView avatarImageView;

            public SubComment(View rootView, SubCommentResponse payload, boolean pending) {
                this.rootView = rootView;
                contentTextView = rootView.findViewById(R.id.wall_sub_comment_content);
                avatarImageView = rootView.findViewById(R.id.wall_sub_comment_avatar);
                avatarImageView.setOnClickListener(this::onAvatarClick);
                userId = payload.getUserId();
                contentTextView.setText(payload.getContent());

                if (payload.getAvatarId() != null) {
                    String avatarUrl = Helpers.buildPathByImgId(payload.getAvatarId(),
                        IMG_RESOLUTION_LOW);
                    glide
                        .load(avatarUrl)
                        .placeholder(R.drawable.user_avatar_placeholder_icon)
                        .error(R.drawable.user_avatar_placeholder_icon)
                        .transform(new RoundedCorners(COMMENT_AVATAR_RADIUS))
                        .into(avatarImageView);
                }
                else {
                    Drawable avatarPlaceholder = resources.getDrawable(R.drawable.user_avatar_placeholder_icon);
                    avatarImageView.setImageDrawable(avatarPlaceholder);
                }

                if (pending) {
                    disable();
                }
            }

            private void onAvatarClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putLong(NAV_BUNDLE_USER_ID_KEY, userId);
                navController.navigate(R.id.profileFragment, bundle);
            }

            private void disable() {
                rootView.setAlpha(0.5f);
                avatarImageView.setEnabled(false);
            }

            private void enable() {
                rootView.setAlpha(1);
                avatarImageView.setEnabled(true);
            }

            public void recycle() {
                glide.clear(avatarImageView);
            }

            public View getRootView() {
                return rootView;
            }

            public void refresh(SendWallCommentResponse r) {
                if (r.getAvatarId() != null) {
                    String avatarUrl = Helpers.buildPathByImgId(r.getAvatarId(),
                        IMG_RESOLUTION_LOW);
                    glide
                        .load(avatarUrl)
                        .placeholder(R.drawable.user_avatar_placeholder_icon)
                        .error(R.drawable.user_avatar_placeholder_icon)
                        .transform(new RoundedCorners(COMMENT_AVATAR_RADIUS))
                        .into(avatarImageView);
                }
                else {
                    Drawable avatarPlaceholder = resources.getDrawable(R.drawable.user_avatar_placeholder_icon);
                    avatarImageView.setImageDrawable(avatarPlaceholder);
                }

                enable();
            }
        }

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            avatarImageView = itemView.findViewById(R.id.wall_comment_avatar);
            firstNameTextView = itemView.findViewById(R.id.wall_comment_first_name);
            lastNameTextView = itemView.findViewById(R.id.wall_comment_last_name);
            contentTextView = itemView.findViewById(R.id.wall_comment_content);
            subCommentLinearLayout = itemView.findViewById(R.id.wall_sub_comment_container);
            itemLinearLayout = itemView.findViewById(R.id.wall_comment_container);
            subCommentEditText = itemView.findViewById(R.id.wall_sub_comment_edit_text);
            wallCommentUserLinearLayout = itemView.findViewById(R.id.wall_comment_user_container);
            sendSubCommentBtn = itemView.findViewById(R.id.wall_sub_comment_send_btn);

            wallCommentUserLinearLayout.setOnClickListener(this::onUserContainerClick);
            sendSubCommentBtn.setOnClickListener(this::onSendSubComment);
        }

        private void onUserContainerClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putLong(NAV_BUNDLE_USER_ID_KEY, userId);
            navController.navigate(R.id.profileFragment, bundle);
        }

        public void setComment(CommentsResponse comment) {
            if (comment.getAvatarId() != null) {
                String avatarUrl = Helpers.buildPathByImgId(comment.getAvatarId(), IMG_RESOLUTION_LOW);
                glide
                    .load(avatarUrl)
                    .placeholder(R.drawable.user_avatar_placeholder_icon)
                    .error(R.drawable.user_avatar_placeholder_icon)
                    .transform(new RoundedCorners(COMMENT_AVATAR_RADIUS))
                    .into(avatarImageView);
            }
            else {
                Drawable avatarPlaceholder = resources.getDrawable(R.drawable.user_avatar_placeholder_icon);
                avatarImageView.setImageDrawable(avatarPlaceholder);
            }

            id = comment.getId();
            firstNameTextView.setText(comment.getFirstName());
            lastNameTextView.setText(comment.getLastName());
            contentTextView.setText(comment.getContent());
            userId = comment.getUserId();

            if (comment.getComments() != null) {
                for(SubCommentResponse subComment : comment.getComments()) {
                    View subCommentView = LayoutInflater.from(subCommentLinearLayout.getContext())
                        .inflate(R.layout.wall_sub_comment_item, subCommentLinearLayout, false);
                    SubComment sc = new SubComment(subCommentView, subComment, false);
                    subComments.add(sc);
                    subCommentLinearLayout.addView(sc.getRootView());
                }
            }

            enable();
        }

        public void setPendingComment(CommentsResponse pendingComment) {
            id = -1;
            firstNameTextView.setText("");
            lastNameTextView.setText("");
            Drawable avatarPlaceholder = resources.getDrawable(R.drawable.user_avatar_placeholder_icon);
            avatarImageView.setImageDrawable(avatarPlaceholder);
            contentTextView.setText(pendingComment.getContent());
            userId = pendingComment.getUserId();

            disable();
        }

        public void recycle() {
            glide.clear(avatarImageView);

            for(SubComment subComment : subComments) {
                subComment.recycle();
            }

            subComments.clear();
            subCommentLinearLayout.removeAllViews();
        }

        private void disable() {
            itemLinearLayout.setAlpha(0.5f);
            subCommentEditText.setEnabled(false);
            wallCommentUserLinearLayout.setEnabled(false);
            sendSubCommentBtn.setEnabled(false);
        }

        private void enable() {
            itemLinearLayout.setAlpha(1);
            subCommentEditText.setEnabled(true);
            wallCommentUserLinearLayout.setEnabled(true);
            sendSubCommentBtn.setEnabled(true);
        }

        private void onSendSubComment(View v) {
            String typedText = subCommentEditText.getText().toString();
            if (typedText.isEmpty()) {
                return;
            }
            subCommentEditText.setText("");

            View subCommentView = LayoutInflater.from(subCommentLinearLayout.getContext())
                .inflate(R.layout.wall_sub_comment_item, subCommentLinearLayout, false);
            SubCommentResponse scp = new SubCommentResponse(-1L, App.getUserId(), null,
                typedText);
            SubComment sc = new SubComment(subCommentView, scp, true);
            subComments.add(sc);
            subCommentLinearLayout.addView(sc.getRootView());
            SendWallCommentBody payload = new SendWallCommentBody(typedText, wallOwnerId,
                id);
            vm.sendSubCommentLiveData().observe(lifecycleOwner, r -> {
                sc.refresh(r.getData().body());
            });
            vm.sendSubComment(payload);
        }
    }

    public WallAdapter(RequestManager glide, NavController navController, long wallOwnerId,
                       LifecycleOwner lifecycleOwner, Resources resources, WallViewModel vm) {
        this.glide = glide;
        this.navController = navController;
        this.wallOwnerId = wallOwnerId;
        this.lifecycleOwner = lifecycleOwner;
        this.resources = resources;
        this.vm = vm;

        vm.getCommentsByUserIdLiveData().observe(lifecycleOwner, this::onCommentsUpdate);
        vm.getCommentsByUserId(wallOwnerId);
    }

    private void onCommentsUpdate(RxStatusDto<Response<List<CommentsResponse>>> response) {
        if (!response.isSuccess()) {
            return;
        }

        comments = response.getData().body();
        this.notifyDataSetChanged();
    }

    private void onSubmitCommentClick(View v) {
        String commentContent = commentEditText.getText().toString();
        if (commentContent.isEmpty()) {
            return;
        }
        commentEditText.setText("");

        CommentsResponse newComment = new CommentsResponse();
        newComment.setPending(true)
            .setContent(commentContent)
            // Only current user can leave pending comment
            .setUserId(App.getUserId());
        comments.add(NEW_ITEM_POSITION, newComment);
        notifyItemInserted(NEW_ITEM_POSITION + HEADER_POS);

        SendWallCommentBody payload = new SendWallCommentBody(commentContent, wallOwnerId,
            null);
        vm.sendWallComment(payload);
        vm.sendWallCommentLiveData().observe(lifecycleOwner, r -> {
            if (!r.isSuccess()) {
                return;
            }

            newComment.refresh(r.getData().body());
            int pos = comments.indexOf(newComment);
            notifyItemChanged(pos + HEADER_POS);
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wall_recycler_item, parent, false);
            return new ItemViewHolder(item);
        }
        else {
            View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wall_recycler_header, parent, false);
            submitCommentBtn = item.findViewById(R.id.wallCommentSubmit);
            submitCommentBtn.setOnClickListener(this::onSubmitCommentClick);
            commentEditText = item.findViewById(R.id.wallCommentInput);
            return new HeaderViewHolder(item);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position > TYPE_HEADER) {
            ItemViewHolder commentItem = (ItemViewHolder)holder;
            CommentsResponse comment = comments.get(position - HEADER_POS);
            if (comment.isPending()) {
                commentItem.setPendingComment(comment);
            }
            else {
                commentItem.setComment(comment);
            }
        }
    }

    @Override
    public int getItemCount() {
        return comments.size() + HEADER_POS;
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder)holder).recycle();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }
}
