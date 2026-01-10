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
        String userId = storage.getUserId();

        new android.os.Handler().postDelayed(() -> {

            if (token == null || token.isEmpty() || userId == null || userId.isEmpty()) {
                startActivity(new Intent(SplashActivity.this, PublicDashboardActivity.class));
                finish();
                return;
            }

            ba.sum.fsre.dentalappointemntapp.data.repository.ProfilesRepository profilesRepository =
                    new ba.sum.fsre.dentalappointemntapp.data.repository.ProfilesRepository(SplashActivity.this);

            profilesRepository.getProfileByUserId(userId, new ba.sum.fsre.dentalappointemntapp.data.repository.RepositoryCallback<ba.sum.fsre.dentalappointemntapp.data.model.Profile>() {
                @Override
                public void onSuccess(ba.sum.fsre.dentalappointemntapp.data.model.Profile profile) {
                    String role = (profile.role == null) ? "" : profile.role.trim().toLowerCase();
                    storage.saveRole(role);

                    Intent intent;
                    if ("owner".equals(role)) {
                        intent = new Intent(SplashActivity.this, OwnerDashboardActivity.class);
                    } else {
                        intent = new Intent(SplashActivity.this, UserDashboardActivity.class);
                    }

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onError(String error) {
                    storage.clear();
                    Intent i = new Intent(SplashActivity.this, PublicDashboardActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }
            });

        }, 1000);

    }
}
