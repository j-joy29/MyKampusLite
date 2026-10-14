package com.example.mykampus.repository;

import android.database.sqlite.SQLiteConstraintException;

import androidx.lifecycle.LiveData;

import com.example.mykampus.data.MahasiswaDao;
import com.example.mykampus.data.MyKampusDatabase;
import com.example.mykampus.model.Mahasiswa;

import java.util.List;

public class MahasiswaRepository implements AkademikRepository<Mahasiswa> {
    private final MahasiswaDao dao;
    public MahasiswaRepository(MahasiswaDao dao) {
        this.dao = dao;
    }

    @Override
    public LiveData<List<Mahasiswa>> getAll() {
        return dao.getAll();
    }

    @Override
    public void insert(Mahasiswa item, RepositoryCallback callback) {
        MyKampusDatabase.writeExecutor.execute(() -> {
            try {
                dao.insert(item);
                callback.onSuccess();
            } catch (SQLiteConstraintException e) {
                callback.onError("NIM sudah terdaftar");
            }
        });
    }

    @Override
    public void update(Mahasiswa item, RepositoryCallback callback) {
        MyKampusDatabase.writeExecutor.execute(() -> {
            try {
                dao.update(item);
                callback.onSuccess();
            } catch (SQLiteConstraintException e) {
                callback.onError("NIM sudah dipakai mahasiswa lain");
            }
        });
    }

    @Override
    public void delete(Mahasiswa item) {
        MyKampusDatabase.writeExecutor.execute(() -> dao.delete(item));
    }

}
