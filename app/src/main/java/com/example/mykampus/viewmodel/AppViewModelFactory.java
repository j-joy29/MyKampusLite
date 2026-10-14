package com.example.mykampus.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mykampus.data.MyKampusDatabase;
import com.example.mykampus.repository.DosenRepository;
import com.example.mykampus.repository.MahasiswaRepository;

public class AppViewModelFactory implements ViewModelProvider.Factory {

    private final Context appContext;

    public AppViewModelFactory(Context context) {
        this.appContext = context.getApplicationContext();
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        MyKampusDatabase db = MyKampusDatabase.getInstance(appContext);

        if (modelClass.isAssignableFrom(MahasiswaViewModel.class)) {
            return (T) new MahasiswaViewModel(new MahasiswaRepository(db.mahasiswaDao()));
        }
        if (modelClass.isAssignableFrom(DosenViewModel.class)) {
            return (T) new DosenViewModel(new DosenRepository(db.dosenDao()));
        }
        throw new IllegalArgumentException("ViewModel tidak dikenal: " + modelClass.getName());
    }
}

