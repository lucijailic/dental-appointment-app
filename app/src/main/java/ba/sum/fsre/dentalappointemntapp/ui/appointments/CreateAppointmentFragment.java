package ba.sum.fsre.dentalappointemntapp.ui.appointments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

import ba.sum.fsre.dentalappointemntapp.R;
import ba.sum.fsre.dentalappointemntapp.data.model.Appointment;
import ba.sum.fsre.dentalappointemntapp.data.network.ApiClient;
import ba.sum.fsre.dentalappointemntapp.data.network.api.AppointmentsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAppointmentFragment extends Fragment {

    public CreateAppointmentFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_create_appointment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Spinner spinnerServices = view.findViewById(R.id.spinner_services);
        List<String> servicesList = new ArrayList<>();
        servicesList.add("Čišćenje zuba");
        servicesList.add("Kontrola");
        servicesList.add("Bijeljenje zuba");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                servicesList
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerServices.setAdapter(adapter);

        DatePicker datePicker = view.findViewById(R.id.date_picker);

        TimePicker timePicker = view.findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);

        Button btnCreate = view.findViewById(R.id.btn_create_appointment);
        btnCreate.setOnClickListener(v -> {

            Calendar selected = Calendar.getInstance();
            selected.set(
                    datePicker.getYear(),
                    datePicker.getMonth(),
                    datePicker.getDayOfMonth(),
                    timePicker.getHour(),
                    timePicker.getMinute()
            );

            if (selected.before(Calendar.getInstance())) {
                Toast.makeText(requireContext(),
                        "Termin mora biti u budućnosti",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            int serviceId = spinnerServices.getSelectedItemPosition() + 1;

            String appointmentTime = String.format(
                    "%04d-%02d-%02dT%02d:%02d:00Z",
                    selected.get(Calendar.YEAR),
                    selected.get(Calendar.MONTH) + 1,
                    selected.get(Calendar.DAY_OF_MONTH),
                    selected.get(Calendar.HOUR_OF_DAY),
                    selected.get(Calendar.MINUTE)
            );

            Appointment appointment = new Appointment();
            appointment.service_id = String.valueOf(serviceId);
            appointment.appointment_time = appointmentTime;
            appointment.status = "pending";

            AppointmentsApi api = ApiClient
                    .get(requireContext())
                    .create(AppointmentsApi.class);

            api.createAppointment(appointment).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(requireContext(),
                                "Rezervacija kreirana",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(),
                                "Greška pri kreiranju rezervacije",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(requireContext(),
                            "Network error",
                            Toast.LENGTH_SHORT).show();
                }
            });
        });

    }
}