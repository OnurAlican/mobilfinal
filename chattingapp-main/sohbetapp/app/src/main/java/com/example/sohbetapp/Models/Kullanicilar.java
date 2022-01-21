package com.example.sohbetapp.Models;

public class Kullanicilar {
    private String bolum;

    private String dogumtarih;

    private String gorev;

    private String isim;

    private String resim;

    private String sınıf;

    private String telefon;

    private Object state;

    public Object getState() {
        return state;
    }

    public void setState(Object state) {
        this.state = state;
    }

    public Kullanicilar() {
    }

    public Kullanicilar(String bolum, String dogumtarih, String gorev, String isim, String resim, String sınıf, String telefon,Object state) {
        this.bolum = bolum;
        this.dogumtarih = dogumtarih;
        this.gorev = gorev;
        this.isim = isim;
        this.resim = resim;
        this.sınıf = sınıf;
        this.telefon = telefon;
        this.state=state;
    }

    public String getBolum() {
        return bolum;
    }

    public void setBolum(String bolum) {
        this.bolum = bolum;
    }

    public String getDogumtarih() {
        return dogumtarih;
    }

    public void setDogumtarih(String dogumtarih) {
        this.dogumtarih = dogumtarih;
    }

    public String getGorev() {
        return gorev;
    }

    public void setGorev(String gorev) {
        this.gorev = gorev;
    }

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public String getResim() {
        return resim;
    }

    public void setResim(String resim) {
        this.resim = resim;
    }

    public String getSınıf() {
        return sınıf;
    }

    public void setSınıf(String sınıf) {
        this.sınıf = sınıf;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }
}
