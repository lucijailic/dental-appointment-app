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

    @SerializedName("services")
    private ServiceInfo serviceInfo;

    public Appointment() {}

    public Appointment(String serviceId, String userId, String appointmentTime, String status) {
        this.serviceId = serviceId;
        this.userId = userId;
        this.appointmentTime = appointmentTime;
        this.status = status;
    }
    public String getId() {
        return id;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getUserId() {
        return userId;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public String getStatus() {
        return status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getServiceName() {
        return (serviceInfo != null) ? serviceInfo.name : "Usluga: " + serviceId;
    }

    public static class ServiceInfo {
        @SerializedName("name")
        public String name;
    }

    @SerializedName("profiles")
    private ProfileInfo profileInfo;

    public String getUserEmail() {
        return (profileInfo != null && profileInfo.email != null) ? profileInfo.email : "";
    }

    public static class ProfileInfo {
        @SerializedName("email")
        public String email;
    }

}
