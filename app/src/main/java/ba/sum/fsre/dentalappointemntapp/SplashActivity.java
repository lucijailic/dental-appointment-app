package ba.sum.fsre.dentalappointemntapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import ba.sum.fsre.dentalappointemntapp.data.local.TokenStorage;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TokenStorage storage = new TokenStorage(this);
        String token = storage.getAccessToken();

        new android.os.Handler().postDelayed(() -> {
            if (token != null) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
            finish();
        }, 1000);
    }
}