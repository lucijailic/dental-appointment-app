package ba.sum.fsre.dentalappointemntapp.data.adapter;

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

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.ViewHolder> {

    private List<Appointment> appointments;
    private AppointmentsRepository repository;
    private Runnable onActionFinished;

    public AppointmentsAdapter(List<Appointment> appointments, AppointmentsRepository repository, Runnable onActionFinished) {
        this.appointments = appointments;
        this.repository = repository;
        this.onActionFinished = onActionFinished;
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
        if (status == null || status.equals("booked")) {
            holder.tvStatus.setVisibility(View.GONE);
            holder.btnCancel.setVisibility(View.VISIBLE);
            holder.btnCancel.setEnabled(true);
        } else {
            holder.btnCancel.setVisibility(View.GONE);
            holder.tvStatus.setVisibility(View.VISIBLE);
            if (status.equals("cancelled_by_owner")) {
                holder.tvStatus.setText("Otkazano od strane vlasnika");
            }else {
                holder.tvStatus.setText("Status: " + status);
            }
        }

        holder.btnCancel.setOnClickListener(v -> {
            holder.btnCancel.setEnabled(false);
            repository.cancelAppointment(app.getId(), new RepositoryCallback<Void>() {
                @Override
                public void onSuccess(Void result) {
                    Toast.makeText(v.getContext(), "Rezervacija otkazana!", Toast.LENGTH_SHORT).show();
                    onActionFinished.run();
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(v.getContext(), "Greška: " + error, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvService, tvTime;
        MaterialButton btnCancel;
        TextView tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvService = itemView.findViewById(R.id.tvServiceName);
            tvTime = itemView.findViewById(R.id.tvAppointmentTime);
            btnCancel = itemView.findViewById(R.id.btnCancel);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}