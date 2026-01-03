package ba.sum.fsre.dentalappointemntapp.data.repository;

import android.content.Context;
import java.util.List;

import ba.sum.fsre.dentalappointemntapp.data.model.Service;
import ba.sum.fsre.dentalappointemntapp.data.network.ApiClient;
import ba.sum.fsre.dentalappointemntapp.data.network.ServicesApi;
import retrofit2.Call;

public class ServicesRepository {
    private final ServicesApi api;

    public ServicesRepository(Context context) {
        api = ApiClient.get(context).create(ServicesApi.class);
    }

    public Call<List<Service>> fetchServices() {
        return api.getServices("*");
    }
}
