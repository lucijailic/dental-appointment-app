package ba.sum.fsre.dentalappointemntapp.data.repository;

import android.content.Context;
import java.util.List;
import ba.sum.fsre.dentalappointemntapp.data.model.Appointment;
import ba.sum.fsre.dentalappointemntapp.data.model.AvailableSlot;
import ba.sum.fsre.dentalappointemntapp.data.network.ApiClient;
import ba.sum.fsre.dentalappointemntapp.data.network.api.AppointmentsApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.Calendar;
import java.util.Locale;
import java.text.SimpleDateFormat;

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

    public void getAvailableSlots(long serviceId, String date, RepositoryCallback<List<AvailableSlot>> callback) {
        AppointmentsApi.SlotsRequest body = new AppointmentsApi.SlotsRequest(serviceId, date);

        api.getAvailableSlots(body).enqueue(new Callback<List<AvailableSlot>>() {
            @Override
            public void onResponse(Call<List<AvailableSlot>> call, Response<List<AvailableSlot>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Greška pri dohvaćanju slobodnih termina");
                }
            }

            @Override
            public void onFailure(Call<List<AvailableSlot>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void getAppointmentsForDay(String dateYYYYMMDD, boolean isOwner, RepositoryCallback<List<Appointment>> callback) {
        String next = nextDay(dateYYYYMMDD);

        String start = "gte." + dateYYYYMMDD + "T00:00:00Z";
        String end   = "lt."  + next + "T00:00:00Z";

        String select = "*,services(name),profiles(email)";
        String status = "eq.booked";
        String order  = "appointment_time.asc";

        api.getAppointmentsForDay(start, end, status, order, select).enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Greška pri dohvaćanju rezervacija");
                }
            }

            @Override
            public void onFailure(Call<List<Appointment>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }


    private String nextDay(String dateYYYYMMDD) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Calendar c = Calendar.getInstance();
            c.setTime(sdf.parse(dateYYYYMMDD));
            c.add(Calendar.DAY_OF_MONTH, 1);
            return sdf.format(c.getTime());
        } catch (Exception e) {
            return dateYYYYMMDD;
        }
    }


}
