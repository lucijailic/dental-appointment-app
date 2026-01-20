package ba.sum.fsre.dentalappointemntapp.data.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import ba.sum.fsre.dentalappointemntapp.MyAppointmentsActivity;
import ba.sum.fsre.dentalappointemntapp.R;
import ba.sum.fsre.dentalappointemntapp.data.local.TokenStorage;
import ba.sum.fsre.dentalappointemntapp.data.model.Appointment;
import ba.sum.fsre.dentalappointemntapp.data.model.AvailableSlot;
import ba.sum.fsre.dentalappointemntapp.data.repository.AppointmentsRepository;
import ba.sum.fsre.dentalappointemntapp.data.repository.RepositoryCallback;

public class CreateAppointmentActivity extends AppCompatActivity {
    private AppointmentsRepository repository;
    private String serviceId;
    private Calendar calendar;
    private String selectedDateTime = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_appointment);

        repository = new AppointmentsRepository(this);
        calendar = Calendar.getInstance();

        serviceId = getIntent().getStringExtra("SERVICE_ID");
        String serviceName = getIntent().getStringExtra("SERVICE_NAME");

        TextView tvService = findViewById(R.id.tvSelectedService);
        tvService.setText("Usluga: " + (serviceName != null ? serviceName : "Odabrana usluga"));

        MaterialButton btnSelectDate = findViewById(R.id.btnSelectDate);
        TextView tvDateDisplay = findViewById(R.id.tvSelectedDateDisplay);
        Spinner spinnerSlots = findViewById(R.id.spinnerSlots);
        MaterialButton btnConfirm = findViewById(R.id.btnConfirm);

        btnConfirm.setEnabled(false);

        btnSelectDate.setOnClickListener(v -> {
            DatePickerDialog datePicker = new DatePickerDialog(this, R.style.CustomPickerTheme, (view, year, month, day) -> {
                calendar.set(year, month, day);
                String dateStr = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
                tvDateDisplay.setText("Odabrani datum: " + dateStr);

                fetchSlots(dateStr, spinnerSlots, btnConfirm);

            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

            datePicker.getDatePicker().setMinDate(System.currentTimeMillis());
            datePicker.show();
        });

        btnConfirm.setOnClickListener(v -> {
            String userId = new TokenStorage(this).getUserId();
            Appointment appointment = new Appointment(serviceId, userId, selectedDateTime, "booked");

            repository.createAppointment(appointment, new RepositoryCallback<Void>() {
                @Override
                public void onSuccess(Void result) {
                    Toast.makeText(CreateAppointmentActivity.this, "Uspješna rezervacija!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CreateAppointmentActivity.this, MyAppointmentsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(CreateAppointmentActivity.this, "Greška: " + error, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
    private void fetchSlots(String date, Spinner spinner, MaterialButton btnConfirm) {
        repository.getAvailableSlots(Long.parseLong(serviceId), date, new RepositoryCallback<List<AvailableSlot>>() {
            @Override
            public void onSuccess(List<AvailableSlot> slots) {
                if (slots == null || slots.isEmpty()) {
                    Toast.makeText(CreateAppointmentActivity.this, "Nema slobodnih termina za ovaj dan.", Toast.LENGTH_SHORT).show();
                    spinner.setAdapter(null);
                    btnConfirm.setEnabled(false);
                    return;
                }

                List<String> times = new ArrayList<>();
                for (AvailableSlot s : slots) {
                    if (s.slotTime != null && s.slotTime.length() >= 16) {
                        times.add(s.slotTime.substring(11, 16));
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(CreateAppointmentActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, times);
                spinner.setAdapter(adapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedDateTime = date + " " + times.get(position);
                        btnConfirm.setEnabled(true);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        btnConfirm.setEnabled(false);
                    }
                });
            }

            @Override
            public void onError(String error) {
                Toast.makeText(CreateAppointmentActivity.this, "Greška pri dohvatu termina", Toast.LENGTH_SHORT).show();
            }
        });
    }
}