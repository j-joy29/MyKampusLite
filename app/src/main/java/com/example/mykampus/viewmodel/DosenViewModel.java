package com.example.mykampus.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.mykampus.model.Dosen;
import com.example.mykampus.repository.AkademikRepository;
import com.example.mykampus.repository.RepositoryCallback;

import java.util.List;

public class DosenViewModel extends ViewModel {

    private final AkademikRepository<Dosen> repository;

    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> editMode = new MutableLiveData<>(false);
    private final MutableLiveData<Event<String>> message = new MutableLiveData<>();
    private final MutableLiveData<Event<Boolean>> resetForm = new MutableLiveData<>();
    private final MutableLiveData<Event<Dosen>> editRequest = new MutableLiveData<>();

    private volatile Dosen editingItem = null;

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

    public DosenViewModel(AkademikRepository<Dosen> repository) {
        this.repository = repository;
    }

    public LiveData<List<Dosen>> getAll() { return repository.getAll(); }
    public LiveData<String> getError() { return error; }
    public LiveData<Boolean> getEditMode() { return editMode; }
    public LiveData<Event<String>> getMessage() { return message; }
    public LiveData<Event<Boolean>> getResetForm() { return resetForm; }
    public LiveData<Event<Dosen>> getEditRequest() { return editRequest; }

    public void simpan(String nama, String email, String nidn, String mataKuliah) {
        Dosen d = new Dosen();
        try {
            d.setNama(nama);
            d.setEmail(email);
            d.setNidn(nidn);
            d.setMataKuliahDiampu(mataKuliah);
        } catch (IllegalArgumentException e) {
            error.setValue(e.getMessage());
            return;
        }

        Dosen editing = editingItem;
        if (editing != null) {
            d.setId(editing.getId());
            d.setCreatedAt(editing.getCreatedAt());
            d.setUpdatedAt(System.currentTimeMillis());
            repository.update(d, callback);
        } else {
            repository.insert(d, callback);
        }
    }

    public void mulaiEdit(Dosen d) {
        editingItem = d;
        error.setValue(null);
        editMode.setValue(true);
        editRequest.setValue(new Event<>(d));
    }

    public void batalEdit() {
        editingItem = null;
        error.setValue(null);
        editMode.setValue(false);
        resetForm.setValue(new Event<>(true));
    }

    public void hapus(Dosen d) {
        Dosen editing = editingItem;
        if (editing != null && editing.getId() == d.getId()) {
            batalEdit();
        }
        repository.delete(d);
        message.setValue(new Event<>("Data dihapus"));
    }
}

