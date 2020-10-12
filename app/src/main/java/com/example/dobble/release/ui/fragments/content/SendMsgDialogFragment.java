package com.example.dobble.release.ui.fragments.content;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.dobble.R;
import com.example.dobble.databinding.SendMsgDialogFragmentBinding;
import com.example.dobble.release.App;
import com.example.dobble.release.di.components.FragmentComponent;
import com.example.dobble.release.dto.RxStatusDto;
import com.example.dobble.release.vm.UsersViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import io.socket.client.Socket;
import retrofit2.Response;

import static com.example.dobble.release.Cfg.WEB_SOCKET_EVENT_MSG_ARRIVE;
import static com.example.dobble.release.Cfg.WEB_SOCKET_EVENT_TYPE;

public class SendMsgDialogFragment extends DialogFragment {

    @Inject
    UsersViewModel vm;

    private long userId;
    private Drawable avatarDrawable;
    private String fullName;
    private SendMsgDialogFragmentBinding binding;
    private Socket socket;
    private TextWatcher sendMsgTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                JSONObject typeJson = new JSONObject();
                typeJson.put("destination", userId);
                socket.emit(WEB_SOCKET_EVENT_TYPE, typeJson);
            } catch (JSONException e) {
                // TODO: Error handling
                Log.d("", e.getLocalizedMessage());
            }
        }

        @Override
        public void afterTextChanged(Editable s) { }
    };

    public SendMsgDialogFragment(long userId, Drawable avatar, String fullName, FragmentComponent fragmentComponent) {
        this.userId = userId;
        this.avatarDrawable = avatar;
        this.fullName = fullName;
        socket = App.getSocket();

        fragmentComponent.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SendMsgDialogFragmentBinding.inflate(inflater);
        binding.sendMsgDialogName.setText(fullName);
        binding.sendMsgDialogAvatar.setImageDrawable(avatarDrawable);

        binding.sendMsgDialogCancelBtn.setOnClickListener(v -> {
            dismiss();
        });

        vm.sendMessageLiveData().observe(getViewLifecycleOwner(), this::sendMessageUpdate);

        binding.sendMsgDialogSendBtn.init(R.drawable.msg, null, null,
            this::onSendMsgClick);

        binding.sendMsgDialogInput.addTextChangedListener(sendMsgTextWatcher);

        return binding.getRoot();
    }

    private void sendMessageUpdate(RxStatusDto<Response<Void>> response) {
        if (!response.isSuccess()) {
            binding.sendMsgDialogSendBtn.onError();
            binding.sendMsgDialogInput.setText("");
            return;
        }

        onSendMsgSuccess();
    }

    private void onSendMsgSuccess() {
        binding.sendMsgDialogSendBtn.onSuccess();
        binding.sendMsgDialogInput.setText("");
    }

    private boolean onSendMsgClick() {
        String typedMsg = binding.sendMsgDialogInput.getText().toString();
        if (typedMsg.isEmpty()) {
            return false;
        }

        JSONObject msgJson = new JSONObject();
        try {
            msgJson.put("destination", userId);
            msgJson.put("content", typedMsg);
            socket.emit(WEB_SOCKET_EVENT_MSG_ARRIVE, msgJson);

            // TODO: Do it on ack
            onSendMsgSuccess();
            return false;
        }
        catch (JSONException e) {
            // TODO: Error handling
            Log.d("", e.getLocalizedMessage());
            return false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        vm.cancelSendMessage();
    }
}
