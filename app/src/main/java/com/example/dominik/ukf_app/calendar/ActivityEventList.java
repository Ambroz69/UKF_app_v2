package com.example.dominik.ukf_app.calendar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dominik.ukf_app.MainActivity;
import com.example.dominik.ukf_app.R;
import com.example.dominik.ukf_app.db_connect.CalendarEvent;

import java.util.ArrayList;
import java.util.List;

import static com.example.dominik.ukf_app.R.layout.activity_event_list;

/**
 * Created by Dominik on 14.03.2018.
 */

public class ActivityEventList extends AppCompatActivity {

    EventAdapter adapter;
    List<CalendarEvent> udalostiList;
    ListView listViewEvents;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        listViewEvents = (ListView) findViewById(R.id.listViewEvents);

        udalostiList = new ArrayList<CalendarEvent>();
        udalostiList = (ArrayList) getIntent().getParcelableArrayListExtra("udalosti");

        adapter = new EventAdapter(udalostiList);
        listViewEvents.setAdapter(adapter);

        listViewEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CalendarEvent udalost = (CalendarEvent) listViewEvents.getItemAtPosition(position);
                Intent intent = new Intent(ActivityEventList.this,ActivityEventDetail.class);
                intent.putExtra("udalost", (Parcelable) udalost);
                startActivity(intent);
            }
        });
    }

    public String convertDate(String SQL_date) {
        String convertedDate, year, month, day;
        year = SQL_date.substring(0,4);
        month = SQL_date.substring(5,7);
        switch (month) {
            case "01":
                month = "január";
                break;
            case "02":
                month = "február";
                break;
            case "03":
                month = "marec";
                break;
            case "04":
                month = "apríl";
                break;
            case "05":
                month = "máj";
                break;
            case "06":
                month = "jún";
                break;
            case "07":
                month = "júl";
                break;
            case "08":
                month = "august";
                break;
            case "09":
                month = "september";
                break;
            case "10":
                month = "október";
                break;
            case "11":
                month = "november";
                break;
            case "12":
                month = "december";
                break;
        }
        if (SQL_date.substring(8,9).equals("0"))
            day = SQL_date.substring(9);
        else
            day = SQL_date.substring(8);

        convertedDate = day + ". " + month + " " + year;
        return convertedDate;
    }

    class EventAdapter extends ArrayAdapter<CalendarEvent> {

        List<CalendarEvent> calendarEventList;

        public EventAdapter(List<CalendarEvent> calendarEventList) {
            super(ActivityEventList.this, activity_event_list, calendarEventList);
            this.calendarEventList = calendarEventList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View listViewEvent = inflater.inflate(R.layout.layout_event_list, null, true);

            TextView textViewNazov = listViewEvent.findViewById(R.id.textViewNazov);
            TextView textViewDatum = listViewEvent.findViewById(R.id.textViewDatum);
            TextView textViewPopis = listViewEvent.findViewById(R.id.textViewPopis);

            final CalendarEvent event = calendarEventList.get(position);
            textViewNazov.setText(event.getNazov());
            textViewDatum.setText(convertDate(event.getDatum()));
            textViewPopis.setText(event.getPopis());

            if ((position%2) == 1) {
                LinearLayout ll = (LinearLayout) textViewNazov.getParent();
                ll.setBackgroundColor(Color.parseColor("#A5D6A7"));
            }
            return listViewEvent;
        }
    }
}
