package com.example.mykampus.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mykampus.R;
import com.example.mykampus.model.Mahasiswa;
import com.example.mykampus.model.Person;
import com.example.mykampus.viewmodel.AppViewModelFactory;
import com.example.mykampus.viewmodel.Event;
import com.example.mykampus.viewmodel.MahasiswaViewModel;

import java.util.ArrayList;

public class FragmentMahasiswa extends Fragment {

    private MahasiswaViewModel viewModel;
    private EditText etNama, etEmail, etNim, etJurusan, etIpk;
    private TextView tvError;
    private Button btnSimpan, btnBatal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mahasiswa, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etNama = view.findViewById(R.id.etNama);
        etEmail = view.findViewById(R.id.etEmail);
        etNim = view.findViewById(R.id.etNim);
        etJurusan = view.findViewById(R.id.etJurusan);
        etIpk = view.findViewById(R.id.etIpk);
        tvError = view.findViewById(R.id.tvError);
        btnSimpan = view.findViewById(R.id.btnSimpan);
        btnBatal = view.findViewById(R.id.btnBatal);
        RecyclerView rv = view.findViewById(R.id.rvMahasiswa);

        viewModel = new ViewModelProvider(this, new AppViewModelFactory(requireContext()))
                .get(MahasiswaViewModel.class);

        PersonAdapter adapter = new PersonAdapter(new PersonAdapter.OnItemActionListener() {
            @Override
            public void onEdit(Person person) {
                viewModel.mulaiEdit((Mahasiswa) person);
            }

            @Override
            public void onDelete(Person person) {
                konfirmasiHapus((Mahasiswa) person);
            }
        });
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.setAdapter(adapter);

        // RecyclerView auto-update via LiveData
        viewModel.getAll().observe(getViewLifecycleOwner(), list -> {
            if (list != null) adapter.submitList(new ArrayList<Person>(list));
        });

        viewModel.getError().observe(getViewLifecycleOwner(), msg -> {
            tvError.setText(msg);
            tvError.setVisibility(msg == null ? View.GONE : View.VISIBLE);
        });

        viewModel.getEditMode().observe(getViewLifecycleOwner(), editing -> {
            boolean isEdit = editing != null && editing;
            btnSimpan.setText(isEdit ? "Update" : "Simpan");
            btnBatal.setVisibility(isEdit ? View.VISIBLE : View.GONE);
        });

        viewModel.getEditRequest().observe(getViewLifecycleOwner(), event -> {
            Mahasiswa m = (event == null) ? null : event.getContentIfNotHandled();
            if (m != null) {
                etNama.setText(m.getNama());
                etEmail.setText(m.getEmail());
                etNim.setText(m.getNim());
                etJurusan.setText(m.getJurusan());
                etIpk.setText(String.valueOf(m.getIpk()));
            }
        });

        viewModel.getResetForm().observe(getViewLifecycleOwner(), event -> {
            if (event != null && event.getContentIfNotHandled() != null) clearForm();
        });

        viewModel.getMessage().observe(getViewLifecycleOwner(), event -> {
            String text = (event == null) ? null : event.getContentIfNotHandled();
            if (text != null) Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show();
        });

        btnSimpan.setOnClickListener(v -> viewModel.simpan(
                etNama.getText().toString(),
                etEmail.getText().toString(),
                etNim.getText().toString(),
                etJurusan.getText().toString(),
                etIpk.getText().toString()));

        btnBatal.setOnClickListener(v -> viewModel.batalEdit());
    }

    private void clearForm() {
        etNama.setText("");
        etEmail.setText("");
        etNim.setText("");
        etJurusan.setText("");
        etIpk.setText("");
    }

    private void konfirmasiHapus(Mahasiswa m) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Hapus data")
                .setMessage("Hapus " + m.getNama() + "?")
                .setPositiveButton("Hapus", (d, w) -> viewModel.hapus(m))
                .setNegativeButton("Batal", null)
                .show();
    }
}
