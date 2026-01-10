package ba.sum.fsre.dentalappointemntapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import ba.sum.fsre.dentalappointemntapp.data.local.TokenStorage;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logo = findViewById(R.id.logo_splash);
        logo.setScaleX(0.85f);
        logo.setScaleY(0.85f);
        logo.setAlpha(0f);

        logo.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(500)
                .start();

        findViewById(R.id.tv_app_name).setAlpha(0f);
        findViewById(R.id.tv_app_name).animate()
                .alpha(1f)
                .setStartDelay(150)
                .setDuration(400)
                .start();

        findViewById(R.id.tv_tagline).setAlpha(0f);
        findViewById(R.id.tv_tagline).animate()
                .alpha(1f)
                .setStartDelay(250)
                .setDuration(400)
                .start();


        TokenStorage storage = new TokenStorage(this);
        String token = storage.getAccessToken();

        new android.os.Handler().postDelayed(() -> {
            if (token != null && !token.isEmpty()) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, PublicDashboardActivity.class));
            }
            finish();
        }, 1000);
    }
}
