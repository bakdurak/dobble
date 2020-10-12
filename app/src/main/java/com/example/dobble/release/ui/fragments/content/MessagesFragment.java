package com.example.dobble.release.ui.fragments.content;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.dobble.R;
import com.example.dobble.databinding.MessagesFragmentBinding;
import com.example.dobble.release.App;
import com.example.dobble.release.di.components.FragmentComponent;
import com.example.dobble.release.dto.RxStatusDto;
import com.example.dobble.release.extensions.animations.PulseCircleAnimationView;
import com.example.dobble.release.repositories.remote.payload.response.MessageResponse;
import com.example.dobble.release.vm.MessagesViewModel;
import com.example.dobble.release.vm.UsersViewModel;
import com.example.dobble.release.utils.Helpers;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import io.socket.client.Socket;
import retrofit2.Response;

import static com.example.dobble.release.Cfg.CANCEL_TYPE_ANIMATION_DELAY;
import static com.example.dobble.release.Cfg.DATE_FORMAT;
import static com.example.dobble.release.Cfg.IMG_RESOLUTION_LOW;
import static com.example.dobble.release.Cfg.NAV_BUNDLE_USER_ID_KEY;
import static com.example.dobble.release.Cfg.WEB_SOCKET_EVENT_MSG_ARRIVE;
import static com.example.dobble.release.Cfg.WEB_SOCKET_EVENT_MSG_JOIN;
import static com.example.dobble.release.Cfg.WEB_SOCKET_EVENT_MSG_LEAVE;
import static com.example.dobble.release.Cfg.WEB_SOCKET_EVENT_TYPE;

public class MessagesFragment extends Fragment {
    private static final int AVATAR_RADIUS = 15;

    @Inject
    MessagesViewModel messagesViewModel;
    @Inject
    UsersViewModel usersViewModel;

    private MessagesFragmentBinding binding;
    private RequestManager glide;
    private RecyclerView recyclerView;
    private NavController navController;
    private MessagesAdapter adapter;
    private DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
    private PulseCircleAnimationView pulseCircleAnimationView;
    private Socket socket;
    private PublishSubject<Boolean> typePublisher = PublishSubject.create();
    private Disposable typeFlow;
    private TextView typingUserNameTextView;
    private FragmentComponent fragmentComponent;

    private class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder> {
        private List<MessageResponse> messages = new ArrayList<>();

        public class MessageViewHolder extends RecyclerView.ViewHolder {
            private ImageView avatarSrcImageView;
            private TextView nameSrcTextView;
            private TextView contentTextView;
            private ImageView avatarDstImageView;
            private TextView nameDstTextView;
            private TextView sendDateTextView;
            private long srcId;
            private long dstId;

            public MessageViewHolder(@NonNull View itemView) {
                super(itemView);

                avatarSrcImageView = itemView.findViewById(R.id.message_avatar_src);
                nameSrcTextView = itemView.findViewById(R.id.message_name_src);
                contentTextView = itemView.findViewById(R.id.message_content);
                avatarDstImageView = itemView.findViewById(R.id.message_avatar_dst);
                nameDstTextView = itemView.findViewById(R.id.message_name_dst);
                sendDateTextView = itemView.findViewById(R.id.message_send_date);
                LinearLayout srcUserContainer = itemView.findViewById(R.id.messages_user_src_container);
                LinearLayout dstUserContainer = itemView.findViewById(R.id.messages_user_dst_container);
                Button sendMsgBtn = itemView.findViewById(R.id.message_send_btn);

                srcUserContainer.setOnClickListener(this::onUserContainerClick);
                dstUserContainer.setOnClickListener(this::onUserContainerClick);
                sendMsgBtn.setOnClickListener(this::onSendBtnClick);
            }

            public void recycle() {
                glide.clear(avatarSrcImageView);
                glide.clear(avatarDstImageView);
            }

            private void onUserContainerClick(View view) {
                long id = view.getId();
                long userId;
                if (id == R.id.messages_user_src_container) {
                    userId = srcId;
                }
                else {
                    userId = dstId;
                }

                Bundle bundle = new Bundle();
                bundle.putLong(NAV_BUNDLE_USER_ID_KEY, userId);
                navController.navigate(R.id.profileFragment, bundle);
            }

            private void onSendBtnClick(View view) {
                long userId;
                Drawable avatar;
                String name;
                if (srcId == App.getUserId()) {
                    userId = dstId;
                    avatar = avatarDstImageView.getDrawable();
                    name = nameDstTextView.getText().toString();
                }
                else {
                    userId = srcId;
                    avatar = avatarSrcImageView.getDrawable();
                    name = nameSrcTextView.getText().toString();
                }

                SendMsgDialogFragment sendDialog = new SendMsgDialogFragment(userId, avatar, name,
                    fragmentComponent);
                sendDialog.show(getParentFragmentManager(), null);
            }

