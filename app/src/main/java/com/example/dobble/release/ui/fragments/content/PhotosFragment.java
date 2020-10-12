package com.example.dobble.release.ui.fragments.content;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.dobble.R;
import com.example.dobble.databinding.PhotosFragmentBinding;
import com.example.dobble.release.App;
import com.example.dobble.release.Cfg;
import com.example.dobble.release.dto.RxStatusDto;
import com.example.dobble.release.repositories.remote.payload.response.PhotosResponse;
import com.example.dobble.release.utils.FileManager;
import com.example.dobble.release.utils.IFileManager;
import com.example.dobble.release.vm.UsersViewModel;
import com.example.dobble.release.utils.Helpers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

import static com.example.dobble.release.Cfg.LOAD_IMG_REQUEST_CODE;

public class PhotosFragment extends Fragment {
    private static final int PHOTOS_PER_LINE = 3;
    private static final int PHOTO_CORNER_RADIUS = 10;

    @Inject
    UsersViewModel vm;
    @Inject
    IFileManager fileManager;

    private PhotosFragmentBinding binding;
    private RecyclerView recyclerView;
    private RequestManager glide;
    private PhotosAdapter adapter;
    private File imageFile;

    private class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder> {
        private List<Long> photosIds = new ArrayList<>();

        public class PhotoViewHolder extends RecyclerView.ViewHolder {
            private ImageView photoImageView;

            public PhotoViewHolder(@NonNull ImageView imageView) {
                super(imageView);

                photoImageView = imageView;
            }

            public void recycle() {
                glide.clear(photoImageView);
            }

            public ImageView getPhotoImageView() {
                return photoImageView;
            }
        }

        @NonNull
        @Override
        public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ImageView photo = (ImageView)(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.photo_recycler_item, parent,false));
            return new PhotoViewHolder(photo);
        }

        @Override
        public void onBindViewHolder(@NonNull PhotoViewHolder holder, int pos) {
            String avatarUrl = Helpers.buildPathByImgId(photosIds.get(pos), Cfg.IMG_RESOLUTION_LOW);
            glide
                .load(avatarUrl)
                .placeholder(R.drawable.no_photo_placeholder)
                .error(R.drawable.no_photo_placeholder)
                .transform(new MultiTransformation(new CenterCrop(), new RoundedCorners(PHOTO_CORNER_RADIUS)))
                .into(holder.getPhotoImageView());
        }

        @Override
        public int getItemCount() {
            return photosIds.size();
        }

        @Override
        public void onViewRecycled(@NonNull PhotoViewHolder holder) {
            super.onViewRecycled(holder);

            holder.recycle();
        }

        @Override
        public long getItemId(int position) {
            return photosIds.get(position);
        }
        
        public void setPhotos(List<PhotosResponse> photos) {
            for(PhotosResponse photo : photos) {
                photosIds.add(photo.getId());
            }
            notifyDataSetChanged();
        }

        public void addPhoto(PhotosResponse photo) {
            photosIds.add(photo.getId());
            notifyItemInserted(photosIds.size() - 1);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App
            .getAppComponent()
            .fragmentSubComponentBuilder()
            .with(this)
            .build()
            .inject(this);

        glide = Glide.with(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = PhotosFragmentBinding.inflate(inflater, container, false);

        vm.getPhotosByUserIdLiveData().observe(getViewLifecycleOwner(), this::observeGetPhotos);
        vm.getPhotosByUserId(App.getUserId());
        vm.uploadPhotoLiveData().observe(getViewLifecycleOwner(), this::observeUploadPhoto);

        binding.photosUploadBtn.setOnClickListener(this::onUploadClick);
        binding.photosSubmitPhotoBtn.setOnClickListener(this::onAvatarSubmitClick);

        recyclerView = binding.photosRecyclerView;
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), PHOTOS_PER_LINE);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PhotosAdapter();
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);
        return binding.getRoot();
    }

    public void observeGetPhotos(RxStatusDto<Response<List<PhotosResponse>>> r) {
        if (adapter == null || r == null || !r.isSuccess() || r.getData().body() == null
            || r.getData().body().isEmpty()) {
            return;
        }

        adapter.setPhotos(r.getData().body());
    }

    public void observeUploadPhoto(RxStatusDto<Response<PhotosResponse>> r) {
        if (adapter == null || r == null || !r.isSuccess() || r.getData().body() == null) {
            return;
        }

        adapter.addPhoto(r.getData().body());
    }

    public void onUploadClick(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, LOAD_IMG_REQUEST_CODE);
    }

    public void onAvatarSubmitClick(View view) {
        binding.photosSubmitPhotoBtn.setVisibility(View.INVISIBLE);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),
            imageFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("photo",
            imageFile.getName(), requestFile);
        vm.uploadPhoto(body);
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
            binding.photosSubmitPhotoBtn.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            // TODO: Error handling
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        recyclerView.setAdapter(null);
        vm.releaseResources();
    }
}
