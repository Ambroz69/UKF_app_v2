package com.example.dominik.ukf_app.calendar;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.TextView;

import com.example.dominik.ukf_app.R;
import com.example.dominik.ukf_app.db_connect.CalendarEvent;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Dominik on 07.03.2018.
 */

public class ActivityCalendar extends AppCompatActivity {

    private List<CalendarEvent> udalostiList;
    private List<String> events;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        MaterialCalendarView calendar = (MaterialCalendarView) findViewById(R.id.calendarView);
        calendar.state().edit()
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setMinimumDate(CalendarDay.from(2017, 9, 1))
                .setMaximumDate(CalendarDay.from(2020, 8, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        udalostiList = new ArrayList<CalendarEvent>();
        udalostiList = (ArrayList) getIntent().getParcelableArrayListExtra("udalosti");
        events = new ArrayList<>();
        for (int i = 0; i < udalostiList.size(); i++) {
            events.add(udalostiList.get(i).getDatum());
        }

        calendar.addDecorator(new CurrentDayDecorator(this));
        calendar.addDecorator(new EventDecorator(this, events));
        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                //dostat datum rovnakeho tvaru ako v DB
                String den = String.valueOf(date.getDay());
                int denInt = date.getDay();
                if (denInt < 10) {
                    den = "0"+den;
                }
                String mesiac = String.valueOf(date.getMonth()+1);
                int mesiacInt = date.getMonth()+1;
                if (mesiacInt < 10) {
                    mesiac = "0"+mesiac;
                }
                String rok = String.valueOf(date.getYear());
                String datum = rok + "-" + mesiac +  "-" + den;

                String eventDate = "Datum: ";
                String eventTitle = "Nazov: ";
                String eventDescription = "Popis";


                //ked sa nasla zhoda v datume z DB
                for (int i = 0; i < udalostiList.size(); i++) {
                    if (udalostiList.get(i).getDatum().equals(datum)) {
                        eventTitle = "<strong>Názov:</strong> " + udalostiList.get(i).getNazov();
                        eventDate = "<strong>Dátum</strong>: " + udalostiList.get(i).getDatum();
                        eventDescription = "<strong>Popis:</strong> " + udalostiList.get(i).getPopis();
                        break;
                    } else {
                       eventDate ="<strong>Dátum:</strong> " + datum;
                       eventTitle = "Žiadna udalosť";
                       eventDescription = "";
                    }
                }

                TextView textViewEventDate = (TextView) findViewById(R.id.textViewEventDate);
                TextView textViewEventTitle = (TextView) findViewById(R.id.textViewEventTitle);
                TextView textViewEventDescription = (TextView) findViewById(R.id.textViewEventDescription);

                textViewEventDate.setText(Html.fromHtml(eventDate));
                textViewEventTitle.setText(Html.fromHtml(eventTitle));
                textViewEventDescription.setText(Html.fromHtml(eventDescription));

            }
        });
    }
}
