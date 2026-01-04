package ba.sum.fsre.dentalappointemntapp.data.network;

import ba.sum.fsre.dentalappointemntapp.data.model.AuthRequest;
import ba.sum.fsre.dentalappointemntapp.data.model.AuthResponse;
import ba.sum.fsre.dentalappointemntapp.data.model.RegisterRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {
    @POST("auth/v1/token?grant_type=password")
    Call<AuthResponse> login(@Body AuthRequest request);

    @POST("auth/v1/signup")
    Call<AuthResponse> register(@Body RegisterRequest request);

}