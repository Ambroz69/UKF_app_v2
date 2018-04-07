package com.example.dominik.ukf_app;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
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

        //load textov
        readItems();
    }

    public void saveImage(Context context, Bitmap b, String imageName) {
        FileOutputStream foStream;
        try {
            foStream = context.openFileOutput(imageName, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.JPEG, 100, foStream);
            foStream.close();
        } catch (Exception e) {
            Log.d("saveImage", "Exception 2, Something went wrong!");
            e.printStackTrace();
        }
    }

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        private String TAG = "DownloadImage";
        String imageName;

        public DownloadImage(String imageName) {
            this.imageName = imageName;
        }
        private Bitmap downloadImageBitmap(String sUrl) {
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new URL(sUrl).openStream();   // Download Image from URL
                bitmap = BitmapFactory.decodeStream(inputStream);       // Decode Bitmap
                inputStream.close();
            } catch (Exception e) {
                Log.d(TAG, "Exception 1, Something went wrong!");
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
        for (int i = 0; i < obrazkyUrlList.size(); i++) {
            new DownloadImage(obrazkyNazovList.get(i)).execute("https://" + obrazkyUrlList.get(i));
        }
    }
    private void readItems() {

        PerformNetworkRequest request1 = new PerformNetworkRequest(Api.URL_READ_ITEMS, null, CODE_GET_REQUEST);
        request1.execute();

        PerformNetworkRequest request2 = new PerformNetworkRequest(Api.URL_READ_PODMIENKY_PRIJATIA, null, CODE_GET_REQUEST);
        request2.execute();

        PerformNetworkRequest request3 = new PerformNetworkRequest(Api.URL_READ_STUDENTSKY_ZIVOT, null, CODE_GET_REQUEST);
        request3.execute();

        PerformNetworkRequest request4 = new PerformNetworkRequest(Api.URL_READ_UDALOSTI, null, CODE_GET_REQUEST);
        request4.execute();

        PerformNetworkRequest request5 = new PerformNetworkRequest(Api.URL_READ_STUDIJNY_PROGRAM, null, CODE_GET_REQUEST);
        request5.execute();

        PerformNetworkRequest request6 = new PerformNetworkRequest(Api.URL_READ_IMAGES, null, CODE_GET_REQUEST);
        request6.execute();

        Toast.makeText(getApplicationContext(),"Načítané.", Toast.LENGTH_SHORT).show();
        locked = false;
    }

    private void refreshItemList(JSONArray items, String message) throws JSONException {

        switch(message) {
            case "moznosti_studia":
                itemList.clear();
                for (int i = 0; i < items.length(); i++) {
                    JSONObject obj = items.getJSONObject(i);

                    itemList.add(new Item(
                            obj.getInt("id"),
                            obj.getString("nazov"),
                            obj.getString("obsah")
                    ));
                }
            break;

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
        HashMap<String, String> params;
        int requestCode;

        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
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
                    //Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    refreshItemList(object.getJSONArray("items"), object.getString("message"));
                    readPictures();

                    //debug
                    if (!obrazkyUrlList.isEmpty()) {
                        System.out.println(Arrays.deepToString(getFilesDir().listFiles()));
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);


            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }
    }
}
