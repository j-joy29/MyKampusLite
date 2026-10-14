package com.example.mykampus.repository;

import android.database.sqlite.SQLiteConstraintException;

import androidx.lifecycle.LiveData;

import com.example.mykampus.data.DosenDao;
import com.example.mykampus.data.MyKampusDatabase;
import com.example.mykampus.model.Dosen;

import java.util.List;

public class DosenRepository implements AkademikRepository<Dosen> {

    private final DosenDao dao;

    public DosenRepository(DosenDao dao) {
        this.dao = dao;
    }

    @Override
    public LiveData<List<Dosen>> getAll() {
        return dao.getAll();
    }

    @Override
    public void insert(Dosen item, RepositoryCallback callback) {
        MyKampusDatabase.writeExecutor.execute(() -> {
            try {
                dao.insert(item);
                callback.onSuccess();
            } catch (SQLiteConstraintException e) {
                callback.onError("NIDN sudah terdaftar");
            }
        });
    }

    @Override
    public void update(Dosen item, RepositoryCallback callback) {
        MyKampusDatabase.writeExecutor.execute(() -> {
            try {
                dao.update(item);
                callback.onSuccess();
            } catch (SQLiteConstraintException e) {
                callback.onError("NIDN sudah dipakai dosen lain");
            }
        });
    }

    @Override
    public void delete(Dosen item) {
        MyKampusDatabase.writeExecutor.execute(() -> dao.delete(item));
    }
}

