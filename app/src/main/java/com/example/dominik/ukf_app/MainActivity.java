package com.example.dominik.ukf_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dominik.ukf_app.calendar.ActivityCalendar;
import com.example.dominik.ukf_app.db_connect.Api;
import com.example.dominik.ukf_app.db_connect.CalendarEvent;
import com.example.dominik.ukf_app.db_connect.Item;
import com.example.dominik.ukf_app.db_connect.RequestHandler;
import com.example.dominik.ukf_app.db_connect.StudijnyProgram;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    EditText editTextId, editTextNazov, editTextObsah;
    ListView listView, podmienkyPrijatiaView, studentskyZivotView;
    List<Item> itemList, podmienkyPrijatiaList, studentskyZivotList;
    List<CalendarEvent> udalostiList;
    List<StudijnyProgram> studijnyProgramList;
    List<String> dataMoznostiStudia, dataPodmienkyPrijatia, dataStudentskyZivot, dataStudijnyProgram;

    GridLayout mainGrid;
    private boolean isReadingDB = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        itemList = new ArrayList<>();
        podmienkyPrijatiaList = new ArrayList<>();
        studentskyZivotList = new ArrayList<>();
        udalostiList = new ArrayList<>();
        studijnyProgramList = new ArrayList<>();

        listView = (ListView) findViewById(R.id.listViewItems);
        podmienkyPrijatiaView = (ListView) findViewById(R.id.listViewPodmienkyPrijatia);
        studentskyZivotView = (ListView) findViewById(R.id.listViewStudentskyZivot);

        mainGrid = (GridLayout) findViewById(R.id.mainGrid);

        readItems();
        setSingleEvent(mainGrid);
        if (!isReadingDB)
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }

    public void calendarButton(View view) {
        Intent intent = new Intent(MainActivity.this,ActivityCalendar.class);
        intent.putParcelableArrayListExtra("udalosti", (ArrayList<? extends Parcelable>) udalostiList);
        startActivity(intent);
    }

    /* test */
    private void setSingleEvent(GridLayout mainGrid) {

        //karta moznosti studia
        CardView cardView1 = (CardView) mainGrid.getChildAt(0);
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ActivityMoznostiStudiaMenu.class);
                intent.putParcelableArrayListExtra("studijne_programy", (ArrayList<? extends Parcelable>) studijnyProgramList);
                startActivity(intent);
            }
        });
        //karta podmienky prijatia
        CardView cardView2 = (CardView) mainGrid.getChildAt(1);
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataPodmienkyPrijatia = new ArrayList<>(podmienkyPrijatiaList.size());
                for (Object object : podmienkyPrijatiaList) {
                    dataPodmienkyPrijatia.add(object != null ? object.toString() : null);
                }
                Intent intent = new Intent(MainActivity.this,ActivityPodmienkyPrijatia.class);
                for (int i = 0; i < dataPodmienkyPrijatia.size(); i++) {
                    intent.putExtra("info", dataPodmienkyPrijatia.get(i).toString());
                }
                startActivity(intent);
            }
        });
        //karta studentsky zivot
        CardView cardView3 = (CardView) mainGrid.getChildAt(2);
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataStudentskyZivot = new ArrayList<>(studentskyZivotList.size());
                for (Object object : studentskyZivotList) {
                    dataStudentskyZivot.add(object != null ? object.toString() : null);
                }
                Intent intent = new Intent(MainActivity.this,ActivityStudentskyZivot.class);
                for (int i = 0; i < dataStudentskyZivot.size(); i++) {
                    intent.putExtra("detailInfo"+i, dataStudentskyZivot.get(i).toString());
                }
                startActivity(intent);
            }
        });
        //karta orientacia
        CardView cardView4 = (CardView) mainGrid.getChildAt(3);
        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ActivityOrientacia.class);
                intent.putExtra("info","Orientacia na fakulte FPV");
                startActivity(intent);
            }
        });
    }

    private void readItems() {
        isReadingDB = true;

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

        isReadingDB = false;
    }

    private void refreshItemList(JSONArray items, String message) throws JSONException {
        ItemAdapter adapter;

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
                adapter = new ItemAdapter(itemList);
                listView.setAdapter(adapter);
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
                adapter = new ItemAdapter(podmienkyPrijatiaList);
                podmienkyPrijatiaView.setAdapter(adapter);
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
                adapter = new ItemAdapter(studentskyZivotList);
                studentskyZivotView.setAdapter(adapter);
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

    class ItemAdapter extends ArrayAdapter<Item> {
        List<Item> itemList;

        public ItemAdapter(List<Item> itemList) {
            super(MainActivity.this, R.layout.layout_item_list, itemList);
            this.itemList = itemList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.layout_item_list, null, true);

            TextView textViewObsah = listViewItem.findViewById(R.id.textViewObsah);
            TextView textViewName = listViewItem.findViewById(R.id.textViewName);

            final Item item = itemList.get(position);
            textViewName.setText(item.getNazov());
            textViewObsah.setText(item.getObsah());

            return listViewItem;
        }
    }

}
