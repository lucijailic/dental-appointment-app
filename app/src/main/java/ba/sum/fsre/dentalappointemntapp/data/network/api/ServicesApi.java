package ba.sum.fsre.dentalappointemntapp.data.network.api;

import java.util.List;
import ba.sum.fsre.dentalappointemntapp.data.model.Service;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ServicesApi {
    @GET("rest/v1/services")
    Call<List<Service>> getServices(@Query("select") String select);
}
