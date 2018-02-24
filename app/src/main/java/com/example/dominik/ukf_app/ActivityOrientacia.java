package com.example.dominik.ukf_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


public class ActivityOrientacia extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orientacia);

        TextView infoOrientacia = (TextView)findViewById(R.id.infoOrientacia);
        if(getIntent() != null)
        {
            String info = getIntent().getStringExtra("info");
            infoOrientacia.setText(info);
        }
    }

}