package com.example.dominik.ukf_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.List;


public class ActivityMoznostiStudiaDetail extends AppCompatActivity {

    List<Item> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moznosti_studia_detail);


        TextView moznostiStudiaDetail = (TextView)findViewById(R.id.moznostiStudiaDetail);
        if(getIntent() != null)
        {
            String info = getIntent().getStringExtra("info");
            moznostiStudiaDetail.setText(info);
        }

    }


}
