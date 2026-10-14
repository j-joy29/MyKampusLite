package com.example.mykampus.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mykampus.model.Dosen;
import com.example.mykampus.model.Mahasiswa;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Mahasiswa.class, Dosen.class}, version = 1, exportSchema = false)
public abstract class MyKampusDatabase extends RoomDatabase {
    public abstract MahasiswaDao mahasiswaDao();
    public abstract DosenDao dosenDao();

    private static volatile MyKampusDatabase INSTANCE;

    public static final ExecutorService writeExecutor = Executors.newFixedThreadPool(2);
    public static MyKampusDatabase getInstance(final Context context)  {
        if (INSTANCE == null) {
            synchronized (MyKampusDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            MyKampusDatabase.class,
                            "mykampus.db").build();
                }
            }
        }
        return INSTANCE;
    }
}
