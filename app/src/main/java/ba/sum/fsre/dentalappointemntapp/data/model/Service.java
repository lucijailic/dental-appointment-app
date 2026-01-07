package ba.sum.fsre.dentalappointemntapp.data.model;

import com.google.gson.annotations.SerializedName;

public class Service {
    public String id;
    public String name;
    public String description;

    @SerializedName("duration_minutes")
    public int duration_minutes;

    // numeric u Postgresu -> u Javi najbolje BigDecimal ili Double
    public Double price;

    @SerializedName("created_at")
    public String createdAt;

    public Service(String name, double price, int duration_minutes, String description) {
        this.name = name;
        this.price = price;
        this.duration_minutes = duration_minutes;
        this.description = description;

    }
}
