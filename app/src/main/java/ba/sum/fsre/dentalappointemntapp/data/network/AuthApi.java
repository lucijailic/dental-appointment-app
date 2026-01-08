package ba.sum.fsre.dentalappointemntapp.data.network;

import java.util.List;

import ba.sum.fsre.dentalappointemntapp.data.model.AuthRequest;
import ba.sum.fsre.dentalappointemntapp.data.model.AuthResponse;
import ba.sum.fsre.dentalappointemntapp.data.model.ProfileRequest;
import ba.sum.fsre.dentalappointemntapp.data.model.RegisterRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthApi {

    @Headers({
            "Content-Type: application/json",
            "Accept: application/json"
    })
    @POST("auth/v1/token?grant_type=password")
    Call<AuthResponse> login(@Body AuthRequest request);

    @Headers({
            "Content-Type: application/json",
            "Accept: application/json"
    })
    @POST("auth/v1/signup")
    Call<AuthResponse> register(@Body RegisterRequest request);

    // Dohvat profila (RLS: korisnik vidi svoj red)
    @Headers({
            "Accept: application/json"
    })
    @GET("rest/v1/profiles")
    Call<List<ba.sum.fsre.dentalappointemntapp.data.model.ProfileRequest>> getProfile(
            @Query("id") String userId,
            @Query("select") String select
    );
}