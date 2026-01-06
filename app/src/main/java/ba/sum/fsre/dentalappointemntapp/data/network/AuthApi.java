package ba.sum.fsre.dentalappointemntapp.data.network;

import java.util.List;

import ba.sum.fsre.dentalappointemntapp.data.model.AuthRequest;
import ba.sum.fsre.dentalappointemntapp.data.model.AuthResponse;
import ba.sum.fsre.dentalappointemntapp.data.model.ProfileRequest;
import ba.sum.fsre.dentalappointemntapp.data.model.RegisterRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthApi {
    @POST("auth/v1/token?grant_type=password")
    Call<AuthResponse> login(@Body AuthRequest request);

    @POST("auth/v1/signup")
    Call<AuthResponse> register(@Body RegisterRequest request);

    @POST("rest/v1/profiles")
    Call<Void> createProfile(@Body ProfileRequest request);

    @GET("rest/v1/profiles")
    Call<List<ProfileRequest>> getProfile(
            @Query("id") String userId,
            @Query("select") String select
    );

}