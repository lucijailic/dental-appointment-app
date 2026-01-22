package ba.sum.fsre.dentalappointemntapp;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Collections;

import ba.sum.fsre.dentalappointemntapp.data.adapter.OwnerAppointmentsAdapter;
import ba.sum.fsre.dentalappointemntapp.data.model.Appointment;
import ba.sum.fsre.dentalappointemntapp.data.repository.AppointmentsRepository;
import ba.sum.fsre.dentalappointemntapp.data.repository.RepositoryCallback;

public class OwnerAppointmentsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AppointmentsRepository repository;
    private CalendarView calendarView;
    private TextView tvSelectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_appointments);

        repository = new AppointmentsRepository(this);

        recyclerView = findViewById(R.id.rvOwnerAppointments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        calendarView = findViewById(R.id.calendarView);
        tvSelectedDate = findViewById(R.id.tv_selected_date);

        // default date = today
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String todayStr = sdf.format(today);
        tvSelectedDate.setText(formatDisplayDate(today));

        loadAppointmentsForDate(todayStr);

        calendarView.setDate(today.getTime());
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar c = Calendar.getInstance();
            c.set(year, month, dayOfMonth, 0, 0, 0);
            String selected = sdf.format(c.getTime());
            tvSelectedDate.setText(formatDisplayDate(c.getTime()));
            loadAppointmentsForDate(selected);
        });

        ImageButton btnBack = findViewById(R.id.btn_back_appointments_owner);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());
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

    private String formatDisplayDate(Date d) {
        SimpleDateFormat out = new SimpleDateFormat("dd.MM.yyyy.", Locale.getDefault());
        return out.format(d);
    }

    private void loadAppointmentsForDate(String dateYYYYMMDD) {
        repository.getAppointmentsForDay(dateYYYYMMDD, true, new RepositoryCallback<List<Appointment>>() {
            @Override
            public void onSuccess(List<Appointment> result) {
                sortAppointments(result);
                recyclerView.setAdapter(new OwnerAppointmentsAdapter(result, repository, () -> loadAppointmentsForDate(dateYYYYMMDD)));
            }

            @Override
            public void onError(String error) {
                Toast.makeText(OwnerAppointmentsActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}


