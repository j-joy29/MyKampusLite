package com.example.mykampus.model;

import androidx.room.Entity;
import androidx.room.Index;

import java.util.Objects;

@Entity(tableName = "dosen",
        indices = {@Index(value = {"nidn"}, unique = true)})
public class Dosen extends Person {

    private String nidn;
    private String mataKuliahDiampu = "";

    // Constructor kosong (dibutuhkan Room)
    public Dosen() { }

    public String getNidn() { return nidn; }

    public void setNidn(String nidn) {
        if (nidn == null || !nidn.trim().matches("\\d{10}")) {
            throw new IllegalArgumentException("NIDN harus 10 digit angka");
        }
        this.nidn = nidn.trim();
    }
    public String getMataKuliahDiampu() { return mataKuliahDiampu; }

    public void setMataKuliahDiampu(String mataKuliahDiampu) {
        this.mataKuliahDiampu = (mataKuliahDiampu == null) ? "" : mataKuliahDiampu.trim();
    }

    public int getJumlahMataKuliah() {
        if (mataKuliahDiampu == null || mataKuliahDiampu.trim().isEmpty()) return 0;
        int count = 0;
        for (String mk : mataKuliahDiampu.split(",")) {
            if (!mk.trim().isEmpty()) count++;
        }
        return count;
    }

    @Override
    public String getRole() { return "Dosen"; }

    @Override
    public String getDetail() {
        String mk = mataKuliahDiampu.isEmpty() ? "-" : mataKuliahDiampu;
        return "NIDN: " + nidn + " | MK (" + getJumlahMataKuliah() + "): " + mk;
    }

    @Override
    public String getStatusAkademik() {
        return getJumlahMataKuliah() > 3 ? "Beban Lebih" : "Normal";
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        Dosen d = (Dosen) o;
        return Objects.equals(nidn, d.nidn)
                && Objects.equals(mataKuliahDiampu, d.mataKuliahDiampu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), nidn, mataKuliahDiampu);
    }
}

