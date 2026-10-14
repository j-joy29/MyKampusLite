package com.example.mykampus.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mykampus.model.Mahasiswa;
import com.example.mykampus.repository.AkademikRepository;
import com.example.mykampus.repository.RepositoryCallback;

import java.util.List;


public class MahasiswaViewModel extends ViewModel {

    // Bergantung pada INTERFACE, bukan MahasiswaRepository
    private final AkademikRepository<Mahasiswa> repository;

    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> editMode = new MutableLiveData<>(false);
    private final MutableLiveData<Event<String>> message = new MutableLiveData<>();
    private final MutableLiveData<Event<Boolean>> resetForm = new MutableLiveData<>();
    private final MutableLiveData<Event<Mahasiswa>> editRequest = new MutableLiveData<>();

    // Item yang sedang diedit (bertahan saat rotasi karena disimpan di ViewModel)
    private volatile Mahasiswa editingItem = null;

    private final RepositoryCallback callback = new RepositoryCallback() {
        @Override
        public void onSuccess() {
            editingItem = null;
            error.postValue(null);
            editMode.postValue(false);
            resetForm.postValue(new Event<>(true));
            message.postValue(new Event<>("Data berhasil disimpan"));
        }

        @Override
        public void onError(String msg) {
            error.postValue(msg);
        }
    };

    public MahasiswaViewModel(AkademikRepository<Mahasiswa> repository) {
        this.repository = repository;
    }

    public LiveData<List<Mahasiswa>> getAll() { return repository.getAll(); }
    public LiveData<String> getError() { return error; }
    public LiveData<Boolean> getEditMode() { return editMode; }
    public LiveData<Event<String>> getMessage() { return message; }
    public LiveData<Event<Boolean>> getResetForm() { return resetForm; }
    public LiveData<Event<Mahasiswa>> getEditRequest() { return editRequest; }

    public void simpan(String nama, String email, String nim, String jurusan, String ipkText) {
        Mahasiswa m = new Mahasiswa();
        try {
            m.setNama(nama);
            m.setEmail(email);
            m.setNim(nim);
            m.setJurusan(jurusan);
            m.setIpk(parseIpk(ipkText));
        } catch (IllegalArgumentException e) {
            error.setValue(e.getMessage()); // validasi gagal -> TIDAK masuk Room
            return;
        }

        Mahasiswa editing = editingItem;
        if (editing != null) {
            m.setId(editing.getId());
            m.setCreatedAt(editing.getCreatedAt());
            m.setUpdatedAt(System.currentTimeMillis());
            repository.update(m, callback);
        } else {
            repository.insert(m, callback);
        }
    }

    public void mulaiEdit(Mahasiswa m) {
        editingItem = m;
        error.setValue(null);
        editMode.setValue(true);
        editRequest.setValue(new Event<>(m));
    }

    public void batalEdit() {
        editingItem = null;
        error.setValue(null);
        editMode.setValue(false);
        resetForm.setValue(new Event<>(true));
    }

    public void hapus(Mahasiswa m) {
        Mahasiswa editing = editingItem;
        if (editing != null && editing.getId() == m.getId()) {
            batalEdit();
        }
        repository.delete(m);
        message.setValue(new Event<>("Data dihapus"));
    }

    private double parseIpk(String text) {
        try {
            return Double.parseDouble(text.trim().replace(',', '.'));
        } catch (NumberFormatException | NullPointerException e) {
            throw new IllegalArgumentException("IPK harus berupa angka");
        }
    }
}

