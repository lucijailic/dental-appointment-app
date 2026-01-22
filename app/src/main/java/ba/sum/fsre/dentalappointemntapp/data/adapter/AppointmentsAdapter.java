package ba.sum.fsre.dentalappointemntapp.data.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.util.List;
import ba.sum.fsre.dentalappointemntapp.R;
import ba.sum.fsre.dentalappointemntapp.data.model.Appointment;
import ba.sum.fsre.dentalappointemntapp.data.repository.AppointmentsRepository;
import ba.sum.fsre.dentalappointemntapp.data.repository.RepositoryCallback;
import ba.sum.fsre.dentalappointemntapp.data.ui.EditAppointmentActivity;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.ViewHolder> {

    private List<Appointment> appointments;
    private AppointmentsRepository repository;
    private Runnable onActionFinished;

    public AppointmentsAdapter(List<Appointment> appointments, AppointmentsRepository repository, Runnable onActionFinished) {
        this.appointments = appointments;
        this.repository = repository;
        this.onActionFinished = onActionFinished;
    }
    public void updateData(List<Appointment> newAppointments) {
        this.appointments = newAppointments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointment app = appointments.get(position);

        holder.tvService.setText("Naziv usluge: " + app.getServiceName());
        String rawDateTime = app.getAppointmentTime();
        if (rawDateTime != null && rawDateTime.length() >= 16) {
            try {
                String datePart = rawDateTime.substring(0, 10);
                String timePart = rawDateTime.substring(11, 16);

                String[] dateSplit = datePart.split("-");
                String formattedDate = dateSplit[2] + "." + dateSplit[1] + "." + dateSplit[0] + ".";

                holder.tvTime.setText("📅 " + formattedDate + "  |  " + "⏰ " + timePart + "h");
            } catch (Exception e) {
                holder.tvTime.setText(rawDateTime);
            }
        }

        String status = app.getStatus();


        holder.tvStatus.setVisibility(View.VISIBLE);
        holder.btnCancel.setVisibility(View.GONE);
        holder.btnEdit.setVisibility(View.GONE);

        if (status == null || "booked".equals(status)) {
            holder.tvStatus.setText("Status: Rezervirano");
            holder.tvStatus.setTextColor(Color.parseColor("#1DB954"));

            holder.btnCancel.setVisibility(View.VISIBLE);
            holder.btnEdit.setVisibility(View.VISIBLE);
            holder.btnCancel.setEnabled(true);

        } else if ("cancelled_by_user".equals(status)) {
            holder.tvStatus.setText("Status: Otkazano od strane korisnika");
            holder.tvStatus.setTextColor(Color.RED);

        } else if ("cancelled_by_owner".equals(status)) {
            holder.tvStatus.setText("Status: Otkazano od strane vlasnika");
            holder.tvStatus.setTextColor(Color.RED);

        } else {
            holder.tvStatus.setText("Status: " + status);
            holder.tvStatus.setTextColor(Color.DKGRAY);
        }


        holder.btnCancel.setOnClickListener(v -> {
            int currentPos = holder.getBindingAdapterPosition();
            if (currentPos == RecyclerView.NO_POSITION) return;

            holder.btnCancel.setEnabled(false);
            repository.cancelAppointment(app.getId(), new RepositoryCallback<Void>() {
                @Override
                public void onSuccess(Void result) {
                    Toast.makeText(v.getContext(), "Rezervacija otkazana!", Toast.LENGTH_SHORT).show();
                    if (onActionFinished != null) onActionFinished.run();
                }

                @Override
                public void onError(String error) {
                    holder.btnCancel.setEnabled(true);
                    Toast.makeText(v.getContext(), "Greška: " + error, Toast.LENGTH_SHORT).show();
                }
            });
        });
        holder.btnEdit.setOnClickListener(v -> {

            Intent intent = new Intent(v.getContext(), EditAppointmentActivity.class);
            intent.putExtra("APPOINTMENT_ID", app.getId());
            intent.putExtra("SERVICE_ID", app.getServiceId());
            intent.putExtra("SERVICE_NAME", app.getServiceName());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvService, tvTime, tvStatus;
        MaterialButton btnCancel, btnEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvService = itemView.findViewById(R.id.tvServiceName);
            tvTime = itemView.findViewById(R.id.tvAppointmentTime);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnCancel = itemView.findViewById(R.id.btnCancel);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }
}