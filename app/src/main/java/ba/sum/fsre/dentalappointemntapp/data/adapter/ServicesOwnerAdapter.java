package ba.sum.fsre.dentalappointemntapp.data.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ba.sum.fsre.dentalappointemntapp.R;
import ba.sum.fsre.dentalappointemntapp.data.model.Service;

public class ServicesOwnerAdapter extends RecyclerView.Adapter<ServicesOwnerAdapter.ServiceViewHolder> {

    private final List<Service> services;
    private final OnServiceActionListener listener;

    public ServicesOwnerAdapter(List<Service> services, OnServiceActionListener listener) {
        this.services = services;
        this.listener = listener;
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, duration;
        Button btnEdit, btnDelete;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nazivUsluge);
            price = itemView.findViewById(R.id.cijenaUsluge);
            duration = itemView.findViewById(R.id.trajanjeUsluge);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_service_owner, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Service service = services.get(position);

        holder.name.setText(service.name);
        holder.price.setText(service.price + " KM");
        holder.duration.setText(service.duration_minutes + " min");

        // Use position to get the service fresh from the list to ensure ID is populated
        holder.btnEdit.setOnClickListener(v -> {
            Service currentService = services.get(holder.getAdapterPosition());
            if (currentService != null && currentService.id != null) {
                listener.onEditClick(currentService);
            }
        });
        holder.btnDelete.setOnClickListener(v -> {
            Service currentService = services.get(holder.getAdapterPosition());
            if (currentService != null && currentService.id != null) {
                listener.onDeleteClick(currentService);
            }
        });
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public interface OnServiceActionListener {
        void onEditClick(Service service);
        void onDeleteClick(Service service);
    }
}
