package com.example.mykampus.model;


import java.util.Objects;

public abstract class Person extends BaseEntity implements EvaluasiAkademik {
    private String nama;
    private String email;

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        if (nama == null || nama.trim().length() < 3) {
            throw new IllegalArgumentException("Nama minimal 3 karakter");
        }
        this.nama = nama.trim();
    }

    public String getEmail() {return email;}
    public void setEmail(String email) {
        if (email == null || !email.trim().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("Format email tidak valid");
        }
        this.email = email.trim();
    }

    public abstract String getRole();

    public abstract String getDetail();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Person p = (Person) o;
        return getId() == p.getId()
                && Objects.equals(nama, p.nama)
                && Objects.equals(email, p.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), nama, email);
    }
}
