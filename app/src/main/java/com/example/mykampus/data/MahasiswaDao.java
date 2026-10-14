package com.example.mykampus.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mykampus.model.Mahasiswa;

import java.util.List;

@Dao
public interface MahasiswaDao {
    @Query("SELECT * FROM mahasiswa ORDER BY nama COLLATE NOCASE")
    LiveData<List<Mahasiswa>> getAll();

    @Insert
    void insert(Mahasiswa mahasiswa);

    @Update
    void update(Mahasiswa mahasiswa);

    @Delete
    void delete(Mahasiswa mahasiswa);
}
