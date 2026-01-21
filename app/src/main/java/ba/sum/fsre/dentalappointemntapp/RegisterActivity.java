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
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;


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

        TextView legalText = findViewById(R.id.tv_legal_register);

        final String privacyUrl = "https://lucijailic.github.io/dental-appointment-app/privacy-policy";
        final String termsUrl   = "https://lucijailic.github.io/dental-appointment-app/terms-of-use";

        String text = "By creating an account, you agree to our Privacy Policy and Terms of Use.";
        SpannableString ss = new SpannableString(text);

        int privacyStart = text.indexOf("Privacy Policy");
        int privacyEnd   = privacyStart + "Privacy Policy".length();
        int termsStart   = text.indexOf("Terms of Use");
        int termsEnd     = termsStart + "Terms of Use".length();

        ClickableSpan privacySpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(privacyUrl)));
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
                ds.setFakeBoldText(true);
            }
        };

        ClickableSpan termsSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(termsUrl)));
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
                ds.setFakeBoldText(true);
            }
        };

        ss.setSpan(privacySpan, privacyStart, privacyEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(termsSpan, termsStart, termsEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        ss.setSpan(new ForegroundColorSpan(0xFF1DB954), privacyStart, privacyEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(0xFF1DB954), termsStart, termsEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        legalText.setText(ss);
        legalText.setMovementMethod(LinkMovementMethod.getInstance());
        legalText.setHighlightColor(0x00000000);


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
