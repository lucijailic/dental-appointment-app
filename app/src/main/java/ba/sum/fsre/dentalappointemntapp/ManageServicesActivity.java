package ba.sum.fsre.dentalappointemntapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ba.sum.fsre.dentalappointemntapp.data.adapter.ServicesOwnerAdapter;
import ba.sum.fsre.dentalappointemntapp.data.model.Service;
import ba.sum.fsre.dentalappointemntapp.data.repository.RepositoryCallback;
import ba.sum.fsre.dentalappointemntapp.data.repository.ServicesRepository;

public class ManageServicesActivity extends AppCompatActivity implements ServicesOwnerAdapter.OnServiceActionListener {

    private RecyclerView rv;
    private ServicesOwnerAdapter adapter;
    private List<Service> serviceList;
    private ServicesRepository repository;
    private TextView tvEmpty;

    private ActivityResultLauncher<Intent> addEditServiceLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_services);
        findViewById(R.id.btn_back_manage_services).setOnClickListener(v -> finish());

        // Register activity result launcher
        addEditServiceLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        loadServices();
                    }
                }
        );

        initViews();
        repository = new ServicesRepository(this);

        Button btnAddService = findViewById(R.id.btn_add_service);
        btnAddService.setOnClickListener(v -> openAddServiceActivity());

        loadServices();
    }

    private void initViews() {
        rv = findViewById(R.id.rv_services);
        tvEmpty = findViewById(R.id.tv_empty);
        serviceList = new ArrayList<>();
        adapter = new ServicesOwnerAdapter(serviceList, this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
    }

    private void loadServices() {
        repository.getServices(new RepositoryCallback<List<Service>>() {
            @Override
            public void onSuccess(List<Service> data) {
                serviceList.clear();
                serviceList.addAll(data);
                adapter.notifyDataSetChanged();
                updateEmptyState();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(ManageServicesActivity.this, "Greška: " + error, Toast.LENGTH_LONG).show();
                updateEmptyState();
            }
        });
    }

    private void updateEmptyState() {
        if (serviceList.isEmpty()) {
            rv.setVisibility(android.view.View.GONE);
            tvEmpty.setVisibility(android.view.View.VISIBLE);
        } else {
            rv.setVisibility(android.view.View.VISIBLE);
            tvEmpty.setVisibility(android.view.View.GONE);
        }
    }

    private void openAddServiceActivity() {
        addEditServiceLauncher.launch(new Intent(this, AddEditServiceActivity.class));
    }

    @Override
    public void onEditClick(Service service) {
        if (service == null || service.id == null) {
            Toast.makeText(this, "Usluga nema validnog ID-a. Pokušajte ponovno učitati stranicu.", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, AddEditServiceActivity.class);
        intent.putExtra("service_id", service.id);
        intent.putExtra("service_name", service.name);
        intent.putExtra("service_price", service.price);
        intent.putExtra("service_duration", service.duration_minutes);
        intent.putExtra("service_description", service.description);
        addEditServiceLauncher.launch(intent);
    }

    @Override
    public void onDeleteClick(Service service) {
        if (service == null || service.id == null) {
            Toast.makeText(this, "Usluga nema validnog ID-a. Pokušajte ponovno učitati stranicu.", Toast.LENGTH_SHORT).show();
            return;
        }
        new AlertDialog.Builder(this)
                .setTitle("Potvrdi brisanje")
                .setMessage("Jeste li sigurni da želite obrisati \"" + service.name + "\"?")
                .setPositiveButton("Obriši", (dialog, which) -> deleteService(service.id))
                .setNegativeButton("Otkaži", null)
                .show();
    }

    private void deleteService(String serviceId) {
        if (serviceId == null || serviceId.isEmpty()) {
            Toast.makeText(this, "Usluga nema validnog ID-a", Toast.LENGTH_SHORT).show();
            return;
        }
        repository.deleteService(serviceId, new RepositoryCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                Toast.makeText(ManageServicesActivity.this, "Usluga je obrisana", Toast.LENGTH_SHORT).show();
                loadServices();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(ManageServicesActivity.this, "Greška pri brisanju: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

