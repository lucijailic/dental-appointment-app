package ba.sum.fsre.dentalappointemntapp.data.network.api;

import java.util.List;
import ba.sum.fsre.dentalappointemntapp.data.model.Appointment;
import retrofit2.Call;
import retrofit2.http.*;

public interface AppointmentsApi {

    @Headers({
            "Content-Type: application/json",
            "Prefer: return=representation"
    })
    @POST("rest/v1/appointments")
    Call<Void> createAppointment(@Body Appointment appointment);

    @GET("rest/v1/appointments")
    Call<List<Appointment>> getMyAppointments(@Query("user_id") String userIdFilter, @Query("select") String select);

    @DELETE("rest/v1/appointments")
    Call<Void> cancelAppointment(@Query("id") String idFilter);
}
