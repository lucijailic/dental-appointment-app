package ba.sum.fsre.dentalappointemntapp.data.model;

public class AuthRequest {
    public String email;
    public String password;

    public AuthRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}