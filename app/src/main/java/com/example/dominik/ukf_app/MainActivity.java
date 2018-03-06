package com.example.dominik.ukf_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    List<String> dataMoznostiStudia, dataPodmienkyPrijatia, dataStudentskyZivot;
    Button buttonAddUpdate;
    boolean isUpdating = false;

    GridLayout mainGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemList = new ArrayList<>();
        podmienkyPrijatiaList = new ArrayList<>();
        studentskyZivotList = new ArrayList<>();

        editTextId = (EditText) findViewById(R.id.editTextId);
        editTextNazov = (EditText) findViewById(R.id.editTextNazov);
        editTextObsah = (EditText) findViewById(R.id.editTextObsah);

        listView = (ListView) findViewById(R.id.listViewItems);
        podmienkyPrijatiaView = (ListView) findViewById(R.id.listViewPodmienkyPrijatia);
        studentskyZivotView = (ListView) findViewById(R.id.listViewStudentskyZivot);

        buttonAddUpdate = (Button) findViewById(R.id.buttonAddUpdate);
        buttonAddUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUpdating) {
                    updateItem();
                } else {
                    createItem();
                }
            }
        });
        readItems();
        readPodmienkyPrijatiaInfo();
        readStudentskyZivotInfo();

        mainGrid = (GridLayout) findViewById(R.id.mainGrid);
        setSingleEvent(mainGrid);

    }

    /* test */
    private void setSingleEvent(GridLayout mainGrid) {

        //karta moznosti studia
        CardView cardView1 = (CardView) mainGrid.getChildAt(0);
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataMoznostiStudia = new ArrayList<>(itemList.size());
                for (Object object : itemList) {
                    dataMoznostiStudia.add(object != null ? object.toString() : null);
                }
                Intent intent = new Intent(MainActivity.this,ActivityMoznostiStudia.class);
                for (int i = 0; i < dataMoznostiStudia.size(); i++) {
                    intent.putExtra("tab"+i, dataMoznostiStudia.get(i).toString());
                }
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
                    intent.putExtra("tab"+i, dataStudentskyZivot.get(i).toString());
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

    private void createItem() {
        String nazov = editTextNazov.getText().toString().trim();
        String obsah = editTextObsah.getText().toString().trim();

        if (TextUtils.isEmpty(nazov)) {
            editTextNazov.setError("Prosím zadajte názov");
            editTextNazov.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(obsah)) {
            editTextObsah.setError("Prosím zadajte obsah");
            editTextObsah.requestFocus();
            return;
        }


        HashMap<String, String> params = new HashMap<>();
        params.put("nazov", nazov);
        params.put("obsah", obsah);

        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_CREATE_ITEM, params, CODE_POST_REQUEST);
        request.execute();
    }

    private void readItems() {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_ITEMS, null, CODE_GET_REQUEST);
        request.execute();
    }

    private void readPodmienkyPrijatiaInfo() {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_PODMIENKY_PRIJATIA, null, CODE_GET_REQUEST);
        request.execute();
    }

    private void readStudentskyZivotInfo() {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_STUDENTSKY_ZIVOT, null, CODE_GET_REQUEST);
        request.execute();
    }

    private void updateItem() {
        String id = editTextId.getText().toString();
        String nazov = editTextNazov.getText().toString().trim();
        String obsah = editTextObsah.getText().toString().trim();



        if (TextUtils.isEmpty(nazov)) {
            editTextNazov.setError("Prosím zadajte názov");
            editTextNazov.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(obsah)) {
            editTextObsah.setError("Prosím zadajte obsah");
            editTextObsah.requestFocus();
            return;
        }


        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("nazov", nazov);
        params.put("obsah", obsah);


        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_UPDATE_ITEM, params, CODE_POST_REQUEST);
        request.execute();

        buttonAddUpdate.setText("Pridať");
        editTextNazov.setText("");

        isUpdating = false;
    }

    private void deleteItem(int id) {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_DELETE_ITEM + id, null, CODE_GET_REQUEST);
        request.execute();
    }

    private void refreshItemList(JSONArray items) throws JSONException {
        ItemAdapter adapter;

        //poriesit switch na rozpoznanie tabulky
        switch (items.length()) {
            case 1: //tabulka podmienky_prijatia
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

            case 10: //tabulka moznosti_studia
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

            case 11: //tabulka studentsky_zivot
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
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    refreshItemList(object.getJSONArray("items"));
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
            TextView textViewUpdate = listViewItem.findViewById(R.id.textViewUpdate);
            TextView textViewDelete = listViewItem.findViewById(R.id.textViewDelete);

            TextView textViewPodmienkyPrijatiaObsah = listViewItem.findViewById(R.id.textViewPodmienkyPrijatiaObsah);
            TextView textViewStudentskyZivotObsah = listViewItem.findViewById(R.id.textViewStudentskyZivotObsah);


            final Item item = itemList.get(position);
            textViewName.setText(item.getNazov());
            textViewObsah.setText(item.getObsah());

            final Item item2 = podmienkyPrijatiaList.get(position);
            textViewPodmienkyPrijatiaObsah.setText(item2.getObsah());

            final Item item3 = studentskyZivotList.get(position);
            textViewStudentskyZivotObsah.setText(item3.getObsah());

            textViewUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isUpdating = true;
                    editTextId.setText(String.valueOf(item.getId()));
                    editTextNazov.setText(item.getNazov());
                    editTextObsah.setText(item.getObsah());
                    buttonAddUpdate.setText("Upraviť");
                }
            });

            textViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    builder.setTitle("Vymazať " + item.getNazov() + "?")
                            .setMessage("Naozaj chcete vymazať položku?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteItem(item.getId());
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }
            });

            return listViewItem;

        }
    }

}
