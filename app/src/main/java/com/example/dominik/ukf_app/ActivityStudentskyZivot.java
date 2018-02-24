package com.example.dominik.ukf_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


public class ActivityStudentskyZivot extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentsky_zivot);

        TextView infoStudentskyZivot = (TextView)findViewById(R.id.infoStudentskyZivot);
        if(getIntent() != null)
        {
            String info = getIntent().getStringExtra("info");
            infoStudentskyZivot.setText(info);
        }
    }

}
