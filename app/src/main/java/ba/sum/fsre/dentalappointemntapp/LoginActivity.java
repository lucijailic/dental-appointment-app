package ba.sum.fsre.dentalappointemntapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import ba.sum.fsre.dentalappointemntapp.data.local.TokenStorage;
import ba.sum.fsre.dentalappointemntapp.data.model.AuthRequest;
import ba.sum.fsre.dentalappointemntapp.data.model.AuthResponse;
import ba.sum.fsre.dentalappointemntapp.data.model.ProfileRequest;
import ba.sum.fsre.dentalappointemntapp.data.network.ApiClient;
import ba.sum.fsre.dentalappointemntapp.data.network.AuthApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText emailInput = findViewById(R.id.email_input);
        EditText passwordInput = findViewById(R.id.password_input);
        Button loginBtn = findViewById(R.id.login_button);

        TextView goToRegister = findViewById(R.id.go_to_register);
        goToRegister.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));

        loginBtn.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Popunite sva polja", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Neispravan email format", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length() < 6) {
                Toast.makeText(this, "Lozinka mora imati najmanje 6 karaktera", Toast.LENGTH_SHORT).show();
                return;
            }

            AuthApi api = ApiClient.get(this).create(AuthApi.class);

            api.login(new AuthRequest(email, password)).enqueue(new Callback<AuthResponse>() {
                @Override
                public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                    if (!response.isSuccessful() || response.body() == null) {
                        Toast.makeText(LoginActivity.this, "Pogrešan email ili lozinka", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    AuthResponse authData = response.body();
                    if (authData.getAccessToken() == null || authData.getUser() == null || authData.getUser().getId() == null) {
                        Toast.makeText(LoginActivity.this, "Greška: Token ili user nije primljen", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // spremi token za interceptor
                    TokenStorage storage = new TokenStorage(LoginActivity.this);
                    storage.saveAccessToken(authData.getAccessToken());

                    String userId = authData.getUser().getId();

                    // sada povuci profil (trigger ga je već napravio)
                    api.getProfile("eq." + userId, "*").enqueue(new Callback<List<ProfileRequest>>() {
                        @Override
                        public void onResponse(Call<List<ProfileRequest>> call, Response<List<ProfileRequest>> profileResponse) {
                            if (profileResponse.isSuccessful() && profileResponse.body() != null && !profileResponse.body().isEmpty()) {
                                String ulogaIzBaze = profileResponse.body().get(0).role;

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("ROLE", ulogaIzBaze);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Greška: Profil nije pronađen.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<ProfileRequest>> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "Greška pri dohvaćanju profila: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onFailure(Call<AuthResponse> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Greška u mreži: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
