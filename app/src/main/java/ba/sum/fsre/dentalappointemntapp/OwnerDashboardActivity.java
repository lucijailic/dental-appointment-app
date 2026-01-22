package ba.sum.fsre.dentalappointemntapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ba.sum.fsre.dentalappointemntapp.data.local.TokenStorage;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ba.sum.fsre.dentalappointemntapp.data.model.Appointment;
import ba.sum.fsre.dentalappointemntapp.data.model.Profile;
import ba.sum.fsre.dentalappointemntapp.data.repository.ProfilesRepository;
import ba.sum.fsre.dentalappointemntapp.data.repository.AppointmentsRepository;
import ba.sum.fsre.dentalappointemntapp.data.repository.RepositoryCallback;


public class OwnerDashboardActivity extends AppCompatActivity {

    private AppointmentsRepository appointmentsRepository;

    private TextView tvTodaySummary;
    private TextView tvGreeting;
    private LinearLayout containerTodayList;
    private TextView tvViewAllToday;
    private View todayCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_dashboard);

        appointmentsRepository = new AppointmentsRepository(this);

        tvTodaySummary = findViewById(R.id.tvTodaySummary);
        tvGreeting = findViewById(R.id.textView3);
        containerTodayList = findViewById(R.id.containerTodayList);
        tvViewAllToday = findViewById(R.id.tvViewAllToday);
        todayCard = findViewById(R.id.todays_reservations_cardview);

        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Load and set owner greeting
        String userId = new ba.sum.fsre.dentalappointemntapp.data.local.TokenStorage(this).getUserId();
        if (userId != null && !userId.isEmpty()) {
            ProfilesRepository profilesRepository = new ProfilesRepository(this);
            profilesRepository.getProfileByUserId(userId, new RepositoryCallback<Profile>() {
                @Override
                public void onSuccess(Profile data) {
                    if (data != null) {
                        String name = "";
                        if (data.firstName != null) {
                            name = data.firstName;
                        }
                        if (data.lastName != null && !data.lastName.isEmpty()) {
                            if (!name.isEmpty()) name += " ";
                            name += data.lastName;
                        }

                        final String greeting;
                        if (name.isEmpty()) {
                            greeting = "Zdravo, Vlasnik";
                        } else {
                            greeting = "Zdravo, " + name;
                        }

                        runOnUiThread(() -> tvGreeting.setText(greeting));
                    }
                }

                @Override
                public void onError(String error) {
                }
            });
        }

        loadTodayAppointments(today);


        Button btnServiceManagement = findViewById(R.id.btn_service_management_owner);
        Button btnReservations = findViewById(R.id.btn_reservation_owner);
        Button logoutBtn = findViewById(R.id.btn_logout_owner);

        btnServiceManagement.setOnClickListener(v -> {
            Intent intent = new Intent(OwnerDashboardActivity.this, ManageServicesActivity.class);
            startActivity(intent);
        });

        btnReservations.setOnClickListener(v -> openOwnerAppointments());
        tvViewAllToday.setOnClickListener(v -> openOwnerAppointments());
        todayCard.setOnClickListener(v -> openOwnerAppointments());


        logoutBtn.setOnClickListener(v -> {
            new TokenStorage(this).clear();
            Intent i = new Intent(this, PublicDashboardActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        });
    }

    private void loadTodayAppointments(String dateYYYYMMDD) {
        tvTodaySummary.setText("Učitavanje...");
        containerTodayList.removeAllViews();

        appointmentsRepository.getAppointmentsForDay(dateYYYYMMDD, true, new RepositoryCallback<List<Appointment>>() {
            @Override
            public void onSuccess(List<Appointment> list) {
                if (list == null || list.isEmpty()) {
                    tvTodaySummary.setText("Nema rezervacija danas");
                    return;
                }

                tvTodaySummary.setText("Danas imate " + list.size() + " rezervacija");

                int limit = Math.min(list.size(), 5);
                for (int i = 0; i < limit; i++) {
                    Appointment a = list.get(i);
                    addTodayRow(a);
                }
            }

            @Override
            public void onError(String error) {
                tvTodaySummary.setText("Greška pri dohvaćanju");
            }
        });
    }

    private void addTodayRow(Appointment a) {
        TextView row = new TextView(this);
        row.setTextSize(14);
        row.setPadding(0, 8, 0, 8);

        String time = formatAppointmentTime(a.getAppointmentTime());

        String service = (a.getServiceName() != null) ? a.getServiceName() : "";
        String email = "";
        try {
            email = a.getUserEmail();
        } catch (Exception ignored) {}

        String text = "• " + time + " | " + service + (email.isEmpty() ? "" : " | " + email);
        row.setText(text);

        containerTodayList.addView(row);
    }

    private void openOwnerAppointments() {
        Intent intent = new Intent(this, OwnerAppointmentsActivity.class);
        startActivity(intent);

    }

    private String formatAppointmentTime(String raw) {
        if (raw == null) return "";

        try {

            java.text.SimpleDateFormat in =
                    new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", java.util.Locale.getDefault());

            java.text.SimpleDateFormat out =
                    new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm", java.util.Locale.getDefault());

            java.util.Date d = in.parse(raw);
            return out.format(d);

        } catch (Exception e) {
            try {
                java.text.SimpleDateFormat in2 =
                        new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault());
                java.text.SimpleDateFormat out2 =
                        new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm", java.util.Locale.getDefault());
                java.util.Date d2 = in2.parse(raw);
                return out2.format(d2);
            } catch (Exception ignored) {}

            return raw;
        }
    }





}
