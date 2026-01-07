package ba.sum.fsre.dentalappointemntapp.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.HashMap;
import java.util.Map;
public class RegisterRequest {
    public String email;
    public String password;

    @SerializedName("data")
    public Map<String, String> data;

    public RegisterRequest(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.data = new HashMap<>();
        data.put("first_name", firstName);
        data.put("last_name", lastName);
        data.put("role", "user");
    }
}
