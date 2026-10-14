package com.example.mykampus.repository;

public interface RepositoryCallback {
    void onSuccess();
    void onError(String message);
}
