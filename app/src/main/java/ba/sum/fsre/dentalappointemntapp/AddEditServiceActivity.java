package ba.sum.fsre.dentalappointemntapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ba.sum.fsre.dentalappointemntapp.data.model.Service;
import ba.sum.fsre.dentalappointemntapp.data.repository.RepositoryCallback;
import ba.sum.fsre.dentalappointemntapp.data.repository.ServicesRepository;

public class AddEditServiceActivity extends AppCompatActivity {

    private EditText etName, etPrice, etDuration, etDescription;
    private Button btnSave, btnCancel;
    private ServicesRepository repository;
    private String serviceId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_service);

        initViews();
        repository = new ServicesRepository(this);

        if (getIntent() != null && getIntent().getStringExtra("service_id") != null) {
            serviceId = getIntent().getStringExtra("service_id");
            loadServiceData();

        }

        btnSave.setOnClickListener(v -> saveService());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void initViews() {
        etName = findViewById(R.id.et_name);
        etPrice = findViewById(R.id.et_price);
        etDuration = findViewById(R.id.et_duration);
        etDescription = findViewById(R.id.et_description);
        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);
    }

    private void loadServiceData() {
        String name = getIntent().getStringExtra("service_name");
        double price = getIntent().getDoubleExtra("service_price", 0.0);
        int duration = getIntent().getIntExtra("service_duration", 0);
        String description = getIntent().getStringExtra("service_description");

        etName.setText(name);
        etPrice.setText(String.valueOf(price));
        etDuration.setText(String.valueOf(duration));
        etDescription.setText(description);
    }

    private void saveService() {
        String name = etName.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String durationStr = etDuration.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (name.isEmpty() || priceStr.isEmpty() || durationStr.isEmpty()) {
            Toast.makeText(this, "Molimo popunite sva obavezna polja", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            int duration = Integer.parseInt(durationStr);

            Service service = new Service(name, price, duration, description);

            if (serviceId == null) {
                repository.createService(service, new RepositoryCallback<Service>() {
                    @Override
                    public void onSuccess(Service data) {
                        if (data != null && data.id != null) {
                            Toast.makeText(AddEditServiceActivity.this, "Usluga je uspješno kreirana", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            Toast.makeText(AddEditServiceActivity.this, "Greška: Usluga je kreirana ali nema ID-a", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(AddEditServiceActivity.this, "Greška: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                repository.updateService(serviceId, service, new RepositoryCallback<java.util.List<Service>>() {
                    @Override
                    public void onSuccess(java.util.List<Service> data) {
                        if (data != null && !data.isEmpty()) {
                            Toast.makeText(AddEditServiceActivity.this, "Usluga je uspješno uređena", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            Toast.makeText(AddEditServiceActivity.this, "Greška: Nema povratnih podataka", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(AddEditServiceActivity.this, "Greška: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Cijena i trajanje moraju biti brojevi", Toast.LENGTH_SHORT).show();
        }
    }
}
