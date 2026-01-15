package ba.sum.fsre.dentalappointemntapp.data.repository;

import android.content.Context;
import java.util.List;

import ba.sum.fsre.dentalappointemntapp.BuildConfig;
import ba.sum.fsre.dentalappointemntapp.data.model.Service;
import ba.sum.fsre.dentalappointemntapp.data.network.ApiClient;
import ba.sum.fsre.dentalappointemntapp.data.network.api.ServicesApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServicesRepository {

    private final ServicesApi api;

    public ServicesRepository(Context context) {
        api = ApiClient.get(context).create(ServicesApi.class);
    }

    public void getServices(RepositoryCallback<List<Service>> callback) {

        api.getServices(BuildConfig.SUPABASE_ANON_KEY).enqueue(new Callback<List<Service>>() {

            @Override
            public void onResponse(
                    Call<List<Service>> call,
                    Response<List<Service>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Response error");
                }
            }

            @Override
            public void onFailure(Call<List<Service>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void createService(Service service, RepositoryCallback<Service> callback) {
        api.createService(service, BuildConfig.SUPABASE_ANON_KEY).enqueue(new Callback<List<Service>>() {
            @Override
            public void onResponse(Call<List<Service>> call, Response<List<Service>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    callback.onSuccess(response.body().get(0));
                } else {
                    callback.onError("Greška pri kreiranju usluge");
                }
            }

            @Override
            public void onFailure(Call<List<Service>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void updateService(String serviceId, Service service, RepositoryCallback<List<Service>> callback) {
        api.updateService(service, "eq." + serviceId, BuildConfig.SUPABASE_ANON_KEY).enqueue(new Callback<List<Service>>() {
            @Override
            public void onResponse(Call<List<Service>> call, Response<List<Service>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Greška pri uređivanju usluge");
                }
            }

            @Override
            public void onFailure(Call<List<Service>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void deleteService(String serviceId, RepositoryCallback<Void> callback) {
        api.deleteService("eq." + serviceId, BuildConfig.SUPABASE_ANON_KEY).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null);
                } else {
                    callback.onError("Greška pri brisanju usluge");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
}
