package ba.sum.fsre.dentalappointemntapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ba.sum.fsre.dentalappointemntapp.data.local.TokenStorage;
import ba.sum.fsre.dentalappointemntapp.data.model.ProfileRequest;
import ba.sum.fsre.dentalappointemntapp.data.model.RegisterRequest;
import ba.sum.fsre.dentalappointemntapp.data.model.AuthResponse;
import ba.sum.fsre.dentalappointemntapp.data.network.ApiClient;
import ba.sum.fsre.dentalappointemntapp.data.network.AuthApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText firstNameInput = findViewById(R.id.first_name_input);
        EditText lastNameInput = findViewById(R.id.last_name_input);
        EditText emailInput = findViewById(R.id.email_input);
        EditText passwordInput = findViewById(R.id.password_input);
        Button registerBtn = findViewById(R.id.register_button);
        TextView goToLogin = findViewById(R.id.go_to_login);

        registerBtn.setOnClickListener(v -> {
            String firstName = firstNameInput.getText().toString().trim();
            String lastName = lastNameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
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

            api.register(new RegisterRequest(email, password, firstName, lastName))
                    .enqueue(new Callback<AuthResponse>() {
                        @Override
                        public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                AuthResponse authData = response.body();


                                if (authData.getUser() != null && authData.getUser().getId() != null) {

                                    String userId = authData.getUser().getId();
                                    ProfileRequest profileRequest = new ProfileRequest(userId, firstName, lastName, email);

                                    AuthApi profileApi = ApiClient.get(RegisterActivity.this).create(AuthApi.class);
                                    profileApi.createProfile(profileRequest).enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> callProfile, Response<Void> responseProfile) {
                                            if (responseProfile.isSuccessful()) {
                                                Toast.makeText(RegisterActivity.this, "Uspješna registracija! Prijavite se.", Toast.LENGTH_LONG).show();
                                                new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> finish(), 1500);
                                            } else {
                                                Toast.makeText(RegisterActivity.this, "Greška pri spremanju profila", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Void> callProfile, Throwable t) {
                                            Toast.makeText(RegisterActivity.this, "Greška mreže: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Greška: Podaci nisu primljeni", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(RegisterActivity.this, "Greška pri registraciji", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<AuthResponse> call, Throwable t) {
                            Toast.makeText(RegisterActivity.this, "Greška u mreži: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        goToLogin.setOnClickListener(v -> finish());
    }
}