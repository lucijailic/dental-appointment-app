package ba.sum.fsre.dentalappointemntapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ba.sum.fsre.dentalappointemntapp.data.local.TokenStorage;
import ba.sum.fsre.dentalappointemntapp.data.model.AuthRequest;
import ba.sum.fsre.dentalappointemntapp.data.model.AuthResponse;
import ba.sum.fsre.dentalappointemntapp.data.model.Profile;
import ba.sum.fsre.dentalappointemntapp.data.network.ApiClient;
import ba.sum.fsre.dentalappointemntapp.data.network.AuthApi;
import ba.sum.fsre.dentalappointemntapp.data.repository.ProfilesRepository;
import ba.sum.fsre.dentalappointemntapp.data.repository.RepositoryCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.login_card).setAlpha(0f);
        findViewById(R.id.login_card).setTranslationY(30f);
        findViewById(R.id.login_card).animate().alpha(1f).translationY(0f).setDuration(350).start();


        EditText emailInput = findViewById(R.id.email_input);
        EditText passwordInput = findViewById(R.id.password_input);
        Button loginBtn = findViewById(R.id.login_button);

        TextView goToRegister = findViewById(R.id.go_to_register);
        goToRegister.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class))
        );

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
                    if (authData.getAccessToken() == null ||
                            authData.getUser() == null ||
                            authData.getUser().getId() == null) {
                        Toast.makeText(LoginActivity.this, "Greška: Token ili user nije primljen", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    TokenStorage storage = new TokenStorage(LoginActivity.this);
                    storage.saveAccessToken(authData.getAccessToken());

                    String userId = authData.getUser().getId();
                    storage.saveUserId(userId);

                    ProfilesRepository profilesRepository = new ProfilesRepository(LoginActivity.this);
                    profilesRepository.getProfileByUserId(userId, new RepositoryCallback<Profile>() {
                        @Override
                        public void onSuccess(Profile profile) {
                            String role = (profile.role == null) ? "" : profile.role.trim().toLowerCase();
                            storage.saveRole(role);

                            Intent intent;
                            if ("owner".equals(role)) {
                                intent = new Intent(LoginActivity.this, OwnerDashboardActivity.class);
                            } else {
                                intent = new Intent(LoginActivity.this, UserDashboardActivity.class);
                            }

                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onError(String error) {
                            storage.clear();
                            Toast.makeText(LoginActivity.this, "Profil ne postoji. Ponovno se prijavite.", Toast.LENGTH_LONG).show();

                            Intent i = new Intent(LoginActivity.this, PublicDashboardActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            finish();
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
