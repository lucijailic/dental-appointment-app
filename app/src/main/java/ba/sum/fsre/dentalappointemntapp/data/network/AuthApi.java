package ba.sum.fsre.dentalappointemntapp.data.network;

import ba.sum.fsre.dentalappointemntapp.data.model.AuthRequest;
import ba.sum.fsre.dentalappointemntapp.data.model.AuthResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

import java.util.List;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ba.sum.fsre.dentalappointemntapp.data.model.ProfileResponse;

public interface AuthApi {
    @POST("auth/v1/signup")
    Call<AuthResponse> register(@Body AuthRequest request);

    @POST("auth/v1/token?grant_type=password")
    Call<AuthResponse> login(@Body AuthRequest request);

    @GET("rest/v1/profiles")
    Call<List<ProfileResponse>> getUserProfile(
            @Query("id") String userId,
            @Query("select") String select
    );

    @POST("rest/v1/profiles")
    Call<Void> createProfile(@Body ProfileResponse profile);

}