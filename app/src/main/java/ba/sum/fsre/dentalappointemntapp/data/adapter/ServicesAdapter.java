package ba.sum.fsre.dentalappointemntapp.data.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ba.sum.fsre.dentalappointemntapp.R;
import ba.sum.fsre.dentalappointemntapp.data.model.Service;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ServiceViewHolder> {

    private final List<Service> services;
    private final OnServiceClickListener listener;

    public ServicesAdapter(List<Service> services, OnServiceClickListener listener) {
        this.services = services;
        this.listener = listener;
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder {

        TextView name, price, duration;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nazivUsluge);
            price = itemView.findViewById(R.id.cijenaUsluge);
            duration = itemView.findViewById(R.id.trajanjeUsluge);
        }
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_service, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Service service = services.get(position);

        holder.name.setText(service.name);
        holder.price.setText(service.price + " KM");
        holder.duration.setText(service.duration_minutes + " min");

        holder.itemView.setOnClickListener(v -> listener.onServiceClick(service));
    }


    @Override
    public int getItemCount() {
        return services.size();
    }

    public interface OnServiceClickListener {
        void onServiceClick(Service service);
    }
}
