package com.example.mykampus.model;

import androidx.room.Entity;
import androidx.room.Index;

import java.util.Locale;
import java.util.Objects;

@Entity(tableName = "mahasiswa", indices = {@Index(value = {"nim"}, unique = true)})
public class Mahasiswa extends Person{
    private String nim;
    private String jurusan;
    private double ipk;

    public Mahasiswa() {}

    public String getNim() {return nim;}
    public void  setNim(String nim) {
        if (nim == null || !nim.trim().matches("\\d{11}")) {
            throw new IllegalArgumentException("NIM harus 11 digit angka");
        }
        this.nim = nim.trim();
    }

    public String getJurusan() {
        return jurusan;
    }

    public void setJurusan(String jurusan) {
        if (jurusan == null || jurusan.trim().isEmpty()) {
            throw new IllegalArgumentException("Jurusan tidak boleh kosong");
        }
        this.jurusan = jurusan.trim();
    }

    public double getIpk() {return ipk;}
    public void setIpk(double ipk) {
        if (Double.isNaN(ipk) || ipk < 0.0 || ipk > 4.0) {
            throw new IllegalArgumentException("IPK harus di antara 0.0 dan 4.0");
        }
        this.ipk = ipk;
    }

    @Override
    public String getRole() { return "Mahasiswa"; }

    @Override
    public String getDetail() {
        return "NIM: " + nim + " | " + jurusan + " | IPK: " + String.format(Locale.US, "%.2f", ipk);
    }

    @Override
    public String getStatusAkademik() {
        if (ipk > 3.5) return "Cumlaude";
        if (ipk > 3.0) return "Memuaskan";
        if (ipk < 2.0) return "Peringatan";
        return "Cukup"; // 2.0 - 3.0 tidak diatur soal
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        Mahasiswa m = (Mahasiswa) o;
        return Double.compare(ipk, m.ipk) == 0
                && Objects.equals(nim, m.nim)
                && Objects.equals(jurusan, m.jurusan);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), nim, jurusan, ipk);
    }

}
