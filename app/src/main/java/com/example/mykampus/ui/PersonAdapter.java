package com.example.mykampus.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mykampus.R;
import com.example.mykampus.model.Person;


public class PersonAdapter extends ListAdapter<Person, PersonAdapter.VH> {

    public interface OnItemActionListener {
        void onEdit(Person person);
        void onDelete(Person person);
    }

    private static final DiffUtil.ItemCallback<Person> DIFF = new DiffUtil.ItemCallback<Person>() {
        @Override
        public boolean areItemsTheSame(@NonNull Person oldItem, @NonNull Person newItem) {
            return oldItem.getClass() == newItem.getClass() && oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Person oldItem, @NonNull Person newItem) {
            return oldItem.equals(newItem); // memakai equals() di Model
        }
    };

    private final OnItemActionListener listener;

    public PersonAdapter(OnItemActionListener listener) {
        super(DIFF);
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Person p = getItem(position);

        // Tanpa instanceof: perilaku ditentukan oleh objek aslinya (polymorphism)
        holder.tvNama.setText(p.getNama());
        holder.tvRole.setText(p.getRole());
        holder.tvDetail.setText(p.getDetail());
        holder.tvStatus.setText("Status: " + p.getStatusAkademik());

        holder.btnEdit.setOnClickListener(v -> listener.onEdit(p));
        holder.btnHapus.setOnClickListener(v -> listener.onDelete(p));
    }

    static class VH extends RecyclerView.ViewHolder {
        final TextView tvNama, tvRole, tvDetail, tvStatus;
        final Button btnEdit, btnHapus;

        VH(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvRole = itemView.findViewById(R.id.tvRole);
            tvDetail = itemView.findViewById(R.id.tvDetail);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnHapus = itemView.findViewById(R.id.btnHapus);
        }
    }
}

