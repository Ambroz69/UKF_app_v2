package com.example.dominik.ukf_app;


class Item {
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


}
