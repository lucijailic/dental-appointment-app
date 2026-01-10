package ba.sum.fsre.dentalappointemntapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import ba.sum.fsre.dentalappointemntapp.data.ui.ServicesActivity;

public class PublicDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_dashboard);
        findViewById(R.id.card_actions).setAlpha(0f);
        findViewById(R.id.card_actions).setTranslationY(30f);
        findViewById(R.id.card_actions).animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(350)
                .start();


        Button btnServices = findViewById(R.id.btn_services);
        Button btnLogin = findViewById(R.id.btn_login);
        Button btnRegister = findViewById(R.id.btn_register);

        btnServices.setOnClickListener(v ->
                startActivity(new Intent(PublicDashboardActivity.this, ServicesActivity.class))
        );

        btnLogin.setOnClickListener(v ->
                startActivity(new Intent(PublicDashboardActivity.this, LoginActivity.class))
        );

        btnRegister.setOnClickListener(v ->
                startActivity(new Intent(PublicDashboardActivity.this, RegisterActivity.class))
        );
    }
}

