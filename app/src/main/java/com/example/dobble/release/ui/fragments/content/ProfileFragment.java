package com.example.dobble.release.ui.fragments.content;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.dobble.R;
import com.example.dobble.databinding.ProfileFragmentBinding;
import com.example.dobble.release.App;
import com.example.dobble.release.dto.RxStatusDto;
import com.example.dobble.release.repositories.remote.payload.body.EditProfileBody;
import com.example.dobble.release.repositories.remote.payload.response.GetUserByIdResponse;
import com.example.dobble.release.repositories.remote.payload.response.ProfileResponse;
import com.example.dobble.release.repositories.remote.payload.response.UploadAvatarResponse;
import com.example.dobble.release.repositories.remote.payload.response.UploadResponse;
import com.example.dobble.release.utils.FileManager;
import com.example.dobble.release.utils.IFileManager;
import com.example.dobble.release.vm.ProfileViewModel;
import com.example.dobble.release.utils.Helpers;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

import static com.example.dobble.release.Cfg.IMG_RESOLUTION_MEDIUM;
import static com.example.dobble.release.Cfg.LOAD_IMG_REQUEST_CODE;
import static com.example.dobble.release.Cfg.NAV_BUNDLE_USER_ID_KEY;

public class ProfileFragment extends Fragment {
    private static final int AVATAR_RADIUS = 10;
    private static final int PROFILE_EDIT_RESULT_DURATION_SEC = 3;

    @Inject
    ProfileViewModel vm;
    @Inject
    IFileManager fileManager;

    private ProfileFragmentBinding binding;
    private File imageFile;
    private RequestManager glide;
    private RoundedCorners avatarTransform = new RoundedCorners(AVATAR_RADIUS);
    private Disposable profileEditResultTimer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ProfileFragmentBinding.inflate(inflater, container, false);

        App
            .getAppComponent()
            .fragmentSubComponentBuilder()
            .with(this)
            .build()
            .inject(this);

        glide = Glide.with(this);

        vm.getUserByIdLiveData().observe(getViewLifecycleOwner(), this::getUserByIdUpdate);
        long userId = getArguments().getLong(NAV_BUNDLE_USER_ID_KEY);
        if (userId != App.getUserId()) {
            binding.profileUploadBtn.setVisibility(View.GONE);
            binding.profileEditWrapper.setVisibility(View.GONE);
        }
        vm.getUserById(userId);

        vm.uploadAvatarLiveData().observe(getViewLifecycleOwner(), this::uploadAvatarUpdate);

        vm.editProfileLiveData().observe(getViewLifecycleOwner(), this::editProfileUpdate);

        binding.profileUploadBtn.setOnClickListener(this::onUploadClick);
        binding.profileSubmitPhotoBtn.setOnClickListener(this::onAvatarSubmitClick);
        binding.profileChangeBtn.setOnClickListener(this::onProfileChange);

        return binding.getRoot();
    }

    private void editProfileUpdate(RxStatusDto<Response<Void>> response) {
        binding.profileChangeBtn.setEnabled(true);
        binding.profileChangeBtn.setAlpha(1);

        profileEditResultTimer = Single.timer(PROFILE_EDIT_RESULT_DURATION_SEC, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(t -> binding.profileEditResult.setText(""));

        Resources r = getResources();
        if (!response.isSuccess()) {
            binding.profileEditResult.setTextColor(r.getColor(R.color.validatable_edit_text_border_false));
            binding.profileEditResult.setText(getString(R.string.profile_edit_result_error));
            return;
        }

        binding.profileEditResult.setTextColor(r.getColor(
            R.color.validatable_edit_text_border_true));
        binding.profileEditResult.setText(getString(R.string.profile_edit_result_success));
        long userId = getArguments().getLong(NAV_BUNDLE_USER_ID_KEY);
        vm.getUserById(userId);
    }

    private void getUserByIdUpdate(RxStatusDto<Response<GetUserByIdResponse>> response) {
        ProfileResponse profileResponse = response.getData().body().getProfileResponse();
        if (profileResponse.getEmail() != null) {
            binding.profileEmail.setText(getString(R.string.profile_text_before_email,
                profileResponse.getEmail()));
        }
        binding.profileFirstName.setText(getString(R.string.profile_text_before_first_name,
            profileResponse.getFirstName()));
        binding.profileLastName.setText(getString(R.string.profile_text_before_last_name,
            profileResponse.getLastName()));
        binding.profileCity.setText(getString(R.string.profile_text_before_city,
            profileResponse.getCity()));
        binding.profileState.setText(getString(R.string.profile_text_before_state,
            profileResponse.getState()));
        binding.profileGender.setText(getString(R.string.profile_text_before_gender,
            profileResponse.getGender()));
        binding.profileDob.setText(getString(R.string.profile_text_before_dob, profileResponse.getDob()));

        UploadResponse uploadResponse = response.getData().body().getUploadResponse();
        if (uploadResponse != null && uploadResponse.getId() != null) {
            String avatarUrl = Helpers.buildPathByImgId(uploadResponse.getId(), IMG_RESOLUTION_MEDIUM);
            setUpAvatar(glide.load(avatarUrl));
        }
        else {
            Drawable avatarPlaceholder = getResources().getDrawable(R.drawable.user_avatar_placeholder_icon);
            binding.profileAvatar.setImageDrawable(avatarPlaceholder);
        }
    }

    private void uploadAvatarUpdate(RxStatusDto<Response<UploadAvatarResponse>> response) {
        if (!response.isSuccess()) {
            binding.profileUploadErrorView.setText(getString(R.string.profile_upload_avatar_error));
            return;
        }

        String avatarUrl = Helpers.buildPathByImgId(response.getData().body().getAvatarId(),
            IMG_RESOLUTION_MEDIUM);
        setUpAvatar(glide.load(avatarUrl));
    }

    private void setUpAvatar(RequestBuilder<Drawable> requestBuilder) {
        requestBuilder
            .fitCenter()
            .centerCrop()
            .transform(avatarTransform)
            .placeholder(R.drawable.user_avatar_placeholder_icon)
            .error(R.drawable.user_avatar_placeholder_icon)
            .into(binding.profileAvatar);
    }

    private void onUploadClick(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, LOAD_IMG_REQUEST_CODE);
    }

    private void onProfileChange(View view) {
        binding.profileChangeBtn.setEnabled(false);
        binding.profileChangeBtn.setAlpha(0.2f);

        String password = binding.profilePasswordInput.getText().toString();
        String firstName = binding.profileFirstNameInput.getText().toString();
        String lastName = binding.profileLastNameInput.getText().toString();
        String city = binding.profileCityInput.getText().toString();
        EditProfileBody payload = new EditProfileBody(password, firstName, lastName, city);
        vm.editProfile(payload);

        binding.profilePasswordInput.setText("");
        binding.profileFirstNameInput.setText("");
        binding.profileLastNameInput.setText("");
        binding.profileCityInput.setText("");
    }

    private void onAvatarSubmitClick(View view) {
        binding.profileSubmitPhotoBtn.setVisibility(View.INVISIBLE);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),
            imageFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("avatar",
            imageFile.getName(), requestFile);
        vm.uploadAvatar(body);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != LOAD_IMG_REQUEST_CODE || data == null || data.getData() == null
            || resultCode != Activity.RESULT_OK) {
            return;
        }
        
        try {
            String filePath = fileManager.getPath(data.getData());
            imageFile = new File(filePath);
            binding.profileSubmitPhotoBtn.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            // TODO: Error handling
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (profileEditResultTimer != null) {
            profileEditResultTimer.dispose();
        }
        vm.releaseResources();
    }
}
