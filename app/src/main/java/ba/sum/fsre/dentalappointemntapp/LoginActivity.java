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
import ba.sum.fsre.dentalappointemntapp.data.model.ProfileResponse;
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
        TextView registerLink = findViewById(R.id.go_to_register);

        registerLink.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        loginBtn.setOnClickListener(v -> {
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Popunite sva polja", Toast.LENGTH_SHORT).show();
                return;
            }

            AuthApi api = ApiClient.get(this).create(AuthApi.class);

            api.login(new AuthRequest(email, password)).enqueue(new Callback<AuthResponse>() {
                @Override
                public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        AuthResponse authData = response.body();

                        TokenStorage storage = new TokenStorage(LoginActivity.this);
                        storage.saveAccessToken(authData.getAccessToken());

                        String userId = authData.getUser().getId();

                        api.getUserProfile("eq." + userId, "role").enqueue(new Callback<List<ProfileResponse>>() {
                            @Override
                            public void onResponse(Call<List<ProfileResponse>> profileCall, Response<List<ProfileResponse>> profileResponse) {
                                String finalRole = "user";

                                if (profileResponse.isSuccessful() && profileResponse.body() != null && !profileResponse.body().isEmpty()) {
                                    finalRole = profileResponse.body().get(0).getRole();
                                }

                                storage.saveUserId(userId);
                                storage.saveRole(finalRole);

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("ROLE", finalRole);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onFailure(Call<List<ProfileResponse>> profileCall, Throwable t) {
                                Toast.makeText(LoginActivity.this, "Greška u mrežnoj provjeri uloge. Pokušajte ponovno.", Toast.LENGTH_LONG).show();
                            }
                        });

                    } else {
                        Toast.makeText(LoginActivity.this, "Pogrešan email ili lozinka", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<AuthResponse> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Greška u mreži pri prijavi", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}