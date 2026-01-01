package ba.sum.fsre.dentalappointemntapp.data.model;

import com.google.gson.annotations.SerializedName;

public class Service {
    public String id;
    public String name;

    @SerializedName("duration_minutes")
    public int durationMinutes;

    // numeric u Postgresu -> u Javi najbolje BigDecimal ili Double
    public Double price;

    @SerializedName("created_at")
    public String createdAt;
}
