package ba.sum.fsre.dentalappointemntapp.data.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ba.sum.fsre.dentalappointemntapp.R;

public class ServiceDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usluge_detaljno);

        try {
            TextView tvName = findViewById(R.id.nazivUslugeDetaljno);
            TextView tvPrice = findViewById(R.id.cijenaUslugeDetaljno);
            TextView tvDuration = findViewById(R.id.trajanjeUslugeDetaljno);
            TextView tvDescription = findViewById(R.id.opisUslugeDetaljno);

            Intent intent = getIntent();
            if (intent != null) {
                String name = intent.getStringExtra("service_name");
                double price = intent.getDoubleExtra("service_price", 0.0);
                int duration = intent.getIntExtra("service_duration", 0);
                String description = intent.getStringExtra("service_description");

                if (tvName != null) tvName.setText(name != null ? name : "");
                if (tvPrice != null) tvPrice.setText("Cijena: " + price + " KM");
                if (tvDuration != null) tvDuration.setText("Trajanje: " + duration + " minuta");
                if (tvDescription != null) tvDescription.setText(description != null ? description : "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
