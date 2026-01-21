package ba.sum.fsre.dentalappointemntapp;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

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

    private BroadcastReceiver appointmentsUpdatedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadAppointments();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appointments);

        repository = new AppointmentsRepository(this);
        recyclerView = findViewById(R.id.rvMyAppointments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        loadAppointments();

        ImageButton btnBack = findViewById(R.id.btn_back_appointments);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
    }

    private void loadAppointments() {
        String userId = new TokenStorage(this).getUserId();
        repository.getMyAppointments(userId, new RepositoryCallback<List<Appointment>>() {
            @Override
            public void onSuccess(List<Appointment> result) {
                recyclerView.setAdapter(new AppointmentsAdapter(result, repository, () -> loadAppointments()));
            }

            @Override
            public void onError(String error) {
                Toast.makeText(MyAppointmentsActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(appointmentsUpdatedReceiver,
                new IntentFilter("ba.sum.fsre.ACTION_APPOINTMENTS_UPDATED"),
                Context.RECEIVER_NOT_EXPORTED);
    }

    @Override
    protected void onPause() {
        super.onPause();
        try { unregisterReceiver(appointmentsUpdatedReceiver); } catch (Exception ignored) {}
    }
}


