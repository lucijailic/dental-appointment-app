package ba.sum.fsre.dentalappointemntapp.data.model;

public class AuthRequest {
    public String email;
    public String password;
    public UserData data;

    public AuthRequest(String email, String password) {
        this.email = email;
        this.password = password;
        this.data = new UserData("user");
    }

    public static class UserData {
        public String role;
        public UserData(String role) { this.role = role; }
    }
}