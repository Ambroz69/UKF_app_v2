package com.example.dominik.ukf_app.DB_connect;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dominik on 09.03.2018.
 */

public class CalendarEvent implements Parcelable{
    private String id;
    private String nazov;
    private String datum;
    private String popis;

    public CalendarEvent(String id, String nazov, String datum, String popis) {
        this.id = id;
        this.nazov = nazov;
        this.datum = datum;
        this.popis = popis;
    }

    public String getId() {
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

    public CalendarEvent(Parcel in){
        String[] data = new String[4];

        in.readStringArray(data);
        // the order needs to be the same as in writeToParcel() method
        this.id = data[0];
        this.nazov = data[1];
        this.datum = data[2];
        this.popis = data[3];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                this.id,
                this.nazov,
                this.datum,
                this.popis});
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public CalendarEvent createFromParcel(Parcel in) {
            return new CalendarEvent(in);
        }

        public CalendarEvent[] newArray(int size) {
            return new CalendarEvent[size];
        }
    };

}
