package ba.sum.fsre.dentalappointemntapp.data.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ba.sum.fsre.dentalappointemntapp.R;
import ba.sum.fsre.dentalappointemntapp.data.adapter.ServicesAdapter;
import ba.sum.fsre.dentalappointemntapp.data.model.Service;
import ba.sum.fsre.dentalappointemntapp.data.repository.RepositoryCallback;
import ba.sum.fsre.dentalappointemntapp.data.repository.ServicesRepository;

public class ServicesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ServicesAdapter adapter;
    private final List<Service> serviceList = new ArrayList<>();
    private ServicesRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_list);

        findViewById(R.id.btn_back_services).setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.rvServices);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ServicesAdapter(serviceList, service -> {
            Intent intent = new Intent(ServicesActivity.this, ServiceDetailsActivity.class);

            intent.putExtra("service_id", String.valueOf(service.id));
            intent.putExtra("service_name", service.name);
            intent.putExtra("service_price", service.price);
            intent.putExtra("service_duration", service.duration_minutes);
            intent.putExtra("service_description", service.description);

            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        repository = new ServicesRepository(this);
        loadServices();
    }

    private void loadServices() {
        repository.getServices(new RepositoryCallback<List<Service>>() {
            @Override
            public void onSuccess(List<Service> data) {
                serviceList.clear();
                serviceList.addAll(data);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(ServicesActivity.this, error, Toast.LENGTH_LONG).show();
            }
        });
    }
}
