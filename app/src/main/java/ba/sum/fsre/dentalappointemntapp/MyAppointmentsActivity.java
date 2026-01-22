package ba.sum.fsre.dentalappointemntapp;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import androidx.core.content.ContextCompat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;


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

        adapter = new AppointmentsAdapter(appointmentList, repository, this::loadAppointments);
        recyclerView.setAdapter(adapter);

        ImageButton btnBack = findViewById(R.id.btn_back_appointments);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }
    }

    private int statusRank(String status) {
        if (status == null || "booked".equals(status)) return 0;
        if ("cancelled_by_user".equals(status) || "cancelled_by_owner".equals(status)) return 1;
        return 2;
    }

    private void sortAppointments(List<Appointment> list) {
        Collections.sort(list, (a, b) -> {
            int ra = statusRank(a.getStatus());
            int rb = statusRank(b.getStatus());
            if (ra != rb) return Integer.compare(ra, rb);

            String ta = a.getAppointmentTime();
            String tb = b.getAppointmentTime();
            if (ta == null && tb == null) return 0;
            if (ta == null) return 1;
            if (tb == null) return -1;
            return ta.compareTo(tb);
        });
    }


    private void loadAppointments() {
        String userId = new TokenStorage(this).getUserId();
        repository.getMyAppointments(userId, new RepositoryCallback<List<Appointment>>() {
            @Override
            public void onSuccess(List<Appointment> result) {
                sortAppointments(result);
                adapter.updateData(result);
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
        loadAppointments();
        IntentFilter filter = new IntentFilter("ba.sum.fsre.ACTION_APPOINTMENTS_UPDATED");
        // Use ContextCompat.registerReceiver with explicit exported flag to satisfy
        // Android U requirement for registering non-system broadcasts.
        ContextCompat.registerReceiver(this, appointmentsUpdatedReceiver, filter, ContextCompat.RECEIVER_NOT_EXPORTED);
    }

    @Override
    protected void onPause() {
        super.onPause();
        try { unregisterReceiver(appointmentsUpdatedReceiver); } catch (Exception ignored) {}
    }
}


