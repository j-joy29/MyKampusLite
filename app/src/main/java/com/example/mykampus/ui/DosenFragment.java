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
import com.example.mykampus.model.Dosen;
import com.example.mykampus.model.Person;
import com.example.mykampus.viewmodel.AppViewModelFactory;
import com.example.mykampus.viewmodel.DosenViewModel;

import java.util.ArrayList;

public class DosenFragment extends Fragment {

    private DosenViewModel viewModel;
    private EditText etNama, etEmail, etNidn, etMataKuliah;
    private TextView tvError;
    private Button btnSimpan, btnBatal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dosen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etNama = view.findViewById(R.id.etNamaDosen);
        etEmail = view.findViewById(R.id.etEmailDosen);
        etNidn = view.findViewById(R.id.etNidn);
        etMataKuliah = view.findViewById(R.id.etMataKuliah);
        tvError = view.findViewById(R.id.tvErrorDosen);
        btnSimpan = view.findViewById(R.id.btnSimpanDosen);
        btnBatal = view.findViewById(R.id.btnBatalDosen);
        RecyclerView rv = view.findViewById(R.id.rvDosen);

        viewModel = new ViewModelProvider(this, new AppViewModelFactory(requireContext()))
                .get(DosenViewModel.class);

        // Adapter yang SAMA dengan tab Mahasiswa
        PersonAdapter adapter = new PersonAdapter(new PersonAdapter.OnItemActionListener() {
            @Override
            public void onEdit(Person person) {
                viewModel.mulaiEdit((Dosen) person);
            }

            @Override
            public void onDelete(Person person) {
                konfirmasiHapus((Dosen) person);
            }
        });
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.setAdapter(adapter);

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
            Dosen d = (event == null) ? null : event.getContentIfNotHandled();
            if (d != null) {
                etNama.setText(d.getNama());
                etEmail.setText(d.getEmail());
                etNidn.setText(d.getNidn());
                etMataKuliah.setText(d.getMataKuliahDiampu());
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
                etNidn.getText().toString(),
                etMataKuliah.getText().toString()));

        btnBatal.setOnClickListener(v -> viewModel.batalEdit());
    }

    private void clearForm() {
        etNama.setText("");
        etEmail.setText("");
        etNidn.setText("");
        etMataKuliah.setText("");
    }

    private void konfirmasiHapus(Dosen d) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Hapus data")
                .setMessage("Hapus " + d.getNama() + "?")
                .setPositiveButton("Hapus", (dlg, w) -> viewModel.hapus(d))
                .setNegativeButton("Batal", null)
                .show();
    }
}

