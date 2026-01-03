package ba.sum.fsre.dentalappointemntapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ba.sum.fsre.dentalappointemntapp.data.model.AuthRequest;
import ba.sum.fsre.dentalappointemntapp.data.model.AuthResponse;
import ba.sum.fsre.dentalappointemntapp.data.model.ProfileResponse;
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

        TextView loginLink = findViewById(R.id.go_to_login);

        loginLink.setOnClickListener(v -> {
            finish();
        });

        registerBtn.setOnClickListener(v -> {
            String firstName = firstNameInput.getText().toString().trim();
            String lastName = lastNameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Popunite sva polja", Toast.LENGTH_SHORT).show();
                return;
            }

            AuthApi api = ApiClient.get(this).create(AuthApi.class);

            api.register(new AuthRequest(email, password)).enqueue(new Callback<AuthResponse>() {
                @Override
                public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String userId = response.body().getUser().getId();

                        api.createProfile(new ProfileResponse(userId, email, "user", firstName, lastName))
                                .enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        if (response.isSuccessful()) {
                                            Log.d("REGISTER_DEBUG", "Profil uspješno kreiran");
                                            Toast.makeText(RegisterActivity.this, "Uspješna registracija!", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Log.e("REGISTER_ERROR", "Greška pri kreiranju profila: " + response.code());
                                            Toast.makeText(RegisterActivity.this, "Problem s bazom profila", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Log.e("REGISTER_ERROR", "Mrežna greška pri kreiranju profila");
                                    }
                                });
                    } else {
                        Toast.makeText(RegisterActivity.this, "Greška: Korisnik već postoji ili je lozinka prekratka", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<AuthResponse> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this, "Mrežna greška pri registraciji", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}

