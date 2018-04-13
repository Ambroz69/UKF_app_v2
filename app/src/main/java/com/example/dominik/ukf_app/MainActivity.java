package com.example.dominik.ukf_app;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dominik.ukf_app.db_connect.MyDatabaseHelper;
import com.example.dominik.ukf_app.orientacia.ActivityOrientacia;
import com.example.dominik.ukf_app.podmienky_prijatia.ActivityPodmienkyPrijatia;
import com.example.dominik.ukf_app.studentsky_zivot.ActivityStudentskyZivot;
import com.example.dominik.ukf_app.udalosti.ActivityCalendar;
import com.example.dominik.ukf_app.db_connect.Api;
import com.example.dominik.ukf_app.db_connect.CalendarEvent;
import com.example.dominik.ukf_app.db_connect.Item;
import com.example.dominik.ukf_app.db_connect.RequestHandler;
import com.example.dominik.ukf_app.db_connect.StudijnyProgram;
import com.example.dominik.ukf_app.moznosti_studia.ActivityMoznostiStudiaMenu;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;
    private boolean locked;

    List<Item> itemList, podmienkyPrijatiaList, studentskyZivotList;
    List<CalendarEvent> udalostiList;
    List<StudijnyProgram> studijnyProgramList;
    List<String> dataPodmienkyPrijatia, dataStudentskyZivot, obrazkyNazovList, obrazkyUrlList;
    ImageView imageView;
    MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locked = true;
        Toast.makeText(getApplicationContext(),"Načitávajú sa údaje, prosím počkajte...", Toast.LENGTH_SHORT).show();

        imageView = findViewById(R.id.imageView);
        itemList = new ArrayList<>();
        podmienkyPrijatiaList = new ArrayList<>();
        studentskyZivotList = new ArrayList<>();
        udalostiList = new ArrayList<>();
        studijnyProgramList = new ArrayList<>();
        obrazkyNazovList = new ArrayList<>();
        obrazkyUrlList = new ArrayList<>();
        dbHelper = new MyDatabaseHelper(this);

        if (isNetworkAvailable()) {
            //pripojenie na server, naplnenie udajov
            readItems();
        } else {
            //pripojenie na internu databazu, naplnenie udajov
            readOfflineDatabase();
            Toast.makeText(getApplicationContext(),"Načítané offline údaje.", Toast.LENGTH_SHORT).show();
            locked = false;
        }
    }

    private void readOfflineDatabase() {
        Cursor data_studijny_program = dbHelper.getAllFromTable("studijny_program");
        studijnyProgramList.clear();
        data_studijny_program.moveToFirst();
        while (!data_studijny_program.isAfterLast()) {
            studijnyProgramList.add(new StudijnyProgram (
                    data_studijny_program.getInt(data_studijny_program.getColumnIndex("id")),
                    data_studijny_program.getString(data_studijny_program.getColumnIndex("nazov")),
                    "-",
                    data_studijny_program.getString(data_studijny_program.getColumnIndex("obsah")),
                    data_studijny_program.getString(data_studijny_program.getColumnIndex("detail"))
            ));
            data_studijny_program.moveToNext();
        }
        Cursor data_studentsky_zivot = dbHelper.getAllFromTable("studentsky_zivot");
        studentskyZivotList.clear();
        data_studentsky_zivot.moveToFirst();
        while (!data_studentsky_zivot.isAfterLast()) {
            studentskyZivotList.add(new Item (
                    data_studentsky_zivot.getInt(data_studentsky_zivot.getColumnIndex("id")),
                    data_studentsky_zivot.getString(data_studentsky_zivot.getColumnIndex("nazov")),
                    data_studentsky_zivot.getString(data_studentsky_zivot.getColumnIndex("obsah"))
            ));
            data_studentsky_zivot.moveToNext();
        }
        Cursor data_podmienky_prijatia = dbHelper.getAllFromTable("podmienky_prijatia");
        podmienkyPrijatiaList.clear();
        data_podmienky_prijatia.moveToFirst();
        while (!data_podmienky_prijatia.isAfterLast()) {
            podmienkyPrijatiaList.add(new Item (
                    data_podmienky_prijatia.getInt(data_podmienky_prijatia.getColumnIndex("id")),
                    data_podmienky_prijatia.getString(data_podmienky_prijatia.getColumnIndex("nazov")),
                    data_podmienky_prijatia.getString(data_podmienky_prijatia.getColumnIndex("obsah"))
            ));
            data_podmienky_prijatia.moveToNext();
        }
        Cursor data_udalosti = dbHelper.getAllFromTable("udalosti");
        udalostiList.clear();
        data_udalosti.moveToFirst();
        while (!data_udalosti.isAfterLast()) {
            udalostiList.add(new CalendarEvent (
                    data_udalosti.getInt(data_udalosti.getColumnIndex("id")),
                    data_udalosti.getString(data_udalosti.getColumnIndex("nazov")),
                    data_udalosti.getString(data_udalosti.getColumnIndex("datum")),
                    data_udalosti.getString(data_udalosti.getColumnIndex("popis"))
            ));
            data_udalosti.moveToNext();
        }
    }

    private void fillOfflineDatabase() {
        for (int i = 0; i < studijnyProgramList.size(); i++) {
            dbHelper.addStudijnyProgram(
                    studijnyProgramList.get(i).getNazov(),
                    studijnyProgramList.get(i).getObsah(),
                    studijnyProgramList.get(i).getDetail());
        }
        for (int i = 0; i < studentskyZivotList.size(); i++) {
            dbHelper.addStudentskyZivot(
                    studentskyZivotList.get(i).getNazov(),
                    studentskyZivotList.get(i).getObsah());
        }
        dbHelper.addPodmienkyPrijatia(
                podmienkyPrijatiaList.get(0).getNazov(),
                podmienkyPrijatiaList.get(0).getObsah());
        for (int i = 0; i < udalostiList.size(); i++) {
            dbHelper.addUdalosti(
                    udalostiList.get(i).getNazov(),
                    udalostiList.get(i).getDatum(),
                    udalostiList.get(i).getPopis());
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void saveImage(Context context, Bitmap b, String imageName) {
        FileOutputStream foStream;
        try {
            foStream = context.openFileOutput(imageName, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.JPEG, 100, foStream);
            foStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        String imageName;

        public DownloadImage(String imageName) {
            this.imageName = imageName;
        }
        private Bitmap downloadImageBitmap(String sUrl) {
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new URL(sUrl).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadImageBitmap(params[0]);
        }

        protected void onPostExecute(Bitmap result) {
            saveImage(getApplicationContext(), result, imageName);
        }
    }

    public void calendarButton(View view) {
        if (!locked) {
            Intent intent = new Intent(MainActivity.this, ActivityCalendar.class);
            intent.putParcelableArrayListExtra("udalosti", (ArrayList<? extends Parcelable>) udalostiList);
            startActivity(intent);
        }
    }

    public void moznostiStudiaButton(View view) {
        if (!locked) {
            Intent intent = new Intent(MainActivity.this, ActivityMoznostiStudiaMenu.class);
            intent.putParcelableArrayListExtra("studijne_programy", (ArrayList<? extends Parcelable>) studijnyProgramList);
            startActivity(intent);
        }
    }

    public void podmienkyPrijatiaButton(View view) {
        if (!locked) {
            dataPodmienkyPrijatia = new ArrayList<>(podmienkyPrijatiaList.size());
            for (Object object : podmienkyPrijatiaList) {
                dataPodmienkyPrijatia.add(object != null ? object.toString() : null);
            }
            Intent intent = new Intent(MainActivity.this, ActivityPodmienkyPrijatia.class);
            for (int i = 0; i < dataPodmienkyPrijatia.size(); i++) {
                intent.putExtra("info", dataPodmienkyPrijatia.get(i).toString());
            }
            startActivity(intent);
        }
    }

    public void studentstkyZivotButton(View view) {
        if (!locked) {
            dataStudentskyZivot = new ArrayList<>(studentskyZivotList.size());
            for (Object object : studentskyZivotList) {
                dataStudentskyZivot.add(object != null ? object.toString() : null);
            }
            Intent intent = new Intent(MainActivity.this, ActivityStudentskyZivot.class);
            for (int i = 0; i < dataStudentskyZivot.size(); i++) {
                intent.putExtra("detailInfo" + i, dataStudentskyZivot.get(i).toString());
            }
            startActivity(intent);
        }
    }

    public void orientaciaButton(View view) {
        if (!locked) {
            Intent intent = new Intent(MainActivity.this, ActivityOrientacia.class);
            intent.putExtra("mesto", "Potrebujem sa dostať do budovy UKF:");
            intent.putExtra("budova", "Som v hlavnej budove UKF a hľadám miestnosť:");
            startActivity(intent);
        }
    }
    private void readPictures() {
        //delete files
        String path = getApplicationContext().getFilesDir().toString();
        try {
            FileUtils.cleanDirectory(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //download new
        for (int i = 0; i < obrazkyUrlList.size(); i++) {
            new DownloadImage(obrazkyNazovList.get(i)).execute("https://" + obrazkyUrlList.get(i));
        }
    }
    private void readItems() {

        PerformNetworkRequest request1 = new PerformNetworkRequest(Api.URL_READ_PODMIENKY_PRIJATIA, CODE_GET_REQUEST, false);
        request1.execute();

        PerformNetworkRequest request2 = new PerformNetworkRequest(Api.URL_READ_STUDENTSKY_ZIVOT, CODE_GET_REQUEST, false);
        request2.execute();

        PerformNetworkRequest request3 = new PerformNetworkRequest(Api.URL_READ_UDALOSTI, CODE_GET_REQUEST, false);
        request3.execute();

        PerformNetworkRequest request4 = new PerformNetworkRequest(Api.URL_READ_STUDIJNY_PROGRAM, CODE_GET_REQUEST, false);
        request4.execute();

        PerformNetworkRequest request5 = new PerformNetworkRequest(Api.URL_READ_IMAGES, CODE_GET_REQUEST, true);
        request5.execute();
    }

    private void refreshItemList(JSONArray items, String message)
            throws JSONException {

        switch(message) {
            case "podmienky_prijatia":
                podmienkyPrijatiaList.clear();
                for (int i = 0; i < items.length(); i++) {
                    JSONObject obj = items.getJSONObject(i);

                    podmienkyPrijatiaList.add(new Item(
                            obj.getInt("id"),
                            obj.getString("nazov"),
                            obj.getString("obsah")
                    ));
                }
            break;

            case "studentsky_zivot":
                studentskyZivotList.clear();
                for (int i = 0; i < items.length(); i++) {
                    JSONObject obj = items.getJSONObject(i);

                    studentskyZivotList.add(new Item(
                            obj.getInt("id"),
                            obj.getString("nazov"),
                            obj.getString("obsah")
                    ));
                }
            break;

            case "udalosti":
                udalostiList.clear();
                for (int i = 0; i < items.length(); i++) {
                    JSONObject obj = items.getJSONObject(i);

                    udalostiList.add(new CalendarEvent (
                            obj.getInt("id"),
                            obj.getString("nazov"),
                            obj.getString("datum"),
                            obj.getString("popis")
                    ));
                }
            break;

            case "studijny_program":
                studijnyProgramList.clear();
                for (int i = 0; i < items.length(); i++) {
                    JSONObject obj = items.getJSONObject(i);

                   studijnyProgramList.add(new StudijnyProgram (
                            obj.getInt("id"),
                            obj.getString("nazov"),
                            obj.getString("typ"),
                            obj.getString("obsah"),
                            obj.getString("detail")
                    ));
                }
            break;

            case "obrazky":
                obrazkyNazovList.clear();
                obrazkyUrlList.clear();
                for (int i = 0; i < items.length(); i++) {
                    JSONObject obj = items.getJSONObject(i);

                    obrazkyNazovList.add(obj.getString("nazov"));
                    obrazkyUrlList.add(obj.getString("url"));
                }
            break;

            default:
                throw new IllegalArgumentException("Invalid argument: " + message);
        }
    }

    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {
        String url;
        int requestCode;
        boolean isLast;

        PerformNetworkRequest(String url, int requestCode, boolean isLast) {
            this.url = url;
            this.requestCode = requestCode;
            this.isLast = isLast;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    refreshItemList(object.getJSONArray("items"), object.getString("message"));
                }
                if (isLast) {
                    readPictures();
                    dbHelper.deleteAll();
                    fillOfflineDatabase();
                    Toast.makeText(getApplicationContext(),"Načítané online údaje.", Toast.LENGTH_SHORT).show();
                    locked = false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }
    }
}
