package com.example.dominik.ukf_app;


import android.os.Parcel;
import android.os.Parcelable;

class Item{
    private int id;
    private String nazov;
    private String obsah;

    public Item(int id, String nazov, String obsah) {
        this.id = id;
        this.nazov = nazov;
        this.obsah = obsah;
    }

    public int getId() {
        return id;
    }

    public String getNazov() {
        return nazov;
    }

    public String getObsah() {
        return obsah;
    }

    @Override
    public String toString() {
        return "Nazov:\n" + this.nazov + "\n\nObsah:\n" + this.obsah+"\n\n\n\n";
    }
}
