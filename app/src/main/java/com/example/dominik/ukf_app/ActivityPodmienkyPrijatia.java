package com.example.dominik.ukf_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


public class ActivityPodmienkyPrijatia extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podmienky_prijatia);

        TextView infoPodmienkyPrijatia = (TextView)findViewById(R.id.infoPodmienkyPrijatia);
        if(getIntent() != null)
        {
            String info = getIntent().getStringExtra("info");
            infoPodmienkyPrijatia.setText(info);
        }
    }

}