package com.example.dominik.ukf_app.calendar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
            textViewDatum.setText(event.getDatum());
            textViewPopis.setText(event.getPopis());

            return listViewEvent;
        }
    }
}
