package ba.sum.fsre.dentalappointemntapp.data.model;
import com.google.gson.annotations.SerializedName;

public class AuthResponse {
    @SerializedName("access_token")
    private String accessToken;
    public String getAccessToken() { return accessToken; }

    @SerializedName("refresh_token")
    private String refreshToken;
    public String getRefreshToken() { return refreshToken; }

    public static class User {
        @SerializedName("id")
        private String id;

        @SerializedName("email")
        private String email;

        public String getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }
    }

    @SerializedName("user")
    private User user;

    public User getUser() {
        return user;
    }

    }
