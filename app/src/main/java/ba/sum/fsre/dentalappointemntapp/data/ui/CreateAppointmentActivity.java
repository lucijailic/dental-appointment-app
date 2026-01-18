package ba.sum.fsre.dentalappointemntapp.data.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ba.sum.fsre.dentalappointemntapp.MyAppointmentsActivity;
import ba.sum.fsre.dentalappointemntapp.R;
import ba.sum.fsre.dentalappointemntapp.data.local.TokenStorage;
import ba.sum.fsre.dentalappointemntapp.data.model.Appointment;
import ba.sum.fsre.dentalappointemntapp.data.repository.AppointmentsRepository;
import ba.sum.fsre.dentalappointemntapp.data.repository.RepositoryCallback;

public class CreateAppointmentActivity extends AppCompatActivity {
    private AppointmentsRepository repository;
    private String serviceId;
    private Calendar calendar;

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

        TextInputEditText etTime = findViewById(R.id.etAppointmentTime);
        MaterialButton btnConfirm = findViewById(R.id.btnConfirm);

        etTime.setFocusable(false);
        etTime.setClickable(true);
        etTime.setOnClickListener(v -> showDateTimePicker(etTime));

        btnConfirm.setOnClickListener(v -> {
            String time = etTime.getText().toString();
            String userId = new TokenStorage(this).getUserId();

            if (time.isEmpty()) {
                Toast.makeText(this, "Molimo odaberite datum i vrijeme!", Toast.LENGTH_SHORT).show();
                return;
            }

            Appointment appointment = new Appointment(serviceId, userId, time, "booked");
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

    private void showDateTimePicker(TextInputEditText etTime) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.CustomPickerTheme, (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.CustomPickerTheme, (view1, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                etTime.setText(sdf.format(calendar.getTime()));

            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

            timePickerDialog.show();

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }
}