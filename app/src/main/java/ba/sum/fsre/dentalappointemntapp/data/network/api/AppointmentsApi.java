package ba.sum.fsre.dentalappointemntapp.data.network.api;

import java.util.List;
import ba.sum.fsre.dentalappointemntapp.data.model.Appointment;
import retrofit2.Call;
import retrofit2.http.*;
import com.google.gson.annotations.SerializedName;
import ba.sum.fsre.dentalappointemntapp.data.model.AvailableSlot;


public interface AppointmentsApi {

    @Headers({
            "Content-Type: application/json",
    })
    @POST("rest/v1/appointments")
    Call<Void> createAppointment(@Body Appointment appointment);

    @GET("rest/v1/appointments")
    Call<List<Appointment>> getMyAppointments(@Query("user_id") String userIdFilter, @Query("status") String statusFilter, @Query("select") String select);

    @DELETE("rest/v1/appointments")
    Call<Void> cancelAppointment(@Query("id") String idFilter);
    @PATCH("rest/v1/appointments")
    Call<Void> rescheduleAppointment(
            @Query("id") String id,
            @Body Appointment appointment
    );

    class SlotsRequest {
        @SerializedName("p_service_id")
        public long serviceId;

        @SerializedName("p_date")
        public String date;

        public SlotsRequest(long serviceId, String date) {
            this.serviceId = serviceId;
            this.date = date;
        }
    }


    @Headers({
            "Content-Type: application/json",
            "Prefer: return=representation"
    })
    @POST("rest/v1/rpc/get_available_slots")
    Call<List<ba.sum.fsre.dentalappointemntapp.data.model.AvailableSlot>> getAvailableSlots(@Body SlotsRequest body);

    @GET("rest/v1/appointments")
    Call<List<Appointment>> getAppointmentsForDay(
            @Query("appointment_time") String startFilter,
            @Query("appointment_time") String endFilter,
            @Query("status") String statusFilter,
            @Query("order") String order,
            @Query("select") String select
    );

        @Headers({
            "Content-Type: application/json",
            "Prefer: return=representation"
        })
        @PATCH("rest/v1/appointments")
        Call<Void> cancelByOwner(@Body java.util.Map<String, String> body, @Query("id") String idFilter);


}
