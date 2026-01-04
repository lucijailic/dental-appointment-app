package ba.sum.fsre.dentalappointemntapp.data.model;
import com.google.gson.annotations.SerializedName;

public class AuthResponse {
    @SerializedName("access_token")
    private String accessToken;
    public String getAccessToken() { return accessToken; }

    }
