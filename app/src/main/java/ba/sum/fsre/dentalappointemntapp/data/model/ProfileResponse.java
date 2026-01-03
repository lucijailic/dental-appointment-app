package ba.sum.fsre.dentalappointemntapp.data.model;

import com.google.gson.annotations.SerializedName;

public class ProfileResponse {
    @SerializedName("id")
    public String id;

    @SerializedName("email")
    public String email;

    @SerializedName("role")
    public String role;

    @SerializedName("first_name")
    public String first_name;

    @SerializedName("last_name")
    public String last_name;

    public ProfileResponse() {}

    public ProfileResponse(String id, String email, String role, String firstName, String lastName) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.first_name = firstName;
        this.last_name = lastName;
    }

    public String getRole() {
        return role;
    }

    public String getFirstName() {
        return first_name;
    }

    public String getLastName() {
        return last_name;
    }
}
