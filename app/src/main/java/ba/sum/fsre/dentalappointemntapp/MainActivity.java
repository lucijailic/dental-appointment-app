package ba.sum.fsre.dentalappointemntapp;

import android.os.Bundle;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.widget.Toast;

import ba.sum.fsre.dentalappointemntapp.data.local.TokenStorage;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TokenStorage storage = new TokenStorage(this);

        if (storage.getAccessToken() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        String role = storage.getRole();
        if (role == null) {
            role = getIntent().getStringExtra("ROLE");
        }


        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        if (role != null) {
            Toast.makeText(this, "Prijavljen kao: " + role, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Dobrodošli nazad!", Toast.LENGTH_SHORT).show();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button logoutBtn = findViewById(R.id.logout_button);
        logoutBtn.setOnClickListener(v -> {
            storage.clear();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });
    }
}