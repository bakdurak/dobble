package com.example.dobble.debug.repositories;

import com.example.dobble.release.repositories.ProfileRepository;
import com.example.dobble.release.repositories.remote.payload.response.GetUserByIdResponse;
import com.example.dobble.release.repositories.remote.payload.body.EditProfileBody;
import com.example.dobble.release.repositories.remote.payload.response.UploadResponse;
import com.example.dobble.release.repositories.remote.payload.response.UploadAvatarResponse;
import com.example.dobble.release.repositories.remote.payload.response.ProfileResponse;

import java.net.HttpURLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import retrofit2.Response;

public class FakeProfileRepositoryImpl implements ProfileRepository {
    public static final long LOGGED_USER_ID = 1;

    private Map<Long, GetUserByIdResponse> users = new HashMap<Long, GetUserByIdResponse>() {{
        put(LOGGED_USER_ID, new GetUserByIdResponse(new ProfileResponse(LOGGED_USER_ID, "Some city", "Some state", "0",
            (new Date(1990, 10, 25)).toString(), "SomeFirstName", "SomeLastName",
            "test@mail.com"), new UploadResponse(1L), false));
    }};

    @Override
    public void onDestroy() { }

    private class ResponseBodyImpl extends ResponseBody {
        @Override
        public MediaType contentType() {
            return null;
        }

        @Override
        public long contentLength() {
            return 0;
        }

        @Override
        public BufferedSource source() {
            return null;
        }
    }

    @Inject
    public FakeProfileRepositoryImpl() { }

    @Override
    public Observable<Response<GetUserByIdResponse>> getUserById(long id) {
        Response<GetUserByIdResponse> response;
        if (users.containsKey(id)) {
            response = Response.success(users.get(id));
        }
        else {
            response = Response.error(HttpURLConnection.HTTP_BAD_REQUEST, new ResponseBodyImpl());
        }
        return Observable.just(response)
            .delay(1, TimeUnit.SECONDS);
    }

    @Override
    public Observable<Response<UploadAvatarResponse>> uploadAvatar(MultipartBody.Part body) {
        // TODO: Implement method
        return null;
    }

    @Override
    public Observable<Response<Void>> editProfile(EditProfileBody editProfileBody) {
        // TODO: Implement method
        return null;
    }
}
