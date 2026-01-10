package ba.sum.fsre.dentalappointemntapp.data.network.api;

import java.util.List;

import ba.sum.fsre.dentalappointemntapp.data.model.Profile;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ProfilesApi {

    @Headers({
            "Accept: application/json"
    })
    @GET("rest/v1/profiles")
    Call<List<Profile>> getProfileById(
            @Header("apikey") String apikey,
            @Query("id") String idFilter,
            @Query("select") String select
    );
}
