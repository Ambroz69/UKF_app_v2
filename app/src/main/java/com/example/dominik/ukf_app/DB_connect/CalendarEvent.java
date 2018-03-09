package com.example.dominik.ukf_app.DB_connect;

/**
 * Created by Dominik on 09.03.2018.
 */

public class CalendarEvent {
    private int id;
    private String nazov;
    private String datum;
    private String popis;

    public CalendarEvent(int id, String nazov, String datum, String popis) {
        this.id = id;
        this.nazov = nazov;
        this.datum = datum;
        this.popis = popis;
    }

    public int getId() {
        return id;
    }

    public String getNazov() {
        return nazov;
    }

    public String getDatum() {
        return datum;
    }

    public String getPopis() {
        return popis;
    }

}
