package ba.sum.fsre.dentalappointemntapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ba.sum.fsre.dentalappointemntapp.data.local.TokenStorage;

public class OwnerDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_dashboard);

        Button btnServiceManagement = findViewById(R.id.btn_service_management_owner);
        Button btnReservations = findViewById(R.id.btn_reservation_owner);
        Button logoutBtn = findViewById(R.id.btn_logout_owner);

        btnServiceManagement.setOnClickListener(v -> {
            Intent intent = new Intent(OwnerDashboardActivity.this, ManageServicesActivity.class);
            startActivity(intent);
        });

        btnReservations.setOnClickListener(v -> {
            Toast.makeText(OwnerDashboardActivity.this, "Rezervacije", Toast.LENGTH_SHORT).show();
        });

        logoutBtn.setOnClickListener(v -> {
            new TokenStorage(this).clear();
            Intent i = new Intent(this, PublicDashboardActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        });
    }
}
