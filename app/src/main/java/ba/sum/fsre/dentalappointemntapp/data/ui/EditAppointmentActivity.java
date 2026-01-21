package ba.sum.fsre.dentalappointemntapp.data.ui;

import android.app.DatePickerDialog;
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

import ba.sum.fsre.dentalappointemntapp.R;
import ba.sum.fsre.dentalappointemntapp.data.model.AvailableSlot;
import ba.sum.fsre.dentalappointemntapp.data.repository.AppointmentsRepository;
import ba.sum.fsre.dentalappointemntapp.data.repository.RepositoryCallback;

public class EditAppointmentActivity extends AppCompatActivity {
    private AppointmentsRepository repository;
    private String appointmentId;
    private String serviceId;
    private Calendar calendar;
    private String selectedDateTime = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_appointment);

        repository = new AppointmentsRepository(this);
        calendar = Calendar.getInstance();


        appointmentId = getIntent().getStringExtra("APPOINTMENT_ID");
        serviceId = getIntent().getStringExtra("SERVICE_ID");
        String serviceName = getIntent().getStringExtra("SERVICE_NAME");


        TextView tvService = findViewById(R.id.tvSelectedService);
        MaterialButton btnSelectDate = findViewById(R.id.btnSelectDate);
        TextView tvDateDisplay = findViewById(R.id.tvSelectedDateDisplay);
        Spinner spinnerSlots = findViewById(R.id.spinnerSlots);
        MaterialButton btnConfirm = findViewById(R.id.btnConfirm);


        tvService.setText("Usluga: " + (serviceName != null ? serviceName : "Nepoznato"));
        btnConfirm.setText("Spremi promjene");
        btnConfirm.setEnabled(false);


        btnSelectDate.setOnClickListener(v -> {

            DatePickerDialog datePicker = new DatePickerDialog(this, R.style.CustomPickerTheme, (view, year, month, day) -> {
                selectedDateTime = null;
                btnConfirm.setEnabled(false);
                spinnerSlots.setAdapter(null);

                calendar.set(year, month, day);
                String dateStr = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
                tvDateDisplay.setText("Novi datum: " + dateStr);

                fetchSlots(dateStr, spinnerSlots, btnConfirm);

            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

            datePicker.getDatePicker().setMinDate(System.currentTimeMillis());
            datePicker.show();
        });


        btnConfirm.setOnClickListener(v -> {
            if (selectedDateTime == null) {
                Toast.makeText(this, "Molimo odaberite termin prije potvrde.", Toast.LENGTH_SHORT).show();
                return;
            }

            repository.rescheduleAppointment(appointmentId, selectedDateTime, new RepositoryCallback<Void>() {
                @Override
                public void onSuccess(Void result) {
                    Toast.makeText(EditAppointmentActivity.this, "Termin uspješno promijenjen!", Toast.LENGTH_LONG).show();
                    finish();
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(EditAppointmentActivity.this, "Greška: " + error, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void fetchSlots(String date, Spinner spinner, MaterialButton btnConfirm) {
        if (serviceId == null) return;

        repository.getAvailableSlots(Long.parseLong(serviceId), date, new RepositoryCallback<List<AvailableSlot>>() {
            @Override
            public void onSuccess(List<AvailableSlot> slots) {
                List<String> times = new ArrayList<>();
                times.add("Odaberi novi termin");

                if (slots != null && !slots.isEmpty()) {
                    for (AvailableSlot s : slots) {
                        if (s.slotTime != null && s.slotTime.length() >= 16) {
                            times.add(s.slotTime.substring(11, 16));
                        }
                    }
                } else {
                    Toast.makeText(EditAppointmentActivity.this, "Nema slobodnih termina za ovaj dan.", Toast.LENGTH_SHORT).show();
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(EditAppointmentActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, times);
                spinner.setAdapter(adapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position > 0) {
                            selectedDateTime = date + " " + times.get(position);
                            btnConfirm.setEnabled(true);
                        } else {
                            selectedDateTime = null;
                            btnConfirm.setEnabled(false);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        btnConfirm.setEnabled(false);
                    }
                });
            }

            @Override
            public void onError(String error) {
                Toast.makeText(EditAppointmentActivity.this, "Greška pri dohvatu termina", Toast.LENGTH_SHORT).show();
            }
        });
    }
}