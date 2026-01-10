package ba.sum.fsre.dentalappointemntapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import ba.sum.fsre.dentalappointemntapp.data.model.AuthResponse;
import ba.sum.fsre.dentalappointemntapp.data.model.RegisterRequest;
import ba.sum.fsre.dentalappointemntapp.data.network.ApiClient;
import ba.sum.fsre.dentalappointemntapp.data.network.AuthApi;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findViewById(R.id.register_card).setAlpha(0f);
        findViewById(R.id.register_card).setTranslationY(30f);
        findViewById(R.id.register_card).animate().alpha(1f).translationY(0f).setDuration(350).start();


        EditText firstNameInput = findViewById(R.id.first_name_input);
        EditText lastNameInput  = findViewById(R.id.last_name_input);
        EditText emailInput     = findViewById(R.id.email_input);
        EditText passwordInput  = findViewById(R.id.password_input);
        Button registerBtn      = findViewById(R.id.register_button);
        TextView goToLogin      = findViewById(R.id.go_to_login);

        goToLogin.setOnClickListener(v -> finish());

        registerBtn.setOnClickListener(v -> {
            String firstName = firstNameInput.getText().toString().trim();
            String lastName  = lastNameInput.getText().toString().trim();
            String email     = emailInput.getText().toString().trim();
            String password  = passwordInput.getText().toString().trim();

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
                            if (!response.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this,
                                        "Greška pri registraciji: " + readError(response.errorBody()),
                                        Toast.LENGTH_LONG).show();
                                return;
                            }


                            Toast.makeText(RegisterActivity.this,
                                    "Registracija uspješna! Sada se prijavite.",
                                    Toast.LENGTH_LONG).show();

                            new android.os.Handler(android.os.Looper.getMainLooper())
                                    .postDelayed(RegisterActivity.this::finish, 1200);
                        }

                        @Override
                        public void onFailure(Call<AuthResponse> call, Throwable t) {
                            Toast.makeText(RegisterActivity.this,
                                    "Greška mreže (signup): " + t.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }

    private String readError(ResponseBody body) {
        if (body == null) return "unknown error";
        try { return body.string(); }
        catch (IOException e) { return "error reading message"; }
    }
}
