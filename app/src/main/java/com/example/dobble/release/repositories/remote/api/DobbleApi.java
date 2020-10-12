package com.example.dobble.release.repositories.remote.api;

import com.example.dobble.release.repositories.remote.payload.body.AddFriendBody;
import com.example.dobble.release.repositories.remote.payload.body.ApproveFriendBody;
import com.example.dobble.release.repositories.remote.payload.response.CheckAuthResponse;
import com.example.dobble.release.repositories.remote.payload.response.CommentsResponse;
import com.example.dobble.release.repositories.remote.payload.response.CheckEmailResponse;
import com.example.dobble.release.repositories.remote.payload.response.FriendResponse;
import com.example.dobble.release.repositories.remote.payload.body.LoginBody;
import com.example.dobble.release.repositories.remote.payload.body.MessageBody;
import com.example.dobble.release.repositories.remote.payload.response.MessageResponse;
import com.example.dobble.release.repositories.remote.payload.body.RegisterUserBody;
import com.example.dobble.release.repositories.remote.payload.response.PhotosResponse;
import com.example.dobble.release.repositories.remote.payload.response.GetUserByIdResponse;
import com.example.dobble.release.repositories.remote.payload.body.EditProfileBody;
import com.example.dobble.release.repositories.remote.payload.body.SendMessageBody;
import com.example.dobble.release.repositories.remote.payload.response.UploadAvatarResponse;
import com.example.dobble.release.repositories.remote.payload.response.UsersResponse;
import com.example.dobble.release.repositories.remote.payload.body.SendWallCommentBody;
import com.example.dobble.release.repositories.remote.payload.response.SendWallCommentResponse;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DobbleApi {
    String BASE_URL = "http://37.193.146.146:4000/api/";

    @POST("users")
    Observable<Response<Void>> registerUser(@Body RegisterUserBody user);

    @POST("users/email/{email}")
    Observable<Response<CheckEmailResponse>> checkEmail(@Path("email") String email);

    @GET("users/check/auth")
    Observable<Response<CheckAuthResponse>> checkAuth();

    @GET("users/{id}")
    Observable<Response<GetUserByIdResponse>> getUserById(@Path("id") long id);

    @POST("users/auth")
    Observable<Response<CheckAuthResponse>> login(@Body LoginBody payload);

    @POST("users/logout")
    Observable<Response<Void>> logout();

    @Multipart
    @POST("uploads/avatar")
    Observable<Response<UploadAvatarResponse>> uploadAvatar(@Part MultipartBody.Part photo);

    @PUT("users")
    Observable<Response<Void>> editProfile(@Body EditProfileBody editProfileBody);

    @GET("users/mobile/all/any/{limit}")
    Observable<Response<List<UsersResponse>>> getRandomUsers(@Path("limit") int limit);

    @GET("users/mobile/all/filters")
    Observable<Response<List<UsersResponse>>> getFilteredUsers(@Query("fullName") String fullName);

    @POST("friends")
    Observable<Response<Void>> addFriend(@Body AddFriendBody payload);

    @POST("messages")
    Observable<Response<Void>> sendMessage(@Body SendMessageBody payload);

    @GET("wall/mobile")
    Observable<Response<List<CommentsResponse>>> getCommentsByUserId(@Query("id") long id);

    @POST("wall")
    Observable<Response<SendWallCommentResponse>> sendWallComment(@Body SendWallCommentBody p);

    @GET("friends/mobile/{id}")
    Observable<Response<List<FriendResponse>>> getFriendsByUserId(@Path("id") long id);

    @GET("friends/mobile/requests")
    Observable<Response<List<FriendResponse>>> getFriendRequests();

    @GET("friends/mobile/own-requests")
    Observable<Response<List<FriendResponse>>> getFriendOwnRequests();

    @DELETE("friends/{id}")
    Observable<Response<Void>> removeFriendByUserId(@Path("id") long userId);

    @PUT("friends")
    Observable<Response<Void>> approveFriendShipByUserId(@Body ApproveFriendBody body);

    @GET("users/mobile/{id}/photo")
    Observable<Response<List<PhotosResponse>>> getPhotosByUserId(@Path("id") long id);

    @Multipart
    @POST("uploads/mobile/photo")
    Observable<Response<PhotosResponse>> uploadPhoto(@Part MultipartBody.Part photo);

    @GET("messages/mobile")
    Observable<Response<List<MessageResponse>>> getOwnMessages();

    @POST("messages")
    Observable<Response<Void>> sendMessage(@Body MessageBody body);
}
