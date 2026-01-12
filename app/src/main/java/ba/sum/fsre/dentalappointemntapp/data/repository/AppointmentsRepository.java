package ba.sum.fsre.dentalappointemntapp.data.repository;

import android.content.Context;
import java.util.List;
import ba.sum.fsre.dentalappointemntapp.data.model.Appointment;
import ba.sum.fsre.dentalappointemntapp.data.network.ApiClient;
import ba.sum.fsre.dentalappointemntapp.data.network.api.AppointmentsApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentsRepository {
    private AppointmentsApi api;

    public AppointmentsRepository(Context context) {
        this.api = ApiClient.get(context).create(AppointmentsApi.class);
    }

    public void createAppointment(Appointment a, RepositoryCallback<Void> callback) {
        api.createAppointment(a).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) callback.onSuccess(null);
                else callback.onError("Greška pri rezervaciji");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void getMyAppointments(String userId, RepositoryCallback<List<Appointment>> callback) {
        api.getMyAppointments("eq." + userId, "*,services(name)").enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                if (response.isSuccessful()) callback.onSuccess(response.body());
                else callback.onError("Greška pri dohvaćanju");
            }

            @Override
            public void onFailure(Call<List<Appointment>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void cancelAppointment(String id, RepositoryCallback<Void> callback) {
        api.cancelAppointment("eq." + id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) callback.onSuccess(null);
                else callback.onError("Greška pri otkazivanju");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
}
