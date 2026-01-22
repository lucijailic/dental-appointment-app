package ba.sum.fsre.dentalappointemntapp.data.adapter;

import android.app.AlertDialog;
import android.content.Intent;
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

public class OwnerAppointmentsAdapter extends RecyclerView.Adapter<OwnerAppointmentsAdapter.ViewHolder> {

    private List<Appointment> appointments;
    private AppointmentsRepository repository;
    private Runnable onActionFinished;

    public OwnerAppointmentsAdapter(List<Appointment> appointments, AppointmentsRepository repository, Runnable onActionFinished) {
        this.appointments = appointments;
        this.repository = repository;
        this.onActionFinished = onActionFinished;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_owner_appointment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointment app = appointments.get(position);

        holder.tvService.setText(app.getServiceName());

        String rawDateTime = app.getAppointmentTime();
        if (rawDateTime != null && rawDateTime.length() >= 16) {
            try {
                String datePart = rawDateTime.substring(0, 10);
                String timePart = rawDateTime.substring(11, 16);
                holder.tvTime.setText(timePart);
                holder.tvDate.setText(datePart);
            } catch (Exception e) {
                holder.tvTime.setText(rawDateTime);
            }
        }

        String user = app.getUserEmail();
        holder.tvUser.setText(user != null && !user.isEmpty() ? user : "Nepoznato");

        String status = app.getStatus();
        holder.tvStatus.setText(getStatusText(status));
        holder.tvStatus.setTextColor(getStatusColor(status));


        if ("booked".equals(status)) {
            holder.btnCancel.setVisibility(View.VISIBLE);
            holder.btnCancel.setEnabled(true);
            holder.btnCancel.setOnClickListener(v -> {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Potvrda")
                        .setMessage("Otkaži ovu rezervaciju?")
                        .setPositiveButton("Da", (dialog, which) -> {
                            holder.btnCancel.setEnabled(false);
                            repository.cancelByOwner(app.getId(), new RepositoryCallback<Void>() {
                                @Override
                                public void onSuccess(Void result) {
                                    Toast.makeText(v.getContext(), "Rezervacija otkazana!", Toast.LENGTH_SHORT).show();
                                    app.status = "cancelled_by_owner";
                                    Intent intent = new Intent("ba.sum.fsre.ACTION_APPOINTMENTS_UPDATED");
                                    intent.putExtra("appointment_id", app.getId());
                                    intent.putExtra("status", "cancelled_by_owner");
                                    v.getContext().sendBroadcast(intent);

                                    if (onActionFinished != null) onActionFinished.run();
                                }

                                @Override
                                public void onError(String error) {
                                    Toast.makeText(v.getContext(), "Greška: " + error, Toast.LENGTH_SHORT).show();
                                    holder.btnCancel.setEnabled(true);
                                }
                            });
                        })
                        .setNegativeButton("Ne", null)
                        .show();
            });
        } else {
            holder.btnCancel.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return appointments == null ? 0 : appointments.size();
    }
    private String getStatusText(String status) {
        if (status == null) return "Status: -";
        switch (status) {
            case "booked":
                return "Status: Rezervirano";
            case "cancelled_by_user":
                return "Status: Otkazano od strane korisnika";
            case "cancelled_by_owner":
                return "Status: Otkazano od strane vlasnika";
            default:
                return "Status: " + status;
        }
    }

    private int getStatusColor(String status) {
        if (status == null) return android.graphics.Color.DKGRAY;
        switch (status) {
            case "booked":
                return android.graphics.Color.parseColor("#1DB954");
            case "cancelled_by_user":
            case "cancelled_by_owner":
                return android.graphics.Color.RED;
            default:
                return android.graphics.Color.DKGRAY;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvService, tvTime, tvUser, tvStatus, tvDate;
        MaterialButton btnCancel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvService = itemView.findViewById(R.id.tvServiceNameOwner);
            tvTime = itemView.findViewById(R.id.tvTimeOwner);
            tvDate = itemView.findViewById(R.id.tvDateOwner);
            tvUser = itemView.findViewById(R.id.tvUserOwner);
            tvStatus = itemView.findViewById(R.id.tvStatusOwner);
            btnCancel = itemView.findViewById(R.id.btnCancelOwner);
        }
    }
}
