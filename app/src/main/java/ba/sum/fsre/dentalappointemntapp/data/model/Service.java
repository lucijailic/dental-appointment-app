package ba.sum.fsre.dentalappointemntapp.data.model;

import com.google.gson.annotations.SerializedName;

public class Service {
    @SerializedName("id")
    public String id;
    
    @SerializedName("name")
    public String name;
    
    @SerializedName("description")
    public String description;

    @SerializedName("duration_minutes")
    public int duration_minutes;

    @SerializedName("price")
    public Double price;

    @SerializedName("created_at")
    public String createdAt;

    public Service() {
    }

    public Service(String name, double price, int duration_minutes, String description) {
        this.name = name;
        this.price = price;
        this.duration_minutes = duration_minutes;
        this.description = description;

    }
}
