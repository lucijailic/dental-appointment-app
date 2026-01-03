package ba.sum.fsre.dentalappointemntapp.data.model;
import com.google.gson.annotations.SerializedName;

public class AuthResponse {
    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("user")
    private User user;

    public String getAccessToken() { return accessToken; }

    public User getUser() { return user; }

    public static class User {
        @SerializedName("id")
        private String id;

        public String getId() { return id; }
    }
}