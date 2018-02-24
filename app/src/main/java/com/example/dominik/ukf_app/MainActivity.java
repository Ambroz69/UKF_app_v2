package com.example.dominik.ukf_app;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
    ListView listView;
    Button buttonAddUpdate;

    List<Item> itemList;

    boolean isUpdating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextId = (EditText) findViewById(R.id.editTextId);
        editTextNazov = (EditText) findViewById(R.id.editTextNazov);
        editTextObsah = (EditText) findViewById(R.id.editTextObsah);

        buttonAddUpdate = (Button) findViewById(R.id.buttonAddUpdate);
        listView = (ListView) findViewById(R.id.listViewItems);

        itemList = new ArrayList<>();


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
        itemList.clear();

        for (int i = 0; i < items.length(); i++) {
            JSONObject obj = items.getJSONObject(i);

            itemList.add(new Item(
                    obj.getInt("id"),
                    obj.getString("nazov"),
                    obj.getString("obsah")
            ));
        }

        ItemAdapter adapter = new ItemAdapter(itemList);
        listView.setAdapter(adapter);
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

            TextView textViewName = listViewItem.findViewById(R.id.textViewName);
            TextView textViewUpdate = listViewItem.findViewById(R.id.textViewUpdate);
            TextView textViewDelete = listViewItem.findViewById(R.id.textViewDelete);

            final Item item = itemList.get(position);

            textViewName.setText(item.getNazov());

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