            public void setMessage(MessageResponse message) {
                nameSrcTextView.setText(message.getSrcFirstName() + " " + message.getSrcLastName());
                contentTextView.setText(message.getContent());
                nameDstTextView.setText(message.getDstFirstName() + " " + message.getDstLastName());

                String sendDateStr = "";
                try {
                    Date sendDate = dateFormat.parse(message.getDate());
                    sendDateStr = (sendDate.getYear() + 1900) + "/" + sendDate.getMonth()
                        + "/" + sendDate.getDay();
                }
                catch (ParseException e) {
                    // TODO: Error handling
                }
                sendDateTextView.setText(sendDateStr);

                srcId = message.getSrcId();
                dstId = message.getDstId();

                if (message.getSrcAvatarId() != null) {
                    String srcAvatarUrl = Helpers.buildPathByImgId(message.getSrcAvatarId(), IMG_RESOLUTION_LOW);
                    glide
                        .load(srcAvatarUrl)
                        .placeholder(R.drawable.user_avatar_placeholder_icon)
                        .error(R.drawable.user_avatar_placeholder_icon)
                        .transform(new RoundedCorners(AVATAR_RADIUS))
                        .into(avatarSrcImageView);
                }
                else {
                    Drawable avatarPlaceholder = getResources().getDrawable(R.drawable.user_avatar_placeholder_icon);
                    avatarSrcImageView.setImageDrawable(avatarPlaceholder);
                }

                if (message.getDstAvatarId() != null) {
                    String dstAvatarUrl = Helpers.buildPathByImgId(message.getDstAvatarId(), IMG_RESOLUTION_LOW);
                    glide
                        .load(dstAvatarUrl)
                        .placeholder(R.drawable.user_avatar_placeholder_icon)
                        .error(R.drawable.user_avatar_placeholder_icon)
                        .transform(new RoundedCorners(AVATAR_RADIUS))
                        .into(avatarDstImageView);
                }
                else {
                    Drawable avatarPlaceholder = getResources().getDrawable(R.drawable.user_avatar_placeholder_icon);
                    avatarDstImageView.setImageDrawable(avatarPlaceholder);
                }
            }
        }

        @NonNull
        @Override
        public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext())
                .inflate(R.layout.message_recycle_item, parent, false);
            return new MessageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
            holder.setMessage(messages.get(messages.size() - 1 - position));
        }

        @Override
        public int getItemCount() {
            return messages.size();
        }

        @Override
        public void onViewRecycled(@NonNull MessageViewHolder holder) {
            super.onViewRecycled(holder);

            holder.recycle();
        }

        @Override
        public long getItemId(int position) {
            return messages.get(position).getId();
        }
        
        public void setMessages(List<MessageResponse> messages) {
            this.messages = messages;
            notifyDataSetChanged();
        }

        public void addMessage(MessageResponse msg) {
            messages.add(msg);
            notifyItemInserted(0);
            recyclerView.smoothScrollToPosition(0);
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

        socket = App.getSocket();

        glide = Glide.with(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = MessagesFragmentBinding.inflate(inflater, container, false);

        socket.on(WEB_SOCKET_EVENT_MSG_ARRIVE, this::onMsgArriveHandler);
        socket.on(WEB_SOCKET_EVENT_TYPE, this::onTypeHandler);
        socket.emit(WEB_SOCKET_EVENT_MSG_JOIN);

        Fragment contentFragmentContainer = getParentFragment().getParentFragmentManager()
            .findFragmentById(R.id.nav_content_fragment_container);
        navController = Navigation.findNavController(contentFragmentContainer.getView());

        recyclerView = binding.messagesRecyclerView;
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MessagesAdapter();
        recyclerView.setAdapter(adapter);

        messagesViewModel.getOwnMessagesLiveData().observe(getViewLifecycleOwner(), this::getOwnMessagesHandler);
        messagesViewModel.getOwnMessages();

        typingUserNameTextView = binding.messagesTypingUserName;

        pulseCircleAnimationView = binding.messagesTypingAnimationView;

        typeFlow = typePublisher
            .debounce(CANCEL_TYPE_ANIMATION_DELAY, TimeUnit.SECONDS)
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                this::typeHandler,
                // TODO: Error handling
                throwable -> Log.d("", throwable.getLocalizedMessage())
            );

        return binding.getRoot();
    }

    private void typeHandler(Boolean b) {
        typingUserNameTextView.setText("");
        pulseCircleAnimationView.cancelAnimation();
    }

    private void onMsgArriveHandler(Object... args) {
        JSONObject jsonMsg = (JSONObject) args[0];
        try {
            MessageResponse msgResponse = new MessageResponse(jsonMsg);
            adapter.addMessage(msgResponse);
        }
        catch (JSONException e) {
            // TODO: Error handling
            Log.d("", e.getLocalizedMessage());
        }
    }

    private void onTypeHandler(Object... args) {
        getActivity().runOnUiThread(() -> {
            if (!pulseCircleAnimationView.isAnimating()) {
                JSONObject jsonMsg = (JSONObject) args[0];
                try {
                    String firstName = jsonMsg.getString("firstName");
                    String lastName = jsonMsg.getString("lastName");
                    typingUserNameTextView.setText(firstName + " " + lastName);
                }
                catch (JSONException e) {
                    // TODO: Error handling
                    Log.d("", e.getLocalizedMessage());
                }

                pulseCircleAnimationView.startAnimation();
            }
            typePublisher.onNext(true);
        });
    }

    private void getOwnMessagesHandler(RxStatusDto<Response<List<MessageResponse>>> r) {
        if (!r.isSuccess() || r.getData().body() == null
            || r.getData().body().isEmpty()) {
            return;
        }

        adapter.setMessages(r.getData().body());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        socket.emit(WEB_SOCKET_EVENT_MSG_LEAVE);
        socket.off(WEB_SOCKET_EVENT_MSG_ARRIVE);
        socket.off(WEB_SOCKET_EVENT_TYPE);
        if (typeFlow != null) {
            typeFlow.dispose();
        }
        recyclerView.setAdapter(null);
        messagesViewModel.releaseResources();
        usersViewModel.releaseResources();
    }
}
