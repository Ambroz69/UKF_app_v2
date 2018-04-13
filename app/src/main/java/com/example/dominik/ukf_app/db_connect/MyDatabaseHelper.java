package com.example.dominik.ukf_app.db_connect;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="Ukf_app_offline_data";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override

    public void onCreate(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE studijny_program (id INTEGER PRIMARY KEY AUTOINCREMENT, nazov TEXT, obsah TEXT, detail TEXT);");
        database.execSQL("CREATE TABLE studentsky_zivot (id INTEGER PRIMARY KEY AUTOINCREMENT, nazov TEXT, obsah TEXT);");
        database.execSQL("CREATE TABLE podmienky_prijatia (id INTEGER PRIMARY KEY AUTOINCREMENT, nazov TEXT, obsah TEXT);");
        database.execSQL("CREATE TABLE udalosti (id INTEGER PRIMARY KEY AUTOINCREMENT, nazov TEXT, datum TEXT, popis TEXT);");
    }

    @Override

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS studijny_program");
        db.execSQL("DROP TABLE IF EXISTS studentsky_zivot");
        db.execSQL("DROP TABLE IF EXISTS podmienky_prijatia");
        db.execSQL("DROP TABLE IF EXISTS udalosti");
        onCreate(db);
    }

    public void addStudijnyProgram(String nazov, String obsah, String detail) {
            ContentValues values = new ContentValues(3);
            values.put("nazov", nazov);
            values.put("obsah", obsah);
            values.put("detail", detail);
            getWritableDatabase().insert("studijny_program",null,values);
        }


    public void addStudentskyZivot(String nazov, String obsah) {
        ContentValues values = new ContentValues(2);
        values.put("nazov", nazov);
        values.put("obsah", obsah);
        getWritableDatabase().insert("studentsky_zivot",null,values);
    }

    public void addPodmienkyPrijatia(String nazov, String obsah) {
        ContentValues values = new ContentValues(2);
        values.put("nazov", nazov);
        values.put("obsah", obsah);
        getWritableDatabase().insert("podmienky_prijatia",null,values);
    }

    public void addUdalosti(String nazov, String datum, String popis) {
        ContentValues values = new ContentValues(3);
        values.put("nazov", nazov);
        values.put("datum", datum);
        values.put("popis", popis);
        getWritableDatabase().insert("udalosti",null,values);
    }

    public Cursor getAllFromTable(String nazovTabulky) {
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + nazovTabulky,null);
        return cursor;
    }

    public void deleteAll () {
        getWritableDatabase().delete("studijny_program", null, null);
        getWritableDatabase().delete("studentsky_zivot", null, null);
        getWritableDatabase().delete("podmienky_prijatia", null, null);
        getWritableDatabase().delete("udalosti", null, null);
    }
}


