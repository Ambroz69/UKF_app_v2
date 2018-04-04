package com.example.dominik.ukf_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.webkit.WebView;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.List;


public class ActivityMoznostiStudiaDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moznosti_studia_detail);


        WebView moznostiStudiaDetail = (WebView)findViewById(R.id.moznostiStudiaDetail);
        if(getIntent() != null)
        {
            String detail = getIntent().getStringExtra("detail");
            moznostiStudiaDetail.loadData(detail,"text/html; charset=utf-8","utf-8");
        }

    }


}
