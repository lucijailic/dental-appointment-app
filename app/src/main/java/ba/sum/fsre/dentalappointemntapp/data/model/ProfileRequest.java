package ba.sum.fsre.dentalappointemntapp.data.model;

import com.google.gson.annotations.SerializedName;

public class ProfileRequest {
    @SerializedName("id")
    public String id;

    @SerializedName("first_name")
    public String firstName;

    @SerializedName("last_name")
    public String lastName;

    @SerializedName("email")
    public String email;

    @SerializedName("role")
    public String role;

    public ProfileRequest(String userId, String firstName, String lastName, String email) {
        this.id = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = "user";
    }
}