package com.example.dominik.ukf_app.udalosti;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.dominik.ukf_app.R;
import com.example.dominik.ukf_app.db_connect.CalendarEvent;

/**
 * Created by Dominik on 21.03.2018.
 */

public class ActivityEventDetail extends AppCompatActivity {

    CalendarEvent udalost;
    TextView nazov, popis;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        nazov = findViewById(R.id.eventDetailNazov);
        popis = findViewById(R.id.eventDetailPopis);
        udalost = getIntent().getExtras().getParcelable("udalost");
        nazov.setText(udalost.getNazov());
        popis.setText(udalost.getPopis());
    }
}
