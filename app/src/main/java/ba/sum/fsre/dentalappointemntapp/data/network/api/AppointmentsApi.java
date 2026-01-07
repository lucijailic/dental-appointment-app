package ba.sum.fsre.dentalappointemntapp.data.network.api;

import ba.sum.fsre.dentalappointemntapp.data.model.Appointment;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AppointmentsApi {

    @POST("/rest/v1/appointments")
    Call<Void> createAppointment(@Body Appointment appointment);
}
