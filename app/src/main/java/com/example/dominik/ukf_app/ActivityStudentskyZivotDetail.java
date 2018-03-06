package com.example.dominik.ukf_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Dominik on 06.03.2018.
 */

class ActivityStudentskyZivotDetail extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentsky_zivot_detail);


        TextView moznostiStudiaDetail = (TextView)findViewById(R.id.studentskyZivotDetail);
        if(getIntent() != null)
        {
            String info = getIntent().getStringExtra("info");
            moznostiStudiaDetail.setText(info);
        }

    }
}
