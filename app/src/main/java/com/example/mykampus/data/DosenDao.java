package com.example.mykampus.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mykampus.model.Dosen;
import com.example.mykampus.model.Mahasiswa;

import java.util.List;

@Dao
public interface DosenDao {
    @Query("SELECT * FROM dosen ORDER BY nama COLLATE NOCASE")
    LiveData<List<Dosen>> getAll();

    @Insert
    void insert(Dosen dosen);

    @Update
    void update(Dosen dosen);

    @Delete
    void delete(Dosen dosen);
}
