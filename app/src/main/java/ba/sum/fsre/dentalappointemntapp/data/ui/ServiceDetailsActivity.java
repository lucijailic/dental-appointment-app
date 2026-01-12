package ba.sum.fsre.dentalappointemntapp.data.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

import ba.sum.fsre.dentalappointemntapp.R;


public class ServiceDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detailed);

        MaterialButton btnReserve = findViewById(R.id.btnReserve);

        try {
            TextView tvName = findViewById(R.id.nazivUslugeDetaljno);
            TextView tvPrice = findViewById(R.id.cijenaUslugeDetaljno);
            TextView tvDuration = findViewById(R.id.trajanjeUslugeDetaljno);
            TextView tvDescription = findViewById(R.id.opisUslugeDetaljno);

            Intent intent = getIntent();
            if (intent != null) {
                String name = intent.getStringExtra("service_name");
                String id = intent.getStringExtra("service_id");
                double price = intent.getDoubleExtra("service_price", 0.0);
                int duration = intent.getIntExtra("service_duration", 0);
                String description = intent.getStringExtra("service_description");

                String currentUserId = intent.getStringExtra("user_id");

                if (tvName != null) tvName.setText(name != null ? name : "");
                if (tvPrice != null) tvPrice.setText("Cijena: " + price + " KM");
                if (tvDuration != null) tvDuration.setText("Trajanje: " + duration + " minuta");
                if (tvDescription != null) tvDescription.setText(description != null ? description : "");

                if (btnReserve != null) {
                    if (currentUserId == null || currentUserId.isEmpty()) {
                        btnReserve.setVisibility(View.GONE);
                    } else {
                        btnReserve.setVisibility(View.VISIBLE);
                        btnReserve.setOnClickListener(new View.OnClickListener() {
                            @Override
                        public void onClick(View v) {
                            Intent createIntent = new Intent(ServiceDetailsActivity.this, CreateAppointmentActivity.class);
                            createIntent.putExtra("SERVICE_ID", id);
                            createIntent.putExtra("SERVICE_NAME", name);
                            startActivity(createIntent);
                        }
                    });
                }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
