package ba.sum.fsre.dentalappointemntapp.data.model;

import com.google.gson.annotations.SerializedName;

public class Appointment {
    public String id;

    @SerializedName("service_id")
    public String serviceId;

    @SerializedName("user_id")
    public String userId;

    @SerializedName("appointment_time")
    public String appointmentTime;

    public String status;

    @SerializedName("created_at")
    public String createdAt;

    public Appointment() {}

    public Appointment(String serviceId, String userId, String appointmentTime, String status) {
        this.serviceId = serviceId;
        this.userId = userId;
        this.appointmentTime = appointmentTime;
        this.status = status;
    }
}
