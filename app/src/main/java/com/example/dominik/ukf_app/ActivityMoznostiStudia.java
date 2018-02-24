package com.example.dominik.ukf_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


public class ActivityMoznostiStudia extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moznosti_studia);

        TextView infoMoznostiStudia = (TextView)findViewById(R.id.infoMoznostiStudia);
        if(getIntent() != null)
        {
            String info = getIntent().getStringExtra("info");
            infoMoznostiStudia.setText(info);
        }
    }

}
