package com.example.mykampus.repository;

import androidx.lifecycle.LiveData;

import com.example.mykampus.model.Person;

import java.util.List;

public interface AkademikRepository<T extends Person> {
    LiveData<List<T>> getAll();
    void insert(T item, RepositoryCallback callback);
    void update(T item, RepositoryCallback callback);
    void delete(T item);
}
