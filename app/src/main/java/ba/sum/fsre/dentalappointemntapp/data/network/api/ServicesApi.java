package ba.sum.fsre.dentalappointemntapp.data.network.api;

import java.util.List;

import ba.sum.fsre.dentalappointemntapp.data.model.Service;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ServicesApi {

    @GET("rest/v1/services")
    Call<List<Service>> getServices(
            @Header("apikey") String apiKey
    );

    @Headers({
            "Content-Type: application/json",
            "Prefer: return=representation"
    })
    @POST("rest/v1/services")
    Call<List<Service>> createService(
            @Body Service service,
            @Header("apikey") String apiKey
    );

    @Headers({
            "Content-Type: application/json",
            "Prefer: return=representation"
    })
    @PATCH("rest/v1/services")
    Call<List<Service>> updateService(
            @Body Service service,
            @Query("id") String id,
            @Header("apikey") String apiKey
    );

    @Headers("Prefer: return=representation")
    @DELETE("rest/v1/services")
    Call<Void> deleteService(
            @Query("id") String id,
            @Header("apikey") String apiKey
    );
}
