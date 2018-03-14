package com.example.dominik.ukf_app.db_connect;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dominik on 14.03.2018.
 */

public class StudijnyProgram implements Parcelable {
    private int id;
    private String nazov;
    private String typ;
    private String obsah;
    private String detail;

    public StudijnyProgram(int id, String nazov, String typ, String obsah, String detail) {
        this.id = id;
        this.nazov = nazov;
        this.typ = typ;
        this.obsah = obsah;
        this.detail = detail;
    }

    public int getId() {
        return id;
    }

    public String getNazov() {
        return nazov;
    }

    public String getTyp() { return typ; }

    public String getObsah() {
        return obsah;
    }

    public String getDetail() { return detail; }

    @Override
    public String toString() {
        return ""+this.obsah;
    }

    public StudijnyProgram(Parcel in){
        String[] data = new String[5];

        in.readStringArray(data);
        // the order needs to be the same as in writeToParcel() method
        this.id = Integer.parseInt(data[0]);
        this.nazov = data[1];
        this.typ = data[2];
        this.obsah = data[3];
        this.detail = data[4];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                String.valueOf(this.id),
                this.nazov,
                this.typ,
                this.obsah,
                this.detail});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public StudijnyProgram createFromParcel(Parcel in) {
            return new StudijnyProgram(in);
        }

        public StudijnyProgram[] newArray(int size) {
            return new StudijnyProgram[size];
        }
    };
}
