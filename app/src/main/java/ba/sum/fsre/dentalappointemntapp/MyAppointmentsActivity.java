package ba.sum.fsre.dentalappointemntapp;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ba.sum.fsre.dentalappointemntapp.data.local.TokenStorage;
import ba.sum.fsre.dentalappointemntapp.data.model.Appointment;
import ba.sum.fsre.dentalappointemntapp.data.repository.AppointmentsRepository;
import ba.sum.fsre.dentalappointemntapp.data.repository.RepositoryCallback;
import ba.sum.fsre.dentalappointemntapp.data.adapter.AppointmentsAdapter;

public class MyAppointmentsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AppointmentsRepository repository;
    private AppointmentsAdapter adapter;
    private List<Appointment> appointmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appointments);

        repository = new AppointmentsRepository(this);
        recyclerView = findViewById(R.id.rvMyAppointments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AppointmentsAdapter(appointmentList, repository, this::loadAppointments);
        recyclerView.setAdapter(adapter);

        ImageButton btnBack = findViewById(R.id.btn_back_appointments);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAppointments();
    }
    private void loadAppointments() {
        String userId = new TokenStorage(this).getUserId();
        repository.getMyAppointments(userId, new RepositoryCallback<List<Appointment>>() {
            @Override
            public void onSuccess(List<Appointment> result) {
                adapter.updateData(result);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(MyAppointmentsActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}


